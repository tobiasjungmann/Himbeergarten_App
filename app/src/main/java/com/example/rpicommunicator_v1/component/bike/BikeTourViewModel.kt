package com.example.rpicommunicator_v1.component.bike

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.database.bike.BikeTourRepository
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.bike.BikeTour

class BikeTourViewModel(application: Application) : AndroidViewModel(application) {
    private val bikeTourRepository: BikeTourRepository
    val allBikeTours: LiveData<List<BikeTour>>
    fun remove(bikeTour: BikeTour) {
        bikeTourRepository.remove(bikeTour)
    }

    fun insert(bikeTour: BikeTour) {
        bikeTourRepository.insert(bikeTour)
    }

    init {

        //networkCommunicator=new NetworkCommunicator();
        bikeTourRepository = BikeTourRepository(application)
        allBikeTours = bikeTourRepository.allBikeTours
    }
}