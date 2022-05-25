package com.example.rpicommunicator_v1.ViewAndModels

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ComparingList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparing_list)

        val buttonAddListe = findViewById<FloatingActionButton>(R.id.button_add_list)
        buttonAddListe.setOnClickListener {
            val intent = Intent(this@StartActivity, AddListActivity::class.java)
            startActivityForResult(
                intent,
                com.example.architectureexamplecif.StartActivity.ADD_LISTE_REQUEST
            )
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_liste)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = ListeAdapter()
        recyclerView.adapter = adapter

        listeViewModel = ViewModelProviders.of(this).get(ListeViewModel::class.java)
        listeViewModel.getAllLists().observe(this,
            Observer<List<Any?>?> { lists -> //update RecyclerView
                adapter.submitList(lists)
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
                letzteGeloescht = adapter.getListeAt(viewHolder.adapterPosition)
                listeViewModel.delete(adapter.getListeAt(viewHolder.adapterPosition))

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
                        findViewById(R.id.cordinator_layout),
                        "Liste gelöscht",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) { listeViewModel.insert(letzteGeloescht) }
                snackbar.show()
            }
        }).attachToRecyclerView(recyclerView)

        adapter.setOnItemClickListener(object : OnItemClickListener() {
            fun onItemClick(liste: Liste) {
                val intent = Intent(this@StartActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_LISTE_ID, liste.getId())
                intent.putExtra(MainActivity.EXTRA_LISTE_COLOR, liste.getColor())
                startActivity(intent)
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ComparingList.ADD_LISTE_REQUEST && resultCode == RESULT_OK) {
            val title = data!!.getStringExtra(AddListActivity.EXTRA_TITLE)
            val colorString = data.getStringExtra(AddListActivity.EXTRA_COLOR)
            val liste = Liste(title, colorString)
            listeViewModel.insert(liste)
            Toast.makeText(this, "Liste Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteAllNotes -> {
                listeViewModel.deleteAllLists()
                Toast.makeText(this, "All lists deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}

}