package com.example.rpicommunicator_v1.database.compare.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.compare.models.BikeTour
import com.example.rpicommunicator_v1.database.plant.daos.DefaultDao

@Dao
interface BikeTourDao: DefaultDao<BikeTour> {

    @get:Query("SELECT * FROM bike_tour_table")
    val allTours: LiveData<List<BikeTour>>
}