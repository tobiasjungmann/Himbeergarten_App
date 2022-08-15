package com.example.rpicommunicator_v1.service

import com.example.rpicommunicator_v1.database.plant.PlantDao
import com.example.rpicommunicator_v1.database.plant.Plant
import android.database.sqlite.SQLiteConstraintException

class NetworkCommunicator : Thread() {
    private var plantDao: PlantDao? = null
    override fun run() {
    }

    private fun addOrUpdate(plant: Plant) {
        try {
            plantDao!!.insert(plant)
        } catch (e: SQLiteConstraintException) {
            plantDao!!.update(plant)
        }
    }

    fun setPlantDao(plantDao: PlantDao?) {
        this.plantDao = plantDao
    }
}