package com.example.rpicommunicator_v1.component.comparing.secondlevel

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
import com.example.rpicommunicator_v1.component.Constants.ADD_REQUEST
import com.example.rpicommunicator_v1.component.Constants.EDIT_REQUEST
import com.example.rpicommunicator_v1.component.Constants.EXTRA_DESCRIPTION
import com.example.rpicommunicator_v1.component.Constants.EXTRA_ID
import com.example.rpicommunicator_v1.component.Constants.EXTRA_IMAGE_PATH
import com.example.rpicommunicator_v1.component.Constants.EXTRA_PRIORITY
import com.example.rpicommunicator_v1.component.Constants.EXTRA_TITLE
import com.example.rpicommunicator_v1.component.Constants.MODE
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import com.example.rpicommunicator_v1.databinding.ActivityComparingElementBinding
import com.google.android.material.snackbar.Snackbar

class ComparingElementActivity : AppCompatActivity() {


    private var comparingElementViewModel: ComparingElementViewModel? = null
    private var listeId = 0
    private var previouslyDeleted: ComparingElement? = null
    private lateinit var binding: ActivityComparingElementBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparingElementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            listeId = intent.getIntExtra(EXTRA_ID, -1)
        }

        binding.buttonAddCompElem.setOnClickListener {
            val nextIntent = Intent(this, AddElementActivity::class.java)
            nextIntent.putExtra(MODE, ADD_REQUEST)
            resultLauncher.launch(nextIntent)
        }
    }

    private fun initRecyclerView() {
        val thumbnailSize = resources.getDimension(R.dimen.thumbnail_size_list).toInt()
        val adapter = ComparingElementAdapter(thumbnailSize)

        val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
            val nextIntent = Intent(this, AddElementActivity::class.java)
            nextIntent.putExtra(MODE, EDIT_REQUEST)
            nextIntent.putExtra(EXTRA_ID, adapter.getElementAt(position).comparingElementId)
            nextIntent.putExtra(EXTRA_TITLE, adapter.getElementAt(position).title)
            nextIntent.putExtra(EXTRA_DESCRIPTION, adapter.getElementAt(position).description)
            nextIntent.putExtra(EXTRA_PRIORITY, adapter.getElementAt(position).rating)

            resultLauncher.launch(nextIntent)
        }
        adapter.setOnItemClickListener(itemOnClick)
        binding.recyclerViewCompElem.adapter = adapter

        comparingElementViewModel = ViewModelProvider(this)[ComparingElementViewModel::class.java]
        comparingElementViewModel!!.getComparingElementByID(listeId).observe(
            this
        ) { elements ->
            adapter.setElementList(elements as List<ComparingElement>, comparingElementViewModel!!)
        }
        binding.recyclerViewCompElem.layoutManager = LinearLayoutManager(this)

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
                previouslyDeleted = adapter.getElementAt(viewHolder.bindingAdapterPosition)
                comparingElementViewModel!!.delete(adapter.getElementAt(viewHolder.bindingAdapterPosition))
                val snackbar = Snackbar
                    .make(
                        binding.coordinatorLayoutElement,
                        "Notiz gelöscht",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Rückgängig"
                    ) { comparingElementViewModel!!.insert(previouslyDeleted, arrayOf()) }
                snackbar.show()
            }
        }).attachToRecyclerView(binding.recyclerViewCompElem)
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultmode = data?.getStringExtra(MODE)
                if (resultmode != null) {
                    if ((resultmode == ADD_REQUEST)) {
                        val title = data.getStringExtra(EXTRA_TITLE)
                        val description = data.getStringExtra(EXTRA_DESCRIPTION)
                        val priority = data.getIntExtra(EXTRA_PRIORITY, 1)
                        val imagePath =
                            data.getStringArrayExtra(EXTRA_IMAGE_PATH)
                        val comparingElement =
                            ComparingElement(
                                title!!,
                                description!!,
                                priority,
                                listeId
                            )
                        comparingElementViewModel?.insert(comparingElement, imagePath)
                        Toast.makeText(this, "Element Saved", Toast.LENGTH_SHORT).show()
                    } else if ((resultmode == EDIT_REQUEST)) {
                        val id = data.getIntExtra(EXTRA_ID, -1)
                        if (id == -1) {
                            Toast.makeText(this, "can not be updated", Toast.LENGTH_SHORT).show()
                        } else {
                            val title = data.getStringExtra(EXTRA_TITLE)
                            val description =
                                data.getStringExtra(EXTRA_DESCRIPTION)
                            val priority = data.getIntExtra(EXTRA_PRIORITY, 1)
                            val imagePath =
                                data.getStringArrayExtra(EXTRA_IMAGE_PATH)
                            val comparingElement =
                                ComparingElement(
                                    title!!,
                                    description!!,
                                    priority,
                                    listeId
                                )
                            comparingElement.comparingElementId = id
                            if (imagePath != null) {
                                comparingElementViewModel?.update(comparingElement, imagePath)
                            }

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

