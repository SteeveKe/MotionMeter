package com.capucinetulipe.motionmeter.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capucinetulipe.motionmeter.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("Select * from " + MotionMeterDatabase.userTable + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE from " + MotionMeterDatabase.userTable)
    void deleteAll();

    @Query("SELECT * from " + MotionMeterDatabase.userTable + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * from " + MotionMeterDatabase.userTable + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
}
