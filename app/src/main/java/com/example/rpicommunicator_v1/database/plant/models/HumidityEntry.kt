package com.example.rpicommunicator_v1.database.plant.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "humidity_entry_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = GpioElement::class,
            parentColumns = arrayOf("gpioElement"),
            childColumns = arrayOf("gpioElement"))
    ))
class HumidityEntry(
    val value: Float,
    val timestamp: Float,
    val gpioElement: Int
) {
    @field:PrimaryKey(autoGenerate = true)
    var humidityEntry: Int = 0
}