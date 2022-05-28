package com.example.rpicommunicator_v1.Database.Note.second_level

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
class Note(
    val title: String,
    val description: String,
    val priority: Int,
    var idListe: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}