package algonquin.cst2335.ju000013.songApi;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import algonquin.cst2335.ju000013.songApi.Song;
import algonquin.cst2335.ju000013.songApi.SongDAO;
import algonquin.cst2335.ju000013.songApi.SongDatabase;

public class SongFavoritesViewModel extends AndroidViewModel {
    private MutableLiveData<List<Song>> songs;
    private SongDAO songDAO;

    public SongFavoritesViewModel(@NonNull Application application) {
        super(application);
        SongDatabase db = SongDatabase.getDatabase(application);
        songDAO = db.sDAO();
        songs = new MutableLiveData<>();
        loadSongs();
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    private void loadSongs() {
        // Run on a background thread
        new Thread(() -> {
            List<Song> songList = songDAO.getAllSongs();
            // Post the result to the UI thread
            new Handler(Looper.getMainLooper()).post(() -> songs.setValue(songList));
        }).start();
    }
}
