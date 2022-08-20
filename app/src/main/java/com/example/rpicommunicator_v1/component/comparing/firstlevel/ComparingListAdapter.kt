package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList

class ComparingListAdapter : RecyclerView.Adapter<ComparingListAdapter.ComparingListHolder>() {

    private var comparingList: List<ComparingList> = ArrayList()
    private lateinit var mListener:  (View, Int, Int)-> Unit


    class ComparingListHolder(itemView: View,listener: (View, Int, Int) -> Unit) : RecyclerView.ViewHolder(itemView){
        val textViewTitle: TextView
        val textViewSecondary: TextView
        val textViewDescription: TextView
        init {
            textViewTitle = itemView.findViewById(R.id.title)
            textViewSecondary = itemView.findViewById(R.id.textView)
            textViewDescription = itemView.findViewById(R.id.textView2)
            itemView.setOnClickListener { listener.invoke(it, bindingAdapterPosition, itemViewType)}
        }
    }

    fun setComparingList(comparingList: List<ComparingList>) {
        this.comparingList = comparingList
        notifyDataSetChanged()
    }



    val DIFF_CALLBACK: DiffUtil.ItemCallback<ComparingList> =
        object : DiffUtil.ItemCallback<ComparingList>() {
            override fun areItemsTheSame(oldItem: ComparingList, newItem: ComparingList): Boolean {
                return oldItem.comparingListId == newItem.comparingListId
            }

            override fun areContentsTheSame(
                oldItem: ComparingList,
                newItem: ComparingList
            ): Boolean {
                return (oldItem.title == newItem.title)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparingListHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_text, parent, false)
        return ComparingListHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ComparingListHolder, position: Int) {
        val currentList: ComparingList = comparingList[position]
        holder.textViewTitle.text = " " + currentList.title
        holder.textViewSecondary.visibility=View.GONE
        holder.textViewDescription.visibility=View.GONE
    }


    fun getListAt(position: Int): ComparingList {
        return comparingList[position]
    }

    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return comparingList.size
    }
}

