package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rpicommunicator_v1.component.Constants.INVALID_DB_ID
import com.example.rpicommunicator_v1.database.compare.LocalRepository
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.ComparingList
import com.example.rpicommunicator_v1.database.compare.models.PathElement


class ComparingListViewModel(application: Application) : AndroidViewModel(application) {

    private var currentElement: ComparingElement?=null
    private var currentList: ComparingList?=null
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

    fun insert(comparingElement: ComparingElement?, imagePaths: Array<String>?) {
        repository.insertComparingElement(comparingElement)
        repository.insertPathElements(imagePaths)
    }

    fun delete(comparingElement: ComparingElement?) {
        repository.deleteComparingElement(comparingElement)        // cascade delete -> paths must not be added
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

    fun setCurrentList(list: ComparingList){
      this.currentList=list
    }
    fun getCurrentList(): ComparingList? {
        return currentList
    }

    fun setCurrentElement(element: ComparingElement){
        this.currentElement=element
    }
    fun getCurrentElement(): ComparingElement? {
        return currentElement
    }

    fun resetCurrentElement() {
        this.currentElement=null
    }

    fun getComparingElementsForCurrentList(): LiveData<List<ComparingElement>> {
        return repository.getComparingElementsById(currentList?.comparingListId ?: INVALID_DB_ID)
    }

    fun getComparingElementByID(listId: Int): LiveData<List<ComparingElement>> {
        return repository.getComparingElementsById(listId)
    }

    fun getAllPathsToElement(listId: Int): List<PathElement>{
        return repository.getPathElementsById(listId)
    }

    fun createOrUpdateNote(title: String, description: String, rating: Int) {
        repository.insertComparingElement(ComparingElement(title,description,rating,currentList!!.comparingListId))
    }
}