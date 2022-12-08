package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementOverviewFragment
import com.example.rpicommunicator_v1.database.compare.models.ComparingList
import com.example.rpicommunicator_v1.databinding.FragmentComparingListOverviewBinding
import com.google.android.material.snackbar.Snackbar

class ComparingListOverviewFragment : Fragment() {

    private lateinit var binding: FragmentComparingListOverviewBinding
    private lateinit var listViewModel: ComparingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComparingListOverviewBinding.inflate(layoutInflater)

        binding.buttonAddCompList.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val view = View.inflate(context, R.layout.picture_dialog, null)

            builder.setView(view)
                .setNegativeButton("cancel", null)
                .setPositiveButton("Remove image", null)

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_background)
            dialog.show()

            /*val nextFrag = AddEditCompListFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_comp_list, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()*/
        }

        val adapter = ComparingListAdapter()
        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            listViewModel.setCurrentList(listViewModel.getComparingListByPosition(position))
            val nextFrag = ComparingElementOverviewFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view_comp_list, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        adapter.setOnItemClickListener(itemOnClick)
        binding.recyclerViewCompList.adapter = adapter

        listViewModel = ViewModelProvider(this)[ComparingListViewModel::class.java]
        listViewModel.getAllComparingLists().observe(
            viewLifecycleOwner
        ) { lists -> //update RecyclerView
            adapter.setComparingList(lists as List<ComparingList>)
        }
        binding.recyclerViewCompList.layoutManager = LinearLayoutManager(requireContext())

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
                val lastDeleted = adapter.getListAt(viewHolder.bindingAdapterPosition)
                listViewModel.delete(lastDeleted)

                val snackbar = Snackbar
                    .make(
                        binding.coordinatorLayoutComparingList,
                        "List deleted",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) {
                        listViewModel.insert(lastDeleted)
                    }
                snackbar.show()
            }
        }).attachToRecyclerView(binding.recyclerViewCompList)
        return binding.root
    }

    // todo add missing functions from history
}