package com.example.rpicommunicator_v1.component.bike


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.bike.BikeTour
import com.example.rpicommunicator_v1.databinding.ActivityBikeBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*

class BikeTourActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBikeBinding
    private var bikeViewModel: BikeTourViewModel? = null

    private var bikeToursList: List<BikeTour>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBikeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bikeViewModel = ViewModelProvider(this)[BikeTourViewModel::class.java]

        initUIElements()
        bikeViewModel!!.allBikeTours.observe(this) { bikeTours ->
            if (bikeTours.isNotEmpty()) {
                binding.directionCardViewDiagram.visibility = View.VISIBLE
                binding.directionCardViewStatistics.visibility = View.VISIBLE
                bikeToursList = bikeTours
                initChart(bikeTours)
                initStatistics()
            } else {
                binding.directionCardViewDiagram.visibility = View.GONE
                binding.directionCardViewStatistics.visibility = View.GONE
            }
        }

        initRecyclerView()
    }


    private fun initRecyclerView() {

        binding.bikeTourRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.bikeTourRecyclerview.setHasFixedSize(true)

        val adapter = BikeAdapter()

        binding.bikeTourRecyclerview.adapter = adapter
        adapter.setViewModel(bikeViewModel)
        bikeViewModel?.allBikeTours?.observe(
            this
        ) { bikeTours: List<BikeTour> ->
            adapter.setBikeTours(
                bikeTours
            )
        }
    }

    private fun initStatistics() {
        var sum = 0
        for (b in bikeToursList!!) {
            sum += b.km.toInt()
        }
        binding.sumOfAllToursTextView.text =
            "$sum km von 2500km"
    }

    private fun initUIElements() {
        binding.addButton.setOnClickListener {
            val from = binding.inputBikeFrom.text.toString()
            val to = binding.inputBikesTo.text.toString()
            val km = binding.editTextNumberDecimal.text.toString()
                .toDouble()
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val time = formatter.format(Date())
            Log.d("TAG", "initUIElements: $time")
            bikeViewModel!!.insert(BikeTour(from, to, km, time))
        }
       binding.buttonDeleteItem.setOnClickListener {
            bikeViewModel!!.remove(
                bikeToursList!![bikeToursList!!.size - 1]
            )
        }
    }

    private fun initChart(bikeTours: List<BikeTour>) {

        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet1 = LineDataSet(getDataSet(bikeTours), "Data 1")
        styleDataset(lineDataSet1)
        styleChart()
        dataSets.add(lineDataSet1)
        val data = LineData(dataSets)
        binding.chartPlantDetail.data = data
        binding.chartPlantDetail.invalidate()
    }

    private fun styleDataset(lineDataSet1: LineDataSet) {
        lineDataSet1.lineWidth = 1f
        lineDataSet1.circleRadius = 2f
        lineDataSet1.circleHoleRadius = 1f
        lineDataSet1.color = ContextCompat.getColor(application,R.color.primary_green_lighter)
        lineDataSet1.setCircleColor(ContextCompat.getColor(application,R.color.primary_green_lighter))
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawVerticalHighlightIndicator(false)
    }

    private fun styleChart() {
        binding.chartPlantDetail.setDrawBorders(true)
        binding.chartPlantDetail.setBorderColor(ContextCompat.getColor(application,R.color.light_grey))
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
      //  binding.chartPlantDetail.axisLeft.axisMaximum = 2500f    // todo togle with button
      //  binding.chartPlantDetail.axisRight.axisMaximum = 2500f
      //  binding.chartPlantDetail.xAxis.axisMaximum = 366f
        binding.chartPlantDetail.legend.isEnabled = false
        binding.chartPlantDetail.xAxis.setDrawAxisLine(false)
        binding.chartPlantDetail.axisLeft.setDrawAxisLine(false)
        binding.chartPlantDetail.axisRight.setDrawAxisLine(false)
        // binding.chart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());
    }

    private fun getDataSet(graphlist: List<BikeTour>): ArrayList<Entry> {
        val dataSets = ArrayList<Entry>()
        /*graphString  String[] coordinates = graphString.split(";");
        for (String s : coordinates) {
            dataSets.add(new Entry(Integer.parseInt(s.substring(0, s.indexOf("_"))), Integer.parseInt(s.substring(s.indexOf("_") + 1))));
        }*/
        //graphlist.forEach(b->dataSets.add(new Entry(b.getKm())));
        var combined = 0
        for (i in graphlist.indices) {
            combined += graphlist[i].km.toInt()
            dataSets.add(Entry(i.toFloat(), combined.toFloat()))
        }
        return dataSets
    }
}