package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class RecipeSearched {

    public String title;
    public String image_url;
    public int id;

    public RecipeSearched() {
    }

    public RecipeSearched(String title, String image_url, int id) {
        this.title = title;
        this.image_url = image_url;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
