package com.example.rpicommunicator_v1.Database.Note.first_level

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comparing_list_table")
class ComparingList(val title: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}