package com.example.rpicommunicator_v1.Database;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.rpicommunicator_v1.Communication.NetworkCommunicator;

import java.util.List;

public class PlantRepository {
public static final String TAG="Repository ";
    private PlantDao plantDao;
    private LiveData<List<Plant>> allPlants;
    private FirebaseAccess firebaseAccess;

    public PlantRepository(Application application) {
        PlantDatabase database = PlantDatabase.getInstance(application);
        plantDao = database.plantDao();
        allPlants = plantDao.getAllPlants();

        firebaseAccess=new FirebaseAccess(this);
        //firebaseAccess.getFromFirebase();

    }

    public void insert(Plant plant) {
        new InsertPlantThread(plantDao, plant).start();
    }

    public void update(Plant plant) {
        new UpdatePlantThread(plantDao, plant).start();
    }

    public LiveData<List<Plant>> getAllPlants() {
        return allPlants;
    }

    public void reloadFromFirestore() {
        firebaseAccess.getFromFirebase();
    }

    public void remove(Plant plant) {
        new RemovePlantThread(plantDao, plant).start();
    }

    public void updateWateredInFirebase(String id, Boolean needsWater) {
        firebaseAccess.updateWateredInFirebase(id,needsWater);
    }


    private static class InsertPlantThread extends Thread {
        private Plant plant;
        private PlantDao plantDao;

        private InsertPlantThread(PlantDao plantDao, Plant plant) {
            this.plantDao = plantDao;
            this.plant=plant;
        }

        @Override
        public void run() {
            try {
                //  plantRepository.update(plant);
                plantDao.insert(plant);
            } catch (SQLiteConstraintException e) {
                plantDao.update(plant);
            }
        }
    }


    private static class UpdatePlantThread extends Thread {
        private Plant plant;
        private PlantDao plantDao;

        private UpdatePlantThread(PlantDao plantDao, Plant plant) {
            this.plantDao = plantDao;
            this.plant=plant;
        }

        @Override
        public void run() {
            plantDao.update(plant);
        }
    }

    private static class RemovePlantThread extends Thread {
        private Plant plant;
        private PlantDao plantDao;

        private RemovePlantThread(PlantDao plantDao, Plant plant) {
            this.plantDao = plantDao;
            this.plant=plant;
        }

        @Override
        public void run() {
            plantDao.delete(plant);
        }
    }
}
