package com.example.rpicommunicator_v1.Database.Note.second_level

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

@Database(entities = [Note::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao


    companion object {
        private var instance: NoteDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, NoteDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().build()
            }
            return instance
        }

    }
}