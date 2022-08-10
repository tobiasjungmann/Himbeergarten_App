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


class AddComparingListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddComparingListBinding
    private lateinit var editTextTitle: EditText
    private lateinit var saveListButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComparingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextTitle = findViewById(R.id.edit_Text_Liste)
        saveListButton = findViewById(R.id.save_new_list_button)
        this.saveListButton.setOnClickListener { saveList() }
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
}