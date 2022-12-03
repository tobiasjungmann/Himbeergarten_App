package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.Constants.EXTRA_DESCRIPTION
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_IMAGE_PATH
import com.example.rpicommunicator_v1.component.Constants.EXTRA_PRIORITY
import com.example.rpicommunicator_v1.component.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.component.Constants.MODE
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraPresenter
import com.example.rpicommunicator_v1.databinding.ActivityAddCompElemBinding

class AddElementActivity : AppCompatActivity(), CameraContract.View {
    private var mode: String? = null
    private lateinit var cameraUtils: CameraUtils
    private lateinit var binding: ActivityAddCompElemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCompElemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraUtils = CameraUtils(this)

        binding.numberPickerPriority.minValue = 1
        binding.numberPickerPriority.maxValue = 10

        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit note"
            binding.editTextCompElemDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            binding.editTextCompElemTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.numberPickerPriority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
            cameraUtils.addAllImages(
                intent.getStringArrayExtra(EXTRA_IMAGE_PATH)?.toList() ?: mutableListOf()
            )

        } else {
            title = "AddNote"
        }
        if (intent.hasExtra(MODE)) {
            mode = intent.getStringExtra(MODE)
        }
        binding.recyclerViewCompElemImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerViewCompElemImages.adapter = cameraUtils.thumbnailsAdapter
        binding.buttonCompElemAddImage.setOnClickListener { cameraUtils.showImageOptionsDialog() }
        binding.buttonSaveCompElem.setOnClickListener { saveNote() }
    }

    private fun saveNote() {
        val title = binding.editTextCompElemTitle.text.toString()
        val description = binding.editTextCompElemDescription.text.toString()
        val priority = binding.numberPickerPriority.value
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert Title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)
        data.putExtra(EXTRA_IMAGE_PATH, cameraUtils.presenter.imageElement.toTypedArray())

        data.putExtra(MODE, mode)
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    @Deprecated("remove later on")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraUtils.processCameraResult(requestCode, resultCode, data)
    }

    override fun openCamera(intent: Intent) {
        startActivityForResult(intent, CameraPresenter.REQUEST_TAKE_PHOTO)
    }

    override fun openGallery(intent: Intent) {
        startActivityForResult(intent, CameraPresenter.REQUEST_GALLERY)
    }


    override fun onImageAdded(path: String) {
        cameraUtils.onImageAdded(path)
    }

    override fun onImageRemoved(position: Int) {
        cameraUtils.onImageRemoved(position)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun showPermissionRequestDialog(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }
}