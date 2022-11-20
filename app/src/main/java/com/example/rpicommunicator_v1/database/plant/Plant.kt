package com.example.rpicommunicator_v1.database.plant

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
class Plant(//(autoGenerate = true)
//    @field:PrimaryKey var id: String,
    var name: String,
    var info: String,
    var watered: String,
    val humidity: String,
    var needsWater: Boolean,
    val graphString: String
) {
    @field:PrimaryKey var id: String="-1"
    var imageID = -1
    var iconID = -1

    init {
        Log.d("Constructor", "Plant: needsWater: $needsWater")
    }
}