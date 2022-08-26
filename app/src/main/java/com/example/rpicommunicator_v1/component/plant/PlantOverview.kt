
package com.example.rpicommunicator_v1.component.plant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.databinding.ActivityPlantOverviewBinding

class PlantOverview : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityPlantOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_plant_overview)
        mainActivityViewModel= ViewModelProvider(this)[MainActivityViewModel::class.java]
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.plantViewRecycler.layoutManager = LinearLayoutManager(this)
       // binding.plantViewRecycler.setHasFixedSize(true)

        val adapter = PlantAdapter()
        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            openPlantView(position)
        }
        adapter.setOnItemClickListener(itemOnClick)

        binding.plantViewRecycler.adapter = adapter
        adapter.setViewModel(mainActivityViewModel)
        mainActivityViewModel.allPlants.observe(
            this
        ) { plants: List<Plant> ->
            adapter.setPlants(
                plants
            )
        }

        initSwipeToRefresh()
    }


    private fun initSwipeToRefresh() {
        val swipeContainer = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            mainActivityViewModel.reloadFromFirestore()
            swipeContainer.isRefreshing = false
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun openPlantView(position: Int) {
        Log.d("overview","method for listener called")
        val intent = Intent(applicationContext, PlantView::class.java)
        val plant: Plant = mainActivityViewModel.getActPlant(position)
        intent.putExtra(Constants.EXTRA_NAME, plant.name)
        intent.putExtra(Constants.EXTRA_HUMIDITY, plant.humidity)
        intent.putExtra(Constants.EXTRA_WATERED, plant.watered)
        intent.putExtra(Constants.EXTRA_NEEDS_WATER, plant.needsWater)
        intent.putExtra(Constants.EXTRA_IMAGE, plant.imageID)
        intent.putExtra(Constants.EXTRA_ICON, plant.iconID)
        intent.putExtra(Constants.EXTRA_INFO, plant.info)
        intent.putExtra(Constants.EXTRA_ID, plant.id)
        intent.putExtra(Constants.EXTRA_GRAPH_STRING, plant.graphString)
        startActivity(intent)
    }
}