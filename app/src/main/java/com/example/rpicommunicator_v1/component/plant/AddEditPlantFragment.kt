package com.example.rpicommunicator_v1.component.plant

import android.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraUtils
import com.example.rpicommunicator_v1.database.plant.models.Device
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.databinding.FragmentAddEditPlantBinding

class AddEditPlantFragment : Fragment(), CameraContract.View {


    private lateinit var plantViewModel: PlantViewModel
    private lateinit var binding: FragmentAddEditPlantBinding
    private lateinit var cameraUtils: CameraUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentAddEditPlantBinding.inflate(layoutInflater)
        plantViewModel = ViewModelProvider(requireActivity()).get(PlantViewModel::class.java)

        if (plantViewModel.getCurrentPlant() != null) {
            binding.editTextAddEditPlantName.setText(plantViewModel.getCurrentPlant()!!.name)
            binding.editTextAddEditPlantInfo.setText(plantViewModel.getCurrentPlant()!!.info)
        }

        binding.recyclerViewComparingElementImages.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.buttonSaveAddEditElement.setOnClickListener { savePlant(true) }

        initGpioList()
        initCameraUI()

        return binding.root
    }

    private fun initGpioList() {

        // todo init spinner list with db
        val deviceTypes = arrayListOf<String>()
        val typeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item, deviceTypes
        )
        plantViewModel.loadAllDevices().observe(
            viewLifecycleOwner
        ) { devices: List<Device> ->
            if (devices.isNotEmpty()) {
                binding.cardViewGpioListView.visibility=View.VISIBLE
                typeAdapter.clear()
                typeAdapter.addAll(devices.map { it.name })
                plantViewModel.queryMutableGpioEntries(devices[0].name)
            }
        }
        binding.spinnerDevice.adapter = typeAdapter
        binding.spinnerDevice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //parent.toString()
               // todo plantViewModel.queryMutableGpioEntries(devices.get(0).name)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("onselected", "onItemSelected: "+view.toString())
            }
        }

        val adapter = GpioAdapter(requireContext(), plantViewModel)
        plantViewModel.getMutableGpioEntries().observe(
            viewLifecycleOwner
        ) { gpioElements: List<GpioElement> ->
            val pairs = ArrayList<GpioElementPair>()
            for (i in 0..gpioElements.size step 2) {
                if (i + 1 < gpioElements.size) {
                    pairs.add(GpioElementPair(gpioElements[i], gpioElements[i + 1]))
                }
            }
            adapter.setGpioElements(pairs)
        }
        binding.recyclerViewGpioSelect.adapter = adapter
        binding.recyclerViewGpioSelect.layoutManager = LinearLayoutManager(context)
    }

    private fun initCameraUI() {
        cameraUtils = CameraUtils(requireContext(), this as CameraContract.View)
        cameraUtils.initRecyclerView(binding.recyclerViewComparingElementImages)
        binding.buttonComparingElementAddImage.setOnClickListener { cameraUtils.showImageOptionsDialog() }
    }

    override fun onStop() {
        savePlant(false)
        super.onStop()
    }

    private fun savePlant(calledFromButton: Boolean) {
        val name = binding.editTextAddEditPlantName.text.toString()
        val info = binding.editTextAddEditPlantInfo.text.toString()

        if (plantViewModel.createUpdateCurrentPlant(
                name,
                info,
                context,
                cameraUtils.getPaths()
            ) && calledFromButton
        ) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            cameraUtils.onNewImageTaken()
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val filePath = result.data?.data
            cameraUtils.onNewImageSelected(filePath)
        }

    override fun openCamera(intent: Intent) {
        try {
            cameraLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Bla", Toast.LENGTH_SHORT).show()
        }
    }

    override fun openGallery(intent: Intent) {
        try {
            galleryLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Bla", Toast.LENGTH_SHORT).show()
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
