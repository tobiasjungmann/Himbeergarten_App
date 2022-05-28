package com.example.rpicommunicator_v1.ViewAndModels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.rpicommunicator_v1.R

class AddComparingListActivity : AppCompatActivity() {

    val EXTRA_ID = "com.example.architectureexamplecif.EXTRA_ID"
    val EXTRA_TITLE = "com.example.architectureexamplecif.EXTRA_TITLE"
    val EXTRA_COLOR = "com.example.architectureexamplecif.EXTRA_COLOR"


    private var editTextTitle: EditText? = null
    private var saveListButton: Button?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comparing_list)
        editTextTitle = findViewById(R.id.edit_Text_Liste)
        saveListButton = findViewById(R.id.save_new_list_button)
        this.saveListButton!!.setOnClickListener{saveListe()}
    }



    private fun saveListe() {
        val title = editTextTitle!!.text.toString()
        if (title.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert title and Discription", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }



   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_new_list_button -> {
                saveListe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}