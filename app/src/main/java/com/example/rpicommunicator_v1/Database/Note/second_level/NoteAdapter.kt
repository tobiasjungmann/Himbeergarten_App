package com.example.rpicommunicator_v1.Database.Note.second_level


import com.example.rpicommunicator_v1.Database.Note.second_level.NoteAdapter.NoteHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.rpicommunicator_v1.R
import java.util.ArrayList

class NoteAdapter : RecyclerView.Adapter<NoteHolder>() {
    private var listener: ((View, Int, Int) -> Unit)? = null
    private var noteList: List<Note> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = noteList.get(position)
        holder.textViewTitle.text = currentNote!!.title
        holder.textViewPriority.text = "" + currentNote.priority
        holder.textViewDescription.text = currentNote.description


    }

    fun getNoteAt(position: Int): Note? {
        return noteList.get(position)
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
            //itemView.setOnClickListener {
             /*   val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(noteList.get(position))
                }*///todo
            //}
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note?)
    }

    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> =
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.priority == newItem.priority
                }
            }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setElementList(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }
}