package com.example.rpicommunicator_v1.database.plant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rpicommunicator_v1.PlantStorageOuterClass
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.component.Constants.GPIO_IN_USE_DEFAULT
import com.example.rpicommunicator_v1.database.compare.daos.PathElementDao
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.database.plant.daos.DeviceDao
import com.example.rpicommunicator_v1.database.plant.daos.GpioElementDao
import com.example.rpicommunicator_v1.database.plant.daos.HumidityEntryDao
import com.example.rpicommunicator_v1.database.plant.daos.PlantDao
import com.example.rpicommunicator_v1.database.plant.models.Device
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry
import com.example.rpicommunicator_v1.database.plant.models.Plant
import java.util.concurrent.Executors

@Database(
    entities = [Plant::class, GpioElement::class, Device::class, HumidityEntry::class, PathElement::class, ComparingElement::class],
    version = 1
)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun gpioElementDao(): GpioElementDao
    abstract fun deviceDao(): DeviceDao
    abstract fun humidityEntryDao(): HumidityEntryDao
    abstract fun pathDao(): PathElementDao


    // is only triggered by the backend if a new device connected to the existing system.
    fun addNewDeviceWithPinout(
        deviceDao: DeviceDao?,
        gpioElementDao: GpioElementDao?,
        deviceType: PlantStorageOuterClass.DeviceTypes,
        interfaceName: String,
        name: String

    ) {
        val id = deviceDao?.insert(Device(interfaceName, deviceType.number, name))
        if (id!!.isNotEmpty()) {
            if (deviceType == PlantStorageOuterClass.DeviceTypes.DEVICE_RPI) {
                addRPiPinout(gpioElementDao, id[0].toInt())
            } else if (deviceType == PlantStorageOuterClass.DeviceTypes.DEVICE_ARDUINO_NANO) {
                addArduinoNanoPinout(gpioElementDao, id[0].toInt())
            }
        }
    }

    fun addArduinoNanoPinout(gpioElementDao: GpioElementDao?, parent: Int) {
        // Added from top to bottom, left to right
        gpioElementDao?.insert(GpioElement(parent, "3.3V", Constants.GPIO_COLOR_3_3V))
    }

    fun addRPiPinout(gpioElementDao: GpioElementDao?, parent: Int) {
        // Added from top to bottom, left to right
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "3.3V",
                Constants.GPIO_COLOR_3_3V,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "5V",
                Constants.GPIO_COLOR_5V,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 2", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "5V",
                Constants.GPIO_COLOR_5V,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 3", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 4", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 14", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 15", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 17", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 18", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 27", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "GND",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 22", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 23", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "3.3V",
                Constants.GPIO_COLOR_3_3V,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 24", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 10", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 9", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 25", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 11", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 8", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 7", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 0", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 1", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 5", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 6", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 12", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 13", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 19", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 16", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 26", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 20", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(
            GpioElement(
                parent,
                "Ground",
                Constants.GPIO_COLOR_GND,
                GPIO_IN_USE_DEFAULT
            )
        )
        gpioElementDao?.insert(GpioElement(parent, "GPIO 21", Constants.GPIO_COLOR_GPIO))
    }


    companion object {
        private var instance: PlantDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): PlantDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plant_database.db"
                ).fallbackToDestructiveMigration().addCallback(object : Callback() {
                    private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
                    fun ioThread(f: () -> Unit) {
                        IO_EXECUTOR.execute(f)
                    }

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            val devicedao = getInstance(context).deviceDao()
                            val gpioDao = getInstance(context).gpioElementDao()
                            val plantDao = getInstance(context).plantDao()
                            val humidityEntryDao = getInstance(context).humidityEntryDao()

                            getInstance(context).addNewDeviceWithPinout(
                                devicedao,
                                gpioDao,
                                PlantStorageOuterClass.DeviceTypes.DEVICE_RPI,
                                "Default",
                                "Pi Station"
                            )
                            getInstance(context).addNewDeviceWithPinout(
                                devicedao,
                                gpioDao,
                                PlantStorageOuterClass.DeviceTypes.DEVICE_ARDUINO_NANO,
                                "Default",
                                "Arduino Test"
                            )

                            populateDB(plantDao, humidityEntryDao)
                        }
                    }
                }).build()
            }
            return instance!!
        }

        private fun populateDB(plantDao: PlantDao, humidityEntryDao: HumidityEntryDao) {
            val gpioId=3
            val id = plantDao.insert(Plant("Default Plant", "info", gpioId))
            if (id.isNotEmpty()) {
                humidityEntryDao.insert(HumidityEntry(100.0F, -6000.0F, gpioId))
                humidityEntryDao.insert(HumidityEntry(400.0F, 1.0F, gpioId))
                humidityEntryDao.insert(HumidityEntry(200.0F, 2.0F, gpioId))
                humidityEntryDao.insert(HumidityEntry(300.0F, 3000.0F, gpioId))
            }
        }
    }
}