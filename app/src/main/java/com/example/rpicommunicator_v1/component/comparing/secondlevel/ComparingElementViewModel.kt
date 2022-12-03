package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.LocalRepository
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.PathElement

class ComparingElementViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocalRepository

    init {
        repository = LocalRepository(application)
    }

    fun insert(comparingElement: ComparingElement?, imagePaths: Array<String>?) {
        repository.insertComparingElement(comparingElement)
        repository.insertPathElements(imagePaths)
    }

    fun update(comparingElement: ComparingElement?, paths: Array<String>) {
        repository.updateComparingElement(comparingElement)
        repository.updatePathElements(paths)
    }

    fun delete(comparingElement: ComparingElement?) {
        repository.deleteComparingElement(comparingElement)        // cascade delete -> paths must not be added
    }

    fun getComparingElementByID(listId: Int): LiveData<List<ComparingElement>> {
        return repository.getComparingElementsById(listId)
    }

    fun getAllPathsToElement(listId: Int): List<PathElement>{
        return repository.getPathElementsById(listId)
    }
}