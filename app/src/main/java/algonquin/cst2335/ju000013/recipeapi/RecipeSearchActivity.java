package algonquin.cst2335.ju000013.recipeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivityRecipeSearchBinding;

public class RecipeSearchActivity extends AppCompatActivity {

    ActivityRecipeSearchBinding binding;
    EditText editSearchText;
    Button buttonSearch;
    RecyclerView searchResultsRecycle;
    Button savedViewButton;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* achieve all the widgets */
        editSearchText = binding.editSearchText;
        buttonSearch = binding.buttonSearch;
        searchResultsRecycle = binding.searchResults;
        savedViewButton = binding.savedViewButton;

        /* save search text to SharedPreference to show automatically next time. */
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();


        buttonSearch.setOnClickListener(click -> {
            /* save search text to SharedPreference */
            editor.putString("SearchText", editSearchText.getText().toString());
            editor.apply();
        });

        /* achieve search text from SharedPreference */
        String searchText = prefs.getString("SearchText", "");
        editSearchText.setText(searchText);
    }
}