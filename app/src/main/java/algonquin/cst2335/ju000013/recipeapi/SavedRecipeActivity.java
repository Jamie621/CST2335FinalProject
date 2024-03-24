package algonquin.cst2335.ju000013.recipeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import algonquin.cst2335.ju000013.R;

public class SavedRecipeActivity extends AppCompatActivity {
    private  RecyclerView.Adapter saveAdapter;
    RecipeSearchedViewModel saveModel;
    private ArrayList<RecipeSaved> savedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipe);
    }
}