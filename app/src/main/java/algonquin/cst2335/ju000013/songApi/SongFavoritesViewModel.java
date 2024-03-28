package algonquin.cst2335.ju000013.songApi;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SongFavoritesViewModel extends AndroidViewModel {
    private LiveData<List<Song>> songs;
    private SongDAO songDAO;

    public SongFavoritesViewModel(@NonNull Application application) {
        super(application);
        SongDatabase db = SongDatabase.getDatabase(application);
        songDAO = db.sDAO();
        songs = (LiveData<List<Song>>) songDAO.getAllSongs();
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }
}
