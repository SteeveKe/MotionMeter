package com.capucinetulipe.motionmeter.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.capucinetulipe.motionmeter.database.MotionMeterDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = MotionMeterDatabase.recordsTable, foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE)
})
public class Records {
    @PrimaryKey(autoGenerate = true)
    private int id;



    @ColumnInfo(index = true)
    private int user_id;

    private LocalDateTime dateAtRecord;

    public Records(int user_id) {
        this.user_id = user_id;
        this.dateAtRecord = LocalDateTime.now();
    }

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

    public LocalDateTime getDateAtRecord() {
        return dateAtRecord;
    }

    public void setDateAtRecord(LocalDateTime dateAtRecord) {
        this.dateAtRecord = dateAtRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Records records = (Records) o;
        return getId() == records.getId() && getUser_id() == records.getUser_id() && Objects.equals(getDateAtRecord(), records.getDateAtRecord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser_id(), getDateAtRecord());
    }


}
