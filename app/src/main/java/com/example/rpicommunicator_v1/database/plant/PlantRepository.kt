package com.example.rpicommunicator_v1.database.plant

import android.app.Application
import com.example.rpicommunicator_v1.database.plant.PlantDatabase.Companion.getInstance

import androidx.lifecycle.LiveData

import android.database.sqlite.SQLiteConstraintException
import com.example.rpicommunicator_v1.database.FirebaseAccess

class PlantRepository(application: Application?) {
    private val plantDao: PlantDao?
    val allPlants: LiveData<List<Plant>>
    private val firebaseAccess: FirebaseAccess
    fun insert(plant: Plant) {
        InsertPlantThread(plantDao, plant).start()
    }

    fun update(plant: Plant) {
        UpdatePlantThread(plantDao, plant).start()
    }

    fun reloadFromFirestore() {
        firebaseAccess.fromFirebase
    }

    fun remove(plant: Plant) {
        RemovePlantThread(plantDao, plant).start()
    }

    fun updateWateredInFirebase(id: String?, needsWater: Boolean?) {
        firebaseAccess.updateWateredInFirebase(id, needsWater!!)
    }

    private class InsertPlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            try {
                //  plantRepository.update(plant);
                plantDao!!.insert(plant)
            } catch (e: SQLiteConstraintException) {
                plantDao!!.update(plant)
            }
        }
    }

    private class UpdatePlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            plantDao!!.update(plant)
        }
    }

    private class RemovePlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            plantDao!!.delete(plant)
        }
    }

    companion object {
        const val TAG = "Repository "
    }

    init {
        val database = getInstance(application!!)
        plantDao = database!!.plantDao()
        allPlants = plantDao!!.allPlants
        firebaseAccess = FirebaseAccess(this)
        //firebaseAccess.getFromFirebase();
    }
}