package com.example.rpicommunicator_v1.Database.Plant

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

    //Wegen den Livedata als returntyp werden werte in der Liste immer automatisch updated
    @get:Query("SELECT * FROM plant_table")
    val allPlants: LiveData<List<Plant>>
}