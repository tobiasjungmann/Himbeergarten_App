package com.example.rpicommunicator_v1.database.plant.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry

@Dao
interface HumidityEntryDao:DefaultDao<HumidityEntry> {
    @get:Query("SELECT * FROM humidity_entry_table")
    val allHumidityEntries: LiveData<List<HumidityEntry>>

    @Query("SELECT * FROM humidity_entry_table where gpioElement = :gpioId")
    fun filteredHumidityEntries(gpioId: Int): LiveData<HumidityEntry>
}