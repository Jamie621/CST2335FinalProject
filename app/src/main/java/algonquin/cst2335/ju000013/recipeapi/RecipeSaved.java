package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecipeSaved {
    @PrimaryKey
    @ColumnInfo (name = "id")
    public int id;
    @ColumnInfo (name = "image_url")
    private String image_url;
    @ColumnInfo (name = "summary")
    private String summary;
    @ColumnInfo (name = "source_url")
    private String source_url;

    public RecipeSaved() {
    }

    public RecipeSaved(String image_url, String summary, String source_url) {
        this.image_url = image_url;
        this.summary = summary;
        this.source_url = source_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
