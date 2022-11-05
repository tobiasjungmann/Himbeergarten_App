package com.example.rpicommunicator_v1.component.general

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import com.example.rpicommunicator_v1.service.GrpcCommunicatorService
import io.grpc.ManagedChannelBuilder

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    // todo possibly not needed in main anymore - rename
    private val plantRepository: PlantRepository
    val allPlants: LiveData<List<Plant>>
    private var outlets= booleanArrayOf(false,false,false)
    private var grpcCommunicationInterface: GrpcCommunicatorService


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

    // Start GRPC Implementation
    fun outletClicked(outletId: Int):Boolean {
        outlets[outletId]=grpcCommunicationInterface.setOutletState(1,!outlets[outletId])
        return outlets[outletId]
    }

    init {
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
        val mChannel =
            ManagedChannelBuilder.forAddress(Constants.IP, 8010).usePlaintext().build()
        grpcCommunicationInterface =
            GrpcCommunicatorService(CommunicatorGrpc.newBlockingStub(mChannel))
    }
}