package com.example.rpicommunicator_v1.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDao {

    @Insert
    void insert(Plant plant);

    @Update
    void update(Plant plant);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM plant_table")
    LiveData<List<Plant>> getAllPlants();//Wegen den Livedata als returntyp werden werte in der Liste immer automatisch updated


}
