package com.example.rpicommunicator_v1.Database.Bike

import android.app.Application
import com.example.rpicommunicator_v1.Database.Bike.BikeTourDatabase.Companion.getInstance
import androidx.lifecycle.LiveData
import android.database.sqlite.SQLiteConstraintException

class BikeTourRepository(application: Application?) {
    private val bikeTourDao: BikeTourDao?
    val allBikeTours: LiveData<List<BikeTour>>
    fun insert(bikeTour: BikeTour) {
        InsertBikeTourThread(bikeTourDao, bikeTour).start()
    }

    fun remove(bikeTour: BikeTour) {
        RemoveBikeTourThread(bikeTourDao, bikeTour).start()
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

    private class RemoveBikeTourThread  //bikeTourDao.getAllTours().getValue().get(bikeTourDao.getAllTours().getValue().size());
        (private val bikeTourDao: BikeTourDao?, private val bikeTour: BikeTour) : Thread() {
        override fun run() {
            bikeTourDao!!.delete(bikeTour)
        }
    }

    companion object {
        const val TAG = "Repository "
    }

    init {
        val database = getInstance(application!!)
        bikeTourDao = database!!.bikeTourDao()
        allBikeTours = bikeTourDao!!.allTours
    }
}