package com.example.paraf_test_project.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paraf_test_project.model.Venue
import com.example.paraf_test_project.repository.converters.LocationConverter

@Database(entities = [Venue::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class VenueDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao

    companion object {
        @Volatile
        private var instance: VenueDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            VenueDatabase::class.java,
            "venueDatabase"
        ).build()

    }
}