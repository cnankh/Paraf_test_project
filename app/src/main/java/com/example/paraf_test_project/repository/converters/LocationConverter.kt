package com.example.paraf_test_project.repository.converters

import androidx.room.TypeConverter
import com.example.paraf_test_project.model.Location
import com.google.gson.Gson
import java.time.Instant

class LocationConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromLocation(value: Location): String {
            val gson = Gson()
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toLocation(value: String): Location {
            val gson = Gson()

            return gson.fromJson(value, Location::class.java)
        }
    }
}