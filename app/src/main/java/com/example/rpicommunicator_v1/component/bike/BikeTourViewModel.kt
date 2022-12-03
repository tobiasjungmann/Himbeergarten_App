package com.example.rpicommunicator_v1.component.bike

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.models.BikeTour
import com.example.rpicommunicator_v1.database.compare.LocalRepository

class BikeTourViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocalRepository
    val allBikeTours: LiveData<List<BikeTour>>
    fun remove(bikeTour: BikeTour) {
        repository.remove(bikeTour)
    }

    fun insert(bikeTour: BikeTour) {
        repository.insert(bikeTour)
    }

    init {
        repository = LocalRepository(application)
        allBikeTours = repository.allBikeTours
    }
}