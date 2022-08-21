package com.example.rpicommunicator_v1.database.image

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.PathElement.image.PathElementDao
import com.example.rpicommunicator_v1.database.image.PathElementDatabase.Companion.getInstance


class PathElementRepository(application: Application?) {
    private val pathElementDao: PathElementDao

    private val allPathElements: LiveData<List<PathElement>>

    fun insert(pathElement: PathElement) {
        InsertPathElementThread(pathElementDao, pathElement).start()
    }


    fun insertList(pathElement: Array<String>?) {
        if (pathElement != null) {
            for (i in pathElement) {
                InsertPathElementThread(pathElementDao, PathElement(i)).start()
            }
        }
    }

    fun update(pathElement: PathElement?) {
        UpdatePathElementThread(pathElementDao, pathElement).start()
    }

    fun update(pathElements: Array<String>) {
        UpdatePathElementsThread(pathElementDao, pathElements).start()
    }

    fun delete(pathElement: PathElement?) {
        DeletePathElementThread(pathElementDao, pathElement).start()
    }


    fun getPathElementsById(idElement: Int): List<PathElement> {
        return pathElementDao.getListPathElements(idElement)
    }


    private class InsertPathElementThread(
        private val pathElementDao: PathElementDao,
        private val pathElement: PathElement
    ) :
        Thread() {
        override fun run() {
            pathElementDao.insert(pathElement)
        }
    }

    private class UpdatePathElementThread(
        private val pathElementDao: PathElementDao,
        private val pathElement: PathElement?
    ) :
        Thread() {
        override fun run() {
            pathElementDao.update(pathElement)
        }
    }

    private class UpdatePathElementsThread(
        private val pathElementDao: PathElementDao,
        private val pathElements: Array<String>
    ) :
        Thread() {
        override fun run() {
            for (s in pathElements) {
                pathElementDao.update(s)
            }
        }
    }

    private class DeletePathElementThread(
        private val pathElementDao: PathElementDao,
        private val pathElement: PathElement?
    ) :
        Thread() {

        override fun run() {
            pathElementDao.delete(pathElement)
        }
    }

    init {
        val database = getInstance(application!!)
        pathElementDao = database!!.pathElementDao()
        allPathElements = pathElementDao.allPathElements
    }
}