package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListViewModel
import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementAdapter.ListHolder
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.databinding.ListItemImageBinding


class ComparingElementAdapter internal constructor(
    private val thumbnailSize: Int
): RecyclerView.Adapter<ListHolder>() {
    private lateinit var comparingElementViewModel: ComparingListViewModel
    private var listener: ((View, Int, Int) -> Unit)? = null
    private var comparingElementList: List<ComparingElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val currentList = comparingElementList[position]
        holder.binding.textViewTitle.text = currentList.title
        holder.binding.textViewInfo.text = currentList.rating.toString()
        holder.binding.textViewDescription.text = currentList.description

        val helper = comparingElementViewModel.getAllPathsToElement(currentList.comparingElementId)

        if (helper.isNotEmpty()) {
            holder.binding.listThumbnailImageView.visibility=View.VISIBLE
            holder.binding.listThumbnailImageView.setImageBitmap(helper[0].loadThumbnail(
                    thumbnailSize))
        }else{
            holder.binding.listThumbnailImageView.visibility=View.GONE
        }
    }

    fun getElementAt(position: Int): ComparingElement {
        return comparingElementList[position]
    }

    inner class ListHolder(val binding: ListItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener?.invoke(it, bindingAdapterPosition, itemViewType) }
        }
    }


    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        this.listener = listener
    }

  /*  companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ComparingElement> =
            object : DiffUtil.ItemCallback<ComparingElement>() {
                override fun areItemsTheSame(
                    oldItem: ComparingElement,
                    newItem: ComparingElement
                ): Boolean {
                    return oldItem.comparingElementId == newItem.comparingElementId
                }

                override fun areContentsTheSame(
                    oldItem: ComparingElement,
                    newItem: ComparingElement
                ): Boolean {
                    return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.rating == newItem.rating
                }
            }
    }*/

    override fun getItemCount(): Int {
        return comparingElementList.size
    }

    fun setElementList(
        comparingElementList: List<ComparingElement>,
        comparingElementViewModel: ComparingListViewModel
    ) {
        this.comparingElementList = comparingElementList
        this.comparingElementViewModel = comparingElementViewModel
        notifyDataSetChanged()
    }
}