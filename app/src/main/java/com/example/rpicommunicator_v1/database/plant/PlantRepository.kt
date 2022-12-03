package com.example.rpicommunicator_v1.database.plant

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.component.Constants.GPIO_COLOR_3_3V
import com.example.rpicommunicator_v1.component.Constants.GPIO_COLOR_5V
import com.example.rpicommunicator_v1.component.Constants.GPIO_COLOR_GND
import com.example.rpicommunicator_v1.component.Constants.GPIO_COLOR_GPIO
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
    private val plantDao: PlantDao?
    val allPlants: LiveData<List<Plant>>

    private val gpioElementDao: GpioElementDao?
    val allGpioElements: LiveData<List<GpioElement>>

    private val deviceDao: DeviceDao?
    private val allDevices: LiveData<List<Device>>
    private val humidityEntryDao: HumidityEntryDao?
    val allHumidityEntries: LiveData<List<HumidityEntry>>

    // todo overload function to potentially include image paths
    fun insertPlant(plant: Plant) {
        InsertPlantThread(plantDao, plant).start()
    }

    fun updatePlant(plant: Plant) {
        UpdatePlantThread(plantDao, plant).start()
    }

    fun removePlant(plant: Plant) {
        RemovePlantThread(plantDao, plant).start()
    }

    fun addRPi() {
        AddRPiThread(gpioElementDao, deviceDao).start()
    }

    fun getHumidityEntriesForSensorSlot(currentPlant: Plant?): LiveData<List<HumidityEntry>> {
       /* if (currentPlant == null) {
            return;
        } else {
            //todo call query ad return live data with gpioElementId
        }*/
        return allHumidityEntries
    }

    /*fun getHumidityEntriesForSensorSlot(device: Device, sensorSlot: Int){
//return allHumidityEntries.
    }*/

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

    private class RemovePlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            plantDao!!.delete(plant)
        }
    }

    private class AddRPiThread(
        private val gpioElementDao: GpioElementDao?,
        private val deviceDao: DeviceDao?
    ) :
        Thread() {
        override fun run() {


            val device = Device("usb 0")
            deviceDao?.insert(device)
            addRPiPinoutForDevice(device.device)
        }

        private fun addRPiPinoutForDevice(parent: Int) {
            // Added from top to bottom, left to right
            gpioElementDao?.insert(GpioElement(parent, "3.3V", GPIO_COLOR_3_3V))
            gpioElementDao?.insert(GpioElement(parent, "5V", GPIO_COLOR_5V))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 2", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "5V", GPIO_COLOR_5V))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 3", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 4", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 14", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 15", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 17", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 18", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 27", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GND", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 22", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 23", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "3.3V", GPIO_COLOR_3_3V))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 24", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 10", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 9", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 25", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 11", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 8", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 7", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 0", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 1", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 5", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 6", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 12", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 13", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 19", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 16", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 26", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 20", GPIO_COLOR_GPIO))
            gpioElementDao?.insert(GpioElement(parent, "Ground", GPIO_COLOR_GND))
            gpioElementDao?.insert(GpioElement(parent, "GPIO 21", GPIO_COLOR_GPIO))
        }
    }

    init {
        val database = getInstance(application!!)
        plantDao = database!!.plantDao()
        allPlants = plantDao!!.allPlants

        gpioElementDao = database.gpioElementDao()
        allGpioElements = gpioElementDao!!.allGpioElements

        deviceDao = database.deviceDao()
        allDevices = deviceDao!!.allDevices

        humidityEntryDao = database.humidityEntryDao()
        allHumidityEntries = humidityEntryDao!!.allHumidityEntries
    }
}