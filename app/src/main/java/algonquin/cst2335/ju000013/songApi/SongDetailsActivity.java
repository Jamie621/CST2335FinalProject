package algonquin.cst2335.ju000013.songApi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import algonquin.cst2335.ju000013.databinding.ActivitySongDetailsBinding;

public class SongDetailsActivity extends AppCompatActivity {

    private ActivitySongDetailsBinding songDetailsBinding;
    private SongDatabase songDatabase;
    private SongDAO songDAO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songDetailsBinding = ActivitySongDetailsBinding.inflate(getLayoutInflater());
        setContentView(songDetailsBinding.getRoot());

        songDatabase = Room.databaseBuilder(getApplicationContext(),
                SongDatabase.class, "song-database").build();
        songDAO = songDatabase.sDAO();

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

            songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
                }
            });
        }
    }

    private void insertSongIntoDatabase(String title, int duration, String albumName, String albumCoverUrl){
        Song song = new Song(title, duration ,albumName, albumCoverUrl);
        new Thread(()->{
            songDAO.insertSong(song);
        }).start();
    }
}
