package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraUtils
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListViewModel
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.databinding.FragmentAddEditCompElementBinding

class AddEditCompElementFragment : Fragment(), CameraContract.View {
    private lateinit var cameraUtils: CameraUtils
    private lateinit var binding: FragmentAddEditCompElementBinding
    private lateinit var listViewModel: ComparingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentAddEditCompElementBinding.inflate(layoutInflater)
        listViewModel = ViewModelProvider(requireActivity())[ComparingListViewModel::class.java]

        binding.numberPickerPriority.minValue = 1
        binding.numberPickerPriority.maxValue = 10

        if (listViewModel.getCurrentElement() != null) {
            binding.editTextCompElemDescription.setText(listViewModel.getCurrentElement()?.description)
            binding.editTextCompElemTitle.setText(listViewModel.getCurrentElement()?.title)
            binding.numberPickerPriority.value = listViewModel.getCurrentElement()!!.rating
            listViewModel.getThumbnailsForList().observe(
                viewLifecycleOwner
            ) { pathList: List<PathElement> ->
                cameraUtils.addAllImages(pathList.filter { it.parentEntry==listViewModel.getCurrentElement()!!.comparingElementId }.map { it.path })
            }

        }
        binding.buttonCompElemAddImage.setOnClickListener { cameraUtils.showImageOptionsDialog() }
        binding.buttonSaveCompElem.setOnClickListener { saveElement() }

        initCameraUI()
        return binding.root
    }

    private fun initCameraUI() {
        cameraUtils = CameraUtils(requireContext(), this as CameraContract.View)
        cameraUtils.initRecyclerView(binding.recyclerViewCompElemImages)
    }

    private fun saveElement() {
        val title = binding.editTextCompElemTitle.text.toString()
        val description = binding.editTextCompElemDescription.text.toString()
        val rating = binding.numberPickerPriority.value
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(requireContext(), "Insert Title and Description", Toast.LENGTH_SHORT)
                .show()
            return
        }
        listViewModel.createOrUpdateElement(title, description, rating, cameraUtils.getPaths())

        val nextFrag = ComparingElementOverviewFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_comp_list, nextFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }

    private val cameraLauncher = registerForActivityResult(StartActivityForResult()) {
        cameraUtils.onNewImageTaken()
    }

    private val galleryLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        val filePath = result.data?.data
        cameraUtils.onNewImageSelected(filePath)
    }

    override fun openCamera(intent: Intent) {
        try {
            cameraLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Bla", LENGTH_SHORT).show()
        }
    }

    override fun openGallery(intent: Intent) {
        try {
            galleryLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Bla", LENGTH_SHORT).show()
        }
    }


    override fun onImageAdded(path: String) {
        cameraUtils.onImageAdded(path)
    }

    override fun onImageRemoved(position: Int) {
        cameraUtils.onImageRemoved(position)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        cameraUtils.processPermissionResult(permissions)
    }

    override fun showPermissionRequestDialog(permission: String) {
        requestPermissionLauncher.launch(arrayOf(permission))
    }
}
