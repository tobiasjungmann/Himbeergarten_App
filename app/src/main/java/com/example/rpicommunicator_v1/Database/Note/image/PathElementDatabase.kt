package com.example.rpicommunicator_v1.Database.Note.image

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.rpicommunicator_v1.Database.PathElement.image.PathElementDao

@Database(entities = [PathElement::class], version = 1)
abstract class PathElementDatabase : RoomDatabase() {
    abstract fun pathElementDao(): PathElementDao


    companion object {
        private var instance: PathElementDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): PathElementDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, PathElementDatabase::class.java,
                    "path_element_database"
                ).fallbackToDestructiveMigration().build()
            }
            return instance
        }
    }
}