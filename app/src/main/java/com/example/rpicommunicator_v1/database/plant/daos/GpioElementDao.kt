package com.example.rpicommunicator_v1.database.plant.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.plant.models.GpioElement

@Dao
interface GpioElementDao : DefaultDao<GpioElement> {

    @get:Query("SELECT * FROM gpio_element_table")
    val allGpioElements: LiveData<List<GpioElement>>
}