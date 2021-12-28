package com.example.rpicommunicator_v1.ViewAndModels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
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
    private EditText ipText;
    private PlantViewModel plantViewModel;
    private String localIP = null;
    boolean debug=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        initIO();
        initRecyclerView();


    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.plant_view_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PlantAdapter adapter = new PlantAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setViewModel(plantViewModel);
        plantViewModel.getAllPlants().observe(this, adapter::setPlants);

        adapter.setOnItemClickListener(this::openPlantView);

        initSwipeToRefresh();
    }

    private void initSwipeToRefresh() {
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            plantViewModel.reloadFromFirestore();
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
        Plant plant = plantViewModel.getActPlant(position);
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
        ipText = findViewById(R.id.inputIP);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.shutdown_button).setOnClickListener(this);
        findViewById(R.id.time_button).setOnClickListener(this);
        findViewById(R.id.connect_button).setOnClickListener(this);
        findViewById(R.id.button_relais1).setOnClickListener(this);
        findViewById(R.id.button_relais2).setOnClickListener(this);
        findViewById(R.id.button_arduino1).setOnClickListener(this);
        findViewById(R.id.button_arduino2).setOnClickListener(this);
        findViewById(R.id.button_songtitle).setOnClickListener(this);
        findViewById(R.id.button_outlet1).setOnClickListener(this);
        findViewById(R.id.button_outlet2).setOnClickListener(this);
        findViewById(R.id.button_outlet3).setOnClickListener(this);
        //findViewById(R.id.debugSelector).setOnClickListener(this);

        ((Switch)findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch Test", "onCheckedChanged: "+isChecked);
                debug=isChecked;
            }
        });

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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Log.i("buttonClick", "Übernehmen was clicked");
                sendStations();
                break;
            case R.id.shutdown_button:
                Log.i("buttonClick", "shutdown was clicked");
                sendDeactivate();
                break;
            case R.id.time_button:
                Log.i("buttonClick", "time_button was clicked");
                sendTime();
                break;
            case R.id.connect_button:
                Log.i("buttonClick", "Übernehmen was clicked");
                connectToRPI();
                break;
            case R.id.button_relais1:
                Log.i("buttonClick", "Relais 1 was clicked");
                sendText("relais1");
                break;
            case R.id.button_relais2:
                Log.i("buttonClick", "Relais 2 was clicked");
                sendText("relais2");
                break;
            case R.id.button_arduino1:
                Log.i("buttonClick", "Arduino 1 was clicked");
                sendText("arduino1");
                break;
            case R.id.button_arduino2:
                Log.i("buttonClick", "Arduino 2 was clicked");
                sendText("arduino2");
                break;
            case R.id.button_outlet1:
                Log.i("buttonClick", "Outlet 1 was clicked");
                sendText("outlet1");
                break;
            case R.id.button_outlet2:
                Log.i("buttonClick", "Outlet 2 was clicked");
                sendText("outlet2");
                break;
            case R.id.button_outlet3:
                Log.i("buttonClick", "Outlet 3 was clicked");
                sendText("outlet3");
                break;
            case R.id.button_songtitle:
                Log.i("buttonClick", "songtitle was clicked");
                sendText("songtitle");
                break;
            default:
                break;
        }
    }

    private void sendText(String message) {
        if (localIP == null) {
            plantViewModel.sendText(message, debug);
        } else {
            plantViewModel.sendText(message, localIP,debug);
        }
    }


    private void sendTime() {
        sendText("changetime");
    }


    private void connectToRPI() {
        localIP = ipText.getText().toString();
    }

    private void sendStations() {
        sendText("Stations;" + startText.getText().toString() + ";" + destText.getText().toString());
    }

    private void sendDeactivate() {
       /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Soll der Raspberry Pi wirklich heruntergefahren werden?")
                .setTitle("Herunterfahren?");
        builder.setPositiveButton("Ja", (dialog, id) -> plantViewModel.sendText("shutdown"));
        builder.setNegativeButton("Nein", (dialog, id) -> {});
        builder.create();
        builder.show();*/


        sendText("deactivate");


    }
}