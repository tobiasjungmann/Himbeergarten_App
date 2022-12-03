package com.example.rpicommunicator_v1.database.compare.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.plant.daos.DefaultDao

@Dao
interface ComparingElementDao:DefaultDao<ComparingElement> {

    @Query("DELETE FROM comparing_element_table")
    fun deleteAllNotes()

    @Query("DELETE FROM comparing_element_table WHERE idList==:id")
    fun deleteAllComparingElements(id: Int)

    @get:Query("SELECT* FROM comparing_element_table ORDER BY rating DESC")
    val allNotes: LiveData<List<ComparingElement>>

    @Query("SELECT* FROM comparing_element_table WHERE idList==:listeNummer ORDER BY rating DESC")
    fun getListNotes(listeNummer: Int): LiveData<List<ComparingElement>>
}