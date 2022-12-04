package com.example.rpicommunicator_v1.database.plant

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.StorageServerOuterClass
import com.example.rpicommunicator_v1.component.Constants.INVALID_DB_ID
import com.example.rpicommunicator_v1.database.compare.daos.PathElementDao
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.database.plant.PlantDatabase.Companion.getInstance
import com.example.rpicommunicator_v1.database.plant.daos.DeviceDao
import com.example.rpicommunicator_v1.database.plant.daos.GpioElementDao
import com.example.rpicommunicator_v1.database.plant.daos.HumidityEntryDao
import com.example.rpicommunicator_v1.database.plant.daos.PlantDao
import com.example.rpicommunicator_v1.database.plant.models.Device
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry
import com.example.rpicommunicator_v1.database.plant.models.Plant

class PlantRepository(application: Application?) {
    private val database: PlantDatabase?
    private val plantDao: PlantDao?
    private val pathDao: PathElementDao?
    val allPlants: LiveData<List<Plant>>

    private val gpioElementDao: GpioElementDao?
    val allGpioElements: LiveData<List<GpioElement>>

    private val deviceDao: DeviceDao?
    private val allDevices: LiveData<List<Device>>
    private val humidityEntryDao: HumidityEntryDao?
    val currentHumidityEntries: LiveData<List<HumidityEntry>>
    val currentPathElements: LiveData<List<PathElement>>

    fun insertPlant(plant: Plant) {
        InsertPlantThread(plantDao, plant).start()
    }

    fun updatePlant(plant: Plant) {
        UpdatePlantThread(plantDao, plant).start()
    }

    fun removePlant(plant: Plant) {
        RemovePlantThread(plantDao, plant).start()
    }

    fun addDevice(device: StorageServerOuterClass.DeviceTypes) {
        AddRPiThread(database, deviceDao, gpioElementDao,device).start()
    }

    fun insertPath(pathElement: String, parentId: Int) {
        InsertImagePathElement(pathDao, pathElement, parentId).start()
    }

    fun getHumidityEntriesForSensorSlot(currentPlant: Plant?) {
        QueryHumidityEntries(humidityEntryDao, currentPlant?.plant ?: -1, currentHumidityEntries)
    }

    fun getImageForCurrentPlant(plant: Int): LiveData<List<PathElement>> {
        // todo start query and return th livedata wrapper
        QueryImagePathsThread(pathDao,plant,currentPathElements)
        return currentPathElements
    }

    private class InsertPlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            try {
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

    private class QueryHumidityEntries(
        private val humidityEntryDao: HumidityEntryDao?,
        private val plantId: Int,
        private var currentHumidityEntries: LiveData<List<HumidityEntry>>
    ) :
        Thread() {
        override fun run() {
            currentHumidityEntries = humidityEntryDao!!.filteredHumidityEntries(plantId)
        }
    }

    private class QueryImagePathsThread(
        private val humidityEntryDao: PathElementDao?,
        private val plantId: Int,
        private var currentPathElements: LiveData<List<PathElement>>
    ) :
        Thread() {
        override fun run() {
            currentPathElements = humidityEntryDao!!.getListPathElementsLiveData(plantId)
        }
    }

    private class RemovePlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            plantDao!!.delete(plant)
        }
    }

    private class AddRPiThread(
        private val database: PlantDatabase?,
        private val deviceDao: DeviceDao?,
        private val gpioElementDao: GpioElementDao?,
        private val device: StorageServerOuterClass.DeviceTypes
    ) :
        Thread() {
        override fun run() {
            database?.addNewDeviceWithPinout(deviceDao, gpioElementDao,device,"usb0 example value")
        }
    }

    private class InsertImagePathElement(
        private val pathElementDao: PathElementDao?,
        private val path: String,
        private val parent: Int
    ) :
        Thread() {
        override fun run() {
            pathElementDao?.insert(PathElement(path, parent))
        }
    }

    init {
        database = getInstance(application!!)
        plantDao = database!!.plantDao()
        pathDao = database.pathDao()
        gpioElementDao = database.gpioElementDao()
        deviceDao = database.deviceDao()
        humidityEntryDao = database.humidityEntryDao()

        allPlants = plantDao!!.allPlants
        allGpioElements = gpioElementDao!!.allGpioElements
        allDevices = deviceDao!!.allDevices
        currentHumidityEntries = humidityEntryDao!!.filteredHumidityEntries(INVALID_DB_ID)
        currentPathElements= pathDao!!.getListPathElementsLiveData(INVALID_DB_ID)

        // todo replace by query and observer
       // allPlants.observe()
       // plantRepository.allPlants.value.orEmpty().filter { v -> !v.syncedWithServer }
       //     .forEach { p -> grpcStorageServerInterface.addUpdatePlant(p, plantRepository) }
    }
}