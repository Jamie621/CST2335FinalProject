package algonquin.cst2335.ju000013.recipeapi;

import static algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity.getBitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySavedRecipeBinding;
import algonquin.cst2335.ju000013.databinding.SaveResultBinding;

public class SavedRecipeActivity extends AppCompatActivity {
    ActivitySavedRecipeBinding binding;
    private  RecyclerView.Adapter savedAdapter;
    RecipeSavedViewModel savedModel;
    private ArrayList<RecipeSearched> savedRecipes;
    private RecipeSearchedDAO sDAO;
    RecyclerView recipeSavedRecycler;

    /* The point of this function is to load a Menu layout file. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.saved_menu, menu);
        return true;
    }

    /* When the user clicks on a menu item, Android will call this function. */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.recipe_delete_all){
            // delete all in database
            AlertDialog.Builder builder = new AlertDialog.Builder( SavedRecipeActivity.this );
            builder.setTitle(R.string.recipe_remove_all)
                    .setNegativeButton(getString(R.string.recipe_no), (dialog, cl) -> {})
                    .setPositiveButton(getString(R.string.recipe_yes), (dialog, cl) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            sDAO.deleteAll(); // clear database
                            savedRecipes.clear(); // clear arraylist
                            // update the Adapter object that something's been removed so the RecyclerView can update itself
                            runOnUiThread(() -> savedAdapter.notifyDataSetChanged());
                        });
                        Snackbar.make(this.getCurrentFocus(), getString(R.string.recipe_clear_confirm), Snackbar.LENGTH_LONG).show();
                    })
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* set toolbar */
        Toolbar recipeToolbar = binding.recipeToolbar;
        setSupportActionBar(recipeToolbar);

        /* open a database */
        RecipeSavedDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeSavedDatabase.class, "database-name").build();
        sDAO = db.rsDAO();

        /* achieve all the widgets */
        recipeSavedRecycler = binding.recipeSavedRecycler;
        /* To specify a single column scrolling in a Vertical direction */
        recipeSavedRecycler.setLayoutManager(new LinearLayoutManager(this));

        /* initialize and retrieve the ArrayList<> that it is storing in database */
        savedModel = new ViewModelProvider(this).get(RecipeSavedViewModel.class);
        savedRecipes = savedModel.savedRecipes.getValue();
        if (savedRecipes == null){
            savedModel.savedRecipes.postValue(savedRecipes = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                savedRecipes.addAll( sDAO.getAllSavedResults() ); // get the data from database to arrayList

                runOnUiThread( () ->  binding.recipeSavedRecycler.setAdapter( savedAdapter )); //load the RecyclerView
            });
        }

        /* set adapter to recycleView */
        recipeSavedRecycler.setAdapter(savedAdapter = new RecyclerView.Adapter<MySavedRowHolder>() {
            @NonNull
            @Override
            public MySavedRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SaveResultBinding saveResultBinding = SaveResultBinding.inflate(getLayoutInflater());
                return new MySavedRowHolder(saveResultBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MySavedRowHolder holder, int position) {
                RecipeSearched recipeSaved = savedRecipes.get(position);
                holder.recipe_saved_title.setText(recipeSaved.getTitle());
                holder.recipe_saved_url.setText(recipeSaved.getSource_url());
                // set image (String) to imageView
                String image_url = recipeSaved.getImage_url();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = getBitmap(image_url);
                        // Set the loaded bitmap to the ImageView on the UI thread
                        holder.recipe_saved_url.post(() -> holder.result_image.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }

            @Override
            public int getItemCount() {
                return savedRecipes.size();
            }
        });

    }

    class MySavedRowHolder extends RecyclerView.ViewHolder {
        TextView recipe_saved_url;
        ImageView result_image;
        TextView recipe_saved_title;
        Button recipe_remove_button;
        public MySavedRowHolder(@NonNull View itemView) {
            super(itemView);
            recipe_saved_url = itemView.findViewById(R.id.recipe_saved_url_text);
            result_image = itemView.findViewById(R.id.recipe_result_image);
            recipe_saved_title = itemView.findViewById(R.id.recipe_saved_title_text);
            recipe_remove_button = itemView.findViewById(R.id.recipe_remove_button);
        }
    }
}