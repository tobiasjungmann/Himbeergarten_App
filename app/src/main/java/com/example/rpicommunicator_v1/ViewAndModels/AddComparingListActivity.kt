package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.ViewAndModels.camera.CameraPresenter.Companion.REQUEST_GALLERY
import com.example.rpicommunicator_v1.ViewAndModels.camera.CameraPresenter.Companion.REQUEST_TAKE_PHOTO
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.ViewAndModels.camera.CameraContract
import com.example.rpicommunicator_v1.ViewAndModels.camera.CameraPresenter
import com.example.rpicommunicator_v1.ViewAndModels.camera.CameraThumbnailsAdapter
import com.example.rpicommunicator_v1.databinding.ActivityAddComparingListBinding
import java.io.File


class AddComparingListActivity : AppCompatActivity(), CameraContract.View {


    private lateinit var binding: ActivityAddComparingListBinding
    private lateinit var editTextTitle: EditText
    private lateinit var saveListButton: Button
    private lateinit var thumbnailsAdapter: CameraThumbnailsAdapter

    var presenter: CameraContract.Presenter = CameraPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComparingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.attachView(this)

        editTextTitle = findViewById(R.id.edit_Text_Liste)
        saveListButton = findViewById(R.id.save_new_list_button)
        this.saveListButton.setOnClickListener { saveList() }

//example img: /storage/emulated/0/Android/data/com.example.rpicommunicator_v1/files/Pictures/IMG_1660139618849_3778973189011056216.jpg
        binding.imageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //   presenter.imageElement.picturePaths.add(0,"/storage/emulated/0/Android/data/com.example.rpicommunicator_v1/files/Pictures/IMG_1660139618849_3778973189011056216.jpg")
        //  val imagePaths = presenter.imageElement.picturePaths
        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size).toInt()
        thumbnailsAdapter =
            CameraThumbnailsAdapter(
                presenter.imageElement.picturePaths,
                { onThumbnailRemoved(it) },
                thumbnailSize
            )
        binding.imageRecyclerView.adapter = thumbnailsAdapter
        binding.addImageButton.setOnClickListener { showImageOptionsDialog() }
    }

    private fun saveList() {
        val title = editTextTitle.text.toString()
        if (title.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_TAKE_PHOTO -> presenter.onNewImageTaken()
            REQUEST_GALLERY -> {
                val filePath = data?.data
                presenter.onNewImageSelected(filePath)
            }
        }
    }

    private fun showImageOptionsDialog() {
        val options = arrayOf("Take image", "gallery")
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Add picture")
            .setItems(options) { _, index -> presenter.onImageOptionSelected(index) }
            .setNegativeButton("cancel", null)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_background)
        alertDialog.show()
    }

    private fun onThumbnailRemoved(path: String) {
        val builder = AlertDialog.Builder(this)
        val view = View.inflate(this, R.layout.picture_dialog, null)

        val imageView = view.findViewById<ImageView>(R.id.feedback_big_image)
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

    override fun openCamera(intent: Intent) {
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }

    override fun openGallery(intent: Intent) {
        startActivityForResult(intent, REQUEST_GALLERY)
    }


    override fun onImageAdded(path: String) {
        thumbnailsAdapter.addImage(path)
    }

    override fun onImageRemoved(position: Int) {
        thumbnailsAdapter.removeImage(position)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun showPermissionRequestDialog(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }
}