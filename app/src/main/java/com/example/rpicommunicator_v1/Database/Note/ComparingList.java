package com.example.rpicommunicator_v1.Database.Note;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "comparing_list_table")
public class ComparingList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;


    public ComparingList(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
