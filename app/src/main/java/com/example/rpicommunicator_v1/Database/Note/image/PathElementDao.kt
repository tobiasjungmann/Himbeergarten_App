package com.example.rpicommunicator_v1.Database.PathElement.image

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rpicommunicator_v1.Database.Note.image.PathElement

@Dao
interface PathElementDao {
    @Insert
    fun insert(pathElement: PathElement?)

    @Update
    fun update(pathElement: PathElement?)

    @Delete
    fun delete(pathElement: PathElement?)

    @Query("DELETE FROM path_element_database")
    fun deleteAllPathElements()

    @Query("DELETE FROM path_element_database WHERE pathElementID==:id")
    fun deleteAllPathElement(id: Int)

    @get:Query("SELECT* FROM path_element_database")
    val allPathElements: LiveData<List<PathElement>>

    @Query("SELECT* FROM path_element_database WHERE pathElementID==:listeNummer")
    fun getListPathElements(listeNummer: Int): LiveData<List<PathElement>>
}