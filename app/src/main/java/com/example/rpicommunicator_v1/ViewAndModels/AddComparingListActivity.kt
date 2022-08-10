package com.example.rpicommunicator_v1.ViewAndModels

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.ViewAndModels.CameraPresenter.Companion.REQUEST_GALLERY
import com.example.rpicommunicator_v1.ViewAndModels.CameraPresenter.Companion.REQUEST_TAKE_PHOTO
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.databinding.ActivityAddComparingListBinding
import java.io.File
import javax.inject.Inject


class AddComparingListActivity : AppCompatActivity(), CameraContract.View {


    private lateinit var binding: ActivityAddComparingListBinding
    private lateinit var editTextTitle: EditText
    private lateinit var saveListButton: Button
    private var imageCapture: ImageCapture? = null
    private lateinit var thumbnailsAdapter: CameraThumbnailsAdapter


    var presenter: CameraContract.Presenter=CameraPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComparingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  presenter = CameraPresenter(this)
        presenter.attachView(this)

        editTextTitle = findViewById(R.id.edit_Text_Liste)
        saveListButton = findViewById(R.id.save_new_list_button)
        this.saveListButton.setOnClickListener { saveList() }


        val imagePaths = presenter.imageElement.picturePaths
        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size).toInt()
        thumbnailsAdapter =
            CameraThumbnailsAdapter(imagePaths, { onThumbnailRemoved(it) }, thumbnailSize)
        binding.imageRecyclerView.adapter = thumbnailsAdapter
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