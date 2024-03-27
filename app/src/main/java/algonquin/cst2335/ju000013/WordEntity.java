package algonquin.cst2335.ju000013;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "definition")
    public String definition;

    public WordEntity(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }


}
