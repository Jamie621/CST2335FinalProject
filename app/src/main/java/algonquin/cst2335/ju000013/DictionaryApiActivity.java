package algonquin.cst2335.ju000013;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DictionaryApiActivity extends AppCompatActivity {

    private EditText editTextWord;
    private Button buttonSearch;
    private RecyclerView recyclerViewDefinitions;
    private DictionaryAdapter dictionaryAdapter;
    private RequestQueue requestQueue;
    private AppDatabase db;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search);

        sharedPreferences = getSharedPreferences("DictionaryPreferences", MODE_PRIVATE);
        editTextWord = findViewById(R.id.word_search_input);
        buttonSearch = findViewById(R.id.search_button);
        recyclerViewDefinitions = findViewById(R.id.definition_list);

        recyclerViewDefinitions.setLayoutManager(new LinearLayoutManager(this));
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>());
        recyclerViewDefinitions.setAdapter(dictionaryAdapter);

        requestQueue = Volley.newRequestQueue(this);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictionary_db").build();

        if (savedInstanceState == null) {
            editTextWord.setText(sharedPreferences.getString("lastSearch", ""));
        } else {
            editTextWord.setText(savedInstanceState.getString("lastSearch", ""));
        }

        buttonSearch.setOnClickListener(v -> {
            String word = editTextWord.getText().toString().trim();
            if (!word.isEmpty()) {
                fetchDefinitions(word);
                sharedPreferences.edit().putString("lastSearch", word).apply();
            } else {
                Toast.makeText(this, "Please enter a word to search.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDefinitions(String word) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                response -> parseAndDisplayDefinitions(word, response),
                error -> {
                    String errorMessage = "Error fetching definitions: ";
                    if (error != null && error.getMessage() != null) {
                        errorMessage += error.getMessage();
                    } else {
                        errorMessage += "Unknown error";
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void parseAndDisplayDefinitions(String word, JSONArray response) {
        try {
            List<Definition> definitions = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject entry = response.getJSONObject(i);
                String definitionText = entry.getJSONArray("meanings")
                        .getJSONObject(0)
                        .getJSONArray("definitions")
                        .getJSONObject(0)
                        .getString("definition");
                definitions.add(new Definition(word, definitionText));
            }
            dictionaryAdapter.updateData(definitions);

            if (!definitions.isEmpty()) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        db.wordDao().insert(new WordEntity(word, definitions.get(0).getDefinition()));
                    } catch (Exception e) {
                        Toast.makeText(this, "Error inserting word into database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "No definitions found.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("lastSearch", editTextWord.getText().toString());
    }

    private class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
        private final List<Definition> definitions;

        DictionaryAdapter(List<Definition> definitions) {
            this.definitions = definitions;
        }

        void updateData(List<Definition> newDefinitions) {
            definitions.clear();
            definitions.addAll(newDefinitions);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Definition definition = definitions.get(position);
            holder.textViewDefinition.setText(definition.getDefinition());
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewDefinition;

            ViewHolder(View itemView) {
                super(itemView);
                textViewDefinition = itemView.findViewById(R.id.definition_text_view);
            }
        }
    }

    private static class Definition {
        private final String word;
        private final String definition;

        Definition(String word, String definition) {
            this.word = word;
            this.definition = definition;
        }

        public String getDefinition() {
            return definition;
        }
    }
}
