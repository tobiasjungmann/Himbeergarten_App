package com.example.rpicommunicator_v1.component.plant

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import com.example.rpicommunicator_v1.service.GrpcCommunicatorService
import io.grpc.ManagedChannelBuilder

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val plantRepository: PlantRepository
    val allPlants: LiveData<List<Plant>>


    fun update(plant: Plant) {
        plantRepository.update(plant)
    }

    fun updateWateredInFirebase(id: String?, needsWater: Boolean?) {
        plantRepository.updateWateredInFirebase(id, needsWater)
    }

    fun remove(plant: Plant) {
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
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
    }
}