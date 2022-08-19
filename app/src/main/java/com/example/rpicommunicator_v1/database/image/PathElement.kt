package com.example.rpicommunicator_v1.database.image

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rpicommunicator_v1.component.Converters
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement



/*@Entity(tableName = "path_element_database", foreignKeys = [ForeignKey(
    entity = ComparingElement::class,
    parentColumns = arrayOf("comparingElementId"),
    childColumns = arrayOf("parentEntry"),
    onDelete = ForeignKey.CASCADE)]
)*/
@Entity(tableName = "path_element_database")
@TypeConverters(Converters::class)
class PathElement(
    val path: String,
) {

    @PrimaryKey(autoGenerate = true)
    var pathElementID = 0


    var parentEntry: Long = 0
}