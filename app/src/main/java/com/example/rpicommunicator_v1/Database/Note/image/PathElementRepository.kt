package com.example.rpicommunicator_v1.Database.Note.image

import android.app.Application
import com.example.rpicommunicator_v1.Database.Note.image.PathElementDatabase.Companion.getInstance
import androidx.lifecycle.LiveData


class PathElementRepository(application: Application?) {
    private val pathElementDao: PathElementDao

    private val allPathElements: LiveData<List<PathElement>>

    fun insert(comparingElement: PathElement?) {
        InsertNoteThread(pathElementDao,comparingElement).start()
    }

    fun update(comparingElement: PathElement?) {
        UpdateNoteThread(pathElementDao,comparingElement).start()
    }

    fun delete(comparingElement: PathElement?) {
        DeleteNoteThread(pathElementDao,comparingElement).start()
    }

    fun deleteAllNotes() {
        DeleteAllNotesThread(pathElementDao).start()
    }

    fun deleteAllNotesList(id: Int) {
        DeleteAllPathElementsOfList(pathElementDao, id).start()
    }

    fun getListeNotes(listeID: Int): LiveData<List<PathElement>> {
        return pathElementDao.getListNotes(listeID)
    }

    fun getAllNotes(): LiveData<List<PathElement>> {
        return allPathElements
    }

    private class InsertNoteThread(private val pathElementDao: PathElementDao, private val comparingElement: PathElement?) :
        Thread() {
        override fun run() {
            pathElementDao.insert(comparingElement)
        }
    }

    private class UpdateNoteThread(private val pathElementDao: PathElementDao, private val comparingElement: PathElement?) :
        Thread() {
        override fun run() {
            pathElementDao.update(comparingElement)
        }
    }

    private class DeleteNoteThread(private val pathElementDao: PathElementDao, private val comparingElement: PathElement?) :
        Thread() {

        override fun run() {
            pathElementDao.delete(comparingElement)
        }
    }

    private class DeleteAllNotesThread(private val pathElementDao: PathElementDao) :
        Thread() {
        override fun run() {
            pathElementDao.deleteAllNotes()
        }
    }

    private class DeleteAllPathElementsOfList(private val pathElementDao: PathElementDao, private val id: Int) :
        Thread() {
        override fun run() {
            pathElementDao.deleteAllPathElements(id)
        }
    }

    init {
        val database = getInstance(application!!)
        pathElementDao=database!!.pathElementDao()
        allPathElements = pathElementDao.allNotes
    }
}