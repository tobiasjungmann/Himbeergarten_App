package com.example.rpicommunicator_v1.database.plant.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.plant.models.Device

@Dao
interface DeviceDao:DefaultDao<Device> {
    @get:Query("SELECT * FROM device_table")
    val allDevices: LiveData<List<Device>>
}