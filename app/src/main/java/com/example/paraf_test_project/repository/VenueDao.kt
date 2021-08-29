package com.example.paraf_test_project.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.paraf_test_project.model.Venue

@Dao
interface VenueDao {
    @Insert
    suspend fun insertAll(vararg venue: Venue): List<Long>

    @Query("SELECT * FROM venue")
    suspend fun getAllVenues(): List<Venue>

    @Query("SELECT * FROM venue WHERE uuid= :venueId")
    suspend fun getVenue(venueId: Int): Venue

    @Query("DELETE FROM venue")
    suspend fun deleteAllVenues()
}