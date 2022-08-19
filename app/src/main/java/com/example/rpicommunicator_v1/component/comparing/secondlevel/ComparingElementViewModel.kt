package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElementRepository
import com.example.rpicommunicator_v1.database.image.PathElement
import com.example.rpicommunicator_v1.database.image.PathElementRepository

class ComparingElementViewModel(application: Application) : AndroidViewModel(application) {
    private val elementRepo: ComparingElementRepository
    private val pathRepo: PathElementRepository

    init {
        elementRepo = ComparingElementRepository(application)
        pathRepo = PathElementRepository(application)
    }

    fun insert(comparingElement: ComparingElement?, imagePaths: Array<String>?) {
        elementRepo.insert(comparingElement)
        pathRepo.insertList(imagePaths)
    }

    fun update(comparingElement: ComparingElement?, paths: Array<String>?) {
        elementRepo.update(comparingElement)
        pathRepo.addAll(paths)
    }

    fun delete(comparingElement: ComparingElement?) {
        elementRepo.delete(comparingElement)        // cascade delete -> paths must not be added -> todo may delete images
    }

    fun getComparingElementByID(listeID: Int): LiveData<List<ComparingElement>> {
        return elementRepo.getComparingElementsById(listeID)
    }

    fun getAllPathsToElement(idListe: Int): List<PathElement>{
        return pathRepo.getPathElementsById(idListe)
    }
}