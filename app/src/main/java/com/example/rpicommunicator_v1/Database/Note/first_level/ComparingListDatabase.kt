package com.example.rpicommunicator_v1.Database.Note.first_level

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ComparingList::class], version = 1)
abstract class ComparingListDatabase : RoomDatabase() {
    abstract fun comparingListDao(): ComparingListDao

 /*   private class PopulateDbAsyncTask private constructor(db: ComparingListDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val listeDao: ListeDao
        protected override fun doInBackground(vararg voids: Void): Void? {
            listeDao.insert(Liste("Title 1", colorEnum.ITEM_BLAU.getName()))
            listeDao.insert(Liste("Title 2", colorEnum.ITEM_ROT.getName()))
            listeDao.insert(Liste("Title 3", colorEnum.ITEM_GELB.getName()))
            return null
        }

        init {
            listeDao = db!!.listeDao()
        }
    }*/

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
