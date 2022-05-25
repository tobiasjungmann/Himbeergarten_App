package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Note.ComparingListRepository

class ComparingListViewModel(application: Application) : AndroidViewModel(application) {

    var repository: ComparingListRepository? = null
    var allLists: LiveData<List<ComparingList?>?>? = null

    fun ListeViewModel(application: Application) {
        super(application)
        repository = ListeRepository(application)
        allLists = repository.getAllLists()
    }

    fun insert(liste: Liste?) {
        repository.insert(liste)
    }

    fun update(liste: Liste?) {
        repository.update(liste)
    }

    fun delete(liste: Liste?) {
        repository.delete(liste)
    }

    fun deleteAllLists() {
        repository.deleteAllLists()
    }

    fun getAllLists(): LiveData<List<Liste?>?>? {
        return allLists
    }


}