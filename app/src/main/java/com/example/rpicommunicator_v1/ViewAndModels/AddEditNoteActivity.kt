package com.example.rpicommunicator_v1.ViewAndModels

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.NumberPicker
import android.os.Bundle
import com.example.rpicommunicator_v1.R
import android.content.Intent
import com.example.rpicommunicator_v1.ViewAndModels.AddEditNoteActivity
import android.widget.Toast
import android.app.Activity
import android.widget.Button
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_DESCRIPTION
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_PRIORITY
import com.example.rpicommunicator_v1.ViewAndModels.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.ViewAndModels.Constants.MODE

class AddEditNoteActivity : AppCompatActivity() {
    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var numberPickerPriority: NumberPicker? = null
    private var mode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        numberPickerPriority = findViewById(R.id.number_picker_priority)
        numberPickerPriority?.setMinValue(1)
        numberPickerPriority?.setMaxValue(10)



        val saveButton=findViewById<Button>(R.id.button_save_comparing_element)
        saveButton.setOnClickListener{saveNote()}

        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit note"
            editTextDescription?.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            editTextTitle?.setText(intent.getStringExtra(EXTRA_TITLE))
            numberPickerPriority?.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1))
        } else {
            title = "AddNote"
        }
        if (intent.hasExtra(MODE)) {
            mode = intent.getStringExtra(MODE)
        }
    }

    private fun saveNote() {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        val priority = numberPickerPriority!!.value
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Insert title and Discription", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)
        data.putExtra(MODE, mode)
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}