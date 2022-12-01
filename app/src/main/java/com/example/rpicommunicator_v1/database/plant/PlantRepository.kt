package com.example.rpicommunicator_v1.database.plant

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.plant.PlantDatabase.Companion.getInstance

class PlantRepository(application: Application?) {
    private val plantDao: PlantDao?
    val allPlants: LiveData<List<Plant>>

    private val gpioElementDao: GpioElementDao?
    val allGpioElements: LiveData<List<GpioElement>>

    private val deviceDao: DeviceDao?
    val allDevices: LiveData<List<Device>>


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
        AddRPiThread(gpioElementDao,deviceDao).start()
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

    private class RemovePlantThread(private val plantDao: PlantDao?, private val plant: Plant) :
        Thread() {
        override fun run() {
            plantDao!!.delete(plant)
        }
    }

    private class AddRPiThread(private val gpioElementDao: GpioElementDao?,private val deviceDao: DeviceDao?) :
        Thread() {
        override fun run() {

            // Added from top to bottom, left to right
            val device=Device("usb 0")
            deviceDao?.insert(device)
            gpioElementDao?.insert(GpioElement(device, "3,3V", R.color.gpio_orange))
            gpioElementDao?.insert(GpioElement(device, "5V", R.color.gpio_red))

            gpioElementDao?.insert(GpioElement(device, "GPIO 2", R.color.arduino_turquise))
            gpioElementDao?.insert(GpioElement(device, "5V", R.color.gpio_red))

            gpioElementDao?.insert(GpioElement(device, "GPIO 3", R.color.arduino_turquise))
            gpioElementDao?.insert(GpioElement(device, "Ground", R.color.gpio_brown))

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
    }
}