package com.example.rpicommunicator_v1.component.plant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.plant.models.Plant
import com.example.rpicommunicator_v1.databinding.FragmentPlantOverviewBinding


class PlantOverviewFragment : Fragment() {

    private lateinit var plantViewModel: PlantViewModel
    private var _binding: FragmentPlantOverviewBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlantOverviewBinding.inflate(inflater, container, false)
        plantViewModel = ViewModelProvider(requireActivity()).get(PlantViewModel::class.java)

        binding.buttonAddPlant.setOnClickListener {
            plantViewModel.clearCurrentPlant()
            val nextFrag = AddEditPlantFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_plant, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        val adapter = PlantAdapter(plantViewModel)
        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            openPlantView(position)
        }
        adapter.setOnItemClickListener(itemOnClick)

        plantViewModel.getAllPlants().observe(
            viewLifecycleOwner
        ) { plants: List<Plant> ->
            adapter.setPlants(plants)
        }
        initSwipeToRefresh()
        binding.recyclerViewPlant.adapter = adapter
        binding.recyclerViewPlant.layoutManager = LinearLayoutManager(context)
    }


    private fun initSwipeToRefresh() {
        // Setup refresh listener which triggers new data loading
        // todo should only be triggered if actively pulled
        binding.swipeRefreshContainerPlant.setOnRefreshListener {
            plantViewModel.reloadFromServer()
            binding.swipeRefreshContainerPlant.isRefreshing = false
        }

        // Configure the refreshing colors
        binding.swipeRefreshContainerPlant.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun openPlantView(position: Int) {
        plantViewModel.setCurrentPlant(position)
        val nextFrag = PlantDetailFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_plant, nextFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }
}