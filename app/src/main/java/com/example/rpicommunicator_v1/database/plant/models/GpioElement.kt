package com.example.rpicommunicator_v1.database.plant.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "gpio_element_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = Device::class,
            parentColumns = arrayOf("device"),
            childColumns = arrayOf("device")),
        /*ForeignKey(entity = Plant::class,
            parentColumns = arrayOf("plant"),
            childColumns = arrayOf("plant"))*/
    ))
class GpioElement(
    val device: Int,
    val label: String,
    val accentColor: Int,
    var plant: Int = -1
) {
    @field:PrimaryKey(autoGenerate = true)
    var gpioElement: Int=0

    var nameAtDevice="lorem ipsum"
}