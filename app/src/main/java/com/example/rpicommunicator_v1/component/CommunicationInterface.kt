package com.example.rpicommunicator_v1.component

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.service.SendThread

class CommunicationInterface(application: Application) : AndroidViewModel(application) {


    var localIP: String = Constants.IP
    var localPort: Int = Constants.PORT


    /*
    Network communication Methods
     */
    fun sendText(message: String) {
        SendThread(message, localIP,localPort).start()
    }


}