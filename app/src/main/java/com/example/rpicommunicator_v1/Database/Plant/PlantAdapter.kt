package com.example.rpicommunicator_v1.Database.Plant

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.Database.Plant.PlantAdapter.PlantHolder
import com.example.rpicommunicator_v1.ViewAndModels.MainActivityViewModel
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.rpicommunicator_v1.R
import android.widget.TextView
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.example.rpicommunicator_v1.ViewAndModels.Constants
import com.example.rpicommunicator_v1.ViewAndModels.PlantView
import java.util.ArrayList

class PlantAdapter : RecyclerView.Adapter<PlantHolder>() {

    private var plants: List<Plant> = ArrayList()
    private lateinit var mListener:  (View, Int, Int)-> Unit;
    private var mlongListener: OnItemLongClickListener? = null
    private var mainActivityViewModel: MainActivityViewModel? = null


    fun setViewModel(mainActivityViewModel: MainActivityViewModel?) {
        this.mainActivityViewModel = mainActivityViewModel
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemClicked(position: Int)
    }

    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        mListener = listener
    }

    fun setOnItemLongClickListener(longListener: OnItemLongClickListener?) {
        mlongListener = longListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.plant_item, parent, false)
        v.setOnClickListener { Log.d("plantadapter","listener active") }
        var plantHolder = PlantHolder(v, mListener, mlongListener)
        return plantHolder;
    }

    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        val currentItem = plants[position]

        if (currentItem.imageID != -1) {
            holder.mImageView.setImageResource(currentItem.iconID)
            val alpha = 1.toFloat()
            holder.mImageView.alpha = alpha
        } else {
            //holder.blurHelper.setCardBackgroundColor(R.color.dark_yellow);//setBackgroundColor(R.color.dark_yellow);
            holder.mImageView.setImageResource(R.drawable.icon_pump)
            val alpha = 0.1.toFloat()
            holder.mImageView.alpha = alpha
        }
        holder.mTextView1.text = currentItem.name
        holder.mTextView2.text = currentItem.info
        holder.datumView.text = currentItem.watered
        holder.button_delete.setOnClickListener { v ->
            v.visibility = View.GONE
            mainActivityViewModel!!.remove(plants[position])
        }
    }

    override fun getItemCount(): Int {
        return plants.size
    }

    fun setPlants(plants: List<Plant>) {
        this.plants = plants
        notifyDataSetChanged()
    }

    class PlantHolder(
        itemView: View,
        listener: (View, Int, Int) -> Unit,
        longListener: OnItemLongClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView
        var mTextView1: TextView
        var mTextView2: TextView
        var datumView: TextView
        var button_delete: Button

        init {
            mImageView = itemView.findViewById(R.id.imageView)
            mTextView1 = itemView.findViewById(R.id.title)
            mTextView2 = itemView.findViewById(R.id.textView)
            datumView = itemView.findViewById(R.id.textView2)
            button_delete = itemView.findViewById(R.id.delete_button)
            itemView.setOnClickListener { listener.invoke(it, getAdapterPosition(), getItemViewType())}
               // val position = bindingAdapterPosition
               // Log.d("holder","listener triggered")
               // if (position != RecyclerView.NO_POSITION) {
//todo hier den neuen listener zum öffnen des neuen intents einfügen
                    //listener.onItemClicked(position)
                    //listener.
                    //listener

                            //idee: listener lambda ausführen
               // }
                /*val intent = Intent(applicationContext, PlantView::class.java)
                val plant: Plant = mainActivityViewModel.getActPlant(position)
                intent.putExtra(Constants.EXTRA_NAME, plant.name)
                intent.putExtra(Constants.EXTRA_HUMIDITY, plant.humidity)
                intent.putExtra(Constants.EXTRA_WATERED, plant.watered)
                intent.putExtra(Constants.EXTRA_NEEDS_WATER, plant.needsWater)
                intent.putExtra(Constants.EXTRA_IMAGE, plant.imageID)
                intent.putExtra(Constants.EXTRA_ICON, plant.iconID)
                intent.putExtra(Constants.EXTRA_INFO, plant.info)
                intent.putExtra(Constants.EXTRA_ID, plant.id)
                intent.putExtra(Constants.EXTRA_GRAPH_STRING, plant.graphString)
                startActivity(intent)*/
           // }
            itemView.setOnLongClickListener(OnLongClickListener {

                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        if (button_delete.visibility == View.GONE) {
                            button_delete.visibility = View.VISIBLE
                        } else {
                            button_delete.visibility = View.GONE
                        }
                        return@OnLongClickListener true
                    }

                false
            })
        }
    }
}