package com.example.rpicommunicator_v1.Database.Note.image

import androidx.room.*

@Dao
interface PathElementDao {
    @Insert
    fun insert(pathElement: PathElement?)

    @Update
    fun update(pathElement: PathElement?)

    @Delete
    fun delete(pathElement: PathElement?)
}