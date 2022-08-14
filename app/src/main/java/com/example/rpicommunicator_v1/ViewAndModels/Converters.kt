package com.example.rpicommunicator_v1.ViewAndModels

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

import androidx.room.TypeConverter
import com.example.rpicommunicator_v1.Database.Note.image.PathElement
import kotlinx.serialization.decodeFromString

/*class Converters {

    @TypeConverter
    fun arrayToJson(value: Array<String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToArray(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}*/

class Converters {
    @TypeConverter
    fun fromList(value : ArrayList<PathElement>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<PathElement>>(value)
}
