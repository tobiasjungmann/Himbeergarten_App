package com.example.rpicommunicator_v1.ViewAndModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rpicommunicator_v1.Database.BikeTour;
import com.example.rpicommunicator_v1.Database.BikeTourRepository;

import java.util.List;

public class BikeTourViewModel extends AndroidViewModel {

    private final BikeTourRepository bikeTourRepository;
    private final LiveData<List<BikeTour>> allBikeTours;

    public BikeTourViewModel(@NonNull Application application) {
        super(application);

        //networkCommunicator=new NetworkCommunicator();
        bikeTourRepository = new BikeTourRepository(application);
        allBikeTours = bikeTourRepository.getAllBikeTours();
    }


    public void remove(BikeTour bikeTour) {
        bikeTourRepository.remove(bikeTour);
    }
    public void insert(BikeTour bikeTour) {
        bikeTourRepository.insert(bikeTour);
    }

    public LiveData<List<BikeTour>> getAllBikeTours() {
        return allBikeTours;
    }
}
