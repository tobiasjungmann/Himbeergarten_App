package com.example.rpicommunicator_v1.database.image

import android.app.Application
import com.example.rpicommunicator_v1.database.image.PathElementDatabase.Companion.getInstance
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.PathElement.image.PathElementDao


class PathElementRepository(application: Application?) {
    private val pathElementDao: PathElementDao

    private val allPathElements: LiveData<List<PathElement>>

    fun insert(pathElement: PathElement) {
        InsertPathElementThread(pathElementDao,pathElement).start()
    }


    fun insertList(pathElement: Array<String>?) {
        if (pathElement != null) {
            for (i in pathElement){
                InsertPathElementThread(pathElementDao,PathElement(i)).start()
            }
        }
    }

    fun update(pathElement: PathElement?) {
        UpdatePathElementThread(pathElementDao,pathElement).start()
    }

    fun delete(pathElement: PathElement?) {
        DeletePathElementThread(pathElementDao,pathElement).start()
    }

    fun addAll(paths: Array<String>?) {
        TODO("Not yet implemented")
    }

    fun getAllById(idList: Int) {
        GetAllByIDThread(pathElementDao,idList).start()
    }

    private class InsertPathElementThread(private val pathElementDao: PathElementDao, private val pathElement: PathElement) :
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

    private class GetAllByIDThread(private val pathElementDao: PathElementDao, private val idList: Int) :
        Thread() {
        override fun run() {
            pathElementDao.getListPathElements(idList)
        }
    }

    init {
        val database = getInstance(application!!)
        pathElementDao=database!!.pathElementDao()
        allPathElements = pathElementDao.allPathElements
    }
}