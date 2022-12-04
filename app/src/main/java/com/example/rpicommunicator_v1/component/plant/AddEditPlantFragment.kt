package com.example.rpicommunicator_v1.component.plant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.camera.CameraUtils
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.databinding.FragmentAddEditPlantBinding

class AddEditPlantFragment : Fragment(){


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


        binding.buttonComparingElementAddImage.setOnClickListener { cameraUtils.showImageOptionsDialog() }
        binding.buttonSaveAddEditElement.setOnClickListener { savePlant() }

        initGpioList()
        initCameraUI()

        return binding.root
    }

    private fun initGpioList() {
        //  val list = ArrayList<GpioElementPair>()
        //  plantViewModel.plantRepository.addRPi()
        val adapter = GpioAdapter(requireContext(), plantViewModel)
        plantViewModel.getGpioEntries().observe(
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
        //   binding.recyclerViewGpioSelect.isNestedScrollingEnabled = false
    }

    private fun initCameraUI() {
        cameraUtils = CameraUtils(requireContext())
        cameraUtils.initRecyclerView(binding.recyclerViewComparingElementImages)
    }


    @Deprecated("remove later on")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraUtils.processCameraResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        savePlant()
        super.onStop()
    }

    private fun savePlant() {
        val name = binding.editTextAddEditPlantName.text.toString()
        val info = binding.editTextAddEditPlantInfo.text.toString()

        if (name.trim { it <= ' ' }.isEmpty() || info.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(context, "Insert Title and Description", Toast.LENGTH_SHORT).show()
            return
        }
        plantViewModel.createUpdateCurrentPlant(name, info)
    }
}
