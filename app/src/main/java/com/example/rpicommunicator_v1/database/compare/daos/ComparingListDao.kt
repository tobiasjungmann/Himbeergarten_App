package com.example.rpicommunicator_v1.database.compare.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.rpicommunicator_v1.database.compare.models.ComparingList
import com.example.rpicommunicator_v1.database.plant.daos.DefaultDao

@Dao
interface ComparingListDao: DefaultDao<ComparingList> {
    @Query("DELETE FROM comparing_list_table")
    fun deleteAllLists()

    @Query("SELECT* FROM comparing_list_table")
    fun getAllLists(): LiveData<List<ComparingList>>
}