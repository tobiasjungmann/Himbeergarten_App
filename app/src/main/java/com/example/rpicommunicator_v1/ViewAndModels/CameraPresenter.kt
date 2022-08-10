package com.example.rpicommunicator_v1.ViewAndModels

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingList
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class CameraPresenter constructor(
    private val context: Context
) : CameraContract.Presenter {

    private var currentPhotoPath: String? = null
    private var view: CameraContract.View? = null
    override val imageElement: ImageElement = ImageElement(mutableListOf());


    override fun attachView(view: CameraContract.View) {
        this.view = view
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    override fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Create the file where the photo should go
        var photoFile: File? = null
        try {
            photoFile = createImageFile(context)
            currentPhotoPath = photoFile.absolutePath
        } catch (e: IOException) {
            Log.d("Error Images", "takePicture: e")
        }

        if (photoFile == null) {
            return
        }

        val authority = "com.example.rpicommunicator_v1.fileprovider"
        val photoURI = FileProvider.getUriForFile(context, authority, photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        try {
            view?.openCamera(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            photoFile.delete()
        }
    }

    override fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val chooser = Intent.createChooser(intent, "Select file")
        view?.openGallery(chooser)
    }

    override fun removeImage(path: String) {
        val index = imageElement.picturePaths.indexOf(path)
        imageElement.picturePaths.remove(path)
        //todo
        File(path).delete()
        view?.onImageRemoved(index)
    }

    /**
     * @return true if user has given permission before
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermission(permission: String): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(context, permission)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            val requestCode = when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> PERMISSION_FILES
                else -> PERMISSION_CAMERA
            }

            view?.showPermissionRequestDialog(permission, requestCode)
            return false
        }
        return true
    }


    override fun onImageOptionSelected(option: Int) {
        if (option == 0) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermission(Manifest.permission.CAMERA)) {
                takePicture()
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery()
            }
        }
    }

    override fun onNewImageTaken() {
        currentPhotoPath?.let {
            rescaleBitmap(context, it)
            imageElement.picturePaths.add(it)
            view?.onImageAdded(it)
        }
    }

    override fun onNewImageSelected(uri: Uri?) {
        val filePath = rescaleBitmap(context, uri ?: return) ?: return
        imageElement.picturePaths.add(filePath)
        view?.onImageAdded(filePath)
    }

    override fun detachView() {
        clearPictures()
// todo store in db as path at the corresponding object
        view = null
    }

    private fun clearPictures() {
        /*    for (path in feedback.picturePaths) {
                File(path).delete()
            }*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //    outState.putParcelable(Const.FEEDBACK, feedback)
    }

    /**
     * Destination is a new image file
     *
     * @return absolute path of the new destination file
     */
    fun rescaleBitmap(context: Context, src: Uri): String? {
        return try {
            val destination = createImageFile(context)
            rescaleBitmap(context, src, destination)
            destination.absolutePath
        } catch (e: IOException) {
            // Utils.log(e)
            null
        }
    }

    /**
     * @param filePath source and destination
     */
    fun rescaleBitmap(context: Context, filePath: String) {
        rescaleBitmap(context, Uri.fromFile(File(filePath)), File(filePath))
    }

    /**
     * Scales down the image and writes it to the destination file
     */
    private fun rescaleBitmap(context: Context, src: Uri, destination: File) {
        try {
            var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, src)
            val out = ByteArrayOutputStream()
            //     Utils.log("img before: ${bitmap.width} x ${bitmap.height}")
            bitmap = getResizedBitmap(bitmap)
            //     Utils.log("img after: ${bitmap.width} x ${bitmap.height}")
            bitmap.compress(Bitmap.CompressFormat.JPEG, FEEDBACK_IMG_COMPRESSION_QUALITY, out)
            FileOutputStream(destination).apply {
                write(out.toByteArray())
                close()
            }
            out.close()
        } catch (e: IOException) {
            //   Utils.log(e)
        }
    }

    /**
     * Scales the bitmap down if it's bigger than maxSize
     *
     * @return a resized copy of the bitmap
     */
    private fun getResizedBitmap(image: Bitmap, maxSize: Int = 1000): Bitmap {
        var width = image.width
        var height = image.height
        if (width < maxSize && height < maxSize) {
            return image
        }

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }


    companion object {
        const val REQUEST_TAKE_PHOTO = 11
        const val REQUEST_GALLERY = 12
        const val PERMISSION_CAMERA = 14
        const val PERMISSION_FILES = 15
        const val FEEDBACK_IMG_COMPRESSION_QUALITY = 50
    }
}