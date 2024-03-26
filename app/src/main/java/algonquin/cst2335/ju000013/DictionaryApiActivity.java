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
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;

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
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>(), this::onDeleteDefinition);
        recyclerViewDefinitions.setAdapter(dictionaryAdapter);

        requestQueue = Volley.newRequestQueue(this);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictionary_db").build();

        // Retrieving the last searched word from SharedPreferences
        editTextWord.setText(savedInstanceState == null ? sharedPreferences.getString("lastSearch", "") : savedInstanceState.getString("lastSearch", ""));

        buttonSearch.setOnClickListener(v -> {
            String word = editTextWord.getText().toString().trim();
            if (!word.isEmpty()) {
                fetchDefinitions(word);
                // Saving the last searched word in SharedPreferences
                sharedPreferences.edit().putString("lastSearch", word).apply();
            } else {
                Toast.makeText(this, "Please enter a word to search.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDefinitions(String word) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                this::parseAndDisplayDefinitions,
                error -> Toast.makeText(this, "Error fetching definitions: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void parseAndDisplayDefinitions(JSONArray response) {
        try {
            List<Definition> definitions = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject entry = response.getJSONObject(i);
                JSONArray meanings = entry.getJSONArray("meanings");
                for (int j = 0; j < meanings.length(); j++) {
                    JSONObject meaning = meanings.getJSONObject(j);
                    JSONArray definitionsArray = meaning.getJSONArray("definitions");
                    for (int k = 0; k < definitionsArray.length(); k++) {
                        JSONObject definitionObject = definitionsArray.getJSONObject(k);
                        String definitionText = definitionObject.getString("definition");
                        definitions.add(new Definition(entry.getString("word"), definitionText));
                    }
                }
            }

            runOnUiThread(() -> dictionaryAdapter.updateData(definitions));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    public void onDeleteDefinition(Definition definition) {
        Executors.newSingleThreadExecutor().execute(() -> db.wordDao().delete(new WordEntity(definition.getWord(), definition.getDefinition())));
        Snackbar.make(findViewById(android.R.id.content), "Definition deleted", Snackbar.LENGTH_SHORT).show();
    }

    private static class Definition {
        private final String word;
        private final String definition;

        Definition(String word, String definition) {
            this.word = word;
            this.definition = definition;
        }

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }
    }

    private class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
        private List<Definition> definitions;
        private final OnDefinitionDeleteListener deleteListener;

        DictionaryAdapter(List<Definition> definitions, OnDefinitionDeleteListener deleteListener) {
            this.definitions = definitions;
            this.deleteListener = deleteListener;
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
            final Button deleteButton;

            ViewHolder(View itemView) {
                super(itemView);
                textViewDefinition = itemView.findViewById(R.id.definition_text_view);
                deleteButton = itemView.findViewById(R.id.delete_button);

                deleteButton.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Definition definition = definitions.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle("Delete Definition")
                                .setMessage("Are you sure you want to delete this definition?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    deleteListener.onDeleteDefinition(definition);
                                    definitions.remove(position);
                                    notifyItemRemoved(position);
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });
            }
        }
    }

    public interface OnDefinitionDeleteListener {
        void onDeleteDefinition(Definition definition);
    }
}
