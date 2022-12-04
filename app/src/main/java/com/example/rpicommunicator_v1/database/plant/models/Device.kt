package com.example.rpicommunicator_v1.database.plant.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
class Device(var serialComPort: String, var type: Int) {
    @field:PrimaryKey(autoGenerate = true)
    var device = -1
}