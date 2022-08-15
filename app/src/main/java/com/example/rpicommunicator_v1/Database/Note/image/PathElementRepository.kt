package com.example.rpicommunicator_v1.Database.Note.image

import android.app.Application
import com.example.rpicommunicator_v1.Database.Note.image.PathElementDatabase.Companion.getInstance
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.PathElement.image.PathElementDao


class PathElementRepository(application: Application?) {
    private val pathElementDao: PathElementDao

    private val allPathElements: LiveData<List<PathElement>>

    fun insert(pathElement: PathElement?) {
        InsertPathElementThread(pathElementDao,pathElement).start()
    }

    fun update(pathElement: PathElement?) {
        UpdatePathElementThread(pathElementDao,pathElement).start()
    }

    fun delete(pathElement: PathElement?) {
        DeletePathElementThread(pathElementDao,pathElement).start()
    }

    fun addAll(paths: List<PathElement>) {
        TODO("Not yet implemented")
    }

    private class InsertPathElementThread(private val pathElementDao: PathElementDao, private val pathElement: PathElement?) :
        Thread() {
        override fun run() {
            pathElementDao.insert(pathElement)
        }
    }

    private class UpdatePathElementThread(private val pathElementDao: PathElementDao, private val pathElement: PathElement?) :
        Thread() {
        override fun run() {
            pathElementDao.update(pathElement)
        }
    }

    private class DeletePathElementThread(private val pathElementDao: PathElementDao, private val pathElement: PathElement?) :
        Thread() {

        override fun run() {
            pathElementDao.delete(pathElement)
        }
    }

    private class DeleteAllPathElementsThread(private val pathElementDao: PathElementDao) :
        Thread() {
        override fun run() {
            pathElementDao.deleteAllPathElements()
        }
    }

    private class DeleteAllPathElementsOfList(private val pathElementDao: PathElementDao, private val id: Int) :
        Thread() {
        override fun run() {
            pathElementDao.deleteAllPathElements()
        }
    }

    init {
        val database = getInstance(application!!)
        pathElementDao=database!!.pathElementDao()
        allPathElements = pathElementDao.allPathElements
    }
}