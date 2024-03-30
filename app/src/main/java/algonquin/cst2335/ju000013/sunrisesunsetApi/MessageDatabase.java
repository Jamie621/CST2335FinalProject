package algonquin.cst2335.ju000013.sunrisesunsetApi;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Specify the entities and the version number for the database
@Database(entities = {SaveLocation.class}, version = 1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static volatile MessageDatabase INSTANCE;

    // Define the DAOs that work with the database
    public abstract FavouriteDao favouriteDao();

    // Define the number of threads for the ExecutorService
    private static final int NUMBER_OF_THREADS = 4;

    // Create an ExecutorService with a fixed thread pool to be used for database operations
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Method to get the singleton instance of the database
    public static MessageDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MessageDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MessageDatabase.class, "message_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this simplified code sample.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
