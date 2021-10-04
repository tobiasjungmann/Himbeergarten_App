package com.example.rpicommunicator_v1.Communication;


import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.Database.PlantDao;
import com.example.rpicommunicator_v1.Database.PlantDatabase;
import com.example.rpicommunicator_v1.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkCommunicator extends Thread {


    private PlantDao plantDao;

    @Override
    public void run() {
        //requestData();
    }

    /**
     * Request Data from the server and update the database accordingly
     */
   /* private void requestData() {
        FirebaseAccess firebaseAccess = new FirebaseAccess();
        for (Plant p : firebaseAccess.getFromFirebase()) {
            addOrUpdate(p);
        }

        // plantDao.insert(new Plant(1,"---","wichtiger Text"));
        /*addOrUpdate(new Plant(0, "Sachsen Gras", "---", R.drawable.plant1));
        addOrUpdate(new Plant(1, "Pflanze", "---",-1));
        addOrUpdate(new Plant(2, "---", "toller Text",R.drawable.plant2));
        addOrUpdate(new Plant(3, "---", "---",-1));
        addOrUpdate(new Plant(4, "---", "---",-1));*/
    //}

    private void addOrUpdate(Plant plant) {
        try {
            plantDao.insert(plant);
        } catch (SQLiteConstraintException e) {
            plantDao.update(plant);
        }
    }

    public void setPlantDao(PlantDao plantDao) {
        this.plantDao = plantDao;
    }
}

