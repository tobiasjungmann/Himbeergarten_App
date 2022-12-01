package com.example.rpicommunicator_v1.database.plant

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
class Device(var serialComPort: String) {
    @field:PrimaryKey(autoGenerate = true)
    var id = -1
}