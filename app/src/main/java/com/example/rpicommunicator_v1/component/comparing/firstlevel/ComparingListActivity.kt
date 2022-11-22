package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementActivity
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList
import com.example.rpicommunicator_v1.databinding.ActivityComparingListBinding
import com.google.android.material.snackbar.Snackbar


class ComparingListActivity : AppCompatActivity() {


    private var listViewModel: ComparingListViewModel? = null
    private lateinit var binding: ActivityComparingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddCompList.setOnClickListener{
            val intent = Intent(this, AddComparingListActivity::class.java)
            resultLauncher.launch(intent)
        }

        val adapter = ComparingListAdapter()
        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            val intent = Intent(this, ComparingElementActivity::class.java)
            intent.putExtra(EXTRA_ID, listViewModel?.getComparingListByPosition(position)?.comparingListId)
            startActivity(intent)
        }
        adapter.setOnItemClickListener(itemOnClick)
        binding.recyclerViewCompList.adapter = adapter

        listViewModel = ViewModelProvider(this)[ComparingListViewModel::class.java]
        listViewModel!!.getAllComparingLists().observe(this
        ) { lists -> //update RecyclerView
            adapter.setComparingList(lists as List<ComparingList>)
        }
        binding.recyclerViewCompList.layoutManager = LinearLayoutManager(this)

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
                adapter.getListAt(viewHolder.bindingAdapterPosition)?.let { listViewModel!!.delete(it) }
                val snackbar = Snackbar
                    .make(
                        findViewById(R.id.coordinator_layout_comparing_list),
                        "List deleted",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) {
                        if (lastDeleted != null) {
                            listViewModel!!.insert(lastDeleted)
                        }
                    }
                snackbar.show()
            }
        }).attachToRecyclerView(binding.recyclerViewCompList)
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val title = data!!.getStringExtra(EXTRA_TITLE)
                val list = title?.let { ComparingList(it) }
                if (list != null) {
                    listViewModel!!.insert(list)
                }
                Toast.makeText(this, "List Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}


