package com.example.rpicommunicator_v1.database.compare.first_level

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comparing_list_table")
class ComparingList(
    val title: String) {

    @PrimaryKey(autoGenerate = true)
    var comparingListId = 0

}