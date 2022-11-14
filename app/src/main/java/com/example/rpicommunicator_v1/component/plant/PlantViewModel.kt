package com.example.rpicommunicator_v1.component.plant

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.StorageServerGrpc
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import com.example.rpicommunicator_v1.service.GrpcStorageServerService
import io.grpc.ManagedChannelBuilder
import java.util.*

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private var grpcStorageServerInterface: GrpcStorageServerService = initStorageGrpcStub()


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

    fun setHumidityTest() {
        grpcStorageServerInterface.setHumidityTest()
    }

    fun addPlant(name: String, type: String, info: String) {
        grpcStorageServerInterface.addPlant(name,type,info)
    }

    init {
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
    }

    private fun initStorageGrpcStub(): GrpcStorageServerService {
        val wildcardConfig: MutableMap<String, Any> = HashMap()
        wildcardConfig["name"] = listOf(emptyMap<Any, Any>())
        wildcardConfig["timeout"] = "7s"
        val mChannel =
            ManagedChannelBuilder.forAddress("192.168.0.8", 12346).usePlaintext()
                .defaultServiceConfig(
                    Collections.singletonMap(
                        "methodConfig", listOf(
                            wildcardConfig
                        )
                    )
                ).build()
        return GrpcStorageServerService(StorageServerGrpc.newStub(mChannel))
    }
}