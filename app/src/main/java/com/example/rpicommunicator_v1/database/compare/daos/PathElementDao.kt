package com.example.rpicommunicator_v1.database.compare.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.database.plant.daos.DefaultDao

@Dao
interface PathElementDao: DefaultDao<PathElement> {

    @Query("DELETE FROM path_element_table")
    fun deleteAllPathElements()

    @Query("DELETE FROM path_element_table WHERE pathElementID==:id")
    fun deleteAllPathElement(id: Int)

    @get:Query("SELECT* FROM path_element_table")
    val allPathElements: LiveData<List<PathElement>>

    @Query("SELECT* FROM path_element_table WHERE pathElementID==:parent")
    fun getListPathElements(parent: Int): List<PathElement>

    @Query("SELECT* FROM path_element_table WHERE pathElementID==:parent")
    fun getListPathElementsLiveData(parent: Int): LiveData<List<PathElement>>

    @Query("SELECT * FROM path_element_table, comparing_element_table  " +
            "WHERE comparing_element_table.idList==:comparingListId " +
            "AND comparing_element_table.comparingElementId==path_element_table.parentEntry")
    fun getAllThumbnailsForComparingList(comparingListId: Int): List<PathElement>
}