package com.example.rpicommunicator_v1.component.general

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.CommunicationInterface

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var cardViewSettings: CardView
    private lateinit var sourcesLayout: CardView
    private lateinit var communicationInterface: CommunicationInterface
    private lateinit var ipText: EditText
    private lateinit var portText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ViewModelProvider(this).get(CommunicationInterface::class.java)
        ipText = findViewById(R.id.inputIP)
        portText = findViewById(R.id.inputPort)
        findViewById<View>(R.id.connect_button).setOnClickListener(this)
        findViewById<View>(R.id.textViewSources).setOnClickListener(this)
        sourcesLayout = findViewById<CardView>(R.id.sourcesContainer)
        cardViewSettings = findViewById<CardView>(R.id.cardViewSettings)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            if (p0.getId() == R.id.connect_button) {
                connectToRPI()
            } else if (p0.id == R.id.textViewSources) {
                if (sourcesLayout.visibility == View.GONE) {
                    sourcesLayout.visibility = View.VISIBLE
                    cardViewSettings.visibility = View.GONE
                } else {
                    sourcesLayout.visibility = View.GONE
                    cardViewSettings.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun connectToRPI() {
        communicationInterface.localIP = ipText.text.toString()
        communicationInterface.localPort = portText.text.toString().toInt();
    }


    /*private fun openSourcesDialog() {
        val builder = AlertDialog.Builder(this)
        val titleView: TextView = TextView(this)
        titleView.setPadding(36, 48, 36, 8)
        titleView.setTextSize(16F)
        titleView.setTextColor(Color.WHITE)
        titleView.autoLinkMask
        titleView.text =
            "Pump icons created by Smashicons - Flaticon: https://www.flaticon.com/free-icons/pump\n\n" +
                    "Plant icons created by Freepik - Flaticon: https://www.flaticon.com/free-icons/plant\n\n" +
                    "Outlet icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/outlet\n\n" +
                    "Water drop icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/water-drop\n" +
                    "\n" +
                    "Clouds and sun icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/clouds-and-sun\n" +
                    "\n" +
                    "Train icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/train\n" +
                    "\n" +
                    "Playlist icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/playlist\n" +
                    "\n" +
                    "Hour icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/hour\n" +
                    "\n" +
                    "Power off icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/power-off\n" +
                    "\n" +
                    "Moon icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/moon\n" +
                    "\n" +
                    "Data storage icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/data-storage\n" +
                    "\n" +
                    "Embedded icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/embedded\n" +
                    "\n" +
                    "Bike icons created by DinosoftLabs - Flaticon: https://www.flaticon.com/free-icons/bike\n" +
                    "\n"
        builder.setCustomTitle(titleView)
        builder.setCancelable(true)
        builder.setPositiveButton("OK", null);
        val dialog: Dialog = builder.create()
        dialog.show()
    }*/
}