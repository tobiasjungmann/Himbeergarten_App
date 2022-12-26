package com.example.rpicommunicator_v1.database.plant.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface DefaultDao<T> {
    @Insert
    fun insert(vararg obj: T?): List<Long>

    @Update
    fun update(vararg obj: T?)

    @Delete
    fun delete(vararg obj: T?)
}