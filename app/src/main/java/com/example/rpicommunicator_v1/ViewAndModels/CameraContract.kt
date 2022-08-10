package com.example.rpicommunicator_v1.ViewAndModels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingList

interface CameraContract {
    interface View {
        fun openCamera(intent: Intent)
        fun openGallery(intent: Intent)
        fun showPermissionRequestDialog(permission: String, requestCode: Int)
        fun onImageAdded(path: String)
        fun onImageRemoved(position: Int)
    }

    interface Presenter {
        val imageElement: ImageElement
        fun attachView(view: View)
        fun removeImage(path: String)
        fun onImageOptionSelected(option: Int)
        fun onNewImageTaken()
        fun onNewImageSelected(uri: Uri?)
        fun takePicture()
        fun openGallery()
        fun onSaveInstanceState(outState: Bundle)
        fun detachView()
    }
}
