package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.LocalRepository
import com.example.rpicommunicator_v1.database.compare.models.ComparingList


class ComparingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalRepository
    private val allLists: LiveData<List<ComparingList>>

    init {
        repository = LocalRepository(application)
        allLists = repository.getAllLists()
    }

    fun insert(list: ComparingList) {
        repository.insertComparingList(list)
    }

    fun update(list: ComparingList) {
        repository.updateComparingList(list)
    }

    fun delete(list: ComparingList) {
        repository.deleteComparingList(list)
    }

    fun getAllComparingLists(): LiveData<List<ComparingList>> {
        return allLists
    }

    fun getComparingListByPosition(position: Int): ComparingList {
        return allLists.value!![position]
    }
}