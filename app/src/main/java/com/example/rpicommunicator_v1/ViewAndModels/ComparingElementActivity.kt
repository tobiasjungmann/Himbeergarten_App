package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.Database.Note.second_level.ComparingElement
import com.example.rpicommunicator_v1.Database.Note.second_level.ComparingElementAdapter
import com.example.rpicommunicator_v1.Database.Note.second_level.ComparingElementViewModel
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.ADD_NOTE_REQUEST
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.EDIT_NOTE_REQUEST
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.EXTRA_DESCRIPTION
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.EXTRA_PRIORITY
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.EXTRA_TITLE
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.Companion.MODE
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ComparingElementActivity : AppCompatActivity() {



    private var comparingElementViewModel: ComparingElementViewModel? = null
    private var listeId = 0
    private var previouslyDeleted: ComparingElement? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparing_element)
        val intent = intent
        if ( intent.hasExtra(EXTRA_ID)) {
            listeId = intent.getIntExtra(EXTRA_ID, -1)
        }

        comparingElementViewModel = ViewModelProvider(this).get(
            ComparingElementViewModel::class.java
        )

        val buttonAddNote = findViewById<FloatingActionButton>(R.id.button_add_element)
        buttonAddNote.setOnClickListener {
            val nextIntent = Intent(this, AddEditNoteActivity::class.java)
            nextIntent.putExtra(MODE, ADD_NOTE_REQUEST)
            resultLauncher.launch(nextIntent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.comparing_element_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = ComparingElementAdapter()

        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            val nextIntent = Intent(this, AddEditNoteActivity::class.java)

            nextIntent.putExtra(MODE, EDIT_NOTE_REQUEST)
            nextIntent.putExtra(EXTRA_ID,adapter.getNoteAt(position)?.id)
            nextIntent.putExtra(EXTRA_TITLE, adapter.getNoteAt(position)?.title)
            nextIntent.putExtra(EXTRA_DESCRIPTION, adapter.getNoteAt(position)?.description)
            nextIntent.putExtra(EXTRA_PRIORITY, adapter.getNoteAt(position)?.rating)

            resultLauncher.launch(nextIntent)
        }
        adapter.setOnItemClickListener(itemOnClick)

        recyclerView.adapter = adapter

        comparingElementViewModel = ViewModelProvider(this).get(
            ComparingElementViewModel::class.java
        )

        comparingElementViewModel!!.getComparingElementByID(listeId).observe(this
        ) { notes ->
            adapter.setElementList(notes as List<ComparingElement>)
        }
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
                previouslyDeleted = adapter.getNoteAt(viewHolder.adapterPosition)
                comparingElementViewModel!!.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                val snackbar = Snackbar
                    .make(
                        findViewById(R.id.coordinator_layout_note),
                        "Notiz gelöscht",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) { comparingElementViewModel!!.insert(previouslyDeleted) }
                snackbar.show()
            }
        }).attachToRecyclerView(recyclerView)
    }



    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultmode = data?.getStringExtra(MODE)
                if (resultmode != null) {
                    if ((resultmode.equals(ADD_NOTE_REQUEST))) {
                        val title = data.getStringExtra(EXTRA_TITLE)
                        val description = data.getStringExtra(EXTRA_DESCRIPTION)
                        val priority = data.getIntExtra(EXTRA_PRIORITY, 1)
                        val comparingElement = ComparingElement(title!!, description!!, priority, listeId)
                        comparingElementViewModel?.insert(comparingElement)
                        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                    } else if ((resultmode.equals(EDIT_NOTE_REQUEST))) {
                        val id = data.getIntExtra(EXTRA_ID, -1)
                        if (id == -1) {
                            Toast.makeText(this, "can not be updated", Toast.LENGTH_SHORT).show()
                        }else {
                            val title = data.getStringExtra(EXTRA_TITLE)
                            val description =
                                data.getStringExtra(EXTRA_DESCRIPTION)
                            val priority = data.getIntExtra(EXTRA_PRIORITY, 1)
                            val comparingElement = ComparingElement(title!!, description!!, priority, listeId)
                            comparingElement.id=id
                            comparingElementViewModel?.update(comparingElement)

                            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Element not Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Element not Saved", Toast.LENGTH_SHORT).show()
            }
        }
}

