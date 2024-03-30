package algonquin.cst2335.ju000013.songApi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    long insertSong(Song song); // Change return type to long

    @Query("SELECT * FROM Song")
    List<Song> getAllSongs(); // Rename method to getAllSongs

    @Delete
    void deleteSong(Song song); // Change method name to deleteSong

    // Add a method to delete all songs
    @Query("DELETE FROM Song")
    void deleteAllSongs();

    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);
}
