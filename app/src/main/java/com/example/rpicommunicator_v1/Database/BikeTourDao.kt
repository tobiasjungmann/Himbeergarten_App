package com.example.rpicommunicator_v1.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;



    @Dao
    public interface BikeTourDao {

        @Insert
        void insert(BikeTour plant);

        @Update
        void update(BikeTour plant);

        @Delete
        void delete(BikeTour plant);

        @Query("SELECT * FROM biketour_table")
        LiveData<List<BikeTour>> getAllTours();


    }


