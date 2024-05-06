package com.capucinetulipe.motionmeter.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.capucinetulipe.motionmeter.database.entities.Folder;
import com.capucinetulipe.motionmeter.database.entities.Records;
import com.capucinetulipe.motionmeter.database.entities.User;
import com.capucinetulipe.motionmeter.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {User.class, Records.class, Folder.class}, version = 4, exportSchema = false)
public abstract class MotionMeterDatabase extends RoomDatabase {
    private static  final String DATABASE_NAME = "MotionMeterDatabase";

    public static final String recordsTable = "recordsTable";
    public static final String userTable = "userTable";
    public static final String folderTable = "folderTable";


    private static volatile MotionMeterDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static MotionMeterDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (MotionMeterDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MotionMeterDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.UserDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }
    };

    public abstract UserDAO UserDAO();
    public abstract RecordsDAO RecordsDAO();
    public abstract FolderDAO FolderDAO();
}
