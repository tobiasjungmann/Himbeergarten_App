package com.example.rpicommunicator_v1.component.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import java.io.File

class CameraUtils(private val context: Context,) {
    private var presenter: CameraContract.Presenter = CameraPresenter(context)
    private var thumbnailsAdapter: CameraThumbnailsAdapter

    init {
        val thumbnailSize = context.resources.getDimension(R.dimen.thumbnail_size).toInt()
        thumbnailsAdapter =
            CameraThumbnailsAdapter(
                presenter.imageElement,
                { onThumbnailRemoved(it) },
                thumbnailSize
            )
        presenter.attachView(context as CameraContract.View)
    }

    fun processCameraResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            CameraPresenter.REQUEST_TAKE_PHOTO -> presenter.onNewImageTaken()
            CameraPresenter.REQUEST_GALLERY -> {
                val filePath = data?.data
                presenter.onNewImageSelected(filePath)
            }
        }
    }

    fun showImageOptionsDialog() {
        val options = arrayOf("Take image", "gallery")
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Add picture")
            .setItems(options) { _, index -> presenter.onImageOptionSelected(index) }
            .setNegativeButton("cancel", null)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_background)
        alertDialog.show()
    }

    private fun onThumbnailRemoved(path: String) {
        val builder = AlertDialog.Builder(context)
        val view = View.inflate(context, R.layout.picture_dialog, null)

        val imageView = view.findViewById<ImageView>(R.id.image_view_picture_dialog)
        imageView.setImageURI(Uri.fromFile(File(path)))

        builder.setView(view)
            .setNegativeButton("cancel", null)
            .setPositiveButton("Remove image") { _, _ -> removeThumbnail(path) }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_background)
        dialog.show()
    }

    private fun removeThumbnail(path: String) {
        presenter.removeImage(path)
    }


    fun onImageAdded(path: String) {
        thumbnailsAdapter.addImage(path)
    }

    fun onImageRemoved(position: Int) {
        thumbnailsAdapter.removeImage(position)
    }


    fun addAllImages(list: List<String>) {
        presenter.imageElement.addAll(0, list)
    }

    fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = thumbnailsAdapter
    }

    fun getImagesAsArray(): Array<String> {
        return presenter.imageElement.toTypedArray()
    }
}
