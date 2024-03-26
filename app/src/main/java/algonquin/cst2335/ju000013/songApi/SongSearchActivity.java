package algonquin.cst2335.ju000013.songApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLEncoder;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivityMainBinding;
import algonquin.cst2335.ju000013.databinding.ActivitySongSearchBinding;
import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;


public class SongSearchActivity extends AppCompatActivity {

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
        if (id == R.id.item_1){

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
        ActivitySongSearchBinding binding = ActivitySongSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);

        queue = Volley.newRequestQueue(this);

        binding.buttonSong.setOnClickListener(click -> {
            artistName = binding.editTextSong.getText().toString();

            // Construct the URL with the artist's name
            String url = null;
            try {
                url =URL_REQUEST_DATA + URLEncoder.encode(artistName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        // Handle successful response
                        // Your code for processing the JSON response goes here
                        try {
                            // Get the "data" array from the JSON response
                            Log.d(TAG, "JSON Response: " + response.toString());
                            JSONArray dataArray = response.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                // Get the first object from the array
                                JSONObject firstObject = dataArray.getJSONObject(0);
                                // Get the tracklist URL from the first object
                                String tracklistUrl = firstObject.getString("tracklist");
                                //int id = firstObject.getInt("id");
                                binding.textViewSong.setText(tracklistUrl);

                            } else {
                                // No artist found with the given name
                                binding.textViewSong.setText("No tracklist found for the artist: " + artistName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                        // Handle error response
                        // Your code for handling errors (e.g., logging, displaying error message) goes here
                        Log.e(TAG, "Error:" + error.getMessage());
                    });
            queue.add(request);
        });

    }

}
