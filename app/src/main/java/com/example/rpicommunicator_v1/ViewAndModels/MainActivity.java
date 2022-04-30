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

import com.example.rpicommunicator_v1.Plants.PlantAdapter;
import com.example.rpicommunicator_v1.R;

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
    }


    private void initIO() {
        destText = findViewById(R.id.inputDest);
        startText = findViewById(R.id.inputStart);

        findViewById(R.id.button).setOnClickListener(this);

        findViewById(R.id.time_button).setOnClickListener(this);
        findViewById(R.id.button_relais1).setOnClickListener(this);
        findViewById(R.id.button_relais2).setOnClickListener(this);
        findViewById(R.id.button_arduino1).setOnClickListener(this);
        findViewById(R.id.button_arduino2).setOnClickListener(this);
        findViewById(R.id.button_songtitle).setOnClickListener(this);
        findViewById(R.id.button_outlet1).setOnClickListener(this);
        findViewById(R.id.button_outlet2).setOnClickListener(this);
        findViewById(R.id.button_outlet3).setOnClickListener(this);
        findViewById(R.id.button_bikeActivity).setOnClickListener(this);
        findViewById(R.id.button_settingsActivity).setOnClickListener(this);
        findViewById(R.id.button_plantoverviewActivity).setOnClickListener(this);

        final SeekBar sk = findViewById(R.id.seekBar);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int currentProgress = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("seekbar", "onStopTrackingTouch");
                currentProgress = ((int) (currentProgress * 2)) + 55;
                Toast.makeText(getApplicationContext(), String.valueOf(currentProgress), Toast.LENGTH_LONG).show();
                communicationInterface.sendText("newBrightness:" + currentProgress);
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
            communicationInterface.sendText("Stations;" + startText.getText().toString() + ";" + destText.getText().toString());
        } else if (v.getId() == R.id.time_button) {
            Log.i("buttonClick", "time_button was clicked");
            communicationInterface.sendText("changetime");
        } else if (v.getId() == R.id.button_relais1) {
            Log.i("buttonClick", "Relais 1 was clicked");
            communicationInterface.sendText("relais1");
        } else if (v.getId() == R.id.button_relais2) {
            Log.i("buttonClick", "Relais 2 was clicked");
            communicationInterface.sendText("relais2");
        } else if (v.getId() == R.id.button_arduino1) {
            Log.i("buttonClick", "Arduino 1 was clicked");
            communicationInterface.sendText("arduino1");
        } else if (v.getId() == R.id.button_arduino2) {
            Log.i("buttonClick", "Arduino 2 was clicked");
            communicationInterface.sendText("arduino2");
        } else if (v.getId() == R.id.button_outlet1) {
            Log.i("buttonClick", "Outlet 1 was clicked");
            communicationInterface.sendText("outlet1");
        } else if (v.getId() == R.id.button_outlet2) {
            Log.i("buttonClick", "Outlet 2 was clicked");
            communicationInterface.sendText("outlet2");
        } else if (v.getId() == R.id.button_outlet3) {
            Log.i("buttonClick", "Outlet 3 was clicked");
            communicationInterface.sendText("outlet3");
        } else if (v.getId() == R.id.button_songtitle) {
            Log.i("buttonClick", "songtitle was clicked");
            communicationInterface.sendText("songtitle");
        } else if (v.getId() == R.id.button_bikeActivity) {
            Log.i("buttonClick", "bike activity was clicked");
            changeToBike();
        } else if (v.getId() == R.id.button_settingsActivity) {
            Log.i("buttonClick", "Setting activity was clicked");
            changeToSettings();

        } else if (v.getId() == R.id.button_plantoverviewActivity) {
            Log.i("buttonClick", "PlantOverview activity was clicked");
            changeToPlantOverview();
        }
    }

    private void changeToBike() {
        Intent intent = new Intent(getApplicationContext(), BikeTourActivity.class);
        startActivity(intent);
    }

    private void changeToSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void changeToPlantOverview() {
        Intent intent = new Intent(getApplicationContext(), PlantOverview.class);
        startActivity(intent);
    }
}