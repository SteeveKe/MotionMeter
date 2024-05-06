package com.capucinetulipe.motionmeter.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.capucinetulipe.motionmeter.database.MotionMeterDatabase;

import java.util.Objects;

@Entity(tableName = MotionMeterDatabase.folderTable, foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE)
})
public class Folder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(index = true)
    private int user_id;

    private String folderName;

    private boolean isGlobal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return id == folder.id && user_id == folder.user_id && isGlobal == folder.isGlobal && Objects.equals(folderName, folder.folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, folderName, isGlobal);
    }

    public Folder(int user_id, String folderName) {
        this.user_id = user_id;
        this.folderName = folderName;
        isGlobal = false;
    }
}
