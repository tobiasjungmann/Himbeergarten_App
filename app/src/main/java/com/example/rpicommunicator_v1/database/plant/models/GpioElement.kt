package com.example.rpicommunicator_v1.database.plant.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gpio_element_table")
class GpioElement(
    val device: Int,
    val label: String,
    val accentColor: Int
) {
    @field:PrimaryKey(autoGenerate = true)
    var gpioElement: Int=0
    var userId = -1
    var nameAtDevice="lorem ipsum"
}