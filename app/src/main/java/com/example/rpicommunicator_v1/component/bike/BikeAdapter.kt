package com.example.rpicommunicator_v1.component.bike


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.bike.BikeTour


class BikeAdapter : RecyclerView.Adapter<BikeAdapter.BikeTourHolder>() {
    private var bikeTours: List<BikeTour> = ArrayList()
    private var bikeViewModel: BikeTourViewModel? = null


    fun setViewModel(bikeViewModel: BikeTourViewModel?) {
        this.bikeViewModel = bikeViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeTourHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_text, parent, false)
        return BikeTourHolder(v)
    }

    override fun onBindViewHolder(holder: BikeTourHolder, position: Int) {
        val currentItem = bikeTours[position]

        holder.mTextView2.text = "Gefahrene Kilometer: "+currentItem.km.toString()
        holder.mTextView1.text = currentItem.from+" - "+currentItem.to
        holder.datumView.text = currentItem.time
        holder.buttonDelete.setOnClickListener { v ->
            v.visibility = View.GONE
            bikeViewModel!!.remove(bikeTours[position])
        }
    }

    override fun getItemCount(): Int {
        return bikeTours.size
    }

    fun setBikeTours(bikeTours: List<BikeTour>) {
        this.bikeTours = bikeTours
        notifyDataSetChanged()
    }

    class BikeTourHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var mTextView1: TextView
        var mTextView2: TextView
        var datumView: TextView
        var buttonDelete: Button

        init {
            mTextView1 = itemView.findViewById(R.id.title)
            mTextView2 = itemView.findViewById(R.id.textView)
            datumView = itemView.findViewById(R.id.textView2)
            buttonDelete = itemView.findViewById(R.id.delete_button)


            itemView.setOnLongClickListener(View.OnLongClickListener {

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