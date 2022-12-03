package com.example.rpicommunicator_v1.component.plant

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.plant.PlantAdapter.PlantHolder
import com.example.rpicommunicator_v1.database.plant.models.Plant
import com.example.rpicommunicator_v1.databinding.ListItemImageBinding

class PlantAdapter (private val plantViewModel: PlantViewModel): RecyclerView.Adapter<PlantHolder>() {

    private var plants: List<Plant> = ArrayList()
    private lateinit var clickListener: (View, Int, Int) -> Unit


    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantHolder {
        val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantHolder(clickListener,binding)
    }

    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        val currentItem = plants[position]

        // todo query images
        /*if (currentItem.imageID != -1) {
            holder.binding.listThumbnailImageView.setImageResource(currentItem.iconID)
            val alpha = 1.toFloat()
            holder.binding.listThumbnailImageView.alpha = alpha
        } else {*/
            holder.binding.listThumbnailImageView.setImageResource(R.drawable.icon_plant)
            val alpha = 0.1.toFloat()
            holder.binding.listThumbnailImageView.alpha = alpha
        //}
        holder.binding.textViewTitle.text = currentItem.name
        holder.binding.textViewInfo.text = currentItem.info
        holder.binding.textViewDescription.text = currentItem.lastWatered
        holder.binding.buttonDeleteItem.setOnClickListener { v ->
            v.visibility = View.GONE
            plantViewModel!!.remove(plants[position])
        }
    }

    override fun getItemCount(): Int {
        return plants.size
    }

    fun setPlants(plants: List<Plant>) {
        this.plants=plants
        notifyItemRangeChanged(0,plants.size)
    }

    class PlantHolder(
        listener: (View, Int, Int) -> Unit,
        val binding: ListItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.invoke(
                    it,
                    bindingAdapterPosition,
                    itemViewType
                )
            }

            itemView.setOnLongClickListener(OnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (binding.buttonDeleteItem.visibility == View.GONE) {
                        binding.buttonDeleteItem.visibility = View.VISIBLE
                    } else {
                        binding.buttonDeleteItem.visibility = View.GONE
                    }
                    return@OnLongClickListener true
                }
                false
            })
        }
    }
}