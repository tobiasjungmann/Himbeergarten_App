package com.example.rpicommunicator_v1.component.general

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.bike.BikeTourActivity
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListHolder
import com.example.rpicommunicator_v1.component.plant.PlantHolderActivity
import com.example.rpicommunicator_v1.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var currentMatrixActivated: Int = R.integer.INVALID_LAYOUT_ID
    private lateinit var binding: ActivityMainBinding
    private var mainActivityViewModel: MainActivityViewModel? = null
    private var gpioButtons = ArrayList<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        initLiveDataObservers()
        mainActivityViewModel!!.loadStatus()
        initIO()
    }

    private fun initLiveDataObservers() {
        mainActivityViewModel!!.gpioStates.observe(this) { res ->
            for (i in 0 until gpioButtons.size) {
                adaptUIGPIO(i, res[i])
            }
        }

        mainActivityViewModel!!.currentMatrixMode.observe(this) { res ->
            toggleMatrixUi(res)
        }

        mainActivityViewModel!!.serverAvailable.observe(this) { res ->
            if (res) {
                binding.textViewConnectionStatus.visibility = SeekBar.GONE
            } else {
                binding.textViewConnectionStatus.visibility = SeekBar.VISIBLE
            }
        }
    }


    private fun initIO() {
        initGpioUi()
        initSeekbarBrightness()
        binding.textViewMoreMatrixOptions.setOnClickListener(this)

        binding.imagetrain.setOnClickListener(this)
        binding.imagetime.setOnClickListener(this)
        binding.imagespotify.setOnClickListener(this)
        binding.imageweather.setOnClickListener(this)

        binding.imagebike.setOnClickListener(this)
        binding.imageplant.setOnClickListener(this)
        binding.imagelistactivity.setOnClickListener(this)
        binding.buttonSettingsActivity.setOnClickListener(this)

        binding.imagequit.setOnClickListener(this)
        binding.imagestandby.setOnClickListener(this)
        binding.imagearduino1.setOnClickListener(this)
        binding.imagearduino2.setOnClickListener(this)
    }

    private fun initSeekbarBrightness() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var currentProgress = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                currentProgress = (currentProgress * 2) + 55
                mainActivityViewModel!!.setMatrixBrightness(currentProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentProgress = progress
            }
        })
    }

    private fun initGpioUi() {
        gpioButtons.add(binding.imageoutlet0)
        gpioButtons.add(binding.imageoutlet1)
        gpioButtons.add(binding.imageoutlet2)
        gpioButtons.add(binding.imagearduino1)
        gpioButtons.add(binding.imagearduino2)
        gpioButtons.forEach {
            it.setOnClickListener(this)
        }
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
            changeToBike()
        } else if (v.id == R.id.imageplant) {
            changeToPlantOverview()
        } else if (v.id == R.id.imagelistactivity) {
            changeToCompareList()
        } else if (v.id == R.id.button_settingsActivity) {
            changeToSettings()
        } else if (v.id == R.id.textViewMoreMatrixOptions) {
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
        if (id != R.integer.INVALID_LAYOUT_ID) {
            findViewById<View>(id).background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.primary_green_transparent
                )
            )
        }
        currentMatrixActivated = id
    }

    private fun getResIdForMatrixState(matrixState: Communication.MatrixState): Int {
        when (matrixState) {
            Communication.MatrixState.MATRIX_MVV -> {
                return R.id.imagetrain
            }
            Communication.MatrixState.MATRIX_STANDBY -> {
                return R.id.imagestandby
            }
            Communication.MatrixState.MATRIX_SPOTIFY -> {
                return R.id.imagespotify
            }
            Communication.MatrixState.MATRIX_WEATHER -> {
                return R.id.imageweather
            }
            Communication.MatrixState.MATRIX_TERMINATE -> {
                return R.id.imagequit
            }
            Communication.MatrixState.MATRIX_TIME -> {
                return R.id.imagetime
            }
            else -> return R.integer.INVALID_LAYOUT_ID
        }
    }

    @SuppressLint("ResourceType")   // will either be an INVALID_LAYOUT_ID or a valid id
    private fun switchOffCurrent() {
        if (currentMatrixActivated != R.integer.INVALID_LAYOUT_ID) {
            findViewById<View>(currentMatrixActivated).background.setTint(
                ContextCompat.getColor(
                    this,
                    R.color.transparent
                )
            )
            currentMatrixActivated = R.integer.INVALID_LAYOUT_ID
        }
    }

    private fun changeToCompareList() {
        val intent = Intent(applicationContext, ComparingListHolder::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }


    private fun changeToBike() {
       val intent = Intent(applicationContext, BikeTourActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    private fun changeToSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up)
    }

    private fun changeToPlantOverview() {
        val intent = Intent(applicationContext, PlantHolderActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    override fun onBackPressed() {
        if (mainActivityViewModel!!.backPressed()) {
            exitProcess(0)
        }
        Toast.makeText(
            this,
            "Um die App zu schließen Taste erneut drücken.",
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ mainActivityViewModel?.resetBackPressed() }, 2000)
    }
}