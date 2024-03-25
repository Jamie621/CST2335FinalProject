package algonquin.cst2335.ju000013;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WordEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
}
