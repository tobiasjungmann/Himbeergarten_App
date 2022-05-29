package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingListAdapter
import com.example.rpicommunicator_v1.Database.Note.first_level.ComparingList
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.EXTRA_TITLE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ComparingListActivity : AppCompatActivity() {


    val ADD_LIST_REQUEST = 1

    private var listViewModel: ComparingListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparing_list)

        val buttonAddListe = findViewById<FloatingActionButton>(R.id.button_add_note)
        buttonAddListe.setOnClickListener{
            val intent = Intent(this, AddComparingListActivity::class.java)
            resultLauncher.launch(intent)
        };


        val recyclerView = findViewById<RecyclerView>(R.id.comparingListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = ComparingListAdapter()
        recyclerView.adapter = adapter

        //listViewModel = ViewModelProviders.of(this).get(ListeViewModel::class.java)
        listViewModel = ViewModelProvider(this).get(
            ComparingListViewModel::class.java
        )
        listViewModel!!.getAllComparingLists().observe(this,
            Observer<List<Any?>?> { lists -> //update RecyclerView
                adapter.setComparingList(lists as List<ComparingList>)
            })

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

                //für drag and drop
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val lastDeleted = adapter.getListeAt(viewHolder.adapterPosition)
                adapter.getListeAt(viewHolder.adapterPosition)?.let { listViewModel!!.delete(it) }

                /*NoteViewModel noteViewModel;

                noteViewModel = ViewModelProviders.of().get(NoteViewModel.class);
                noteViewModel.getListeNotes(listeId).observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(@Nullable List<Note> notes) {
                        //update RecyclerView
                        adapter.submitList(notes);
                    }
                });*/
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
        }).attachToRecyclerView(recyclerView)

       // adapter.setOnItemClickListener(ComparingListAdapter.OnItemClickListener)
        /*adapter.setOnItemClickListener{
            fun onItemClick(list: ComparingList) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(EXTRA_ID, list.getId())
                startActivity(intent)
            }
        }*/
    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val title = data!!.getStringExtra(EXTRA_TITLE)
                val liste = title?.let { ComparingList(it) }
                if (liste != null) {
                    listViewModel!!.insert(liste)
                }
                Toast.makeText(this, "List Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}


