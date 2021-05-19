package com.example.rpicommunicator_v1.Communication;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.example.rpicommunicator_v1.ViewAndModels.Constants.IP;
import static com.example.rpicommunicator_v1.ViewAndModels.Constants.PORT;

public class SendThread extends Thread{

    private final String message;
    private  InetAddress localIP;

    public SendThread(String message, String localIP) {
        this.message = message;
        try {
            this.localIP = InetAddress.getByName(localIP);
        } catch (UnknownHostException e) {
            try {
                this.localIP = InetAddress.getByName(IP);
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public SendThread(String message) {
        this.message = message;
    }

    public void run() {
        Log.i("Communicator","Message: "+message);
        Socket socket = null;
        try {
            if(localIP==null) {
                socket = new Socket(IP, PORT);
            }else{
                socket=new Socket(localIP, PORT);
            }
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            DataInputStream din = new DataInputStream(socket.getInputStream());

            dout.writeUTF(message);
            dout.flush();


            Log.i("run", message + " sent");
            String str = din.readUTF();//in.readLine();

            Log.i("run", "Reply: " + str);


            dout.close();
            din.close();
            socket.close();
            //
            // Toast.makeText(context, text+" was successful", Toast.LENGTH_LONG).show();
            Log.i("run", "Thread terminated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
