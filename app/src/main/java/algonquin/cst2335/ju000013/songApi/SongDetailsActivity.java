package algonquin.cst2335.ju000013.songApi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import algonquin.cst2335.ju000013.databinding.ActivitySongDetailsBinding;

public class SongDetailsActivity extends AppCompatActivity {

    private ActivitySongDetailsBinding songDetailsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songDetailsBinding = ActivitySongDetailsBinding.inflate(getLayoutInflater());
        setContentView(songDetailsBinding.getRoot());

        // Get the extras passed from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
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

            // Load album cover image (if available)
            // You can use your preferred method to load images from URLs here
            // For example, using Picasso, Glide, etc.
        }
    }
}
