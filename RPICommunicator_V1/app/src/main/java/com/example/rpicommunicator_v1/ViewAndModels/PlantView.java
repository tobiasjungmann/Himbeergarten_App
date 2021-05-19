package com.example.rpicommunicator_v1.ViewAndModels;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_GRAPH_STRING;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_HUMIDITY;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ICON;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_IMAGE;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_INFO;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NAME;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NEEDS_WATER;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_WATERED;

public class PlantView extends AppCompatActivity {


    private PlantViewModel plantViewModel;

    private boolean dataWasChanged = false;
    private boolean waterNeededChanged = false;
    private Plant plant;
    private boolean needsWater;
    private TextView willbeWaterdTextView;
    private Button waterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_view);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);


        initPlant();
        initViewComponents();
        if(!plant.getGraphString().equals("")){
        initChart(plant.getGraphString());}
        else {
            findViewById(R.id.chart).setVisibility(View.GONE);
        }

    }

    private void initViewComponents() {
        ImageView headerView = findViewById(R.id.header_image);
        willbeWaterdTextView = findViewById(R.id.will_be_watered);
        waterButton = ((Button) findViewById(R.id.water_button));

        if (plant.getImageID() != -1) {
            headerView.setImageResource(plant.getImageID());
            float alpha = (float) 1;
            headerView.setAlpha(alpha);
        } else {
            headerView.setImageResource(R.drawable.pump);
            float alpha = (float) 0.1;
            headerView.setAlpha(alpha);
        }

        ((TextView) findViewById(R.id.plantNameTextView)).setText(plant.getName());
        ((TextView) findViewById(R.id.humidityTextView)).setText(plant.getHumidity());
        ((TextView) findViewById(R.id.wateredTextView)).setText(plant.getWatered());
        ((TextView) findViewById(R.id.infoTextView)).setText(plant.getInfo());


        if (needsWater) {
            willbeWaterdTextView.setVisibility(View.VISIBLE);
            waterButton.setText(R.string.doNotWater);
        } else {
            willbeWaterdTextView.setVisibility(View.INVISIBLE);
            waterButton.setText(R.string.doWater);
        }

        waterButton.setOnClickListener(v -> {
            if (needsWater) {
                if (!waterNeededChanged) {
                    willbeWaterdTextView.setVisibility(View.INVISIBLE);
                    waterButton.setText(R.string.doWater);
                } else {
                    willbeWaterdTextView.setVisibility(View.VISIBLE);
                    waterButton.setText(R.string.doNotWater);
                }

            } else {
                if (!waterNeededChanged) {
                    willbeWaterdTextView.setVisibility(View.VISIBLE);
                    waterButton.setText(R.string.doWater);
                } else {
                    willbeWaterdTextView.setVisibility(View.INVISIBLE);
                    waterButton.setText(R.string.doNotWater);
                }
            }
            waterNeededChanged = !waterNeededChanged;
        });
    }

    private void initPlant() {
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String humidity = getIntent().getStringExtra(EXTRA_HUMIDITY);
        String watered = getIntent().getStringExtra(EXTRA_WATERED);
        needsWater = getIntent().getBooleanExtra(EXTRA_NEEDS_WATER, false);
        int imageid = getIntent().getIntExtra(EXTRA_IMAGE, -1);
        int iconid = getIntent().getIntExtra(EXTRA_ICON, -1);
        String info = getIntent().getStringExtra(EXTRA_INFO);
        String id = getIntent().getStringExtra(EXTRA_ID);
        String graphString = getIntent().getStringExtra(EXTRA_GRAPH_STRING);

        plant = new Plant(id, name, info, watered, humidity, needsWater, graphString);
        plant.setImageID(imageid);
        plant.setIconID(iconid);
    }


    @Override
    protected void onStop() {
        if (dataWasChanged || waterNeededChanged) {
            Log.d("PlantView", "onstop: unchange data: " + plant.getNeedsWater());
            plant.setNeedsWater(!needsWater);
            Log.d("PlantView", "onstop: data must be saved Plant: " + plant.getNeedsWater());

            plantViewModel.update(plant);
            plantViewModel.updateWateredInFirebase(plant.getId(), plant.getNeedsWater());
        }
        super.onStop();
    }

    private void initChart(String graphString) {
        LineChart chart = findViewById(R.id.chart);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(getDataSet(graphString), "Data 1");

        styleDataset(lineDataSet1);
        styleChart(chart);

        dataSets.add(lineDataSet1);


        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.invalidate();
    }


    private void styleDataset(LineDataSet lineDataSet1) {
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleHoleRadius(3);
        lineDataSet1.setColor(getResources().getColor(R.color.dark_yellow));
        lineDataSet1.setCircleColor(getResources().getColor(R.color.dark_yellow));
        lineDataSet1.setDrawValues(false);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawVerticalHighlightIndicator(false);
    }

    private void styleChart(LineChart chart) {
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.light_grey));
        chart.setDrawGridBackground(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);    // Hide the description
        // chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(false);
        //chart.getYAxis().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMinValue(0f);
        chart.getAxisRight().setAxisMinValue(0f);
        chart.getAxisLeft().setAxisMaximum(500);
        chart.getAxisRight().setAxisMaximum(500);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawAxisLine(false);
       // chart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());

        LimitLine limitLine = new LimitLine(200f); // set where the line should be drawn
        limitLine.setLineColor(getResources().getColor(R.color.light_green));
        limitLine.setLineWidth(2f);
        chart.getAxisLeft().addLimitLine(limitLine);
        LimitLine limitLine2 = new LimitLine(100f); // set where the line should be drawn
        limitLine2.setLineColor(getResources().getColor(R.color.red));
        limitLine2.setLineWidth(2f);
        chart.getAxisLeft().addLimitLine(limitLine2);


        chart.getAxisLeft().addLimitLine(limitLine);
    }


    private ArrayList<Entry> getDataSet(String graphString) {
        ArrayList<Entry> dataSets = new ArrayList<>();
        String[] coordinates = graphString.split(";");
        for (String s : coordinates) {
            dataSets.add(new Entry(Integer.parseInt(s.substring(0, s.indexOf("_"))), Integer.parseInt(s.substring(s.indexOf("_") + 1))));
        }


        return dataSets;
    }

   /* public class GraphXAxisValueFormatter implements IAxisValueFormatter {

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