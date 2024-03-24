package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeSavedDAO {
    @Insert
    public long insertRecipe(RecipeSaved result);

    @Query("select * from RecipeSaved")
    public List<RecipeSaved> getAllSearchResults();

    @Delete
    public void deleteSearchResult(RecipeSaved result);

    @Query("delete from RecipeSaved")
    public void deleteAll();
}
