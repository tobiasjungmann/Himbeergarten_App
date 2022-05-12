package com.example.rpicommunicator_v1.ViewAndModels

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
        findViewById<View>(R.id.textViewSources).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            if (p0.getId() == R.id.shutdown_button) {
                Log.i("buttonClick", "shutdown was clicked")
                sendDeactivate()
            } else if (p0.getId() == R.id.connect_button) {
                Log.i("buttonClick", "Übernehmen was clicked")
                connectToRPI()
            } else if (p0.id == R.id.textViewSources) {
                Log.i("buttonClick", "OpenSources activity was clicked")
                openSourcesDialog()
            }
        }
    }

    private fun sendDeactivate() {

        communicationInterface.sendText("deactivate");
    }

    private fun connectToRPI() {
        communicationInterface.localIP = ipText.text.toString();
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


}