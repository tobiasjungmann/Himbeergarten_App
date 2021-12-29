package com.example.rpicommunicator_v1.Database;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BikeTourRepository {

    public static final String TAG="Repository ";
    private BikeTourDao bikeTourDao;
    private LiveData<List<BikeTour>> allBikeTours;

    public BikeTourRepository(Application application) {
        BikeTourDatabase database = BikeTourDatabase.getInstance(application);
        bikeTourDao = database.bikeTourDao();
        allBikeTours = bikeTourDao.getAllTours();
    }

    public void insert(BikeTour bikeTour) {
        new BikeTourRepository.InsertBikeTourThread(bikeTourDao, bikeTour).start();
    }


    public LiveData<List<BikeTour>> getAllBikeTours() {
        return allBikeTours;
    }



    public void remove(BikeTour bikeTour) {
        new BikeTourRepository.RemoveBikeTourThread(bikeTourDao, bikeTour).start();
    }




    private static class InsertBikeTourThread extends Thread {
        private BikeTour bikeTour;
        private BikeTourDao bikeTourDao;

        private InsertBikeTourThread(BikeTourDao bikeTourDao, BikeTour bikeTour) {
            this.bikeTourDao = bikeTourDao;
            this.bikeTour=bikeTour;
        }

        @Override
        public void run() {
            try {
                //  bikeTourRepository.update(bikeTour);
                bikeTourDao.insert(bikeTour);
            } catch (SQLiteConstraintException e) {
                bikeTourDao.update(bikeTour);
            }
        }
    }




    private static class RemoveBikeTourThread extends Thread {
        private BikeTour bikeTour;
        private BikeTourDao bikeTourDao;

        private RemoveBikeTourThread(BikeTourDao bikeTourDao, BikeTour bikeTour) {
            this.bikeTourDao = bikeTourDao;
            this.bikeTour=bikeTour;//bikeTourDao.getAllTours().getValue().get(bikeTourDao.getAllTours().getValue().size());
        }

        @Override
        public void run() {
            bikeTourDao.delete(bikeTour);
        }
    }
}
