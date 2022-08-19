package com.example.rpicommunicator_v1.component.comparing.secondlevel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rpicommunicator_v1.R
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.Constants.EXTRA_DESCRIPTION
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_IMAGE_PATH
import com.example.rpicommunicator_v1.component.Constants.EXTRA_PRIORITY
import com.example.rpicommunicator_v1.component.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.component.Constants.MODE
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraPresenter
import com.example.rpicommunicator_v1.component.camera.CameraThumbnailsAdapter
import com.example.rpicommunicator_v1.databinding.ActivityAddNoteBinding
import java.io.File

class AddComparingElementActivity : AppCompatActivity(), CameraContract.View {
    //private var editTextTitle: EditText? = null
    //private var editTextDescription: EditText? = null
    //private var numberPickerPriority: NumberPicker? = null
    private var mode: String? = null

    var presenter: CameraContract.Presenter = CameraPresenter(this)
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var thumbnailsAdapter: CameraThumbnailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.attachView(this)
        // editTextTitle = findViewById(R.id.edit_text_title)
        // editTextDescription = findViewById(R.id.edit_text_description)
        // numberPickerPriority = findViewById(R.id.number_picker_priority)
        binding.numberPickerPriority.setMinValue(1)
        binding.numberPickerPriority.setMaxValue(10)

        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit note"
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1))
            presenter.imageElement.addAll(
                0,
                intent.getStringArrayExtra(EXTRA_IMAGE_PATH)?.toList() ?: mutableListOf()
            )
        } else {
            title = "AddNote"
        }
        if (intent.hasExtra(MODE)) {
            mode = intent.getStringExtra(MODE)
        }
        binding.imageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size).toInt()
        thumbnailsAdapter =
            CameraThumbnailsAdapter(
                presenter.imageElement,
                { onThumbnailRemoved(it) },
                thumbnailSize
            )
        binding.imageRecyclerView.adapter = thumbnailsAdapter
        binding.addImageButton.setOnClickListener { showImageOptionsDialog() }
        binding.buttonSaveComparingElement.setOnClickListener { saveNote() }
    }

    private fun saveNote() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val priority = binding.numberPickerPriority.value
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert Title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)
        data.putExtra(EXTRA_IMAGE_PATH, presenter.imageElement.toTypedArray())

        data.putExtra(MODE, mode)
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
            CameraPresenter.REQUEST_TAKE_PHOTO -> presenter.onNewImageTaken()
            CameraPresenter.REQUEST_GALLERY -> {
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
        startActivityForResult(intent, CameraPresenter.REQUEST_TAKE_PHOTO)
    }

    override fun openGallery(intent: Intent) {
        startActivityForResult(intent, CameraPresenter.REQUEST_GALLERY)
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