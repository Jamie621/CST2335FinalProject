package algonquin.cst2335.ju000013;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Dao
public interface WordDao {
    @Insert
    long insertWord(WordEntity word);

/*    @Delete
    void deleteWord(WordEntity word);*/

    @Query("SELECT * FROM words")
    List<WordEntity> getAllWords();

    @Query("DELETE FROM words WHERE id IN (:ids)")
    void deleteWordsByIds(List<Long> ids);
}