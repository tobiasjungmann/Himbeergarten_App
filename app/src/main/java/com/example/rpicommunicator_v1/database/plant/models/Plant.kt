package com.example.rpicommunicator_v1.database.plant.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.rpicommunicator_v1.R

@Entity(tableName = "plant_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = GpioElement::class,
            parentColumns = arrayOf("gpioElement"),
            childColumns = arrayOf("gpioElement"))
    ))
class Plant(
    var name: String,
    var info: String,
    var gpioElement:Int
) {
    @field:PrimaryKey(autoGenerate = true)
    var plant: Int=0
    var iconID = R.drawable.icon_plant
    var syncedWithServer = false
    var humidity: String = ""

}