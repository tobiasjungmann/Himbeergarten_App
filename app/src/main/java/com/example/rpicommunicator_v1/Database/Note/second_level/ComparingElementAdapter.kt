package com.example.rpicommunicator_v1.Database.Note.second_level


import com.example.rpicommunicator_v1.Database.Note.second_level.ComparingElementAdapter.NoteHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.rpicommunicator_v1.R
import java.util.ArrayList

class ComparingElementAdapter : RecyclerView.Adapter<NoteHolder>() {
    private var listener: ((View, Int, Int) -> Unit)? = null
    private var comparingElementList: List<ComparingElement> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = comparingElementList.get(position)
        holder.textViewTitle.text = currentNote.title
        holder.textViewPriority.text = currentNote.rating.toString()
        holder.textViewDescription.text = currentNote.description


    }

    fun getNoteAt(position: Int): ComparingElement {
        return comparingElementList.get(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView


        init {
            textViewTitle = itemView.findViewById(R.id.title)
            textViewDescription = itemView.findViewById(R.id.textView)
            textViewPriority = itemView.findViewById(R.id.textView2)
            itemView.setOnClickListener { listener?.invoke(it, getAdapterPosition(), getItemViewType())}
        }
    }


    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ComparingElement> =
            object : DiffUtil.ItemCallback<ComparingElement>() {
                override fun areItemsTheSame(oldItem: ComparingElement, newItem: ComparingElement): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: ComparingElement, newItem: ComparingElement): Boolean {
                    return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.rating == newItem.rating
                }
            }
    }

    override fun getItemCount(): Int {
        return comparingElementList.size
    }

    fun setElementList(comparingElementList: List<ComparingElement>) {
        this.comparingElementList = comparingElementList
        notifyDataSetChanged()
    }
}