package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.rpicommunicator_v1.R

class ComparingListHolder : AppCompatActivity(R.layout.activity_comparing_list_holder) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ComparingListOverviewFragment>(R.id.fragment_container_view_comp_list)
            }
        }
    }
}