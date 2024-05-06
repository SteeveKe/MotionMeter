package com.capucinetulipe.motionmeter.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.capucinetulipe.motionmeter.database.MotionMeterDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = MotionMeterDatabase.recordsTable, foreignKeys = {@ForeignKey(entity = Folder.class,
        parentColumns = "id",
        childColumns = "folder_id",
        onDelete = ForeignKey.CASCADE)
})
public class Records {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(index = true)
    private int folder_id;

    private double speed;

    private LocalDateTime dateAtRecord;

    public Records(int user_id) {
        this.folder_id = user_id;
        this.dateAtRecord = LocalDateTime.now();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public LocalDateTime getDateAtRecord() {
        return dateAtRecord;
    }

    public void setDateAtRecord(LocalDateTime dateAtRecord) {
        this.dateAtRecord = dateAtRecord;
    }

    public Records(double speed, LocalDateTime dateAtRecord) {
        this.speed = speed;
        this.dateAtRecord = dateAtRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Records records = (Records) o;
        return id == records.id && folder_id == records.folder_id && Double.compare(speed, records.speed) == 0 && Objects.equals(dateAtRecord, records.dateAtRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, folder_id, speed, dateAtRecord);
    }
}
