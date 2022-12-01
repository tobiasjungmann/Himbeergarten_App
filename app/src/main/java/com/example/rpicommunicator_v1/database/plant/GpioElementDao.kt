package com.example.rpicommunicator_v1.database.plant

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GpioElementDao {
    @Insert
    fun insert(gpioElement: GpioElement?)

    @Update
    fun update(gpioElement: GpioElement?)

    @Delete
    fun delete(gpioElement: GpioElement?)

    @get:Query("SELECT * FROM gpio_element_table")
    val allGpioElements: LiveData<List<GpioElement>>
}