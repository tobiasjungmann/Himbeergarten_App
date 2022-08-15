package com.example.rpicommunicator_v1.database.plant

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
class Plant(//(autoGenerate = true)
    @field:PrimaryKey var id: String,
    val name: String,
    val info: String,
    val watered: String,
    val humidity: String,
    var needsWater: Boolean,
    val graphString: String
) {
    var imageID = -1
    var iconID = -1

    init {
        Log.d("Constructor", "Plant: needsWater: $needsWater")
    }
}