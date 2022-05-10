package com.example.rpicommunicator_v1.ViewAndModels

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var destText: EditText? = null
    private var startText: EditText? = null
    var mainActivityViewModel: MainActivityViewModel? = null
    var communicationInterface: CommunicationInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(
            MainActivityViewModel::class.java
        )
        communicationInterface = ViewModelProvider(this).get(
            CommunicationInterface::class.java
        )
        initIO()
    }

    private fun initIO() {
        destText = findViewById(R.id.inputDest)
        startText = findViewById(R.id.inputStart)
        findViewById<View>(R.id.imagetrain).setOnClickListener(this)
        findViewById<View>(R.id.imagetime).setOnClickListener(this)
        findViewById<View>(R.id.imagespotify).setOnClickListener(this)
        findViewById<View>(R.id.button_weather).setOnClickListener(this)
        findViewById<View>(R.id.button_relais1).setOnClickListener(this)
        findViewById<View>(R.id.button_relais2).setOnClickListener(this)
        findViewById<View>(R.id.button_arduino1).setOnClickListener(this)
        findViewById<View>(R.id.button_arduino2).setOnClickListener(this)

        findViewById<View>(R.id.imageoutlet1).setOnClickListener(this)
        findViewById<View>(R.id.imageoutlet2).setOnClickListener(this)
        findViewById<View>(R.id.imageoutlet3).setOnClickListener(this)
        findViewById<View>(R.id.button_bikeActivity).setOnClickListener(this)
        findViewById<View>(R.id.button_settingsActivity).setOnClickListener(this)
        findViewById<View>(R.id.button_plantoverviewActivity).setOnClickListener(this)
        findViewById<View>(R.id.textViewSources).setOnClickListener(this)
        findViewById<View>(R.id.button_quit).setOnClickListener(this)
        findViewById<View>(R.id.button_standby).setOnClickListener(this)

        val sk = findViewById<SeekBar>(R.id.seekBar)
        sk.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            private var currentProgress = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStopTrackingTouch")
                currentProgress = (currentProgress * 2) + 55
                Toast.makeText(applicationContext, currentProgress.toString(), Toast.LENGTH_LONG)
                    .show()
                communicationInterface!!.sendText("newBrightness:$currentProgress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStartTrackingTouch")
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("seekbar", "onProgresschanged")
                currentProgress = progress
            }
        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.imagetrain) {
            Log.i("buttonClick", "Übernehmen was clicked")
            communicationInterface!!.sendText("Stations;" + startText!!.text.toString() + ";" + destText!!.text.toString())
        } else if (v.id == R.id.imagespotify) {
            Log.i("buttonClick", "songtitle was clicked")
            communicationInterface!!.sendText("songtitle")
        } else if (v.id == R.id.imagetime) {
            Log.i("buttonClick", "time_button was clicked")
            communicationInterface!!.sendText("changetime")
        } else if (v.id == R.id.button_weather) {
            Log.i("buttonClick", "weather button was clicked")
            communicationInterface!!.sendText("weather")

        } else if (v.id == R.id.button_relais1) {
            Log.i("buttonClick", "Relais 1 was clicked")
            communicationInterface!!.sendText("relais1")
        } else if (v.id == R.id.button_relais2) {
            Log.i("buttonClick", "Relais 2 was clicked")
            communicationInterface!!.sendText("relais2")
        } else if (v.id == R.id.button_arduino1) {
            Log.i("buttonClick", "Arduino 1 was clicked")
            communicationInterface!!.sendText("arduino1")
        } else if (v.id == R.id.button_arduino2) {
            Log.i("buttonClick", "Arduino 2 was clicked")
            communicationInterface!!.sendText("arduino2")
        } else if (v.id == R.id.imageoutlet1) {
            Log.i("buttonClick", "Outlet 1 was clicked")
            communicationInterface!!.sendText("outlet1")
            if(mainActivityViewModel!!.toggleOutlet1()){
                findViewById<View>(R.id.imageoutlet1).setBackgroundTintList(getResources().getColorStateList(R.color.light_yellow_palette));
            }else{
                findViewById<View>(R.id.imageoutlet1).setBackgroundTintList(getResources().getColorStateList(R.color.beige_palette));
            }
        } else if (v.id == R.id.imageoutlet2) {
            Log.i("buttonClick", "Outlet 2 was clicked")
            communicationInterface!!.sendText("outlet2")
            if(mainActivityViewModel!!.toggleOutlet2()){
                findViewById<View>(R.id.imageoutlet2).setBackgroundTintList(getResources().getColorStateList(R.color.light_yellow_palette));
            }else{
                findViewById<View>(R.id.imageoutlet2).setBackgroundTintList(getResources().getColorStateList(R.color.beige_palette));
            }
        } else if (v.id == R.id.imageoutlet3) {
            Log.i("buttonClick", "Outlet 3 was clicked")
            communicationInterface!!.sendText("outlet3")

            if(mainActivityViewModel!!.toggleOutlet3()){
                findViewById<View>(R.id.imageoutlet3).setBackgroundTintList(getResources().getColorStateList(R.color.light_yellow_palette));
            }else{
                findViewById<View>(R.id.imageoutlet3).setBackgroundTintList(getResources().getColorStateList(R.color.beige_palette));
            }

        } else if (v.id == R.id.button_bikeActivity) {
            Log.i("buttonClick", "bike activity was clicked")
            changeToBike()
        } else if (v.id == R.id.button_settingsActivity) {
            Log.i("buttonClick", "Setting activity was clicked")
            changeToSettings()
        } else if (v.id == R.id.button_plantoverviewActivity) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            changeToPlantOverview()
        } else if (v.id == R.id.button_standby) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            communicationInterface!!.sendText("standby")
        } else if (v.id == R.id.button_quit) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            communicationInterface!!.sendText("quit")
        } else if (v.id == R.id.textViewSources) {
            Log.i("buttonClick", "OpenSources activity was clicked")
            openSourcesDialog()
        }
    }

    private fun openSourcesDialog() {
        val builder = AlertDialog.Builder(this)
        val titleView: TextView = TextView(this)
        titleView.setPadding(36, 48, 36, 8)
        titleView.setTextSize(16F)
        titleView.setTextColor(Color.WHITE)
        titleView.autoLinkMask
        titleView.text =
            "Pump icons created by Smashicons - Flaticon: https://www.flaticon.com/free-icons/pump\n\nPlant icons created by Freepik - Flaticon: https://www.flaticon.com/free-icons/plant\n\nOutlet icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/outlet\n\nWater drop icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/water-drop\""
        builder.setCustomTitle(titleView)
        builder.setCancelable(true)
        builder.setPositiveButton("OK", null);
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun changeToBike() {
        val intent = Intent(applicationContext, BikeTourActivity::class.java)
        startActivity(intent)
    }

    private fun changeToSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun changeToPlantOverview() {
        val intent = Intent(applicationContext, PlantOverview::class.java)
        startActivity(intent)
    }
}