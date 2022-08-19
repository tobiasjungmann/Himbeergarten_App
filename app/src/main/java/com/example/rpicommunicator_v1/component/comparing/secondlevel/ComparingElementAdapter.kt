package com.example.rpicommunicator_v1.component.comparing.secondlevel


import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.comparing.secondlevel.ComparingElementAdapter.ListHolder
import com.example.rpicommunicator_v1.database.compare.second_level.ComparingElement
import java.io.File


class ComparingElementAdapter : RecyclerView.Adapter<ListHolder>() {
    private lateinit var comparingElementViewModel: ComparingElementViewModel
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

        val helper = comparingElementViewModel.getAllPathsToElement(currentList.comparingElementId)
        /*.observe { elements ->
            holder.listThumbnailImageView.setImageResource(R.)
        }*/
        //todo set image view from path
        Log.d("TAG", "onBindViewHolder: " + (helper.size ?: -1))
        if (helper.size > 0) {
            val imgFile = File(helper.get(0).path)

            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                holder.listThumbnailImageView.setImageBitmap(myBitmap)
            }
        }
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
            listThumbnailImageView = itemView.findViewById(R.id.list_thumbnail_image_view)
            itemView.setOnClickListener { listener?.invoke(it, adapterPosition, itemViewType) }
        }
    }


    fun setOnItemClickListener(listener: (View, Int, Int) -> Unit) {
        this.listener = listener
    }

    companion object {
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
    }

    override fun getItemCount(): Int {
        return comparingElementList.size
    }

    fun setElementList(
        comparingElementList: List<ComparingElement>,
        comparingElementViewModel: ComparingElementViewModel
    ) {
        this.comparingElementList = comparingElementList
        this.comparingElementViewModel = comparingElementViewModel
        notifyDataSetChanged()
    }
}