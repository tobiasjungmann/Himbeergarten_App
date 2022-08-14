package com.example.rpicommunicator_v1.Database.Note.second_level

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rpicommunicator_v1.ViewAndModels.Converters

//@TypeConverters({ Converters.class})

@Entity(tableName = "comparing_element_database")
@TypeConverters(Converters::class)
class ComparingElement(
    val title: String,
    val description: String,
    val rating: Int,
    var idListe: Int
) {


    @PrimaryKey(autoGenerate = true)
    var id = 0


  //  var imagePaths: ArrayList<PathElement> = arrayListOf<PathElement>()
}