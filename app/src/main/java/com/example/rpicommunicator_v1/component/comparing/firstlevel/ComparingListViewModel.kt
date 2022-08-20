package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingListRepository


class ComparingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ComparingListRepository
    private val allLists: LiveData<List<ComparingList>>

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

    fun getAllComparingLists(): LiveData<List<ComparingList>> {
        return allLists
    }

    fun getComparingListByPosition(position: Int): ComparingList {
        return allLists.value!![position]
    }
}