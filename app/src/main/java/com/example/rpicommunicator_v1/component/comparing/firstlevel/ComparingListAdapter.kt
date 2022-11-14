package com.example.rpicommunicator_v1.component.comparing.firstlevel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.database.compare.first_level.ComparingList
import com.example.rpicommunicator_v1.databinding.ListItemTextBinding

class ComparingListAdapter : RecyclerView.Adapter<ComparingListAdapter.ComparingListHolder>() {

    private var comparingList: List<ComparingList> = ArrayList()
    private lateinit var mListener:  (View, Int, Int)-> Unit


    class ComparingListHolder(val binding: ListItemTextBinding, listener: (View, Int, Int) -> Unit) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener { listener.invoke(it, bindingAdapterPosition, itemViewType)}
        }
    }

    fun setComparingList(comparingList: List<ComparingList>) {
        this.comparingList = comparingList
        notifyItemRangeChanged(0,comparingList.size)
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
        Log.d("fdsa", "onCreateViewHolder: Called")
        val binding = ListItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComparingListHolder(binding,mListener)
    }

    override fun onBindViewHolder(holder: ComparingListHolder, position: Int) {
        val currentList: ComparingList = comparingList[position]
        holder.binding.title.text = " " + currentList.title
        holder.binding.upperInfo.visibility=View.GONE
        holder.binding.lowerInfo.visibility=View.GONE
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

