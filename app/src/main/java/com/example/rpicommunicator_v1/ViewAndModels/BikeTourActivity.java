package com.example.rpicommunicator_v1.ViewAndModels;

import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_GRAPH_STRING;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_HUMIDITY;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ICON;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_IMAGE;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_INFO;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NAME;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NEEDS_WATER;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_WATERED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rpicommunicator_v1.Database.BikeTour;
import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BikeTourActivity extends AppCompatActivity {


    private BikeTourViewModel bikeViewModel;
  //  private LineDataSet lineDataSet1;
    private List<BikeTour> bikeToursList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        bikeViewModel = new ViewModelProvider(this).get(BikeTourViewModel.class);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String time = formatter.format(new Date());


        initUIElements();
        bikeViewModel.getAllBikeTours().observe(this, new Observer<List<BikeTour>>() {


            @Override
            public void onChanged(List<BikeTour> bikeTours) {
                if (!bikeTours.isEmpty()) {
                    findViewById(R.id.direction_card_viewDiagram).setVisibility(View.VISIBLE);
                    findViewById(R.id.direction_card_viewStatistics).setVisibility(View.VISIBLE);
                    bikeToursList=bikeTours;
                    initChart(bikeTours);
                    initStatistics();
                } else {
                    findViewById(R.id.direction_card_viewDiagram).setVisibility(View.GONE);
                    findViewById(R.id.direction_card_viewStatistics).setVisibility(View.GONE);

                }
            }
        });
    }

    private void initStatistics() {
        //int sum = data.values().stream().reduce(0, Integer::sum);
        int sum=0;
        for (BikeTour b: bikeToursList) {
            sum+=b.getKm();
        }
        ((TextView)findViewById(R.id.sumOfAllToursTextView)).setText(""+sum+" km von 2500km");
    }

    private void initUIElements() {
        findViewById(R.id.add_button).setOnClickListener(v -> {
            String from = ((EditText) findViewById(R.id.inputBikeFrom)).getText().toString();
            String to = ((EditText) findViewById(R.id.inputBikesTo)).getText().toString();
            double km = Double.parseDouble(((EditText) findViewById(R.id.editTextNumberDecimal)).getText().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String time = formatter.format(new Date());
            Log.d("TAG", "initUIElements: "+time);
            bikeViewModel.insert(new BikeTour(from, to, km, time));
            ((EditText) findViewById(R.id.inputBikeFrom)).setText("Unity Beta");
            ((EditText) findViewById(R.id.inputBikesTo)).setText("Zuhause");
            ((EditText) findViewById(R.id.editTextNumberDecimal)).setText("20");

        });


        findViewById(R.id.delete_button).setOnClickListener(v -> {
           bikeViewModel.remove(bikeToursList.get(bikeToursList.size()-1));
          /*  bikeViewModel.getAllBikeTours().observe(this, bikeTours -> {
                if (!bikeTours.isEmpty()) {
                    bikeViewModel.remove(bikeTours.get(bikeTours.size() - 1));
                }
               // bikeViewModel.getAllBikeTours().removeObserver(this);
            });*/
        });
    }


    private void initChart(List<BikeTour> bikeTours) {
        LineChart chart = findViewById(R.id.chart);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(getDataSet(bikeTours), "Data 1");

        styleDataset(lineDataSet1);
        styleChart(chart);

        dataSets.add(lineDataSet1);


        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.invalidate();
    }


    private void styleDataset(LineDataSet lineDataSet1) {
        lineDataSet1.setLineWidth(1);
        lineDataSet1.setCircleRadius(2);
        lineDataSet1.setCircleHoleRadius(1);
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

        chart.getAxisLeft().setAxisMaximum(2500);
        chart.getAxisRight().setAxisMaximum(2500);
        chart.getXAxis().setAxisMaximum(366);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawAxisLine(false);
        // chart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());


    }


    private ArrayList<Entry> getDataSet(List<BikeTour> graphlist) {
        ArrayList<Entry> dataSets = new ArrayList<>();
      /*graphString  String[] coordinates = graphString.split(";");
        for (String s : coordinates) {
            dataSets.add(new Entry(Integer.parseInt(s.substring(0, s.indexOf("_"))), Integer.parseInt(s.substring(s.indexOf("_") + 1))));
        }*/
        //graphlist.forEach(b->dataSets.add(new Entry(b.getKm())));
        int combined = 0;
        for (int i = 0; i < graphlist.size(); i++) {
            combined += graphlist.get(i).getKm();
            dataSets.add(new Entry(i, combined));
        }


        return dataSets;
    }
}