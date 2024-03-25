package algonquin.cst2335.ju000013.recipeapi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.ju000013.databinding.ActivityRecipeSearchBinding;

public class RecipeSearchActivity extends AppCompatActivity {

    ActivityRecipeSearchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}