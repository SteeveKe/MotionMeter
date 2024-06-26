package com.capucinetulipe.motionmeter.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capucinetulipe.motionmeter.database.entities.Records;

import java.util.List;

@Dao
public interface RecordsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Records records);

    @Query("Select * from " + MotionMeterDatabase.recordsTable)
    List<Records> getAllRecords();

    @Query("Select * from " + MotionMeterDatabase.recordsTable + " WHERE folder_id LIKE :id")
    LiveData<List<Records>> getAllRecordByFolder(int id);
}
