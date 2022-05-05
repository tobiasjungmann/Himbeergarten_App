package com.example.rpicommunicator_v1.Database

import android.content.Context
import androidx.room.Database
import com.example.rpicommunicator_v1.Database.Plant
import androidx.room.RoomDatabase
import com.example.rpicommunicator_v1.Database.PlantDao
import com.example.rpicommunicator_v1.Database.PlantDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Plant::class],
    version = 8
) //mehrere entities in die geschweiften klammern version ändern, wenn tabelle verändert
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