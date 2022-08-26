package com.example.rpicommunicator_v1.component.general

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.CommunicationInterface
import com.example.rpicommunicator_v1.component.bike.BikeTourActivity
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListActivity
import com.example.rpicommunicator_v1.component.plant.PlantOverview
import com.example.rpicommunicator_v1.databinding.ActivityMainBinding
import io.grpc.ManagedChannelBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var currentOn: Int = 0
    private lateinit var binding: ActivityMainBinding
    private var mainActivityViewModel: MainActivityViewModel? = null
    var communicationInterface: CommunicationInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        communicationInterface = ViewModelProvider(this)[CommunicationInterface::class.java]
        initIO()

        var mChannel =
            ManagedChannelBuilder.forAddress("127.0.0.1", 8000).usePlaintext().build();
        /*Communication
        blockingStub = RouteGuideGrpc.newBlockingStub(mChannel);
        asyncStub = RouteGuideGrpc.newStub(mChannel);*/

    }

    private fun initIO() {
        binding.textViewMorematrixOptions.setOnClickListener(this)

        binding.imagetrain.setOnClickListener(this)
        binding.imagetime.setOnClickListener(this)
        binding.imagespotify.setOnClickListener(this)
        binding.imageweather.setOnClickListener(this)

        binding.imageoutlet1.setOnClickListener(this)
        binding.imageoutlet2.setOnClickListener(this)
        binding.imageoutlet3.setOnClickListener(this)

        binding.imagebike.setOnClickListener(this)
        binding.imageplant.setOnClickListener(this)
        binding.imagelistactivity.setOnClickListener(this)
        binding.buttonSettingsActivity.setOnClickListener(this)


        binding.imagequit.setOnClickListener(this)
        binding.imagestandby.setOnClickListener(this)
        binding.imagearduino1.setOnClickListener(this)
        binding.imagearduino2.setOnClickListener(this)

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
            toggleMatrixUi(R.id.imagetrain)
            communicationInterface!!.sendText("Stations;" + binding.inputStart.text.toString() + ";" + binding.inputDest.text.toString())
        } else if (v.id == R.id.imagespotify) {
            Log.i("buttonClick", "songtitle was clicked")
            toggleMatrixUi(R.id.imagespotify)
            communicationInterface!!.sendText("songtitle")
        } else if (v.id == R.id.imagetime) {
            Log.i("buttonClick", "time_button was clicked")
            toggleMatrixUi(R.id.imagetime)
            communicationInterface!!.sendText("changetime")
        } else if (v.id == R.id.imageweather) {
            Log.i("buttonClick", "weather button was clicked")
            toggleMatrixUi(R.id.imageweather)
            communicationInterface!!.sendText("weather")
        } else if (v.id == R.id.imagearduino1) {
            Log.i("buttonClick", "Arduino 1 was clicked")
            communicationInterface!!.sendText("arduino1")
        } else if (v.id == R.id.imagearduino2) {
            Log.i("buttonClick", "Arduino 2 was clicked")
            communicationInterface!!.sendText("arduino2")
        } else if (v.id == R.id.imageoutlet1) {
            Log.i("buttonClick", "Outlet 1 was clicked")
            communicationInterface!!.sendText("outlet1")
            if (mainActivityViewModel!!.toggleOutlet1()) {
                binding.imageoutlet1.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_green_transparent
                    )
                )
            } else {
                binding.imageoutlet1.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.transparent
                    )
                )
            }
        } else if (v.id == R.id.imageoutlet2) {
            Log.i("buttonClick", "Outlet 2 was clicked")
            communicationInterface!!.sendText("outlet2")
            if (mainActivityViewModel!!.toggleOutlet2()) {
                binding.imageoutlet2.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_green_transparent
                    )
                )
            } else {
                binding.imageoutlet2.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.transparent
                    )
                )
            }
        } else if (v.id == R.id.imageoutlet3) {
            Log.i("buttonClick", "Outlet 3 was clicked")
            communicationInterface!!.sendText("outlet3")

            if (mainActivityViewModel!!.toggleOutlet3()) {
                binding.imageoutlet3.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_green_transparent
                    )
                )
            } else {
                binding.imageoutlet3.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.transparent
                    )
                )
            }

        } else if (v.id == R.id.imagebike) {
            Log.i("buttonClick", "bike activity was clicked")
            changeToBike()
        } else if (v.id == R.id.imageplant) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            changeToPlantOverview()
        } else if (v.id == R.id.imagelistactivity) {
            Log.i("buttonClick", "List activity was clicked was clicked. Not yet implemented")
            changeToCompareList()
        } else if (v.id == R.id.button_settingsActivity) {
            Log.i("buttonClick", "Setting activity was clicked")
            changeToSettings()
        } else if (v.id == R.id.imagestandby) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            communicationInterface!!.sendText("standby")
            toggleMatrixUi(R.id.imagestandby)
        } else if (v.id == R.id.imagequit) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            communicationInterface!!.sendText("quit")

        } else if (v.id == R.id.textViewMorematrixOptions) {
            Log.i("buttonClick", "view more options activity was clicked")
            if (binding.layoutmatrixmoreoptions.visibility == View.GONE) {
                binding.layoutmatrixmoreoptions.visibility = View.VISIBLE
                binding.textViewMorematrixOptions.text = "Less Options"
            } else {
                binding.layoutmatrixmoreoptions.visibility = View.GONE
                binding.textViewMorematrixOptions.text = "More Options"
            }
        }
    }

    private fun toggleMatrixUi(imageID: Int) {
        if (imageID == R.id.imagestandby) {
            switchOffCurrent()

        } else {
            switchOffCurrent()

            findViewById<View>(imageID).background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.primary_green_transparent
                )
            )
            currentOn = imageID
        }
    }

    private fun switchOffCurrent() {
        if (currentOn != 0) {
            findViewById<View>(currentOn).background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.transparent
                )
            )
            currentOn = 0
        }
    }

    private fun changeToCompareList() {
        val intent = Intent(applicationContext, ComparingListActivity::class.java)
        startActivity(intent)
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