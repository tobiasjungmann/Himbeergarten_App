package com.example.rpicommunicator_v1.component.comparing.secondlevel


import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementAdapter.ListHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import java.util.ArrayList

class ComparingElementAdapter : RecyclerView.Adapter<ListHolder>() {
    private var listener: ((View, Int, Int) -> Unit)? = null
    private var comparingElementList: List<ComparingElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return ListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val currentList = comparingElementList.get(position)
        holder.textViewTitle.text = currentList.title
        holder.textViewPriority.text = currentList.rating.toString()
        holder.textViewDescription.text = currentList.description
       // holder.listThumbnailImageView.setImageResource(R.)//todo darf keine ressource sein? in welchem format kann das vorliegen - erstmal abspecihern just load it from the path + thumbnail
    }

    fun getElementAt(position: Int): ComparingElement {
        return comparingElementList.get(position)
    }

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView
        val listThumbnailImageView: ImageView

        init {
            textViewTitle = itemView.findViewById(R.id.title)
            textViewDescription = itemView.findViewById(R.id.textView)
            textViewPriority = itemView.findViewById(R.id.textView2)
            listThumbnailImageView=itemView.findViewById(R.id.list_thumbnail_image_view)
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
                    return oldItem.comparingElementId == newItem.comparingElementId
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