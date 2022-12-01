package com.example.rpicommunicator_v1.database.plant

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DeviceDao {
    @Insert
    fun insert(device: Device)

    @Update
    fun update(device: Device)

    @Delete
    fun delete(device: Device)

    @get:Query("SELECT * FROM device_table")
    val allDevices: LiveData<List<Device>>
}