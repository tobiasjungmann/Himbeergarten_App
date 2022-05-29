package com.example.rpicommunicator_v1.Database.Note.second_level

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ComparingElementViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ComparingElementRepository

    init {
        repository = ComparingElementRepository(application)
    }

    fun insert(comparingElement: ComparingElement?) {
        repository.insert(comparingElement)
    }

    fun update(comparingElement: ComparingElement?) {
        repository.update(comparingElement)
    }

    fun delete(comparingElement: ComparingElement?) {
        repository.delete(comparingElement)
    }

    fun getComparingElementByID(listeID: Int): LiveData<List<ComparingElement>> {
        return repository.getListeNotes(listeID)
    }
}