package com.example.rpicommunicator_v1.Database.Note.first_level

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingList
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingListRepository


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

    fun getComparingListByPosition(position: Int): ComparingList {
        return allLists.value!![position]
    }


}