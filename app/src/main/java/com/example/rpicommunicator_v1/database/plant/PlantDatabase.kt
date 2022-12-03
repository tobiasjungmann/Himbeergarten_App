package com.example.rpicommunicator_v1.database.plant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rpicommunicator_v1.database.image.PathElement
import com.example.rpicommunicator_v1.database.plant.daos.DeviceDao
import com.example.rpicommunicator_v1.database.plant.daos.GpioElementDao
import com.example.rpicommunicator_v1.database.plant.daos.HumidityEntryDao
import com.example.rpicommunicator_v1.database.plant.daos.PlantDao
import com.example.rpicommunicator_v1.database.plant.models.Device
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry
import com.example.rpicommunicator_v1.database.plant.models.Plant

@Database(
    entities = [Plant::class, GpioElement::class, Device::class,HumidityEntry::class, PathElement::class],
    version = 15
)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao?
    abstract fun gpioElementDao(): GpioElementDao?
    abstract fun deviceDao(): DeviceDao?
    abstract fun humidityEntryDao(): HumidityEntryDao?

    companion object {
        private var instance: PlantDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): PlantDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plant_database"
                ).fallbackToDestructiveMigration().addCallback(
                    roomCallback
                ).build()
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}