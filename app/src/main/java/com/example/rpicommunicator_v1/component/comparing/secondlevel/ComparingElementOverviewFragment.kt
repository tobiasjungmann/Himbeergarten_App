package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListViewModel
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.databinding.FragmentComparingElementOverviewBinding
import com.google.android.material.snackbar.Snackbar

class ComparingElementOverviewFragment : Fragment() {

    private var previouslyDeleted: ComparingElement? = null
    private lateinit var binding: FragmentComparingElementOverviewBinding
    private lateinit var listViewModel: ComparingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentComparingElementOverviewBinding.inflate(layoutInflater)
        listViewModel = ViewModelProvider(requireActivity())[ComparingListViewModel::class.java]

        initRecyclerView()

        binding.buttonAddCompElem.setOnClickListener {
            listViewModel.resetCurrentElement()
            val nextFrag = AddEditCompElementFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_comp_list, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun initRecyclerView() {
        listViewModel.queryAllThumbnailsForCurrentList()
        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size_list).toInt()
        val adapter = ComparingElementAdapter(thumbnailSize,viewLifecycleOwner)

        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            listViewModel.setCurrentElement(adapter.getElementAt(position))
            val nextFrag = AddEditCompElementFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_comp_list, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        adapter.setOnItemClickListener(itemOnClick)
        binding.recyclerViewCompElem.adapter = adapter


        listViewModel.getComparingElementsForCurrentList().observe(
            viewLifecycleOwner
        ) { elements ->
            adapter.setElementList(elements as List<ComparingElement>, listViewModel)
        }
        binding.recyclerViewCompElem.layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                previouslyDeleted = adapter.getElementAt(viewHolder.bindingAdapterPosition)
                listViewModel.delete(adapter.getElementAt(viewHolder.bindingAdapterPosition))
                val snackbar = Snackbar
                    .make(
                        binding.coordinatorLayoutElement,
                        "Notiz gelöscht",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) { listViewModel.insert(previouslyDeleted, arrayOf()) }
                snackbar.show()
            }
        }).attachToRecyclerView(binding.recyclerViewCompElem)
    }
}