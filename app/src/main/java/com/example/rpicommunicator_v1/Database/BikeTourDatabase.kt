package com.example.rpicommunicator_v1.Database

import android.content.Context
import androidx.room.Database
import com.example.rpicommunicator_v1.Database.BikeTour
import androidx.room.RoomDatabase
import com.example.rpicommunicator_v1.Database.BikeTourDao
import com.example.rpicommunicator_v1.Database.BikeTourDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BikeTour::class], version = 2)
abstract class BikeTourDatabase : RoomDatabase() {
    abstract fun bikeTourDao(): BikeTourDao?

    companion object {
        private var instance: BikeTourDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): BikeTourDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    BikeTourDatabase::class.java,
                    "biketour_database"
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