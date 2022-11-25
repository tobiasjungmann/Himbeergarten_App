package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.databinding.ActivityAddCompListBinding


class AddComparingListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddCompListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCompListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSaveNewList.setOnClickListener { saveList() }
    }

    private fun saveList() {
        val title = binding.editTextList.text.toString()
        if (title.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert title and Description", Toast.LENGTH_SHORT).show()
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
}