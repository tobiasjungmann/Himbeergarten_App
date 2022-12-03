package com.example.rpicommunicator_v1.database.compare

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingListDao
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElementDao

@Database(entities = [ComparingElement::class, ComparingList::class], version = 7)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun comparingElementDao(): ComparingElementDao
    abstract fun comparingListDao(): ComparingListDao


    companion object {
        private var instance: LocalDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): LocalDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, LocalDatabase::class.java,
                    "local_database.db"
                ).fallbackToDestructiveMigration().build()
            }
            return instance
        }

    }
}