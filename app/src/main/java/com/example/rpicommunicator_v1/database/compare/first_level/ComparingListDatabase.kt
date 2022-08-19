package com.example.rpicommunicator_v1.database.compare.first_level

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ComparingList::class], version = 3)
abstract class ComparingListDatabase : RoomDatabase() {
    abstract fun comparingListDao(): ComparingListDao

    companion object {

        private var instance: ComparingListDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ComparingListDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComparingListDatabase::class.java,
                    "comparing_list_table"
                ).fallbackToDestructiveMigration().build()
            }
            return instance
        }

    }
}
