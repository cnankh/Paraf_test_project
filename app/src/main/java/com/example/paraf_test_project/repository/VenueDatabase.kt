package com.example.paraf_test_project.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.paraf_test_project.model.Venue

@Database(entities = arrayOf(Venue::class), version = 1)
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