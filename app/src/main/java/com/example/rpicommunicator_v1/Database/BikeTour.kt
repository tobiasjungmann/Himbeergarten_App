package com.example.rpicommunicator_v1.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;


@Entity(tableName = "biketour_table")
public class BikeTour {
    private  String from;
    private  String to;
    private  double km;
    private  String time;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;


    public BikeTour( String from, String to, double km, String time) {
        this.from = from;
        this.to = to;
        this.km = km;
        this.time=time;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getKm() {
        return km;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


