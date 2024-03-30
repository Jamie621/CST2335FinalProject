package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import algonquin.cst2335.ju000013.sunrisesunsetApi.SaveLocation;

import java.util.List;

@Dao
public interface FavouriteDao {
    // 插入一个新的收藏位置
    @Insert
    Long insertFavourite(SaveLocation saveLocation);

    // 检索所有收藏位置
    @Query("SELECT * FROM favourites")
    LiveData<List<SaveLocation>> getFavourites();

    // 根据ID删除一个收藏位置
    @Query("DELETE FROM favourites WHERE id = :favouriteId")
    int deleteFavouriteById(int favouriteId);

    // 根据ID查找一个收藏位置，用于重新查询日出和日落时间
    @Query("SELECT * FROM favourites WHERE id = :favouriteId")
    SaveLocation getFavouriteById(int favouriteId);

    // Add this method to update a SaveLocation
    @Update
    int updateFavourite(SaveLocation saveLocation);
}
