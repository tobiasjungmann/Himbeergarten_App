package com.example.rpicommunicator_v1.component.plant

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import com.example.rpicommunicator_v1.component.plant.PlantAdapter.PlantHolder
import com.example.rpicommunicator_v1.database.plant.Plant

class PlantAdapter : RecyclerView.Adapter<PlantHolder>() {

    private var plants: List<Plant> = ArrayList()
    private lateinit var clickListener:  (View, Int, Int)-> Unit
    private var longClickListener: OnItemLongClickListener? = null
    private var mainActivityViewModel: MainActivityViewModel? = null


    fun setViewModel(mainActivityViewModel: MainActivityViewModel?) {
        this.mainActivityViewModel = mainActivityViewModel
    }

    interface OnItemLongClickListener

    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return PlantHolder(v, clickListener)
    }

    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        val currentItem = plants[position]

        if (currentItem.imageID != -1) {
            holder.mImageView.setImageResource(currentItem.iconID)
            val alpha = 1.toFloat()
            holder.mImageView.alpha = alpha
        } else {
            holder.mImageView.setImageResource(R.drawable.icon_plant)
            val alpha = 0.1.toFloat()
            holder.mImageView.alpha = alpha
        }
        holder.mTextView1.text = currentItem.name
        holder.mTextView2.text = currentItem.info
        holder.datumView.text = currentItem.watered
        holder.buttonDelete.setOnClickListener { v ->
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
        listener: (View, Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView
        var mTextView1: TextView
        var mTextView2: TextView
        var datumView: TextView
        var buttonDelete: Button

        init {
            mImageView = itemView.findViewById(R.id.list_thumbnail_image_view)
            mTextView1 = itemView.findViewById(R.id.title)
            mTextView2 = itemView.findViewById(R.id.textView)
            datumView = itemView.findViewById(R.id.textView2)
            buttonDelete = itemView.findViewById(R.id.delete_button)
            itemView.setOnClickListener { listener.invoke(it, bindingAdapterPosition, itemViewType)}

            itemView.setOnLongClickListener(OnLongClickListener {

                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        if (buttonDelete.visibility == View.GONE) {
                            buttonDelete.visibility = View.VISIBLE
                        } else {
                            buttonDelete.visibility = View.GONE
                        }
                        return@OnLongClickListener true
                    }

                false
            })
        }
    }
}