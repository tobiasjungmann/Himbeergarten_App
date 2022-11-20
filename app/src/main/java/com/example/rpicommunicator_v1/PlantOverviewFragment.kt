package com.example.rpicommunicator_v1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.component.plant.PlantAdapter
import com.example.rpicommunicator_v1.component.plant.PlantViewModel
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.databinding.ActivityPlantOverviewBinding
import com.example.rpicommunicator_v1.databinding.FragmentPlantDetailBinding
import com.example.rpicommunicator_v1.databinding.FragmentPlantOverviewBinding


class PlantOverviewFragment : Fragment() {

    private lateinit var plantViewModel: PlantViewModel
  //  private lateinit var binding: FragmentPlantDetailBinding

    private var _binding: FragmentPlantOverviewBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlantOverviewBinding.inflate(inflater, container, false)
      //  binding = FragmentPlantDetailBinding.inflate(layoutInflater)
        val view = binding.root
    //    setContentView(view)

        plantViewModel = ViewModelProvider(requireActivity()).get(PlantViewModel::class.java)
//_binding.
//binding.buttonAddElement
        binding.buttonAddElementFragement .setOnClickListener {
            val nextFrag = PlantDetailFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
            /*  fixme val nextIntent = Intent(this, AddEditPlantActivity::class.java)
              nextIntent.putExtra(Constants.MODE, Constants.ADD_REQUEST)
              resultLauncher.launch(nextIntent)*/
        }
        initRecyclerView()
        return binding.root// inflater.inflate(R.layout.fragment_plant_overview, container, false)
    }




override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
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
        binding.plantViewRecycler.layoutManager = LinearLayoutManager(context)
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
        /* fixme Log.d("overview", "method for listener called")
        val intent = Intent(applicationContext, PlantViewActivity::class.java)
        plantViewModel.setCurrentPlant(position)
        startActivity(intent)*/
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

                        Toast.makeText(context, "New Plant saved Saved", Toast.LENGTH_SHORT).show()
                    }  else {
                        Toast.makeText(context, "Plant not Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Plant not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}