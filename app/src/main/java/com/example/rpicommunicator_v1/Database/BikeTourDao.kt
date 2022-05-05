package com.example.rpicommunicator_v1.Database

import com.example.rpicommunicator_v1.Database.BikeTour
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BikeTourDao {
    @Insert
    fun insert(plant: BikeTour?)

    @Update
    fun update(plant: BikeTour?)

    @Delete
    fun delete(plant: BikeTour?)

    @get:Query("SELECT * FROM biketour_table")
    val allTours: LiveData<List<BikeTour>>
}