package com.example.rpicommunicator_v1.database.compare.second_level

import android.app.Application
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElementDatabase.Companion.getInstance
import androidx.lifecycle.LiveData


class ComparingElementRepository(application: Application?) {
    private val comparingElementDao: ComparingElementDao

    private val allComparingElements: LiveData<List<ComparingElement>>

    fun insert(comparingElement: ComparingElement?) {
        InsertNoteThread(comparingElementDao, comparingElement).start()
    }

    fun update(comparingElement: ComparingElement?) {
        UpdateNoteThread(comparingElementDao, comparingElement).start()
    }

    fun delete(comparingElement: ComparingElement?) {
        DeleteNoteThread(comparingElementDao, comparingElement).start()
    }

    fun deleteAllNotes() {
        DeleteAllNotesThread(comparingElementDao).start()
    }

    fun deleteAllNotesList(id: Int) {
        DeleteAllComparingElementsOfList(comparingElementDao, id).start()
    }

    fun getComparingElementsById(listeID: Int): LiveData<List<ComparingElement>> {
        return comparingElementDao.getListNotes(listeID)
    }

    fun getAllNotes(): LiveData<List<ComparingElement>> {
        return allComparingElements
    }

    private class InsertNoteThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {
        override fun run() {
            comparingElementDao.insert(comparingElement)
        }
    }

    private class UpdateNoteThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {
        override fun run() {
            comparingElementDao.update(comparingElement)
        }
    }

    private class DeleteNoteThread(
        private val comparingElementDao: ComparingElementDao,
        private val comparingElement: ComparingElement?
    ) :
        Thread() {

        override fun run() {
            comparingElementDao.delete(comparingElement)
        }
    }

    private class DeleteAllNotesThread(private val comparingElementDao: ComparingElementDao) :
        Thread() {
        override fun run() {
            comparingElementDao.deleteAllNotes()
        }
    }

    private class DeleteAllComparingElementsOfList(
        private val comparingElementDao: ComparingElementDao,
        private val id: Int
    ) :
        Thread() {
        override fun run() {
            comparingElementDao.deleteAllComparingElements(id)
        }
    }

    init {
        val database = getInstance(application!!)
        comparingElementDao = database!!.noteDao()
        allComparingElements = comparingElementDao.allNotes
    }
}