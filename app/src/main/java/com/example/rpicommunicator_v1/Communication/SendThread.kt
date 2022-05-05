package com.example.rpicommunicator_v1.Communication

import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode
import android.util.Log
import com.example.rpicommunicator_v1.ViewAndModels.Constants.IP
import com.example.rpicommunicator_v1.ViewAndModels.Constants.PORT_DEBUG
import com.example.rpicommunicator_v1.ViewAndModels.Constants.PORT_Production
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException

class SendThread : Thread {
    private val message: String
    private var localIP: InetAddress? = null
    private var debug: Boolean

    constructor(message: String, localIP: String?, debug: Boolean) {

        // todo remove after testing
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        this.message = message
        Log.d("Communicator", "debug in send constructor: $debug")
        this.debug = debug
        try {
            this.localIP = InetAddress.getByName(localIP)
        } catch (e: UnknownHostException) {
            try {
                this.localIP = InetAddress.getByName(IP)
            } catch (unknownHostException: UnknownHostException) {
                unknownHostException.printStackTrace()
            }
            e.printStackTrace()
        }
    }

    constructor(message: String, debug: Boolean) {
        this.debug = debug
        this.message = message
    }

    override fun run() {
        Log.i("Communicator", "Message: $message")
        var socket: Socket? = null
        try {
            var port: Int = PORT_Production
            debug = true //todo remove
            if (debug) {
                port = PORT_DEBUG
                Log.d("Communicator", "debug in send: $debug")
            }
            Log.d("Communicator", "Port before send: $port")
            if (localIP == null) {
                socket = Socket(IP, port)
            } else {
                socket = Socket(localIP, port)
            }
            val dout = DataOutputStream(socket!!.getOutputStream())
            val din = DataInputStream(socket.getInputStream())
            dout.writeUTF(message)
            dout.flush()
            Log.i("run", "$message sent")
            val str = din.readUTF() //in.readLine();
            Log.i("run", "Reply: $str")
            dout.close()
            din.close()
            socket.close()
            //
            // Toast.makeText(context, text+" was successful", Toast.LENGTH_LONG).show();
            Log.i("run", "Thread terminated")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}