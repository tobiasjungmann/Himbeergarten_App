package com.example.rpicommunicator_v1.database.compare.first_level

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ComparingListDao {
    @Insert
    fun insert(liste: ComparingList?)

    @Update
    fun update(liste: ComparingList?)

    @Delete
    fun delete(liste: ComparingList?)

    @Query("DELETE FROM comparing_list_table")
    fun deleteAllLists()

    @Query("SELECT* FROM comparing_list_table")
    fun getAllLists(): LiveData<List<ComparingList>>
}