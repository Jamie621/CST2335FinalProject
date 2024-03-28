package algonquin.cst2335.ju000013.songApi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySongFavoritesBinding;
import algonquin.cst2335.ju000013.databinding.ActivitySongSearchBinding;
import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;

public class SongFavoritesActivity extends AppCompatActivity {
    private ActivitySongFavoritesBinding songFavoritesBinding;

    //ArrayList<Song> songsDatabaseEntity;
    //Song songFavorite;
    private SongFavoritesViewModel songFavoritesViewModel;
    private SongFavoritesAdapter songFavoritesAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toobar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) { // Handle the Up button click
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        else if (id == R.id.item_1) {

        }
        else if (id == R.id.item_2) {
            Intent intent = new Intent(SongFavoritesActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongFavoritesActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongFavoritesActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        songFavoritesBinding = ActivitySongFavoritesBinding.inflate(getLayoutInflater());
//        setContentView(songFavoritesBinding.getRoot());
//
//        Toolbar tool_bar = findViewById(R.id.toolbarFavorites);
//        setSupportActionBar(tool_bar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        songFavoritesModel = new ViewModelProvider(this).get(SongViewModel.class);
////        songsDatabaseEntity = songFavoritesModel.songs.getValue();
////
////        if (songsDatabaseEntity == null) {
////            songFavoritesModel.songs.postValue(songsDatabaseEntity = new ArrayList<>());
////        }
//        RecyclerView recyclerView = findViewById(R.id.rvFavorites);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        songFavoritesAdapter = new SongAdapter(new ArrayList<>(),this);
//        recyclerView.setAdapter(songFavoritesAdapter);
//
////        songFavoritesModel.getAllSongs().observe(this, new Observer<List<Song>>() {
////            @Override
////            public void onChanged(List<Song> songs) {
////                songFavoritesAdapter.setSongs(songs);
////            }
////        });
//
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songFavoritesBinding = ActivitySongFavoritesBinding.inflate(getLayoutInflater());
        setContentView(songFavoritesBinding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbarFavorites);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerViewFavorites = findViewById(R.id.rvFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        songFavoritesAdapter = new SongFavoritesAdapter(new ArrayList<>(), this);
        recyclerViewFavorites.setAdapter(songFavoritesAdapter);

        songFavoritesViewModel = new ViewModelProvider(this).get(SongFavoritesViewModel.class);
        songFavoritesViewModel.getSongs().observe(this, new Observer<List<Song>>() {

            @Override
            public void onChanged(List<Song> songs) {
                // Update the adapter with the new list of songs
                songFavoritesAdapter.setSongs(songs);
            }
        });
    }

}
