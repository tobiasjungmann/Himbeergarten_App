package com.example.rpicommunicator_v1.Database.Bike

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "biketour_table")
class BikeTour(var from: String, var to: String, var km: Double, var time: String) {

    @PrimaryKey(autoGenerate = true)
    var id = 0

}