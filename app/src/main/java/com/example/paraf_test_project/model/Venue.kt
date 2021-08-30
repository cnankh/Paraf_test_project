package com.example.paraf_test_project.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class Venue(
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "location")
    var location: Location? = null
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

}

@Entity
data class Location(
    @ColumnInfo(name = "lat")
    val lat: Double? = null,
    @ColumnInfo(name = "lng")
    val lng: Double? = null,
    @ColumnInfo(name = "distance")
    val distance: Long? = null
)

