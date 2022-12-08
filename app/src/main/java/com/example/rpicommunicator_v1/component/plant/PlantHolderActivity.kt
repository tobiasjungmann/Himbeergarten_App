package com.example.rpicommunicator_v1.component.plant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.rpicommunicator_v1.R

class PlantHolderActivity : AppCompatActivity(R.layout.activity_plant_holder) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<PlantOverviewFragment>(R.id.fragment_container_view_plant)
                }
            }
        }
}