package com.example.rpicommunicator_v1.component.general

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.bike.BikeTourActivity
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListActivity
import com.example.rpicommunicator_v1.component.plant.PlantOverview
import com.example.rpicommunicator_v1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var currentMatrixActivated: Int = 0
    private lateinit var binding: ActivityMainBinding
    private var mainActivityViewModel: MainActivityViewModel? = null
    private var gpioButtons = ArrayList<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentMatrixActivated = R.id.imagetime
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        mainActivityViewModel!!.gpioStates.observe(
            this
        ) { res ->
            for (i in 0 until gpioButtons.size) {
                adaptUIGPIO(i, res[i])
            }
        }

        mainActivityViewModel!!.currentMatrixMode.observe(
            this
        ) { res ->
            toggleMatrixUi(res)
        }


        mainActivityViewModel!!.loadStatus()
        initIO()
    }


    private fun initIO() {
        binding.textViewMoreMatrixOptions.setOnClickListener(this)

        binding.imagetrain.setOnClickListener(this)
        binding.imagetime.setOnClickListener(this)
        binding.imagespotify.setOnClickListener(this)
        binding.imageweather.setOnClickListener(this)

        gpioButtons.add(binding.imageoutlet0)
        gpioButtons.add(binding.imageoutlet1)
        gpioButtons.add(binding.imageoutlet2)
        gpioButtons.add(binding.imagearduino1)
        gpioButtons.add(binding.imagearduino2)
        gpioButtons.forEach {
            it.setOnClickListener(this)
        }

        binding.imagebike.setOnClickListener(this)
        binding.imageplant.setOnClickListener(this)
        binding.imagelistactivity.setOnClickListener(this)
        binding.buttonSettingsActivity.setOnClickListener(this)

        binding.imagequit.setOnClickListener(this)
        binding.imagestandby.setOnClickListener(this)
        binding.imagearduino1.setOnClickListener(this)
        binding.imagearduino2.setOnClickListener(this)

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            private var currentProgress = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStopTrackingTouch")
                currentProgress = (currentProgress * 2) + 55
                Toast.makeText(applicationContext, currentProgress.toString(), Toast.LENGTH_LONG)
                    .show()
                mainActivityViewModel!!.setMatrixBrightness(currentProgress)
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
            mainActivityViewModel!!.matrixChangeToMVV(
                binding.inputStart.text.toString(),
                binding.inputDest.text.toString()
            )
        } else if (v.id == R.id.imagespotify) {
            mainActivityViewModel!!.matrixChangeMode(Communication.MatrixState.MATRIX_SPOTIFY)
        } else if (v.id == R.id.imagetime) {
            mainActivityViewModel!!.matrixChangeMode(Communication.MatrixState.MATRIX_TIME)
        } else if (v.id == R.id.imageweather) {
            mainActivityViewModel!!.matrixChangeMode(Communication.MatrixState.MATRIX_WEATHER)
        } else if (v.id == R.id.imagequit) {
            mainActivityViewModel!!.matrixChangeMode(Communication.MatrixState.MATRIX_TERMINATE)
        } else if (v.id == R.id.imagestandby) {
            mainActivityViewModel!!.matrixChangeMode(Communication.MatrixState.MATRIX_STANDBY)
        } else if (v.id == R.id.imagearduino1) {
            mainActivityViewModel!!.gpioButtonClicked(Communication.GPIOInstances.GPIO_ARDUINO_1)
        } else if (v.id == R.id.imagearduino2) {
            mainActivityViewModel!!.gpioButtonClicked(Communication.GPIOInstances.GPIO_ARDUINO_2)
        } else if (v.id == R.id.imageoutlet0) {
            mainActivityViewModel!!.gpioButtonClicked(Communication.GPIOInstances.GPIO_OUTLET_1)
        } else if (v.id == R.id.imageoutlet1) {
            mainActivityViewModel!!.gpioButtonClicked(Communication.GPIOInstances.GPIO_OUTLET_2)
        } else if (v.id == R.id.imageoutlet2) {
            mainActivityViewModel!!.gpioButtonClicked(Communication.GPIOInstances.GPIO_OUTLET_3)
        } else if (v.id == R.id.imagebike) {
            Log.i("buttonClick", "bike activity was clicked")
            changeToBike()
        } else if (v.id == R.id.imageplant) {
            Log.i("buttonClick", "PlantOverview activity was clicked")
            changeToPlantOverview()
        } else if (v.id == R.id.imagelistactivity) {
            Log.i("buttonClick", "List activity was clicked was clicked.")
            changeToCompareList()
        } else if (v.id == R.id.button_settingsActivity) {
            Log.i("buttonClick", "Setting activity was clicked")
            changeToSettings()
        } else if (v.id == R.id.textViewMoreMatrixOptions) {
            Log.i("buttonClick", "view more options activity was clicked")
            if (binding.layoutmatrixmoreoptions.visibility == View.GONE) {
                binding.layoutmatrixmoreoptions.visibility = View.VISIBLE
                binding.textViewMoreMatrixOptions.text = "Less Options"
            } else {
                binding.layoutmatrixmoreoptions.visibility = View.GONE
                binding.textViewMoreMatrixOptions.text = "More Options"
            }
        }
    }

    private fun adaptUIGPIO(outletId: Int, state: Boolean) {
        if (state) {
            gpioButtons[outletId].background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.primary_green_transparent
                )
            )
        } else {
            gpioButtons[outletId].background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.transparent
                )
            )
        }
    }

    private fun toggleMatrixUi(matrixId: Communication.MatrixState) {
        val id = getResIdForMatrixState(matrixId)
        switchOffCurrent()
        findViewById<View>(id).background.setTint(
            ContextCompat.getColor(
                this,
                R.color.primary_green_transparent
            )
        )
        currentMatrixActivated = id
    }

    private fun getResIdForMatrixState(matrixState: Communication.MatrixState): Int {
        if (matrixState == Communication.MatrixState.MATRIX_MVV) {
            return R.id.imagetrain
        } else if (matrixState == Communication.MatrixState.MATRIX_STANDBY) {
            return R.id.imagestandby
        } else if (matrixState == Communication.MatrixState.MATRIX_SPOTIFY) {
            return R.id.imagespotify
        } else if (matrixState == Communication.MatrixState.MATRIX_WEATHER) {
            return R.id.imageweather
        } else if (matrixState == Communication.MatrixState.MATRIX_TERMINATE) {
            return R.id.imagequit
        }
        return R.id.imagetime
    }

    private fun switchOffCurrent() {
        if (currentMatrixActivated != 0) {
            findViewById<View>(currentMatrixActivated).background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.transparent
                )
            )
            currentMatrixActivated = 0
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