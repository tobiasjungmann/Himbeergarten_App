package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.Database.Plant.PlantRepository
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Plant.Plant

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val plantRepository: PlantRepository
    val allPlants: LiveData<List<Plant>>
    private var outlet1 = false;
    private var outlet2 = false;
    private var outlet3 = false;


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

    fun toggleOutlet1(): Boolean {
        outlet1 = !outlet1
        return outlet1;
    }

    fun toggleOutlet2(): Boolean {
        outlet2 = !outlet2
        return outlet2;
    }

    fun toggleOutlet3(): Boolean {
        outlet3 = !outlet3
        return outlet3;
    }

    init {

        //networkCommunicator=new NetworkCommunicator();
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
    }
}