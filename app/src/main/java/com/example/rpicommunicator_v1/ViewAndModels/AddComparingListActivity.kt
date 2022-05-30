package com.example.rpicommunicator_v1.ViewAndModels

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.databinding.ActivityAddComparingListBinding
import java.lang.Exception


class AddComparingListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddComparingListBinding
    private lateinit var editTextTitle: EditText
    private lateinit var saveListButton: Button
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comparing_list)
       /* binding = ActivityAddComparingListBinding.inflate(LayoutInflater)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSION
            )
        } else {
            startCamera()
        }*/

        editTextTitle = findViewById(R.id.edit_Text_Liste)
        saveListButton = findViewById(R.id.save_new_list_button)
        this.saveListButton.setOnClickListener { saveList() }
    }

  /*  private fun allPermissionGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d("CameraModule", "camera failed: ", e)
            }
        }, ContextCompat.getMainExecutor())
    }*/


    private fun saveList() {
        val title = editTextTitle.text.toString()
        if (title.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert title and Discription", Toast.LENGTH_SHORT).show()
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
}