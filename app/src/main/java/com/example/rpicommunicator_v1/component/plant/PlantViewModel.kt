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

    private var currentPlant: Plant? = null

    fun update(plant: Plant) {
        plant.syncedWithServer = false
        plantRepository.update(plant)
        grpcStorageServerInterface.addUpdatePlant(plant, plantRepository)
    }

    fun remove(plant: Plant) {
        plantRepository.remove(plant)
        grpcStorageServerInterface.removePlant(plant)
    }

    fun reloadFromServer() {
        Log.d("PlantviewModel", "reloading")
        grpcStorageServerInterface.reloadPlantsFromServer(plantRepository)
    }

    fun setCurrentPlant(position: Int) {
        currentPlant = allPlants.value!![position]
    }

    fun getCurrentPlant(): Plant? {
        return currentPlant
    }

    fun clearCurrentPlant() {
        currentPlant = null
    }


    fun createUpdateCurrentPlant(title: String, info: String, gpio: Int) {
        if (title.isNotEmpty() || info.isNotEmpty() || gpio > 0) {
            if (currentPlant == null) {
                currentPlant = Plant(title, info, gpio)
                plantRepository.insert(currentPlant!!)
                grpcStorageServerInterface.addUpdatePlant(currentPlant!!, plantRepository)
            } else {
                currentPlant!!.name = title
                currentPlant!!.info = info
                currentPlant!!.gpio = gpio
                update(currentPlant!!)
            }
        }
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

    init {
        plantRepository = PlantRepository(application)
        allPlants = plantRepository.allPlants
        // todo sync unsynced plants with server
    }
}