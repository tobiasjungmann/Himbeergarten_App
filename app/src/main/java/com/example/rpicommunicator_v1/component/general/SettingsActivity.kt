package com.example.rpicommunicator_v1.component.general

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_IP
import com.example.rpicommunicator_v1.databinding.ActivitySettingsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException


class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mPref: SharedPreferences
    private val allSensors: MutableLiveData<List<String>> = MutableLiveData()
    private val selectedSensors = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSaveSettings.setOnClickListener(this)
        binding.textViewSources.setOnClickListener(this)

        mPref = this.application.getSharedPreferences(
            this.application.resources.getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )

        initAddressUi()
        initDeviceSelection()
        setupChip()
    }

    private fun setupChip() {
        val url = "http://192.168.0.6:8123/api/states"
        val token =

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("tag", "request not successfull")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("body", responseBody!!)
                val entityIds = mutableListOf<String>()
                val jsonResponseArray = JSONArray(responseBody)
                for (i in 0 until jsonResponseArray.length()) {
                    val jsonObject = jsonResponseArray.getJSONObject(i)
                    if (jsonObject.has("entity_id")) {
                        val s = jsonObject.getString("entity_id")
                        if (s.startsWith("sensor."))
                            entityIds.add(s)
                    }
                }
                allSensors.postValue(entityIds)

            }
        })

        allSensors.observe(this) { ratingTagsResults ->
            for (tag in ratingTagsResults) {
                val chip = createChip(tag)
                binding.chipGroup.addView(chip)
            }
        }
    }

    private fun loadColor(input: Int): ColorStateList {
        return ColorStateList.valueOf(
            ContextCompat.getColor(
                this,
                input
            )
        )
    }

    private fun createChip(title: String): Chip {
        val chip = Chip(this)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this,
            null,
            0, R.style.Widget_Material3_Chip_Filter
        )

        chip.setChipDrawable(chipDrawable)
        chip.chipStrokeColor = loadColor(R.color.primary_green)
        chip.setTextColor(loadColor(R.color.text_color))
        chip.chipBackgroundColor = loadColor(R.color.cardview_light_background)

        chip.text = title

        chip.setOnClickListener {
            if (chip.isChecked) {
                chip.chipBackgroundColor = loadColor(R.color.primary_green)
                selectedSensors.add(chip.text.toString())
            } else {
                chip.chipBackgroundColor = loadColor(R.color.cardview_light_background)
                selectedSensors.removeAll { it == chip.text.toString() }
            }
        }
        return chip
    }

    private fun initAddressUi() {
        binding.editTextServerAddress.setText(
            mPref.getString(
                this.application.resources.getString(R.string.ADDRESS_SERVER_PREF),
                DEFAULT_SERVER_IP
            )
        )
        binding.editTextStationAddress.setText(
            mPref.getString(
                this.application.resources.getString(R.string.ADDRESS_STATION_PREF),
                DEFAULT_SERVER_IP
            )
        )

        binding.editTextServerPort.setText(
            String.format(
                "%d",
                mPref.getInt(
                    this.application.resources.getString(R.string.PORT_SERVER_PREF),
                    R.integer.DEFAULT_PORT_STATION
                )
            )
        )

        binding.editTextStationPort.setText(
            String.format(
                "%d",
                mPref.getInt(
                    this.application.resources.getString(R.string.PORT_STATION_PREF),
                    R.integer.DEFAULT_PORT_SERVER
                )
            )
        )
    }

    private fun initDeviceSelection() {
        // todo load from repository
        val deviceTypes = arrayOf("Nicht ausgewählt", "Arduino Nano", "Raspberry Pi")
        val deviceInterfaces = arrayOf("RPi 1 - usb0", "RPi 1 - usb1", "RPi 1 - usb3")
        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, deviceTypes
        )
        val interfaceAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, deviceInterfaces
        )
        binding.spinnerDeviceType.adapter = typeAdapter
        binding.spinnerParentDevice.adapter = interfaceAdapter
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.id == R.id.buttonSaveSettings) {
                updateStationAddress()
            } else if (view.id == R.id.textViewSources) {
                if (binding.sourcesContainer.visibility == View.GONE) {
                    binding.sourcesContainer.visibility = View.VISIBLE
                    binding.cardViewSettings.visibility = View.GONE
                } else {
                    binding.sourcesContainer.visibility = View.GONE
                    binding.cardViewSettings.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateStationAddress() {
        updateAddresses(
            binding.editTextStationAddress.text.toString(),
            binding.editTextStationPort.text.toString().toInt(),
            this.application.resources.getString(R.string.ADDRESS_STATION_PREF),
            this.application.resources.getString(R.string.PORT_STATION_PREF),
            this.application.resources.getInteger(R.integer.DEFAULT_PORT_STATION)
        )
        updateAddresses(
            binding.editTextServerAddress.text.toString(),
            binding.editTextServerPort.text.toString().toInt(),
            this.application.resources.getString(R.string.ADDRESS_SERVER_PREF),
            this.application.resources.getString(R.string.PORT_SERVER_PREF),
            this.application.resources.getInteger(R.integer.DEFAULT_PORT_SERVER)
        )
        // todo store and forward list of sensors
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up)
    }

    private fun updateAddresses(
        ip: String,
        port: Int,
        keyAddress: String,
        keyPort: String,
        defaultPort: Int
    ) {
        val ipCurrent = mPref.getString(keyAddress, "192.168.0.8")
        val portCurrent = mPref.getInt(keyPort, defaultPort)

        if (!(ip == ipCurrent && port == portCurrent)) {

            with(mPref.edit()) {
                putString(keyAddress, ip)
                putInt(keyPort, port)
                apply()
            }
        }
    }
}