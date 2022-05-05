package com.example.rpicommunicator_v1.Communication

import com.example.rpicommunicator_v1.Database.PlantDao
import com.example.rpicommunicator_v1.Database.Plant
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