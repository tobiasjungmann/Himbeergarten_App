package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Note.ComparingList
import com.example.rpicommunicator_v1.Database.Note.ComparingListRepository


class ComparingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ComparingListRepository
    val allLists: LiveData<List<ComparingList>>




    init {
        repository = ComparingListRepository(application)
        allLists = repository.getAllLists()
    }

    fun insert(list: ComparingList) {
        repository.insert(list)
    }

    fun update(list: ComparingList) {
        repository.update(list)
    }

    fun delete(list: ComparingList) {
        repository.delete(list)
    }

    fun deleteAllLists() {
        repository.deleteAllLists()
    }

    fun getAllComparingLists(): LiveData<List<ComparingList>> {
        return allLists
    }


}