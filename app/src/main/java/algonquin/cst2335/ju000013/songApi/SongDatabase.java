package algonquin.cst2335.ju000013.songApi;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class},version =1)
public abstract class SongDatabase extends RoomDatabase {
    public abstract SongDAO sDAO();
    private static volatile SongDatabase INSTANCE;

    public static synchronized SongDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongDatabase.class, "song-database")
                    .build();
        }
        return INSTANCE;
    }
}
