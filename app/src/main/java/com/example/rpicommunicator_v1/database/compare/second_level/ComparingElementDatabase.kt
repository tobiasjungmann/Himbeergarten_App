package com.example.rpicommunicator_v1.database.compare.second_level

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ComparingElement::class], version = 6)
abstract class ComparingElementDatabase : RoomDatabase() {
    abstract fun noteDao(): ComparingElementDao


    companion object {
        private var instance: ComparingElementDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): ComparingElementDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, ComparingElementDatabase::class.java,
                    "comparing_element_database"
                ).fallbackToDestructiveMigration().build()
            }
            return instance
        }

    }
}