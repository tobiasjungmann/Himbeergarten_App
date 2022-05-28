package com.example.rpicommunicator_v1.Database.Note

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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