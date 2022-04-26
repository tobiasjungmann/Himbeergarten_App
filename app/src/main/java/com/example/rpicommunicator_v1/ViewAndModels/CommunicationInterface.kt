package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.Communication.SendThread

class CommunicationInterface(application: Application) : AndroidViewModel(application) {


    public var localIP: String = "test"
    public var debugMode: Boolean = false



    /*
    Network communication Methods
     */
    fun sendText(message: String) {
        if (localIP != null) {
            SendThread(message, localIP, debugMode).start()
        } else {
            SendThread(message, debugMode).start()
        }
    }


}