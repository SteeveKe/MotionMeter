package com.capucinetulipe.motionmeter.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capucinetulipe.motionmeter.database.entities.Folder;
import com.capucinetulipe.motionmeter.database.entities.Records;

import java.util.List;

@Dao
public interface FolderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Folder folder);

    @Query("Select * from " + MotionMeterDatabase.folderTable + " WHERE user_id LIKE :userID OR isGlobal")
    LiveData<List<Folder>> getAllFolderByUserID(int userID);

    @Query("Select * from " + MotionMeterDatabase.folderTable + " WHERE folderName LIKE :folder AND user_id LIKE :id")
    LiveData<Folder> getFolderName(String folder, int id);
}
