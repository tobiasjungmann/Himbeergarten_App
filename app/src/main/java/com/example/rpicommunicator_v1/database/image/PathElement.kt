package com.example.rpicommunicator_v1.database.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Converters
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import java.io.File
import java.io.FileOutputStream
import java.security.AccessController.getContext

@Entity(tableName = "path_element_database")
@TypeConverters(Converters::class)
class PathElement(
    val path: String,
) {

    @PrimaryKey(autoGenerate = true)
    var pathElementID = 0
    var parentEntry: Long = 0

    fun loadThumbnail(thumbnailSize: Int): Bitmap {
        val thumbnailPath=path.substring(0,path.length-5)+"_Thumbnail.jpg"
        val file = File(thumbnailPath)
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath())
        } else {
            file.createNewFile()
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, bmOptions)
            val width = bmOptions.outWidth
            val height = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(width / thumbnailSize, height / thumbnailSize)

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            val bitmap=BitmapFactory.decodeFile(path, bmOptions)
            val os=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,85,os)
            os.flush()
            os.close()
            return bitmap
        }
    }
}