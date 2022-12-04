package com.example.rpicommunicator_v1.component.general

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_SERVER_IP
import com.example.rpicommunicator_v1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.connectButton.setOnClickListener(this)
        binding.textViewSources.setOnClickListener(this)

        mPref = this.application.getSharedPreferences(
            this.application.resources.getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )

        initAddressUi()
        initDeviceSelection()

    }

    private fun initAddressUi() {
        binding.editTextServerAddress.setText(
            mPref.getString(
                this.application.resources.getString(R.string.ADDRESS_SERVER_PREF),
                DEFAULT_SERVER_IP
            )
        )
        binding.editTextServerPort.setText(
            mPref.getString(
                this.application.resources.getString(R.string.PORT_SERVER_PREF),
                this.application.resources.getInteger(R.integer.DEFAULT_PORT_SERVER).toString()
            )
        )

        binding.editTextStationAddress.setText(
            mPref.getString(
                this.application.resources.getString(R.string.ADDRESS_STATION_PREF),
                DEFAULT_SERVER_IP
            )
        )
        binding.editTextStationPort.setText(
            mPref.getString(
                this.application.resources.getString(R.string.PORT_STATION_PREF),
                this.application.resources.getInteger(R.integer.DEFAULT_PORT_SERVER).toString()
            )
        )
    }

    private fun initDeviceSelection() {
        // todo load from repository
        val deviceTypes = arrayOf("Nicht ausgewählt","Arduino Nano", "Raspberry Pi")
        val deviceInterfaces = arrayOf("RPi 1 - usb0","RPi 1 - usb1", "RPi 1 - usb3")
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
            if (view.id == R.id.connect_button) {
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