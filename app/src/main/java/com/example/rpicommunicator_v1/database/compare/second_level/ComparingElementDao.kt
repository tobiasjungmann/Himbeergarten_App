package com.example.rpicommunicator_v1.database.compare.second_level

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ComparingElementDao {
    @Insert
    fun insert(comparingElement: ComparingElement?)

    @Update
    fun update(comparingElement: ComparingElement?)

    @Delete
    fun delete(comparingElement: ComparingElement?)

    @Query("DELETE FROM comparing_element_database")
    fun deleteAllNotes()

    @Query("DELETE FROM comparing_element_database WHERE idList==:id")
    fun deleteAllComparingElements(id: Int)

    @get:Query("SELECT* FROM comparing_element_database ORDER BY rating DESC")
    val allNotes: LiveData<List<ComparingElement>>

    @Query("SELECT* FROM comparing_element_database WHERE idList==:listeNummer ORDER BY rating DESC")
    fun getListNotes(listeNummer: Int): LiveData<List<ComparingElement>>
}