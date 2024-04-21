package com.capucinetulipe.motionmeter.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.capucinetulipe.motionmeter.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class MotionMeterDatabase extends RoomDatabase {
    private static  final String DATABASE_NAME = "MotionMeter_database";
    public static final String userTable = "userTable";
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
            //Log.i(MainActivity.TAG, "DATABASE CREATED!");
            //TODO: add databaseWriteExecutor.execute() -> {...}
        }
    };

    public abstract UserDAO UserDAO();
}