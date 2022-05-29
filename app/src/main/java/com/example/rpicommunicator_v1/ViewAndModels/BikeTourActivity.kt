package com.example.rpicommunicator_v1.ViewAndModels


import androidx.appcompat.app.AppCompatActivity
import com.example.rpicommunicator_v1.Database.Bike.BikeTour
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rpicommunicator_v1.R
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.Database.Bike.BikeAdapter
import com.example.rpicommunicator_v1.Database.Plant.Plant
import com.example.rpicommunicator_v1.Database.Plant.PlantAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import java.text.SimpleDateFormat
import java.util.*

class BikeTourActivity : AppCompatActivity() {
    private var bikeViewModel: BikeTourViewModel? = null

    //  private LineDataSet lineDataSet1;
    private var bikeToursList: List<BikeTour>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        bikeViewModel = ViewModelProvider(this).get(BikeTourViewModel::class.java)
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val time = formatter.format(Date())
        initUIElements()
        bikeViewModel!!.allBikeTours.observe(this) { bikeTours ->
            if (!bikeTours.isEmpty()) {
                findViewById<View>(R.id.direction_card_viewDiagram).visibility = View.VISIBLE
                findViewById<View>(R.id.direction_card_viewStatistics).visibility = View.VISIBLE
                bikeToursList = bikeTours
                initChart(bikeTours)
                initStatistics()
            } else {
                findViewById<View>(R.id.direction_card_viewDiagram).visibility = View.GONE
                findViewById<View>(R.id.direction_card_viewStatistics).visibility = View.GONE
            }
        }

        initRecyclerView()
    }


    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.bike_tour_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = BikeAdapter()

        recyclerView.adapter = adapter
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
        //int sum = data.values().stream().reduce(0, Integer::sum);
        var sum = 0
        for (b in bikeToursList!!) {
            sum += b.km.toInt()
        }
        (findViewById<View>(R.id.sumOfAllToursTextView) as TextView).text =
            "$sum km von 2500km"
    }

    private fun initUIElements() {
        findViewById<View>(R.id.add_button).setOnClickListener { v: View? ->
            val from = (findViewById<View>(R.id.inputBikeFrom) as EditText).text.toString()
            val to = (findViewById<View>(R.id.inputBikesTo) as EditText).text.toString()
            val km = (findViewById<View>(R.id.editTextNumberDecimal) as EditText).text.toString()
                .toDouble()
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val time = formatter.format(Date())
            Log.d("TAG", "initUIElements: $time")
            bikeViewModel!!.insert(BikeTour(from, to, km, time))
            (findViewById<View>(R.id.inputBikeFrom) as EditText).setText("Unity Beta")
            (findViewById<View>(R.id.inputBikesTo) as EditText).setText("Zuhause")
            (findViewById<View>(R.id.editTextNumberDecimal) as EditText).setText("20")
        }
        findViewById<View>(R.id.delete_button).setOnClickListener { v: View? ->
            bikeViewModel!!.remove(
                bikeToursList!![bikeToursList!!.size - 1]
            )
        }
    }

    private fun initChart(bikeTours: List<BikeTour>) {
        val chart = findViewById<LineChart>(R.id.chart)
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet1 = LineDataSet(getDataSet(bikeTours), "Data 1")
        styleDataset(lineDataSet1)
        styleChart(chart)
        dataSets.add(lineDataSet1)
        val data = LineData(dataSets)
        chart.data = data
        chart.invalidate()
    }

    private fun styleDataset(lineDataSet1: LineDataSet) {
        lineDataSet1.lineWidth = 1f
        lineDataSet1.circleRadius = 2f
        lineDataSet1.circleHoleRadius = 1f
        lineDataSet1.color = resources.getColor(R.color.green_yellow_palette)
        lineDataSet1.setCircleColor(resources.getColor(R.color.green_yellow_palette))
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawVerticalHighlightIndicator(false)
    }

    private fun styleChart(chart: LineChart) {
        chart.setDrawBorders(true)
        chart.setBorderColor(resources.getColor(R.color.light_grey))
        chart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        chart.description = description // Hide the description
        // chart.getAxisLeft().setDrawLabels(false);
        chart.axisRight.setDrawLabels(false)
        chart.axisRight.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setDrawLabels(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.setDrawLabels(false)
        chart.axisLeft.axisMaximum = 2500f
        chart.axisRight.axisMaximum = 2500f
        chart.xAxis.axisMaximum = 366f
        chart.legend.isEnabled = false
        chart.xAxis.setDrawAxisLine(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisRight.setDrawAxisLine(false)
        // chart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());
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