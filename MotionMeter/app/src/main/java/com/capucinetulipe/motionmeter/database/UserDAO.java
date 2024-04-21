package com.capucinetulipe.motionmeter.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capucinetulipe.motionmeter.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("Select * from " + MotionMeterDatabase.userTable)
    List<User> getAllRecords();
}
