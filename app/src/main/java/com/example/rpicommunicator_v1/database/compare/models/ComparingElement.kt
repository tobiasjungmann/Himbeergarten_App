package com.example.rpicommunicator_v1.database.compare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comparing_element_table")
class ComparingElement(
    val title: String,
    val description: String,
    val rating: Int,
    var idList: Int
) {

    @PrimaryKey(autoGenerate = true)
    var comparingElementId = 0
}