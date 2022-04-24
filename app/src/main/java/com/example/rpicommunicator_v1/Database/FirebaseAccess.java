package com.example.rpicommunicator_v1.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAccess {
    private static final String TAG = "firebaseAccess";
    private final PlantRepository plantRepository;
    private FirebaseFirestore db;

    public FirebaseAccess(PlantRepository plantRepository) {
        db = FirebaseFirestore.getInstance();
        this.plantRepository = plantRepository;

    }

    public void getFromFirebase() {
        //ArrayList<Plant> retVal = new ArrayList<>();
        db.collection("plants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Plant plant;
                                try {

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Log.d(TAG, document.get("typ").toString());
                                    String imageID = document.get("picture").toString();
                                    plant = new Plant(document.getId(), document.get("typ").toString(), document.get("info").toString(),
                                            document.get("watered").toString(),  document.get("humidity").toString(), document.getBoolean("needsWater"),
                                            document.getString("graph"));
                                    switch (imageID) {
                                        case "1":
                                            plant.setImageID(R.drawable.plant1);
                                            plant.setIconID(R.drawable.plant1);
                                            break;
                                        case "2":
                                            plant.setImageID(R.drawable.plant2);
                                            plant.setIconID(R.drawable.plant2);
                                            break;
                                        case "3":
                                            plant.setImageID(R.drawable.plant3);
                                            plant.setIconID(R.drawable.plant31p);
                                            break;
                                        case "4":
                                            plant.setImageID(R.drawable.plant4);
                                            plant.setIconID(R.drawable.plant41p);
                                            break;
                                        case "5":
                                            plant.setImageID(R.drawable.plant5);
                                            plant.setIconID(R.drawable.plant51p);
                                            break;
                                        case "6":
                                            plant.setImageID(R.drawable.plant6);
                                            plant.setIconID(R.drawable.plant61p);
                                            break;
                                        case "7":
                                            plant.setImageID(R.drawable.plant7);
                                            plant.setIconID(R.drawable.plant71p);
                                            break;
                                        case "8":
                                            plant.setImageID(R.drawable.plant8);
                                            plant.setIconID(R.drawable.plant81p);
                                            break;
                                        case "9":
                                            plant.setImageID(R.drawable.plant9);
                                            plant.setIconID(R.drawable.plant91p);
                                            break;
                                        case "10":
                                            plant.setImageID(R.drawable.plant10);
                                            plant.setIconID(R.drawable.plant101p);
                                            break;
                                        default:
                                            break;
                                    }
                                    //plant.setImageID(R.drawable.plant1);
                                    //plant.setIconID(R.drawable.plant1);
                                }catch (NullPointerException e){
                                    plant =new Plant("-1","Error","NullointerException","","-1",false,"");
                                }
                                plantRepository.insert(plant);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void updateWateredInFirebase(String id, boolean newValue){
        Map<String, Object> data = new HashMap<>();
        data.put("needsWater", newValue);
        db.collection("plants").document(id)
                .set(data, SetOptions.merge());
    }


    private void addOrUpdate(Plant plant) {
        try {
          //  plantRepository.update(plant);
            plantRepository.insert(plant);
        } catch (SQLiteConstraintException e) {
            plantRepository.update(plant);
        }
    }
}
