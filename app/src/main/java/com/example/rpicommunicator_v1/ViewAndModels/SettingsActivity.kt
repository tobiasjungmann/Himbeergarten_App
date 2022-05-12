package com.example.rpicommunicator_v1.ViewAndModels

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var communicationInterface: CommunicationInterface
    private lateinit var ipText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ViewModelProvider(this).get(CommunicationInterface::class.java)
        ipText = findViewById(R.id.inputIP)
        findViewById<View>(R.id.shutdown_button).setOnClickListener(this)
        findViewById<View>(R.id.connect_button).setOnClickListener(this)

        //findViewById(R.id.debugSelector).setOnClickListener(this);
        /*(findViewById<View>(R.id.switch1) as Switch).setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("Switch Test", "onCheckedChanged: $isChecked")
            debug = isChecked
        }*/
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            if (p0.getId() == R.id.shutdown_button) {
                Log.i("buttonClick", "shutdown was clicked")
                sendDeactivate()
            } else if (p0.getId() == R.id.connect_button) {
                Log.i("buttonClick", "Übernehmen was clicked")
                connectToRPI()
            }
        }
    }

    private fun sendDeactivate() {

        communicationInterface.sendText("deactivate");
    }

    private fun connectToRPI() {
        communicationInterface.localIP = ipText.text.toString();
    }


}