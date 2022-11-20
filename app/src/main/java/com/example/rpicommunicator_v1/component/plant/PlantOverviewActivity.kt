package com.example.rpicommunicator_v1.component.plant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.databinding.ActivityPlantOverviewBinding

class PlantOverviewActivity : AppCompatActivity() {

    private lateinit var plantViewModel: PlantViewModel
    private lateinit var binding: ActivityPlantOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        plantViewModel = ViewModelProvider(this)[PlantViewModel::class.java]

        initRecyclerView()
        binding.buttonAddElement.setOnClickListener {
            val nextIntent = Intent(this, AddEditPlantActivity::class.java)
            nextIntent.putExtra(Constants.MODE, Constants.ADD_REQUEST)
            resultLauncher.launch(nextIntent)
        }
    }

    private fun initRecyclerView() {
        val adapter = PlantAdapter()
        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            openPlantView(position)
        }
        adapter.setOnItemClickListener(itemOnClick)
        adapter.setViewModel(plantViewModel)

        plantViewModel.allPlants.observe(
            this
        ) { plants: List<Plant> ->
            adapter.setPlants(plants as List<Plant>)
        }
        initSwipeToRefresh()
        binding.plantViewRecycler.adapter = adapter
        binding.plantViewRecycler.layoutManager = LinearLayoutManager(this)
    }


    private fun initSwipeToRefresh() {
        // Setup refresh listener which triggers new data loading
        binding.swipeContainer.setOnRefreshListener {
            plantViewModel.reloadFromFirestore()
            binding.swipeContainer.isRefreshing = false
        }

        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun openPlantView(position: Int) {
        Log.d("overview", "method for listener called")
        val intent = Intent(applicationContext, PlantViewActivity::class.java)
        plantViewModel.setCurrentPlant(position)
        startActivity(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultMode = data?.getStringExtra(Constants.MODE)
                if (resultMode != null) {
                    if ((resultMode == Constants.ADD_REQUEST)) {
                        var name = data.getStringExtra(Constants.EXTRA_TITLE)
                        var type = data.getStringExtra(Constants.EXTRA_DESCRIPTION)
                        var info = data.getStringExtra(Constants.EXTRA_INFO)
                        val imagePath =
                            data.getStringArrayExtra(Constants.EXTRA_IMAGE_PATH)
                        if (name == null) {name="Name"}
                        if (type == null) {type=""}
                        if (info == null) {info=""}
                            plantViewModel.addPlant(name,type,info)

                        Toast.makeText(this, "New Plant saved Saved", Toast.LENGTH_SHORT).show()
                    }  else {
                        Toast.makeText(this, "Plant not Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Plant not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}