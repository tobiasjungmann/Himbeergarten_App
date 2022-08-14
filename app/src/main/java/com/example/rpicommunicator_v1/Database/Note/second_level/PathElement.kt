package com.example.rpicommunicator_v1.Database.Note.second_level

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.rpicommunicator_v1.Database.Note.second_level.ComparingElement



@Entity(tableName = "path_element_database", foreignKeys =arrayOf(ForeignKey(entity = ComparingElement::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parentEntry"),
        onDelete = ForeignKey.CASCADE)))
class PathElement(
    val path: String,
) {

    @PrimaryKey(autoGenerate = true)
    var pathElementID = 0


    var parentEntry: Long = 0
}