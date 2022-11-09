package com.example.rpicommunicator_v1.component.general

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private var mainActivityViewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.connectButton.setOnClickListener(this)
        binding.textViewSources.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            if (p0.id == R.id.connect_button) {
                connectToRPI()
            } else if (p0.id == R.id.textViewSources) {
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


    private fun connectToRPI() {
        mainActivityViewModel!!.setCommunicationCredentials(
            binding.inputIP.text.toString(),
            binding.inputPort.text.toString().toInt()
        )
    }
}