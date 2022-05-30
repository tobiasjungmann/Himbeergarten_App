package com.example.rpicommunicator_v1.Database.Note.second_level

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comparing_element_database")
class ComparingElement(
    val title: String,
    val description: String,
    val rating: Int,
    var idListe: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray?=null
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}