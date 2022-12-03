package com.example.rpicommunicator_v1.database.compare

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.LocalDatabase.Companion.getInstance
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingListDao
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElementDao


class LocalRepository(application: Application?) {
    private val comparingElementDao: ComparingElementDao
    private val allComparingElements: LiveData<List<ComparingElement>>
    private var listDao: ComparingListDao? = null
    private var allComparingLists: LiveData<List<ComparingList>>

    fun insertComparingElement(comparingElement: ComparingElement?) {
        InsertComparingElementThread(comparingElementDao, comparingElement).start()
    }

    fun updateComparingElement(comparingElement: ComparingElement?) {
        UpdateComparingElementThread(comparingElementDao, comparingElement).start()
    }

    fun deleteComparingElement(comparingElement: ComparingElement?) {
        DeleteComparingElementThread(comparingElementDao, comparingElement).start()
    }

    fun getComparingElementsById(listID: Int): LiveData<List<ComparingElement>> {
        return comparingElementDao.getListNotes(listID)
    }

    fun insertComparingList(list: ComparingList) {
        InsertComparingListThread(listDao, list).start()
    }

    fun updateComparingList(list: ComparingList) {
        UpdateComparingListThread(listDao, list).start()
    }

    fun deleteComparingList(list: ComparingList) {
        DeleteComparingListThread(listDao, list).start()
    }

    fun getAllLists(): LiveData<List<ComparingList>> {
        return allComparingLists
    }


    private class InsertComparingElementThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {
        override fun run() {
            comparingElementDao.insert(comparingElement)
        }
    }

    private class UpdateComparingElementThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {
        override fun run() {
            comparingElementDao.update(comparingElement)
        }
    }

    private class DeleteComparingElementThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {

        override fun run() {
            comparingElementDao.delete(comparingElement)
        }
    }

    private class InsertComparingListThread(private val listDao: ComparingListDao?, private val list: ComparingList) : Thread() {
        override fun run() {
            listDao?.insert(list)
        }
    }

    private class UpdateComparingListThread(private val listDao: ComparingListDao?, private val list: ComparingList)  : Thread(){
        override fun run() {
            listDao?.update(list)
        }
    }

    private class DeleteComparingListThread(
        private val listDao: ComparingListDao?,
        private val list: ComparingList
    )  : Thread(){
        override fun run() {
            listDao?.delete(list)
        }
    }

    init {
        val database = getInstance(application!!)
        comparingElementDao = database!!.comparingElementDao()
        allComparingElements = comparingElementDao.allNotes
        listDao = database.comparingListDao()
        allComparingLists = listDao!!.getAllLists()
    }
}