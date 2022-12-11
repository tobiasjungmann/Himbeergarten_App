package com.example.rpicommunicator_v1.component.plant

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.StorageServerGrpc
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_IP
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_PORT
import com.example.rpicommunicator_v1.component.Constants.INVALID_DB_ID
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry
import com.example.rpicommunicator_v1.database.plant.models.Plant
import com.example.rpicommunicator_v1.service.GrpcServerService
import io.grpc.ManagedChannelBuilder
import java.util.*


class PlantViewModel(application: Application) : AndroidViewModel(application) {


    private var grpcStorageServerInterface: GrpcServerService = initStorageGrpcStub()

    private val plantRepository: PlantRepository


    private var currentPlant: Plant? = null
    private var currentGpioTextView: TextView? = null
    private var currentGpioElement: GpioElement? = null

    fun update(plant: Plant) {
        plant.syncedWithServer = false
        plantRepository.updatePlant(plant)
        grpcStorageServerInterface.addUpdatePlant(plant, plantRepository)
    }

    fun remove(plant: Plant) {
        plantRepository.removePlant(plant)
    }

    fun reloadFromServer() {
        Log.d("PlantviewModel", "reloading")
        grpcStorageServerInterface.reloadPlantsFromServer(plantRepository)
    }

    fun setCurrentPlant(position: Int) {
        currentPlant = plantRepository.allPlants.value!![position]
      //  grpcStorageServerInterface.setHumidityTest()
        update(currentPlant!!)// todo only while testing
    }

    fun getCurrentPlant(): Plant? {
        return currentPlant
    }

    fun clearCurrentPlant() {
        currentPlant = null
    }


    fun createUpdateCurrentPlant(name: String, info: String, context: Context?) {
        if (name.trim { it <= ' ' }.isEmpty() || info.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(context, "Insert Title and Description", Toast.LENGTH_SHORT).show()
            return
        }else
        if(currentGpioElement==null){
            Toast.makeText(context, "Select an empty GPIO", Toast.LENGTH_SHORT).show()
        }else {
            if (currentPlant == null) {
                currentPlant = Plant(name, info, currentGpioElement!!.gpioElement)
                plantRepository.insertPlant(currentPlant!!)

            } else {
                // todo check if changes have occurred
                currentPlant!!.name = name
                currentPlant!!.info = info
                currentPlant!!.gpioElement = currentGpioElement!!.gpioElement
                update(currentPlant!!)
            }
        }
    }

    private fun initStorageGrpcStub(): GrpcServerService {
        val mPref: SharedPreferences = this.getApplication<Application>().getSharedPreferences(
            this.getApplication<Application>().resources.getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )
        val ipServer = mPref.getString(
            this.getApplication<Application>().resources.getString(R.string.ADDRESS_SERVER_PREF),
            DEFAULT_SERVER_IP
        )
        val portServer = mPref.getInt(
            this.getApplication<Application>().resources.getString(R.string.PORT_SERVER_PREF),
            DEFAULT_SERVER_PORT
        )


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
        return GrpcServerService(StorageServerGrpc.newStub(mChannel))
    }

    fun gpioSelectedForElement(gpioElement: GpioElement, label: TextView):TextView? {
        val previousTextView=currentGpioTextView
        currentGpioTextView=label
        currentGpioElement = gpioElement
        return previousTextView
    }

    fun getHumidityEntriesForCurrentPlant():LiveData<List<HumidityEntry>> {
        plantRepository.getHumidityEntriesForSensorSlot(currentPlant)
        return plantRepository.currentHumidityEntries
    }

    fun getGpioEntries(): LiveData<List<GpioElement>> {
        return plantRepository.allGpioElements
    }

    fun getAllPlants(): LiveData<List<Plant>> {
        return plantRepository.allPlants
    }

    fun getImageForCurrentPlant(): LiveData<List<PathElement>> {
        return plantRepository.getImageForCurrentPlant(currentPlant?.plant ?: INVALID_DB_ID)
    }

    init {
        plantRepository = PlantRepository(application,grpcStorageServerInterface)
    }
}