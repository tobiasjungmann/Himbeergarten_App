package com.example.rpicommunicator_v1.database.plant

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.StorageServerOuterClass
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
import com.example.rpicommunicator_v1.service.GrpcServerService

class PlantRepository(
    application: Application?,
    private val grpcStorageServerInterface: GrpcServerService
) {
    private var database: PlantDatabase
    private var pathDao: PathElementDao
    private var plantDao: PlantDao
    private var gpioElementDao: GpioElementDao
    private var deviceDao: DeviceDao

    val allPlants: LiveData<List<Plant>>

    val allGpioElements: LiveData<List<GpioElement>>
    private val allDevices: LiveData<List<Device>>
    private val humidityEntryDao: HumidityEntryDao?
    private val currentHumidityEntries: MutableLiveData<List<HumidityEntry>>
    private val currentGPIOElements: MutableLiveData<List<GpioElement>>
    private val currentThumbnails: LiveData<List<PathElement>>


    fun insert(plant: Plant) {
        InsertPlantThread(
            plantDao,
            null,
            plant,
            null,
            grpcStorageServerInterface,
            this,
            listOf(),
            pathDao
        ).start()
    }

    fun insert(plant: Plant, gpioElement: GpioElement, paths: List<String>) {
        InsertPlantThread(
            plantDao,
            gpioElementDao,
            plant,
            gpioElement,
            grpcStorageServerInterface,
            this,
            paths,
            pathDao
        ).start()
    }

    fun update(plant: Plant, paths: List<String>) {
        UpdatePlantThread(plantDao, plant, grpcStorageServerInterface, this, pathDao, paths).start()
    }

    fun remove(plant: Plant) {
        RemovePlantThread(plantDao, plant, grpcStorageServerInterface).start()
    }

    fun insert(device: StorageServerOuterClass.DeviceTypes) {
        AddRPiThread(database, deviceDao, gpioElementDao, device).start()
    }

    fun insert(pathElement: String, parentId: Int) {
        InsertImagePathElement(pathDao, pathElement, parentId).start()
    }

    fun getValuesForSensorSlot(gpioElement: Int) {
        QueryHumidityEntriesThread(humidityEntryDao, gpioElement, currentHumidityEntries).start()
    }

    fun getThumbnailsForList(): LiveData<List<PathElement>> {
        return currentThumbnails
    }

    fun updateConnectedDevices() {
        Log.d("Repository", "updateConnectedDevices: called - not yet implemented")
    }

    fun queryMutableGpioEntries(device: String) {
        QueryGpioElementsThread(gpioElementDao, device, currentGPIOElements).start()
    }

    fun getMutableGpioEntries(): LiveData<List<GpioElement>> {
        return currentGPIOElements
    }

    fun getAllDevices(): LiveData<List<Device>>{
        return deviceDao.allDevices
    }

    fun getCurrentHumidityEntries(): LiveData<List<HumidityEntry>> {
        return currentHumidityEntries
    }

    private class InsertPlantThread(
        private val plantDao: PlantDao,
        private val gpioElementDao: GpioElementDao?,
        private val plant: Plant,
        private val gpioElement: GpioElement?,
        private val grpcStorageServerInterface: GrpcServerService,
        private val plantRepository: PlantRepository,
        private val paths: List<String>,
        private val pathElementDao: PathElementDao,
    ) :
        Thread() {
        override fun run() {
            try {
                val id = plantDao.insert(plant)
                if (id.isNotEmpty()) {
                    if (gpioElement != null && gpioElementDao != null) {
                        gpioElement.plant = id[0].toInt()
                        gpioElementDao.update(gpioElement)
                    }
                    grpcStorageServerInterface.addUpdatePlant(plant, plantRepository)

                    for (s in paths) {
                        pathElementDao.insert(PathElement(s, id.first().toInt()))
                    }
                }
            } catch (e: SQLiteConstraintException) {
                plantDao.update(plant)
            }
        }
    }

    private class UpdatePlantThread(
        private val plantDao: PlantDao?,
        private val plant: Plant,
        private val grpcStorageServerInterface: GrpcServerService,
        private val plantRepository: PlantRepository,
        private val pathDao: PathElementDao,
        private val paths: List<String>
    ) :
        Thread() {
        override fun run() {
            plantDao!!.update(plant)
            grpcStorageServerInterface.addUpdatePlant(plant, plantRepository)
            // todo update only if necessary
            for (s in paths) {
                pathDao.insert(PathElement(s, plant.plant))
            }
        }
    }

    private class QueryHumidityEntriesThread(
        private val humidityEntryDao: HumidityEntryDao?,
        private val plantId: Int,
        private var currentHumidityEntries: MutableLiveData<List<HumidityEntry>>
    ) :
        Thread() {
        override fun run() {
            currentHumidityEntries.postValue(humidityEntryDao!!.filteredHumidityEntries(plantId))
        }
    }

    private class QueryGpioElementsThread(
        private val gpioElementDao: GpioElementDao?,
        private val device: String,
        private var currentGpioElements: MutableLiveData<List<GpioElement>>
    ) :
        Thread() {
        override fun run() {
            currentGpioElements.postValue(gpioElementDao!!.queryEntriesForDeviceByName(device))
        }
    }

    private class RemovePlantThread(
        private val plantDao: PlantDao?,
        private val plant: Plant,
        private val grpcStorageServerInterface: GrpcServerService
    ) :
        Thread() {
        override fun run() {
            plantDao!!.delete(plant)
            grpcStorageServerInterface.removePlant(plant)
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
            database?.addNewDeviceWithPinout(
                deviceDao,
                gpioElementDao,
                device,
                "usb0 example value",
            "New Device"
            )
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
        plantDao = database.plantDao()
        pathDao = database.pathDao()
        gpioElementDao = database.gpioElementDao()
        deviceDao = database.deviceDao()
        humidityEntryDao = database.humidityEntryDao()

        allPlants = plantDao.allPlants
        allGpioElements = gpioElementDao.allGpioElements
        allDevices = deviceDao.allDevices
        currentHumidityEntries = MutableLiveData()
        currentGPIOElements = MutableLiveData()
        currentThumbnails = pathDao.allPathElements

        // todo replace by query and observer
        // allPlants.observe()
        // plantDao.allPlants.value.orEmpty().filter { v -> !v.syncedWithServer }
        //     .forEach { p -> grpcStorageServerInterface.addUpdatePlant(p, plantRepository) }
    }
}