package com.example.rpicommunicator_v1.component.plant


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants.EXTRA_GRAPH_STRING
import com.example.rpicommunicator_v1.component.Constants.EXTRA_HUMIDITY
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ICON
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_IMAGE
import com.example.rpicommunicator_v1.component.Constants.EXTRA_INFO
import com.example.rpicommunicator_v1.component.Constants.EXTRA_NAME
import com.example.rpicommunicator_v1.component.Constants.EXTRA_NEEDS_WATER
import com.example.rpicommunicator_v1.component.Constants.EXTRA_WATERED
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.databinding.ActivityPlantViewBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class PlantView : AppCompatActivity() {
    private var mainActivityViewModel: MainActivityViewModel? = null
    private val dataWasChanged = false
    private var waterNeededChanged = false
    private var plant: Plant? = null
    private var needsWater = false
    private lateinit var binding: ActivityPlantViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

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
    }

    private fun initPlant() {
        val name = intent.getStringExtra(EXTRA_NAME)
        val humidity = intent.getStringExtra(EXTRA_HUMIDITY)
        val watered = intent.getStringExtra(EXTRA_WATERED)
        needsWater = intent.getBooleanExtra(EXTRA_NEEDS_WATER, false)
        val imageid = intent.getIntExtra(EXTRA_IMAGE, -1)
        val iconid = intent.getIntExtra(EXTRA_ICON, -1)
        val info = intent.getStringExtra(EXTRA_INFO)
        val id = intent.getStringExtra(EXTRA_ID)
        val graphString = intent.getStringExtra(EXTRA_GRAPH_STRING)
        plant = Plant(
            id.toString(),
            name.toString(),
            info.toString(),
            watered.toString(),
            humidity.toString(),
            needsWater,
            graphString.toString()
        )
        plant!!.imageID = imageid
        plant!!.iconID = iconid
    }

    override fun onStop() {
        if (dataWasChanged || waterNeededChanged) {
            Log.d("PlantView", "onstop: unchange data: " + plant!!.needsWater)
            plant!!.needsWater = !needsWater
            Log.d("PlantView", "onstop: data must be saved Plant: " + plant!!.needsWater)
            mainActivityViewModel!!.update(plant!!)
            mainActivityViewModel!!.updateWateredInFirebase(plant!!.id, plant!!.needsWater)
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
        lineDataSet1.setCircleColor(ContextCompat.getColor(application, R.color.primary_green_lighter))
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
        binding.chart.axisLeft.axisMinimum=0F
        binding.chart.axisRight.axisMinimum=0F
        binding.chart.axisLeft.axisMaximum = 500f
        binding.chart.axisRight.axisMaximum = 500f
        binding.chart.legend.isEnabled = false
        binding.chart.xAxis.setDrawAxisLine(false)
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.axisRight.setDrawAxisLine(false)
        val limitLine = LimitLine(200f) // set where the line should be drawn
        limitLine.lineColor = ContextCompat.getColor(application,R.color.primary_green_lighter)
        limitLine.lineWidth = 2f
        binding.chart.axisLeft.addLimitLine(limitLine)
        val limitLine2 = LimitLine(100f) // set where the line should be drawn
        limitLine2.lineColor = ContextCompat.getColor(application,R.color.red)
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
    } /* public class GraphXAxisValueFormatter implements IAxisValueFormatter {

        private int MINUTES_INTERVAL = 5;
        private String[] mValues;
        private int mInterval;
        public SensorInterval.Interval mSlot;

        public GraphXAxisValueFormatter(List<BinSensorData> range, int interval, SensorInterval.Interval slot) {
            mValues = new String[range.size()];
            mInterval = interval;
            mSlot = slot;

            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < range.size(); i++) {
                calendar.setTimeInMillis(range.get(i).getTime());

                int unroundedMinutes = calendar.get(Calendar.MINUTE);
                int mod = unroundedMinutes % MINUTES_INTERVAL;
                calendar.add(Calendar.MINUTE, mod < 8 ? -mod : (MINUTES_INTERVAL - mod));


                String s = "";

                if (slot.equals(SensorInterval.Interval.HOUR) || slot.equals(SensorInterval.Interval.DAY))
                    s = Util.getTimeFromTimestamp(calendar.getTimeInMillis());
                else if (slot.equals(SensorInterval.Interval.WEEK))
                    s = Util.getDayFromTimestamp(calendar.getTimeInMillis());
                else if (slot.equals(SensorInterval.Interval.MONTH))
                    s = Util.getMonthFromTimestamp(calendar.getTimeInMillis());
                else if (slot.equals(SensorInterval.Interval.YEAR))
                    s = Util.getYearFromTimestamp(calendar.getTimeInMillis());


                Util.setLog("Time : " + s);
                mValues[i] = s;
            }
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value % mInterval == 0 && value >= 0) {
                return mValues[(int) value % mValues.length];
            } else
                return "";

        }


        public int getDecimalDigits() {
            return 0;
        }
    }*/
    /*public class MyYAxisValueFormatterExample implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatterExample() {

            // format values to 1 decimal digit
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            //2021.04.06:14:39_650;2021.04.06:14:44_331;2021.04.06:14:45_435;2021.04.06:14:47_357;2021.04.06:14:48_322
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
                return sdf.format(new Date(1,));

            } catch (Exception e) {

                return  dateInMillisecons;
            }
            return mFormat.format(value) + " $";
        }
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getXValue(String dateInMillisecons, int index, ViewPortHandler viewPortHandler) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
                return sdf.format(new Date(Long.parseLong(dateInMillisecons)));

            } catch (Exception e) {

                return  dateInMillisecons;
            }
        }*/
}