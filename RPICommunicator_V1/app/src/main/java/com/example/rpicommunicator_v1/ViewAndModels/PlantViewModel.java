package com.example.rpicommunicator_v1.ViewAndModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rpicommunicator_v1.Communication.SendThread;
import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.Database.PlantRepository;

import java.util.List;

public class PlantViewModel extends AndroidViewModel {

    private final PlantRepository plantRepository;
    private final LiveData<List<Plant>> allPlants;

    public PlantViewModel(@NonNull Application application) {
        super(application);

        //networkCommunicator=new NetworkCommunicator();
        plantRepository =new PlantRepository(application);
        allPlants=plantRepository.getAllPlants();
    }

    /*public void insert(Plant plant){
        plantRepository.insert(plant);
    }*/

    public void update(Plant plant){
        plantRepository.update(plant);
    }


    public void updateWateredInFirebase(String id, Boolean needsWater) {
        plantRepository.updateWateredInFirebase(id,needsWater);
    }

    public void remove(Plant plant) {
    plantRepository.remove(plant);
    }

    public LiveData<List<Plant>> getAllPlants() {
        return allPlants;
    }


    /*
    Network communication Methodes
     */
    public void sendText(String message, boolean debug) {
        new SendThread(message,debug).start();
    }

    public void sendText(String message, String localIP, boolean debug) {
        new SendThread(message,localIP, debug).start();
    }


    public void reloadFromFirestore(){
        Log.d("PlantviewModel", "reloading");
        plantRepository.reloadFromFirestore();
    }


    public Plant getActPlant(int position) {
        return allPlants.getValue().get(position);
    }

}
