package com.example.rpicommunicator_v1.component.comparing.secondlevel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.component.comparing.firstlevel.ComparingListViewModel
import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementAdapter.ListHolder
import com.example.rpicommunicator_v1.database.compare.models.ComparingElement
import com.example.rpicommunicator_v1.database.compare.models.PathElement
import com.example.rpicommunicator_v1.databinding.ListItemImageBinding


class ComparingElementAdapter internal constructor(
    private val thumbnailSize: Int,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ListHolder>() {
    private lateinit var comparingElementViewModel: ComparingListViewModel
    private var listener: ((View, Int, Int) -> Unit)? = null
    private var comparingElementList: List<ComparingElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding =
            ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val currentElement = comparingElementList[position]
        holder.binding.textViewTitle.text = currentElement.title
        holder.binding.textViewInfo.text = currentElement.rating.toString()
        holder.binding.textViewDescription.text = currentElement.description

        observeThumbnail(holder,currentElement.comparingElementId)
    }

    private fun observeThumbnail(holder: ListHolder, elementId: Int) {
        comparingElementViewModel.getThumbnailsForList().observe(
            viewLifecycleOwner
        ) { pathList: List<PathElement> ->
            val firstThumbnailIndex=pathList.indexOfFirst { it.parentEntry == elementId }

            if (firstThumbnailIndex>=0) {
                holder.binding.listThumbnailImageView.visibility = View.VISIBLE
                holder.binding.listThumbnailImageView.setImageBitmap(
                    pathList[firstThumbnailIndex].loadThumbnail(
                        thumbnailSize
                    )
                )
            } else {
                holder.binding.listThumbnailImageView.visibility = View.GONE
            }
        }
    }

    fun getElementAt(position: Int): ComparingElement {
        return comparingElementList[position]
    }

    inner class ListHolder(val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener?.invoke(
                    it,
                    bindingAdapterPosition,
                    itemViewType
                )
            }
        }
    }


    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        this.listener = listener
    }

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