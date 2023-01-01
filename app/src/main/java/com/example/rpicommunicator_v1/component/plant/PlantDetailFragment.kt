package com.example.rpicommunicator_v1.component.plant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.database.plant.models.HumidityEntry
import com.example.rpicommunicator_v1.database.plant.models.Plant
import com.example.rpicommunicator_v1.databinding.FragmentPlantDetailBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class PlantDetailFragment : Fragment() {
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var binding: FragmentPlantDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantDetailBinding.inflate(layoutInflater)
        plantViewModel = ViewModelProvider(requireActivity())[PlantViewModel::class.java]

        if (plantViewModel.getCurrentPlant() == null) {
            Log.d("Debug", "onCreateView: No plant specified for the detail view")
        }

        initViewComponents()
        initChart()

        return binding.root
    }

    private fun initViewComponents() {
        val plant = plantViewModel.getCurrentPlant()!!
        initImage(plant)

        binding.textViewPlantName.text = plant.name
        binding.textViewHumidity.text = plant.humidity
        binding.textViewPlantDetailDescription.text = plant.info


        binding.buttonEditPlant.setOnClickListener {
            // current plant can be kept - no need to reset it
            val nextFrag = AddEditPlantFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_plant, nextFrag, "findThisFragment")
                .addToBackStack("addedit")
                .commit()
        }
    }

    private fun initImage(plant: Plant) {
        plantViewModel.getThumbnailsForList().observe(
            viewLifecycleOwner
        ) { pathList: List<PathElement> ->
            val firstThumbnailIndex = pathList.indexOfFirst { it.parentEntry == plant.plant }

            if (firstThumbnailIndex >= 0) {
                binding.imageViewPlantDetailHeader.visibility = View.VISIBLE
                binding.imageViewPlantDetailHeader.setImageBitmap(
                    pathList[firstThumbnailIndex].loadFullImage()
                )
                val alpha = 1.toFloat()
                binding.imageViewPlantDetailHeader.alpha = alpha

            } else {
                binding.imageViewPlantDetailHeader.visibility = View.GONE
            }
        }
    }


    private fun initChart() {
        plantViewModel.queryHumidityEntriesForCurrentPlant().observe(
            viewLifecycleOwner
        ) { humidityEntries: List<HumidityEntry> ->
            if (humidityEntries.isNotEmpty()) {
                val dataSets = ArrayList<ILineDataSet>()
                val lineDataSet1 = LineDataSet(getDataSet(humidityEntries), "Data 1")
                styleDataset(lineDataSet1)
                styleChart()
                dataSets.add(lineDataSet1)
                val data = LineData(dataSets)
                binding.chartPlantDetail.data = data
                binding.chartPlantDetail.invalidate()
                binding.layoutHumidityContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun styleDataset(lineDataSet1: LineDataSet) {
        lineDataSet1.lineWidth = 2f
        lineDataSet1.circleRadius = 6f
        lineDataSet1.circleHoleRadius = 3f
        lineDataSet1.color = ContextCompat.getColor(
            requireContext().applicationContext,
            R.color.primary_green
        )
        lineDataSet1.setCircleColor(
            ContextCompat.getColor(
                requireContext().applicationContext,
                R.color.primary_green
            )
        )
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawVerticalHighlightIndicator(false)
    }


    private fun styleChart() {
        binding.chartPlantDetail.setDrawBorders(true)
        binding.chartPlantDetail.setBorderColor(
            ContextCompat.getColor(
                requireContext().applicationContext,
                R.color.light_grey
            )
        )
        binding.chartPlantDetail.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        binding.chartPlantDetail.description = description // Hide the description
        binding.chartPlantDetail.axisRight.setDrawLabels(false)
        binding.chartPlantDetail.axisRight.setDrawGridLines(false)
        binding.chartPlantDetail.axisLeft.setDrawGridLines(false)
        binding.chartPlantDetail.axisLeft.setDrawLabels(false)
        binding.chartPlantDetail.xAxis.setDrawGridLines(false)
        binding.chartPlantDetail.xAxis.setDrawLabels(false)
        binding.chartPlantDetail.axisLeft.axisMinimum = 0F
        binding.chartPlantDetail.axisRight.axisMinimum = 0F
        binding.chartPlantDetail.axisLeft.axisMaximum = 500f
        binding.chartPlantDetail.axisRight.axisMaximum = 500f
        binding.chartPlantDetail.legend.isEnabled = false
        binding.chartPlantDetail.xAxis.setDrawAxisLine(false)
        binding.chartPlantDetail.axisLeft.setDrawAxisLine(false)
        binding.chartPlantDetail.axisRight.setDrawAxisLine(false)
        val limitLine = LimitLine(200f) // set where the line should be drawn
        limitLine.lineColor = ContextCompat.getColor(
            requireContext().applicationContext,
            R.color.primary_green
        )
        limitLine.lineWidth = 2f
        binding.chartPlantDetail.axisLeft.addLimitLine(limitLine)
        val limitLine2 = LimitLine(100f) // set where the line should be drawn
        limitLine2.lineColor = ContextCompat.getColor(
            requireContext().applicationContext,
            R.color.red
        )
        limitLine2.lineWidth = 2f
        binding.chartPlantDetail.axisLeft.addLimitLine(limitLine2)
        binding.chartPlantDetail.axisLeft.addLimitLine(limitLine)
    }

    private fun getDataSet(entries: List<HumidityEntry>): ArrayList<Entry> {
        val dataSets = ArrayList<Entry>()
        for (s in entries) {
            dataSets.add(Entry(s.value, s.timestamp))
        }
        return dataSets
    }
}