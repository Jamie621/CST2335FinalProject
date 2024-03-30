package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

import algonquin.cst2335.ju000013.R;

public class FavouritePageActivity extends AppCompatActivity {
    private RecyclerView favouritesRecyclerView;
    private FavouriteViewModel favouriteViewModel;
    private FavouriteAdapter adapter;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_page);

        // Initialize the ViewModel
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Initialize RecyclerView and its adapter
        favouritesRecyclerView = findViewById(R.id.recycleView);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavouriteAdapter(new ArrayList<>(), favouriteViewModel, favouritesRecyclerView);
        favouritesRecyclerView.setAdapter(adapter);

        // Set up the delete button
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> adapter.setDeleteMode(true));

        // Observe the LiveData list of favourites
        favouriteViewModel.getFavourites().observe(this, newFavourites -> adapter.setFavourites(newFavourites));

        // Handling deletion from the adapter
        adapter.setOnItemDeleteListener((location, position) -> {
            new AlertDialog.Builder(FavouritePageActivity.this)
                    .setMessage("Do you want to delete this location?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        favouriteViewModel.deleteFavouriteById(location.getId());
                        adapter.removeItem(position);
                        adapter.setDeleteMode(false); // Exit delete mode
                    })
                    .setNegativeButton("No", (dialog, which) -> adapter.setDeleteMode(false))
                    .show();
        });
    }

    public void onNewFavouriteSaved(SaveLocation newFavourite) {
        // This method can be used to add a new favourite location
        favouriteViewModel.addFavourite(newFavourite);
    }
}