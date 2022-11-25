package com.example.rpicommunicator_v1.database.plant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Plant::class],
    version = 13
)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao?

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