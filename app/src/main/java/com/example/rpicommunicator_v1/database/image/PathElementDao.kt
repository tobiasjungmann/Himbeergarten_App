package com.example.rpicommunicator_v1.database.PathElement.image

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.image.PathElement
import com.example.rpicommunicator_v1.database.plant.daos.DefaultDao

@Dao
interface PathElementDao: DefaultDao<PathElement> {

    @Query("DELETE FROM path_element_database")
    fun deleteAllPathElements()

    @Query("DELETE FROM path_element_database WHERE pathElementID==:id")
    fun deleteAllPathElement(id: Int)

    @get:Query("SELECT* FROM path_element_database")
    val allPathElements: LiveData<List<PathElement>>

    @Query("SELECT* FROM path_element_database WHERE pathElementID==:listeNummer")
    fun getListPathElements(listeNummer: Int): List<PathElement>
}