package com.capucinetulipe.motionmeter.database.typeConverters;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTypeConverter {
    @TypeConverter
    public long convertDateToLong(LocalDateTime date){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }

    @TypeConverter
    public LocalDateTime convertLongToDate(Long dateLong){
        Instant instant = Instant.ofEpochMilli(dateLong);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
