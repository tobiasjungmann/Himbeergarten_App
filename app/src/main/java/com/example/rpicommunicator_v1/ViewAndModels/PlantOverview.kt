
package com.example.rpicommunicator_v1.ViewAndModels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rpicommunicator_v1.Database.Plant.Plant
import com.example.rpicommunicator_v1.Database.Plant.PlantAdapter
import com.example.rpicommunicator_v1.R

class PlantOverview : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_overview)
        mainActivityViewModel=ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.plant_view_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = PlantAdapter()
        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
            Log.d("plantclick","plant was cklicked")
            openPlantView(position)

        }
        adapter.setOnItemClickListener(itemOnClick);

        recyclerView.adapter = adapter
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