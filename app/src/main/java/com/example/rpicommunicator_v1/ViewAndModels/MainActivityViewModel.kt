package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.Database.PlantRepository
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Plant

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val plantRepository: PlantRepository
    val allPlants: LiveData<List<Plant>>
    fun update(plant: Plant?) {
        plantRepository.update(plant)
    }

    fun updateWateredInFirebase(id: String?, needsWater: Boolean?) {
        plantRepository.updateWateredInFirebase(id, needsWater)
    }

    fun remove(plant: Plant?) {
        plantRepository.remove(plant)
    }

    fun reloadFromFirestore() {
        Log.d("PlantviewModel", "reloading")
        plantRepository.reloadFromFirestore()
    }

    fun getActPlant(position: Int): Plant {
        return allPlants.value!![position]
    }

    init {

        //networkCommunicator=new NetworkCommunicator();
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
    }
}