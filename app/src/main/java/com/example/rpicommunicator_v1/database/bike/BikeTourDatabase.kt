package com.example.rpicommunicator_v1.database.bike

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BikeTour::class], version = 3)
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