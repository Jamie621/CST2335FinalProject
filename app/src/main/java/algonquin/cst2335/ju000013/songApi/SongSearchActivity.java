package algonquin.cst2335.ju000013.songApi;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.songApi.SongDAO;
import algonquin.cst2335.ju000013.songApi.SongViewModel;
import algonquin.cst2335.ju000013.songApi.SongDatabase;
//import algonquin.cst2335.ju000013.songApi.SongNameBinding;
import algonquin.cst2335.ju000013.databinding.ActivityMainBinding;
import algonquin.cst2335.ju000013.databinding.ActivitySongSearchBinding;
import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;

public class SongSearchActivity extends AppCompatActivity {
    ActivitySongSearchBinding songBinding;
    ArrayList<Song> songsEntity;
    SongViewModel songModel;
    //RecyclerView recyclerViewSongs;
    RecyclerView.Adapter songAdapter;
    SongDatabase songDB;
    SongDAO sDAO;
    int postition;



    private final String URL_REQUEST_DATA = "https://api.deezer.com/search/artist/?q=";
    protected String artistName;
    protected RequestQueue queue;
    private final String TAG = getClass().getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toobar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_1) {

        } else if (id == R.id.item_2) {
            Intent intent = new Intent(SongSearchActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongSearchActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongSearchActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songBinding = ActivitySongSearchBinding.inflate(getLayoutInflater());
        setContentView(songBinding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);

        songDB = Room.databaseBuilder(getApplicationContext(),SongDatabase.class, "database-name").build();

        sDAO = songDB.sDAO();

        //recyclerViewSongs = songBinding.recyclerViewSongs;

        songBinding.recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        songModel = new ViewModelProvider(this).get(SongViewModel.class);

        songsEntity = songModel.songs.getValue();

        if(songsEntity == null){
            songModel.songs.postValue(songsEntity = new ArrayList<Song>());
            ExecutorService threadSong = Executors.newSingleThreadExecutor();
            threadSong.execute(()->{
                songsEntity.addAll(sDAO.getAllMessages());
                runOnUiThread(()->{
                    songBinding.recyclerViewSongs.setAdapter(songAdapter);
                        });
                    });
        }



        queue = Volley.newRequestQueue(this);

        songBinding.buttonSong.setOnClickListener(click -> {
            artistName = songBinding.editTextSong.getText().toString();

            // Construct the URL with the artist's name
            String url = null;
            try {
                url = URL_REQUEST_DATA + URLEncoder.encode(artistName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error encoding URL: " + e.getMessage());
                return;
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            // Handle successful response
                            // Get the "data" array from the JSON response
                            JSONArray dataArray = response.getJSONArray("data");
                            String tracklistUrl = null;
                            if (dataArray.length() > 0) {
                                // Get the first object from the array
                                JSONObject firstObject = dataArray.getJSONObject(0);
                                // Get the tracklist URL from the first object
                                tracklistUrl = firstObject.getString("tracklist");
                                songBinding.textViewSong.setText(tracklistUrl);

                                JsonObjectRequest tracklistRequest = new JsonObjectRequest(Request.Method.GET, tracklistUrl, null,
                                        (tracklistResponse) -> {
                                            try {
                                                // Handle successful response for tracklist
                                                // Get the "data" array from the JSON response
                                                JSONArray dataArray2 = tracklistResponse.getJSONArray("data");

                                                songsEntity = new ArrayList<>();

                                                for (int i = 0; i < dataArray2.length(); i++) {
                                                    JSONObject songObject = dataArray2.getJSONObject(i);
                                                    String songTitle = songObject.getString("title");
                                                    int duration = songObject.getInt("duration");
                                                    JSONObject albumObject = songObject.getJSONObject("album");
                                                    String albumName = albumObject.getString("title");
                                                    String albumCoverUrl = albumObject.getString("cover");

                                                    // Create a new Song instance and add it to the list
                                                    Song song = new Song(songTitle, duration, albumName, albumCoverUrl);
                                                    songsEntity.add(song);
                                                }



                                                if (dataArray2.length() > 0) {
                                                    // Get the first object from the array
                                                    JSONObject firstObject2 = dataArray2.getJSONObject(0);
                                                    // Get the title of the first song
                                                    String songTitle = firstObject2.getString("title");
                                                    songBinding.textViewName.setText(songTitle);

                                                    // Get duration, album name, and album cover
                                                    int duration = firstObject2.getInt("duration");
                                                    JSONObject albumObject = firstObject2.getJSONObject("album");
                                                    String albumName = albumObject.getString("title");
                                                    String albumCoverUrl = albumObject.getString("cover");

                                                    // Update UI with album information
                                                    songBinding.textViewDuration.setText("Duration: " + duration);
                                                    songBinding.textViewAlbumName.setText("Album: " + albumName);

                                                    // Load album cover image using a separate thread
                                                    new Thread(() -> {
                                                        try {
                                                            URL url3 = new URL(albumCoverUrl);
                                                            HttpURLConnection connection = (HttpURLConnection) url3.openConnection();
                                                            connection.setDoInput(true);
                                                            connection.connect();
                                                            InputStream input = connection.getInputStream();
                                                            final Bitmap bitmap = BitmapFactory.decodeStream(input);

                                                            // Update ImageView on the main UI thread
                                                            runOnUiThread(() -> songBinding.imageViewAlbumCover.setImageBitmap(bitmap));
                                                        } catch (IOException e) {
                                                            Log.e(TAG, "Error downloading image: " + e.getMessage());
                                                        }
                                                    }).start();
                                                }
                                            } catch (JSONException e) {
                                                Log.e(TAG, "Error parsing tracklist JSON: " + e.getMessage());
                                            }
                                        },
                                        (tracklistError) -> {
                                            // Handle error response for tracklist
                                            Log.e(TAG, "Error fetching tracklist: " + tracklistError.getMessage());
                                        });
                                queue.add(tracklistRequest);
                            } else {
                                // No artist found with the given name
                                songBinding.textViewSong.setText("No tracklist found for the artist: " + artistName);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        }
                    },
                    (error) -> {
                        // Handle error response
                        Log.e(TAG, "Error fetching artist data: " + error.getMessage());
                    });
            queue.add(request);
        });
    }
}
