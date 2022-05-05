package com.example.rpicommunicator_v1.Database


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.rpicommunicator_v1.R
import com.google.firebase.firestore.SetOptions
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.android.gms.tasks.Task
import java.lang.NullPointerException
import java.util.*

class FirebaseAccess(plantRepository: PlantRepository) {
    private val plantRepository: PlantRepository
    private val db: FirebaseFirestore

    //plant.setImageID(R.drawable.plant1);
    //plant.setIconID(R.drawable.plant1);
    val fromFirebase: Unit
        get() {
            db.collection("plants")
                .get()
                .addOnCompleteListener { task: Task<QuerySnapshot?> ->
                    if (task.isSuccessful) {
                        for (document in Objects.requireNonNull(task.result)!!) {
                            var plant: Plant
                            try {
                                Log.d(TAG, document.id + " => " + document.data)
                                Log.d(
                                    TAG, Objects.requireNonNull(
                                        document["typ"]
                                    ).toString()
                                )
                                val imageID = Objects.requireNonNull(document["picture"]).toString()
                                plant = Plant(
                                    document.id,
                                    Objects.requireNonNull(
                                        document["typ"]
                                    ).toString(),
                                    Objects.requireNonNull(document["info"]).toString(),
                                    Objects.requireNonNull(document["watered"]).toString(),
                                    Objects.requireNonNull(
                                        document["humidity"]
                                    ).toString(),
                                    document.getBoolean("needsWater")!!,
                                    document.getString("graph")!!
                                )
                                when (imageID) {
                                    "1" -> {
                                        plant.imageID = R.drawable.icon_plant
                                        plant.iconID = R.drawable.icon_plant
                                    }
                                    "2" -> {
                                        plant.imageID = R.drawable.icon_plant
                                        plant.iconID = R.drawable.icon_plant
                                    }
                                    "3" -> {
                                        plant.imageID = R.drawable.plant3
                                        plant.iconID = R.drawable.plant31p
                                    }
                                    "4" -> {
                                        plant.imageID = R.drawable.plant4
                                        plant.iconID = R.drawable.plant41p
                                    }
                                    "5" -> {
                                        plant.imageID = R.drawable.plant5
                                        plant.iconID = R.drawable.plant51p
                                    }
                                    "6" -> {
                                        plant.imageID = R.drawable.plant6
                                        plant.iconID = R.drawable.plant61p
                                    }
                                    "7" -> {
                                        plant.imageID = R.drawable.plant7
                                        plant.iconID = R.drawable.plant71p
                                    }
                                    "8" -> {
                                        plant.imageID = R.drawable.plant8
                                        plant.iconID = R.drawable.plant81p
                                    }
                                    "9" -> {
                                        plant.imageID = R.drawable.plant9
                                        plant.iconID = R.drawable.plant91p
                                    }
                                    "10" -> {
                                        plant.imageID = R.drawable.plant10
                                        plant.iconID = R.drawable.plant101p
                                    }
                                    else -> {}
                                }
                                //plant.setImageID(R.drawable.plant1);
                                //plant.setIconID(R.drawable.plant1);
                            } catch (e: NullPointerException) {
                                plant =
                                    Plant("-1", "Error", "NullointerException", "", "-1", false, "")
                            }
                            plantRepository.insert(plant)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        }

    fun updateWateredInFirebase(id: String?, newValue: Boolean) {
        val data: MutableMap<String, Any> = HashMap()
        data["needsWater"] = newValue
        db.collection("plants").document(id!!)[data] = SetOptions.merge()
    }

    private fun addOrUpdate(plant: Plant) {
        try {
            plantRepository.insert(plant)
        } catch (e: SQLiteConstraintException) {
            plantRepository.update(plant)
        }
    }

    companion object {
        private const val TAG = "firebaseAccess"
    }

    init {
        db = FirebaseFirestore.getInstance()
        this.plantRepository = plantRepository
    }
}