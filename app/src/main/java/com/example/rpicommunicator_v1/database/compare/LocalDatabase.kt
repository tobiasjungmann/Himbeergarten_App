package com.example.rpicommunicator_v1.database.compare

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rpicommunicator_v1.database.compare.daos.BikeTourDao
import com.example.rpicommunicator_v1.database.compare.daos.ComparingElementDao
import com.example.rpicommunicator_v1.database.compare.daos.ComparingListDao
import com.example.rpicommunicator_v1.database.compare.daos.PathElementDao
import com.example.rpicommunicator_v1.database.compare.models.BikeTour
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.ComparingList
import com.example.rpicommunicator_v1.database.compare.models.PathElement

@Database(
    entities = [ComparingElement::class, ComparingList::class, BikeTour::class, PathElement::class],
    version = 8
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun comparingElementDao(): ComparingElementDao
    abstract fun comparingListDao(): ComparingListDao
    abstract fun bikeTourDao(): BikeTourDao
    abstract fun pathElementDao(): PathElementDao


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