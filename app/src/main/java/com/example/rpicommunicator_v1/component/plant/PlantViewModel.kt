package com.example.rpicommunicator_v1.component.plant

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.StorageServerGrpc
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_IP
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_PORT
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
        grpcStorageServerInterface.setHumidityTest()
        update(currentPlant!!)// todo only while testing
    }

    fun getCurrentPlant(): Plant? {
        return currentPlant
    }

    fun clearCurrentPlant() {
        currentPlant = null
    }


    fun createUpdateCurrentPlant(name: String, info: String, gpio: Int) {
        if (name.isNotEmpty() || info.isNotEmpty() || gpio > 0) {
            if (currentPlant == null) {
                currentPlant = Plant(name, info, gpio)
                plantRepository.insert(currentPlant!!)
                grpcStorageServerInterface.addUpdatePlant(currentPlant!!, plantRepository)
            } else {
                // todo check if changes have occured
                currentPlant!!.name = name
                currentPlant!!.info = info
                currentPlant!!.gpio = gpio
                update(currentPlant!!)
            }
        }
    }

    private fun initStorageGrpcStub(): GrpcStorageServerService {
        val mPref: SharedPreferences = this.getApplication<Application>().getSharedPreferences(this.getApplication<Application>().resources.getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )
        val ipServer = mPref.getString(this.getApplication<Application>().resources.getString(R.string.ADDRESS_SERVER_PREF), DEFAULT_SERVER_IP)
        val portServer = mPref.getInt(this.getApplication<Application>().resources.getString(R.string.PORT_SERVER_PREF), DEFAULT_SERVER_PORT)


        val wildcardConfig: MutableMap<String, Any> = HashMap()
        wildcardConfig["name"] = listOf(emptyMap<Any, Any>())
        wildcardConfig["timeout"] = "7s"
        val mChannel =
            ManagedChannelBuilder.forAddress(ipServer, portServer).usePlaintext()
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

        allPlants.value.orEmpty().filter { v -> !v.syncedWithServer }
            .forEach { p -> grpcStorageServerInterface.addUpdatePlant(p, plantRepository) }
    }
}