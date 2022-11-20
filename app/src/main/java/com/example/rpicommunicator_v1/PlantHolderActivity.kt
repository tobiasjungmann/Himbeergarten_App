package com.example.rpicommunicator_v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class PlantHolderActivity : AppCompatActivity(R.layout.activity_plant_holder) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<PlantOverviewFragment>(R.id.fragment_container_view)
                }
            }
        }

}