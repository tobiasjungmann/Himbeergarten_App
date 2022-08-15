package com.example.rpicommunicator_v1.Database.Note.second_level

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.Database.Note.image.PathElement
import com.example.rpicommunicator_v1.Database.Note.image.PathElementRepository

class ComparingElementViewModel(application: Application) : AndroidViewModel(application) {
    private val elementRepo: ComparingElementRepository
    private val pathRepo: PathElementRepository

    // todo add path element here
    init {
        elementRepo = ComparingElementRepository(application)
        pathRepo = PathElementRepository(application)
    }

    fun insert(comparingElement: ComparingElement?) {
        elementRepo.insert(comparingElement)
    }

    fun update(comparingElement: ComparingElement?,paths: List<PathElement>) {
        elementRepo.update(comparingElement)
        pathRepo.addAll(paths)
    }

    fun delete(comparingElement: ComparingElement?) {
        elementRepo.delete(comparingElement)        // cascade delete -> paths must not be added -> todo may delete images
    }

    fun getComparingElementByID(listeID: Int): LiveData<List<ComparingElement>> {
        return elementRepo.getListeNotes(listeID)
    }
}