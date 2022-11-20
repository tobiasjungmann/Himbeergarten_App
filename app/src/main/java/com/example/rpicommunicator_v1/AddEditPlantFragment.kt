package com.example.rpicommunicator_v1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraPresenter
import com.example.rpicommunicator_v1.component.camera.CameraThumbnailsAdapter
import com.example.rpicommunicator_v1.component.plant.PlantViewModel
import com.example.rpicommunicator_v1.databinding.FragmentAddEditPlantBinding
import java.io.File

class AddEditPlantFragment : Fragment(), CameraContract.View {


    private lateinit var plantViewModel: PlantViewModel
    private lateinit var presenter: CameraContract.Presenter
    private lateinit var binding: FragmentAddEditPlantBinding
    private lateinit var thumbnailsAdapter: CameraThumbnailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentAddEditPlantBinding.inflate(layoutInflater)
        // setContentView(binding.root)
        plantViewModel = ViewModelProvider(requireActivity()).get(PlantViewModel::class.java)


        if (plantViewModel.getCurrentPlant() != null) {

            binding.editTextDescription.setText(plantViewModel.getCurrentPlant()!!.info)
            binding.editTextTitle.setText(plantViewModel.getCurrentPlant()!!.name)
            binding.editTextInfo.setText(plantViewModel.getCurrentPlant()!!.watered)
        }

        binding.imageRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        binding.addImageButton.setOnClickListener { showImageOptionsDialog() }
        binding.buttonSaveComparingElement.setOnClickListener { saveNote() }

        initCameraUI()

        return binding.root
    }

    private fun initCameraUI() {
        presenter = CameraPresenter(requireContext())
        presenter.attachView(this)
        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size).toInt()
        thumbnailsAdapter =
            CameraThumbnailsAdapter(
                presenter.imageElement,
                { onThumbnailRemoved(it) },
                thumbnailSize
            )
        binding.imageRecyclerView.adapter = thumbnailsAdapter
    }

    private fun saveNote() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(context, "Insert Title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        plantViewModel.getCurrentPlant()!!.name = title
        //  plantViewModel.currentPlant!!.watered=description
        plantViewModel.getCurrentPlant()!!.info = description
        //fixme   finish()
    }

    @Deprecated("remove later on")
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
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Add picture")
            .setItems(options) { _, index -> presenter.onImageOptionSelected(index) }
            .setNegativeButton("cancel", null)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_background)
        alertDialog.show()
    }

    private fun onThumbnailRemoved(path: String) {
        val builder = AlertDialog.Builder(requireContext())
        val view = View.inflate(context, R.layout.picture_dialog, null)

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