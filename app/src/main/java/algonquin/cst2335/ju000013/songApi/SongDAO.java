package algonquin.cst2335.ju000013.songApi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    public long insertSong (Song s);

    @Query("Select * from Song")
    public List<Song> getAllMessages();

    @Delete
    Void deleteMessage (Song s);
}
