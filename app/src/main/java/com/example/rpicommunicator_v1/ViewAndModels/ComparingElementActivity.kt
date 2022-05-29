package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.Database.Note.second_level.Note
import com.example.rpicommunicator_v1.Database.Note.second_level.NoteAdapter
import com.example.rpicommunicator_v1.Database.Note.second_level.NoteViewModel
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



    private var noteViewModel: NoteViewModel? = null
    private var listeId = 0
    private var previouslyDeleted: Note? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparing_element)
        val intent = intent
        if ( intent.hasExtra(EXTRA_ID)) {
            listeId = intent.getIntExtra(EXTRA_ID, -1)
        }

        noteViewModel = ViewModelProvider(this).get(
            NoteViewModel::class.java
        )

        val buttonAddNote = findViewById<FloatingActionButton>(R.id.button_add_element)
        buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra(MODE, ADD_NOTE_REQUEST)
            resultLauncher.launch(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.comparing_element_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = NoteAdapter()

        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
            val intent = Intent(this, AddEditNoteActivity::class.java)

            intent.putExtra(MODE, EDIT_NOTE_REQUEST)
            intent.putExtra(EXTRA_ID,adapter.getNoteAt(position)?.id)// noteViewModel?.getListNotesByPosition(position)?.id)
            intent.putExtra(EXTRA_TITLE, adapter.getNoteAt(position)?.title)
            intent.putExtra(EXTRA_DESCRIPTION, adapter.getNoteAt(position)?.description)
            intent.putExtra(EXTRA_PRIORITY, adapter.getNoteAt(position)?.priority)

            resultLauncher.launch(intent)
        }
        adapter.setOnItemClickListener(itemOnClick)

        recyclerView.adapter = adapter

        noteViewModel = ViewModelProvider(this).get(
            NoteViewModel::class.java
        )
        // noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel!!.getListeNotes(listeId)?.observe(this,
            Observer<List<Any?>?> { notes -> //update RecyclerView
                adapter.setElementList(notes as List<Note>)
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
                previouslyDeleted = adapter.getNoteAt(viewHolder.adapterPosition)
                noteViewModel!!.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                val snackbar = Snackbar
                    .make(
                        findViewById(R.id.coordinator_layout_note),
                        "Notiz gelöscht",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) { noteViewModel!!.insert(previouslyDeleted) }
                snackbar.show()
            }
        }).attachToRecyclerView(recyclerView)


        // todo so umsetzten wie auch schon n den anderen mit listener
      /*  adapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val intent = Intent(this, ComparingElementActivity::class.java)
                //val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.MODE, EDIT_NOTE_REQUEST)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId())
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle())
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription())
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority())

                resultLauncher.launch(intent)
                //startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })*/
    }



    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultmode = data?.getStringExtra(MODE)
                if (resultmode != null) {
                    if ((resultmode.equals(ADD_NOTE_REQUEST))) {
                        val title = data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
                        val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
                        val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
                        val note = Note(title!!, description!!, priority, listeId)
                        noteViewModel?.insert(note)
                        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                    } else if ((resultmode.equals(EDIT_NOTE_REQUEST))) {
                        val id = data!!.getIntExtra(EXTRA_ID, -1)
                        if (id == -1) {
                            Toast.makeText(this, "can not be updated", Toast.LENGTH_SHORT).show()
                        }else {
                            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
                            val description =
                                data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
                            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
                            val note = Note(title!!, description!!, priority, listeId)
                            //note.setId(id)
                            noteViewModel?.update(note)

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

