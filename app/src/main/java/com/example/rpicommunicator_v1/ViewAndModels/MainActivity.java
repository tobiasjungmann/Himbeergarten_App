package com.example.rpicommunicator_v1.ViewAndModels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.Plants.PlantAdapter;
import com.example.rpicommunicator_v1.R;

import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_GRAPH_STRING;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_HUMIDITY;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ICON;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_IMAGE;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_INFO;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NAME;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_NEEDS_WATER;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_WATERED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText destText;
    private EditText startText;
    private MainActivityViewModel mainActivityViewModel;
    private CommunicationInterface communicationInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        communicationInterface = new ViewModelProvider(this).get(CommunicationInterface.class);

        initIO();
        initRecyclerView();
    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.plant_view_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PlantAdapter adapter = new PlantAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setViewModel(mainActivityViewModel);
        mainActivityViewModel.getAllPlants().observe(this, adapter::setPlants);

        adapter.setOnItemClickListener(this::openPlantView);

        initSwipeToRefresh();
    }

    private void initSwipeToRefresh() {
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            mainActivityViewModel.reloadFromFirestore();
            swipeContainer.setRefreshing(false);
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void openPlantView(int position) {
        Intent intent = new Intent(getApplicationContext(), PlantView.class);
        Plant plant = mainActivityViewModel.getActPlant(position);
        intent.putExtra(EXTRA_NAME, plant.getName());
        intent.putExtra(EXTRA_HUMIDITY, plant.getHumidity());
        intent.putExtra(EXTRA_WATERED, plant.getWatered());
        intent.putExtra(EXTRA_NEEDS_WATER, plant.getNeedsWater());
        intent.putExtra(EXTRA_IMAGE, plant.getImageID());
        intent.putExtra(EXTRA_ICON, plant.getIconID());
        intent.putExtra(EXTRA_INFO, plant.getInfo());
        intent.putExtra(EXTRA_ID, plant.getId());
        intent.putExtra(EXTRA_GRAPH_STRING, plant.getGraphString());
        startActivity(intent);
    }

    private void initIO() {
        destText = findViewById(R.id.inputDest);
        startText = findViewById(R.id.inputStart);

        findViewById(R.id.button).setOnClickListener(this);

        findViewById(R.id.button_relais1).setOnClickListener(this);
        findViewById(R.id.button_relais2).setOnClickListener(this);
        findViewById(R.id.button_arduino1).setOnClickListener(this);
        findViewById(R.id.button_arduino2).setOnClickListener(this);
        findViewById(R.id.button_songtitle).setOnClickListener(this);
        findViewById(R.id.button_outlet1).setOnClickListener(this);
        findViewById(R.id.button_outlet2).setOnClickListener(this);
        findViewById(R.id.button_outlet3).setOnClickListener(this);
        findViewById(R.id.bikeActivity).setOnClickListener(this);
        //findViewById(R.id.debugSelector).setOnClickListener(this);


        final SeekBar sk = findViewById(R.id.seekBar);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int currentProgress = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("seekbar", "onStopTrackingTouch");
                currentProgress = ((int) (currentProgress * 2)) + 55;
                Toast.makeText(getApplicationContext(), String.valueOf(currentProgress), Toast.LENGTH_LONG).show();
                sendText("newBrightness:" + currentProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("seekbar", "onStartTrackingTouch");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("seekbar", "onProgresschanged");
                currentProgress = progress;


            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Log.i("buttonClick", "Übernehmen was clicked");
            sendStations();
        } else if (v.getId() == R.id.time_button) {
            Log.i("buttonClick", "time_button was clicked");
            sendTime();
        } else if (v.getId() == R.id.button_relais1) {
            Log.i("buttonClick", "Relais 1 was clicked");
            sendText("relais1");
        } else if (v.getId() == R.id.button_relais2) {
            Log.i("buttonClick", "Relais 2 was clicked");
            sendText("relais2");
        } else if (v.getId() == R.id.button_arduino1) {
            Log.i("buttonClick", "Arduino 1 was clicked");
            sendText("arduino1");
        } else if (v.getId() == R.id.button_arduino2) {
            Log.i("buttonClick", "Arduino 2 was clicked");
            sendText("arduino2");
        } else if (v.getId() == R.id.button_outlet1) {
            Log.i("buttonClick", "Outlet 1 was clicked");
            sendText("outlet1");
        } else if (v.getId() == R.id.button_outlet2) {
            Log.i("buttonClick", "Outlet 2 was clicked");
            sendText("outlet2");
        } else if (v.getId() == R.id.button_outlet3) {
            Log.i("buttonClick", "Outlet 3 was clicked");
            sendText("outlet3");
        } else if (v.getId() == R.id.button_songtitle) {
            Log.i("buttonClick", "songtitle was clicked");
            sendText("songtitle");
        } else if (v.getId() == R.id.bikeActivity) {
            Log.i("buttonClick", "bike activity was clicked");
            changeToBike();
        }
    }


    private void sendText(String message) {
        communicationInterface.sendText(message);
    }


    private void sendTime() {
        sendText("changetime");
    }


    private void sendStations() {
        sendText("Stations;" + startText.getText().toString() + ";" + destText.getText().toString());
    }


    private void changeToBike() {
        Intent intent = new Intent(getApplicationContext(), BikeTourActivity.class);
        startActivity(intent);
    }
}