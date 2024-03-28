package algonquin.cst2335.ju000013.songApi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SongViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Song>> songs = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Song>> getSongs() {
        return songs;
    }
}


