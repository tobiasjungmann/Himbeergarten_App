package com.example.rpicommunicator_v1.database.compare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_tour_table")
class BikeTour(var from: String, var to: String, var km: Double, var time: String) {

    @PrimaryKey(autoGenerate = true)
    var id = 0

}