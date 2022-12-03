package com.example.rpicommunicator_v1.database.compare.second_level

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rpicommunicator_v1.component.Converters

@Entity(tableName = "comparing_element_table")
@TypeConverters(Converters::class)
class ComparingElement(
    val title: String,
    val description: String,
    val rating: Int,
    var idList: Int
) {

    @PrimaryKey(autoGenerate = true)
    var comparingElementId = 0
}