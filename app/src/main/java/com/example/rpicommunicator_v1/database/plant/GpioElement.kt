package com.example.rpicommunicator_v1.database.plant

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gpio_element_table")
class GpioElement(
    val deviceId: Int,
    val label: String,
    val accentColor: Int
) {
    @field:PrimaryKey(autoGenerate = true)
    var position: Int=0
    var userId = -1
    var nameAtDevice="lorem ipsum"
}