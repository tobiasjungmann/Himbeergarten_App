package com.example.rpicommunicator_v1.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.rpicommunicator_v1.R;

@Entity(tableName = "plant_table")
public class Plant {
    @NonNull
    @PrimaryKey//(autoGenerate = true)
    private String id;
    private String name;
    private String info;
    private String watered;
    private int imageID= -1;
    private String humidity;
    private Boolean needsWater;
    private String graphString;
    private int iconID=-1;

    public Plant(String id, String name, String info, String watered, String humidity, Boolean needsWater, String graphString) {
        this.name = name;
        this.info = info;
        this.id = id;
        this.watered=watered;
        this.humidity=humidity;
        this.needsWater=needsWater;
        this.graphString=graphString;
        Log.d("Constructor", "Plant: needsWater: "+needsWater);
    }


    public String getHumidity() {
        return humidity;
    }

    public Boolean getNeedsWater() {
        return needsWater;
    }

    public void setNeedsWater(Boolean needsWater) {
        this.needsWater = needsWater;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getInfo() {
        return info;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getWatered() {
        return watered;
    }

    public String getGraphString() {
        return graphString;
    }

    public void setIconID(int iconID) {
        this.iconID=iconID;
    }

    public int getIconID() {
        return iconID;
    }
}
