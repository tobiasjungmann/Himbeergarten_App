package com.example.rpicommunicator_v1.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Plant.class}, version = 8)
//mehrere entities in die geschweiften klammern version ändern, wenn tabelle verändert
public abstract class PlantDatabase extends RoomDatabase {
    private static PlantDatabase instance;

    public abstract PlantDao plantDao();

    public static synchronized PlantDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PlantDatabase.class, "plant_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };


  /*  private static class PopulateDBThread extends Thread {
        private PlantDao plantDao;

        private PopulateDBThread(PlantDatabase db) {
            plantDao = db.plantDao();
        }

        @Override
        public void run() {
            plantDao.insert(new Plant("---", "---"));
        }
    }*/
}
