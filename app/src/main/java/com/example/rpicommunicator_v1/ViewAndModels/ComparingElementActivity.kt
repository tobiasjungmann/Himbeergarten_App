package com.example.rpicommunicator_v1.ViewAndModels

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity.MODE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ComparingElementActivity : AppCompatActivity() {
    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2

    val EXTRA_LISTE_ID = "EXTRA_LISTE_ID"
    val EXTRA_LISTE_COLOR = "EXTRA_LISTE_COLOR"
    private var noteViewModel: NoteViewModel? = null
    private var listeColor: String? = null
    private var listeId = 0
    private var previouslyDeleted: Note? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        if (intent.hasExtra(EXTRA_LISTE_COLOR) && intent.hasExtra(EXTRA_LISTE_ID)) {
            listeColor = intent.getStringExtra(EXTRA_LISTE_COLOR)
            listeId = intent.getIntExtra(EXTRA_LISTE_ID, -1)
        }
        val buttonAddNote = findViewById<FloatingActionButton>(R.id.button_add_note)
        buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra(AddEditNoteActivity.MODE, ADD_NOTE_REQUEST)
            resultLauncher.launch(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.comparing_element_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = NoteAdapter()
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
        /*adapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
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
        })*///todo
    }

    /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
              val title = data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
              val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
              val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
              val note = Note(title, description, priority, listeColor, listeId)
              noteViewModel.insert(note)
              Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
          } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
              val id = data!!.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
              if (id == -1) {
                  Toast.makeText(this, "can not be updated", Toast.LENGTH_SHORT).show()
                  return
              }
              val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
              val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
              val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
              val note = Note(title, description, priority, listeColor, listeId)
              note.setId(id)
              noteViewModel.update(note)
              Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
          } else {
              Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
          }
      }*/


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
                        val id = data!!.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
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

