package com.example.shoppinglist.data.convertors

import androidx.room.TypeConverter
import java.util.Date

// Converters for Room to handle Date type
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}