package algonquin.cst2335.ju000013.songApi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySongDetailsBinding;
import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;

public class SongDetailsActivity extends AppCompatActivity {

    private ActivitySongDetailsBinding songDetailsBinding;
    private SongDatabase songDatabase;
    private SongDAO songDAO;

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
            Intent intent = new Intent(SongDetailsActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongDetailsActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongDetailsActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songDetailsBinding = ActivitySongDetailsBinding.inflate(getLayoutInflater());
        setContentView(songDetailsBinding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songDatabase = Room.databaseBuilder(getApplicationContext(),
                SongDatabase.class, "song-database").build();
        songDAO = songDatabase.sDAO();

        // Get the extras passed from the previous activity

//        Bundle extras = getIntent().getExtras();
//
//        if (extras != null) {
//            String title = extras.getString("title");
//            int duration = extras.getInt("duration");
//            String albumName = extras.getString("albumName");
//            String albumCoverUrl = extras.getString("albumCoverUrl");
//
//            // Set the details in the views
//            songDetailsBinding.tvSongTitleDetail.setText(title);
//            songDetailsBinding.tvSongDurationDetail.setText(String.valueOf(duration));
//            songDetailsBinding.tvSongAlbumNameDetail.setText(albumName);
//
//            new Thread(() -> {
//                try {
//                    URL detailUrl = new URL(albumCoverUrl);
//                    HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream inputStream = connection.getInputStream();
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                    // Update ImageView on the main UI thread
//                    runOnUiThread(() -> songDetailsBinding.ivSongAlbumCoverDetail.setImageBitmap(bitmap));
//                } catch (IOException e) {
//                    Log.e(null, "Error downloading image: " + e.getMessage());
//                }
//            }).start();
//
//
//
//            songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
//                }
//            });
//
//            songDetailsBinding.btnShowFavorites.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(SongDetailsActivity.this, SongFavoritesActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//        }
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // Check the source of the extras
            String source = extras.getString("SOURCE");
            if ("FAVORITES".equals(source)) {
                // Extras are from SongFavoritesActivity
                String title = extras.getString("SONG_TITLE");
                int duration = extras.getInt("SONG_DURATION");
                String albumName = extras.getString("SONG_ALBUM_NAME");
                String albumCoverUrl = extras.getString("SONG_ALBUM_COVER_URL");

                // Set the details in the views
                songDetailsBinding.tvSongTitleDetail.setText(title);
                songDetailsBinding.tvSongDurationDetail.setText(String.valueOf(duration));
                songDetailsBinding.tvSongAlbumNameDetail.setText(albumName);

                new Thread(() -> {
                    try {
                        URL detailUrl = new URL(albumCoverUrl);
                        HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        // Update ImageView on the main UI thread
                        runOnUiThread(() -> songDetailsBinding.ivSongAlbumCoverDetail.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        Log.e(null, "Error downloading image: " + e.getMessage());
                    }
                }).start();

                songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
                    }
                });

                songDetailsBinding.btnShowFavorites.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SongDetailsActivity.this, SongFavoritesActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                // Extras are from SongSearchActivity
                String title = extras.getString("title");
                int duration = extras.getInt("duration");
                String albumName = extras.getString("albumName");
                String albumCoverUrl = extras.getString("albumCoverUrl");

                // Set the details in the views
                songDetailsBinding.tvSongTitleDetail.setText(title);
                songDetailsBinding.tvSongDurationDetail.setText(String.valueOf(duration));
                songDetailsBinding.tvSongAlbumNameDetail.setText(albumName);

                new Thread(() -> {
                    try {
                        URL detailUrl = new URL(albumCoverUrl);
                        HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        // Update ImageView on the main UI thread
                        runOnUiThread(() -> songDetailsBinding.ivSongAlbumCoverDetail.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        Log.e(null, "Error downloading image: " + e.getMessage());
                    }
                }).start();

                songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
                    }
                });

                songDetailsBinding.btnShowFavorites.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SongDetailsActivity.this, SongFavoritesActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void insertSongIntoDatabase(String title, int duration, String albumName, String albumCoverUrl){
        Song song = new Song(title, duration ,albumName, albumCoverUrl);
        new Thread(()->{
            songDAO.insertSong(song);
        }).start();
    }
}
