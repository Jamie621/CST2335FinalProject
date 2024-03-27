package algonquin.cst2335.ju000013;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;

public class DictionaryApiActivity extends AppCompatActivity {
    private EditText editTextWord;
    private Button buttonSearch;
    private RecyclerView recyclerViewDefinitions;
    private DictionaryAdapter dictionaryAdapter;
    private RequestQueue requestQueue;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    private Toolbar toolbar; // Declare the Toolbar object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search);

        toolbar = findViewById(R.id.toolbar_dictionary); // Initialize the Toolbar
        setSupportActionBar(toolbar); // Set the Toolbar as the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the Up button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity (if there is one)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }


    private void fetchDefinitions(String word) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                this::parseAndDisplayDefinitions,
                error -> Toast.makeText(this, "Error fetching definitions: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonArrayRequest);
    }

   /* private void parseAndDisplayDefinitions(JSONArray response) {
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
    }*/

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
                        final String word = entry.getString("word");

                        // Create a WordEntity to insert
                        WordEntity wordEntity = new WordEntity(word, definitionText);

                        // Insert the WordEntity into the database and get the inserted ID
                        long id = Executors.newSingleThreadExecutor().submit(() -> db.wordDao().insert(wordEntity)).get();

                        // Now create the Definition object with the ID, word, and definition
                        Definition definition = new Definition((int) id, word, definitionText);
                        definitions.add(definition);
                    }
                }
            }

            // Since we are doing UI work, this needs to run on the main thread
            runOnUiThread(() -> dictionaryAdapter.updateData(definitions));
        } catch (Exception e) {
            // Exception handling for both JSON parsing and database operation
            runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }





    /*  public void onDeleteDefinition(Definition definition) {
        Executors.newSingleThreadExecutor().execute(() -> db.wordDao().delete(new WordEntity(definition.getWord(), definition.getDefinition())));
        Snackbar.make(findViewById(android.R.id.content), "Definition deleted", Snackbar.LENGTH_SHORT).show();
    }*/
  public void onDeleteDefinition(Definition definition) {
      Executors.newSingleThreadExecutor().execute(() -> {
          // Assuming that the definition object contains the correct id
          WordEntity wordEntity = db.wordDao().getWordById(definition.getId());
          if (wordEntity != null) {
              db.wordDao().delete(wordEntity);
              runOnUiThread(() -> {
                  // You can update your UI to reflect the deletion here
                  dictionaryAdapter.notifyDataSetChanged();
              });
          }
      });
  }


    private static class Definition {
        private final int id;
        private final String word;
        private final String definition;

        Definition(int id, String word, String definition) {
            this.id = id;
            this.word = word;
            this.definition = definition;
        }

        // Getters (and setters if you need to modify the values)
        public int getId() {
            return id;
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
