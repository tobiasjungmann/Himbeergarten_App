package com.example.rpicommunicator_v1.database.compare

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.database.compare.LocalDatabase.Companion.getInstance
import com.example.rpicommunicator_v1.database.compare.daos.BikeTourDao
import com.example.rpicommunicator_v1.database.compare.daos.ComparingElementDao
import com.example.rpicommunicator_v1.database.compare.daos.ComparingListDao
import com.example.rpicommunicator_v1.database.compare.daos.PathElementDao
import com.example.rpicommunicator_v1.database.compare.models.BikeTour
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.ComparingList
import com.example.rpicommunicator_v1.database.compare.models.PathElement


class LocalRepository(application: Application?) {
    private val comparingElementDao: ComparingElementDao
    private val allComparingElements: LiveData<List<ComparingElement>>
    private var listDao: ComparingListDao?
    private var allComparingLists: LiveData<List<ComparingList>>
    private val bikeTourDao: BikeTourDao?

    val allBikeTours: LiveData<List<BikeTour>>
    private val pathElementDao: PathElementDao
    private val allPathElements: LiveData<List<PathElement>>
    private val currentThumbnails: MutableLiveData<List<PathElement>>


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

    fun insert(bikeTour: BikeTour) {
        InsertBikeTourThread(bikeTourDao, bikeTour).start()
    }

    fun remove(bikeTour: BikeTour) {
        RemoveBikeTourThread(bikeTourDao, bikeTour).start()
    }

    fun insert(pathElement: PathElement) {
        InsertPathElementThread(pathElementDao, pathElement).start()
    }


    fun insertPathElements(pathElement: Array<String>?) {
        if (pathElement != null) {
            for (i in pathElement) {
                InsertPathElementThread(pathElementDao, PathElement(i)).start()
            }
        }
    }

    fun update(pathElement: PathElement?) {
        UpdatePathElementThread(pathElementDao, pathElement).start()
    }

    fun updatePathElements(pathElements: Array<String>) {
        UpdatePathElementsThread(pathElementDao, pathElements).start()
    }

    fun delete(pathElement: PathElement?) {
        DeletePathElementThread(pathElementDao, pathElement).start()
    }


    fun getPathElementsById(idElement: Int): List<PathElement> {
        return pathElementDao.getListPathElements(idElement)
    }

    fun queryAllThumbnailsForCurrentList(listID: Int) {
        QueryAllThumbnailsForCurrentListThread(pathElementDao, listID, currentThumbnails).start()
    }

    fun getThumbnailsForList(): LiveData<List<PathElement>> {
        return currentThumbnails
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

    private class InsertComparingListThread(
        private val listDao: ComparingListDao?,
        private val list: ComparingList
    ) : Thread() {
        override fun run() {
            listDao?.insert(list)
        }
    }

    private class UpdateComparingListThread(
        private val listDao: ComparingListDao?,
        private val list: ComparingList
    ) : Thread() {
        override fun run() {
            listDao?.update(list)
        }
    }

    private class DeleteComparingListThread(
        private val listDao: ComparingListDao?,
        private val list: ComparingList
    ) : Thread() {
        override fun run() {
            listDao?.delete(list)
        }
    }


    private class InsertBikeTourThread(
        private val bikeTourDao: BikeTourDao?,
        private val bikeTour: BikeTour
    ) : Thread() {
        override fun run() {
            try {
                //  bikeTourRepository.update(bikeTour);
                bikeTourDao!!.insert(bikeTour)
            } catch (e: SQLiteConstraintException) {
                bikeTourDao!!.update(bikeTour)
            }
        }
    }

    private class RemoveBikeTourThread
        (private val bikeTourDao: BikeTourDao?, private val bikeTour: BikeTour) : Thread() {
        override fun run() {
            bikeTourDao!!.delete(bikeTour)
        }
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
                // todo delete all elements and insert new onespathElementDao.update(s)
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

    // pathElementDao, listID, currentThumbnails)
    private class QueryAllThumbnailsForCurrentListThread(
        private val pathElementDao: PathElementDao,
        private val listID: Int,
        private val currentThumbnails: MutableLiveData<List<PathElement>>
    ) : Thread() {
        override fun run() {
            currentThumbnails.postValue(pathElementDao.getAllThumbnailsForComparingList(listID))
        }
    }


    init {
        val database = getInstance(application!!)
        comparingElementDao = database!!.comparingElementDao()
        listDao = database.comparingListDao()
        bikeTourDao = database.bikeTourDao()
        pathElementDao = database.pathElementDao()

        allComparingElements = comparingElementDao.allNotes
        allComparingLists = listDao!!.getAllLists()
        allBikeTours = bikeTourDao.allTours
        allPathElements = pathElementDao.allPathElements
        currentThumbnails = MutableLiveData()
    }
}