package com.example.rpicommunicator_v1.database.plant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.database.PathElement.image.PathElementDao
import com.example.rpicommunicator_v1.database.image.PathElement
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
    entities = [Plant::class, GpioElement::class, Device::class, HumidityEntry::class, PathElement::class],
    version = 1
)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao?
    abstract fun gpioElementDao(): GpioElementDao?
    abstract fun deviceDao(): DeviceDao?
    abstract fun humidityEntryDao(): HumidityEntryDao?
    abstract fun pathDao(): PathElementDao?



    fun addRPiPinoutForDevice(deviceDao: DeviceDao?, gpioElementDao: GpioElementDao?) {
        val device = Device("Default")
        deviceDao?.insert(device)
        val parent=device.device

        // Added from top to bottom, left to right
        gpioElementDao?.insert(GpioElement(parent, "3.3V", Constants.GPIO_COLOR_3_3V))
        gpioElementDao?.insert(GpioElement(parent, "5V", Constants.GPIO_COLOR_5V))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 2", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "5V", Constants.GPIO_COLOR_5V))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 3", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 4", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 14", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 15", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 17", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 18", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 27", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GND", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 22", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 23", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "3.3V", Constants.GPIO_COLOR_3_3V))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 24", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 10", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 9", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 25", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 11", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 8", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 7", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 0", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 1", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 5", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 6", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 12", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 13", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 19", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 16", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 26", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 20", Constants.GPIO_COLOR_GPIO))
        gpioElementDao?.insert(GpioElement(parent, "Ground", Constants.GPIO_COLOR_GND))
        gpioElementDao?.insert(GpioElement(parent, "GPIO 21", Constants.GPIO_COLOR_GPIO))

    }


    companion object {
        private var instance: PlantDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): PlantDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plant_database.db"
                ).fallbackToDestructiveMigration().addCallback(object : Callback() {
                    private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
                    fun ioThread(f : () -> Unit) {
                        IO_EXECUTOR.execute(f)
                    }
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                        getInstance(context)?.addRPiPinoutForDevice(getInstance(context)?.deviceDao(),
                            getInstance(context)?.gpioElementDao()
                        )}
                    }
                }).build()
            }
            return instance
        }
    }
}