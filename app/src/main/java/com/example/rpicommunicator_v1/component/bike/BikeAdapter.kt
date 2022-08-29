package com.example.rpicommunicator_v1.component.bike


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.database.bike.BikeTour
import com.example.rpicommunicator_v1.databinding.ListItemTextBinding


class BikeAdapter : RecyclerView.Adapter<BikeAdapter.BikeTourHolder>() {
    private var bikeTours: List<BikeTour> = ArrayList()
    private var bikeViewModel: BikeTourViewModel? = null


    fun setViewModel(bikeViewModel: BikeTourViewModel?) {
        this.bikeViewModel = bikeViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeTourHolder {
        val binding = ListItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BikeTourHolder(binding)
    }

    override fun onBindViewHolder(holder: BikeTourHolder, position: Int) {
        val currentItem = bikeTours[position]

        holder.binding.title.text = "Gefahrene Kilometer: "+currentItem.km.toString()
        holder.binding.upperInfo.text = currentItem.from+" - "+currentItem.to
        holder.binding.lowerInfo.text = currentItem.time
        holder.binding.deleteButton.setOnClickListener { v ->
            v.visibility = View.GONE
            bikeViewModel!!.remove(bikeTours[position])
        }
    }

    override fun getItemCount(): Int {
        return bikeTours.size
    }

    fun setBikeTours(bikeTours: List<BikeTour>) {
        this.bikeTours = bikeTours
        notifyItemRangeInserted(0,bikeTours.size)
    }

    class BikeTourHolder(
       val binding: ListItemTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {
                init {
            itemView.setOnLongClickListener(View.OnLongClickListener {

                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (binding.deleteButton.visibility == View.GONE) {
                        binding.deleteButton.visibility = View.VISIBLE
                    } else {
                        binding.deleteButton.visibility = View.GONE
                    }
                    return@OnLongClickListener true
                }

                false
            })
        }
    }
}