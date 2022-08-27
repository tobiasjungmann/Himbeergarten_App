package com.example.rpicommunicator_v1.service

import android.app.Activity
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import com.example.rpicommunicator_v1.component.Constants.IP
import com.example.rpicommunicator_v1.component.Constants.PORT
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException


class SendThread
    (private val message: String, localIP: String, localPort: Int) : Thread() {
    private var localIP: InetAddress? = null
    private var localPort: Int = PORT
    private var activity: Activity? = null

    init {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        this.localPort = localPort
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


    override fun run() {
        val socket: Socket
        try {
            if (localIP == null) {
                socket = Socket(IP, localPort)
            } else {
                socket = Socket(localIP, localPort)
            }
            val dout = DataOutputStream(socket.getOutputStream())
            val din = DataInputStream(socket.getInputStream())
            dout.writeUTF(message)
            dout.flush()
            val str = din.readUTF()

            Log.i("run", "Reply: $str")
            dout.close()
            din.close()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}