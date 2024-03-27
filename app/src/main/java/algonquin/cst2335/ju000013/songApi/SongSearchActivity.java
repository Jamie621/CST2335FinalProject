package algonquin.cst2335.ju000013.songApi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import algonquin.cst2335.ju000013.databinding.SongInfoBinding;
import algonquin.cst2335.ju000013.songApi.SongDAO;
import algonquin.cst2335.ju000013.songApi.SongViewModel;
import algonquin.cst2335.ju000013.songApi.SongDatabase;
import algonquin.cst2335.ju000013.databinding.ActivityMainBinding;
import algonquin.cst2335.ju000013.databinding.ActivitySongSearchBinding;
import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;
import algonquin.cst2335.ju000013.songApi.Song;

public class SongSearchActivity extends AppCompatActivity {
    ActivitySongSearchBinding songBinding;
    ArrayList<Song> songsEntity;
    Song song;
    SongViewModel songViewModel;
    SongAdapter songAdapter;
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

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songsEntity = songViewModel.songs.getValue();

        if (songsEntity == null) {
            songViewModel.songs.postValue(songsEntity = new ArrayList<>());
        }

        queue = Volley.newRequestQueue(this);

        songBinding.btnSongSearch.setOnClickListener(click -> {

            artistName = songBinding.etSong.getText().toString();

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
                                //songBinding.tvUrl.setText(tracklistUrl);

                                JsonObjectRequest tracklistRequest = new JsonObjectRequest(Request.Method.GET, tracklistUrl, null,
                                        (tracklistResponse) -> {
                                            try {
                                                // Handle successful response for tracklist
                                                // Get the "data" array from the JSON response
                                                JSONArray dataArray2 = tracklistResponse.getJSONArray("data");

                                                for (int i = 0; i < dataArray2.length(); i++) {
                                                    JSONObject songObject = dataArray2.getJSONObject(i);
                                                    String songTitle = songObject.getString("title");
                                                    int duration = songObject.getInt("duration");

                                                    JSONObject albumObject = songObject.getJSONObject("album");
                                                    String albumName = albumObject.getString("title");
                                                    String albumCoverUrl = albumObject.getString("cover");

                                                    // Create a new Song instance and add it to the list
                                                    song = new Song(songTitle, duration, albumName, albumCoverUrl);
                                                    songsEntity.add(song);

                                                }
                                                songAdapter.notifyDataSetChanged();
                                                songViewModel.songs.setValue(songsEntity);

//                                                if (dataArray2.length() > 0) {
//                                                    // Get the first object from the array
//                                                    JSONObject firstObject2 = dataArray2.getJSONObject(0);
//                                                    // Get the title of the first song
//                                                    String songTitle2 = firstObject2.getString("title");
//                                                    songBinding.tvSongName.setText(songTitle2);
//
//                                                    // Get duration, album name, and album cover
//                                                    int duration2 = firstObject2.getInt("duration");
//                                                    JSONObject albumObject = firstObject2.getJSONObject("album");
//                                                    String albumName2 = albumObject.getString("title");
//                                                    String albumCoverUrl2 = albumObject.getString("cover");
//
//                                                    // Update UI with album information
//                                                    songBinding.tvDuration.setText(String.valueOf(duration2));
//                                                    songBinding.tvAlbumName.setText(albumName2);
//
//                                                    // Load album cover image using a separate thread
//                                                    new Thread(() -> {
//                                                        try {
//                                                            URL url3 = new URL(albumCoverUrl2);
//                                                            HttpURLConnection connection = (HttpURLConnection) url3.openConnection();
//                                                            connection.setDoInput(true);
//                                                            connection.connect();
//                                                            InputStream input = connection.getInputStream();
//                                                            final Bitmap bitmap = BitmapFactory.decodeStream(input);
//
//                                                            // Update ImageView on the main UI thread
//                                                            runOnUiThread(() -> songBinding.ivAlbumCover.setImageBitmap(bitmap));
//                                                        } catch (IOException e) {
//                                                            Log.e(TAG, "Error downloading image: " + e.getMessage());
//                                                        }
//                                                    }).start();
//                                                }
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
                                //songBinding.tvUrl.setText("No tracklist found for the artist: " + artistName);
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

        songBinding.rvSongs.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(songsEntity,this);
        songBinding.rvSongs.setAdapter(songAdapter);

//        if (songsEntity == null) {
//            songViewModel.songs.postValue(songsEntity = new ArrayList<Song>());
////            ExecutorService threadSong = Executors.newSingleThreadExecutor();
////            threadSong.execute(()->{
////                songsEntity.addAll(sDAO.getAllMessages());
////                runOnUiThread(()->{
////                    songBinding.rvSongs.setAdapter(songAdapter);
////                        });
////                    });
//       }

//        songDB = Room.databaseBuilder(getApplicationContext(),SongDatabase.class, "database-name").build();
//        sDAO = songDB.sDAO();


    }

}



