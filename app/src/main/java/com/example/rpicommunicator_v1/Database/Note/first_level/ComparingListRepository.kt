package com.example.rpicommunicator_v1.Database.Note.first_level

import android.app.Application
import androidx.lifecycle.LiveData

class ComparingListRepository(application: Application?) {
    private var listDao: ComparingListDao? = null
    private var allLists: LiveData<List<ComparingList>>


    init {
        val database = ComparingListDatabase.getInstance(application!!)
        listDao = database!!.comparingListDao()
        allLists = listDao!!.getAllLists()
    }

    fun insert(list: ComparingList) {
        InsertListThread(listDao, list).start()
    }

    fun update(list: ComparingList) {
        UpdateListThread(listDao, list).start()
    }

    fun delete(list: ComparingList) {
        DeleteListThread(listDao, list).start()
    }

    fun deleteAllLists() {
        DeleteAllListThread(listDao).start()
    }

    fun getAllLists(): LiveData<List<ComparingList>> {
        return allLists
    }

    private class InsertListThread(listDao: ComparingListDao?, list: ComparingList) : Thread() {

        val comparingList = listDao
        val list = list
        override fun run() {
            comparingList?.insert(list)
        }

    }

    private class UpdateListThread(listDao: ComparingListDao?, list: ComparingList)  : Thread(){

        val comparingList = listDao
        val list = list
        override fun run() {
            comparingList?.update(list)

        }


    }

    private class DeleteListThread(
        listDao: ComparingListDao?,
        list: ComparingList
    )  : Thread(){

        val comparingList = listDao
        val list = list
        override fun run() {
            comparingList?.delete(list)

        }

    }


    private class DeleteAllListThread(listDao: ComparingListDao?) : Thread() {
        val comparingList = listDao
        override fun run() {
            comparingList!!.deleteAllLists()
        }
    }
}

