package com.example.rpicommunicator_v1.database.plant

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rpicommunicator_v1.R

@Entity(tableName = "plant_table")
class Plant(
    var name: String,
    var info: String,
    var gpio: Int
) {
    @field:PrimaryKey(autoGenerate = true)
    var id: Int=0
    var imageID = R.drawable.plant2
    var iconID = R.drawable.icon_plant
    var syncedWithServer = false
    var humidity: String = ""
    var needsWater: Boolean = false
    var graphString: String = ""
    var lastWatered: String = ""
}