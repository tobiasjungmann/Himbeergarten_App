package com.example.rpicommunicator_v1.database.plant.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.plant.models.Plant

@Dao
interface PlantDao : DefaultDao<Plant> {
    @get:Query("SELECT * FROM plant_table")
    val allPlants: LiveData<List<Plant>>
}