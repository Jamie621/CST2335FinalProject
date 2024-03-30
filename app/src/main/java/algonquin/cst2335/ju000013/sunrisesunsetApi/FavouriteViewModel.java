package algonquin.cst2335.ju000013.sunrisesunsetApi;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class FavouriteViewModel extends AndroidViewModel {

    private FavouriteDao favouriteDao;
    private LiveData<List<SaveLocation>> favouritesList;

    public FavouriteViewModel(Application application) {
        super(application);
        MessageDatabase db = MessageDatabase.getDatabase(application);
        favouriteDao = db.favouriteDao();
        favouritesList = favouriteDao.getFavourites(); // 用于RecyclerView
    }

    public void deleteFavouriteById(int favouriteId) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.deleteFavouriteById(favouriteId);
        });
    }

    public void addFavourite(SaveLocation saveLocation) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.insertFavourite(saveLocation);
        });
    }

    public LiveData<List<SaveLocation>> getFavourites() {
        return favouritesList;
    }


    // 这个方法用于根据用户点击的收藏位置重新查询日出和日落时间
    public void refreshFavourite(int favouriteId) {
        // 获取特定ID的Favourite，发送网络请求，更新数据库记录
    }

    // 更新 SharedPreferences 来保存用户的搜索词
    public void saveSearchTerm(String searchTerm) {
        SharedPreferences prefs = getApplication().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("search_term", searchTerm);
        editor.apply(); // 使用 apply() 而不是 commit() 以异步保存数据
    }

    // 获取 SharedPreferences 中的搜索词
    public String getSavedSearchTerm() {
        SharedPreferences prefs = getApplication().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        return prefs.getString("search_term", null); // 如果没有找到，则返回null
    }

    public void updateFavourite(SaveLocation saveLocation) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.updateFavourite(saveLocation);
        });
    }

    // Inside your FavouriteViewModel class
    public void insertFavourite(SaveLocation saveLocation) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.insertFavourite(saveLocation);
        });
    }
}
