package com.example.rpicommunicator_v1.component.plant


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.databinding.ActivityPlantViewBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class PlantViewActivity : AppCompatActivity() {
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var binding: ActivityPlantViewBinding
    private val dataWasChanged = false      // todo move in viewmodel - does not belong here
    private var waterNeededChanged = false
    private var plant: Plant? = null
    private var needsWater = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        plantViewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        initPlant()
        initViewComponents()
        if (plant!!.graphString != "") {
            initChart(plant!!.graphString)
        } else {
            binding.chart.visibility = View.GONE
        }
    }


    private fun initViewComponents() {
        if (plant!!.imageID != -1) {
            binding.headerImage.setImageResource(plant!!.imageID)
            val alpha = 1.toFloat()
            binding.headerImage.alpha = alpha
        } else {
            binding.headerImage.setImageResource(R.drawable.icon_plant)
            val alpha = 0.1.toFloat()
            binding.headerImage.alpha = alpha
        }
        binding.plantNameTextView.text = plant!!.name
        binding.humidityTextView.text = plant!!.humidity
        binding.wateredTextView.text = plant!!.watered
        binding.infoTextView.text = plant!!.info
        if (needsWater) {
            binding.willBeWatered.visibility = View.VISIBLE
            binding.waterButton.setText(R.string.doNotWater)
        } else {
            binding.willBeWatered.visibility = View.INVISIBLE
            binding.waterButton.setText(R.string.doWater)
        }
        binding.waterButton.setOnClickListener {
            if (needsWater) {
                if (!waterNeededChanged) {
                    binding.willBeWatered.visibility = View.INVISIBLE
                    binding.waterButton.setText(R.string.doWater)
                } else {
                    binding.willBeWatered.visibility = View.VISIBLE
                    binding.waterButton.setText(R.string.doNotWater)
                }
            } else {
                if (!waterNeededChanged) {
                    binding.willBeWatered.visibility = View.VISIBLE
                    binding.waterButton.setText(R.string.doWater)
                } else {
                    binding.willBeWatered.visibility = View.INVISIBLE
                    binding.waterButton.setText(R.string.doNotWater)
                }
            }
            waterNeededChanged = !waterNeededChanged
        }
        binding.buttonEdit.setOnClickListener {
            val nextIntent = Intent(this, AddEditPlantActivity::class.java)
            nextIntent.putExtra(Constants.MODE, Constants.EDIT_REQUEST)
            resultLauncher.launch(nextIntent)
        }
    }

    private fun initPlant() {
        plant = plantViewModel.getCurrentPlant()
    }

    override fun onStop() {
        if (dataWasChanged || waterNeededChanged) {
            Log.d("PlantView", "onstop: unchange data: " + plant!!.needsWater)
            plant!!.needsWater = !needsWater
            Log.d("PlantView", "onstop: data must be saved Plant: " + plant!!.needsWater)
            plantViewModel.update(plant!!)
            plantViewModel.updateWateredInFirebase(plant!!.id, plant!!.needsWater)
        }
        super.onStop()
    }

    private fun initChart(graphString: String) {
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet1 = LineDataSet(getDataSet(graphString), "Data 1")
        styleDataset(lineDataSet1)
        styleChart()
        dataSets.add(lineDataSet1)
        val data = LineData(dataSets)
        binding.chart.data = data
        binding.chart.invalidate()
    }

    private fun styleDataset(lineDataSet1: LineDataSet) {
        lineDataSet1.lineWidth = 2f
        lineDataSet1.circleRadius = 6f
        lineDataSet1.circleHoleRadius = 3f
        lineDataSet1.color = ContextCompat.getColor(application, R.color.primary_green_lighter)
        lineDataSet1.setCircleColor(
            ContextCompat.getColor(
                application,
                R.color.primary_green_lighter
            )
        )
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawVerticalHighlightIndicator(false)
    }


    private fun styleChart() {
        binding.chart.setDrawBorders(true)
        binding.chart.setBorderColor(ContextCompat.getColor(application, R.color.light_grey))
        binding.chart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        binding.chart.description = description // Hide the description
        binding.chart.axisRight.setDrawLabels(false)
        binding.chart.axisRight.setDrawGridLines(false)
        binding.chart.axisLeft.setDrawGridLines(false)
        binding.chart.axisLeft.setDrawLabels(false)
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.xAxis.setDrawLabels(false)
        binding.chart.axisLeft.axisMinimum = 0F
        binding.chart.axisRight.axisMinimum = 0F
        binding.chart.axisLeft.axisMaximum = 500f
        binding.chart.axisRight.axisMaximum = 500f
        binding.chart.legend.isEnabled = false
        binding.chart.xAxis.setDrawAxisLine(false)
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.axisRight.setDrawAxisLine(false)
        val limitLine = LimitLine(200f) // set where the line should be drawn
        limitLine.lineColor = ContextCompat.getColor(application, R.color.primary_green_lighter)
        limitLine.lineWidth = 2f
        binding.chart.axisLeft.addLimitLine(limitLine)
        val limitLine2 = LimitLine(100f) // set where the line should be drawn
        limitLine2.lineColor = ContextCompat.getColor(application, R.color.red)
        limitLine2.lineWidth = 2f
        binding.chart.axisLeft.addLimitLine(limitLine2)
        binding.chart.axisLeft.addLimitLine(limitLine)
    }

    private fun getDataSet(graphString: String): ArrayList<Entry> {
        val dataSets = ArrayList<Entry>()
        val coordinates = graphString.split(";").toTypedArray()
        for (s in coordinates) {
            dataSets.add(
                Entry(
                    s.substring(0, s.indexOf("_")).toInt().toFloat(),
                    s.substring(s.indexOf("_") + 1).toInt()
                        .toFloat()
                )
            )
        }
        return dataSets
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultMode = data?.getStringExtra(Constants.MODE)
                if (resultMode != null) {
                    if ((resultMode == Constants.EDIT_REQUEST)) {
                        var name = data.getStringExtra(Constants.EXTRA_TITLE)
                        var type = data.getStringExtra(Constants.EXTRA_DESCRIPTION)
                        var info = data.getStringExtra(Constants.EXTRA_INFO)
                        val imagePath =
                            data.getStringArrayExtra(Constants.EXTRA_IMAGE_PATH)
                        if (name == null) {
                            name = "Name"
                        }
                        if (type == null) {
                            type = ""
                        }
                        if (info == null) {
                            info = ""
                        }
                        plantViewModel.addPlant(name, type, info)

                        Toast.makeText(this, "Plant edited", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Plant not Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Plant not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}