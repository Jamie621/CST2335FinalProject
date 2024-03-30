package algonquin.cst2335.ju000013;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "definition")
    public String definition;

    @ColumnInfo(name = "saved_date")
    public long savedDate;

    // Constructor
    public WordEntity(String word, String definition, long savedDate) {
        this.word = word;
        this.definition = definition;
        this.savedDate = savedDate;
    }

    // Getters and setters if you're not using public fields
    // It's generally a good idea to keep fields private and access them via getters/setters
 /*   public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public long getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(long savedDate) {
        this.savedDate = savedDate;
    }*/
}