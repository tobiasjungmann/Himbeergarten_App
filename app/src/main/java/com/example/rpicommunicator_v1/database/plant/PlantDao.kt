package com.example.rpicommunicator_v1.database.plant

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {
    @Insert
    fun insert(plant: Plant?)

    @Update
    fun update(plant: Plant?)

    @Delete
    fun delete(plant: Plant?)

    @get:Query("SELECT * FROM plant_table")
    val allPlants: LiveData<List<Plant>>
}