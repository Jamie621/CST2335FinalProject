package algonquin.cst2335.ju000013;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insert(WordEntity word);

    @Query("SELECT * FROM words")
    List<WordEntity> getAllWords();

    @Delete
    void delete(WordEntity word);
}
