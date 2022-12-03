package com.example.rpicommunicator_v1.component.plant
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.plant.models.GpioElement
import com.example.rpicommunicator_v1.databinding.ListItemGpioBinding


class GpioAdapter(
    private val context: Context,
    private val plantViewModel: PlantViewModel
) : RecyclerView.Adapter<GpioAdapter.GpioHolder>() {

    private var gpioElementList: List<GpioElementPair> = ArrayList()

    override fun getItemCount(): Int {
        return gpioElementList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GpioHolder {
        val binding =
            ListItemGpioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GpioHolder(binding)
    }

    override fun onBindViewHolder(holder: GpioHolder, position: Int) {

        holder.getLabel(true).setOnClickListener { v ->
            gpioClicked(gpioElementList[position].left, v as TextView)
        }
        holder.getLabel(false).setOnClickListener { v ->
            gpioClicked(gpioElementList[position].right, v as TextView)
        }
        colorLabel(holder, gpioElementList[position], true)
        colorLabel(holder, gpioElementList[position], false)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setGpioElements(gpios: List<GpioElementPair>) {
        this.gpioElementList = gpios
        notifyItemRangeChanged(0, gpios.size)
    }

    private fun gpioClicked(gpioElement: GpioElement, label: TextView) {
        if (gpioElement.plant == -1) {
            label.setBackgroundColor(
                ContextCompat.getColor(context, R.color.gpio_red)
            )
            // todo update all other values - remove color from unselected element
            // todo get view for previous, set color in it
            plantViewModel.gpioSelectedForElement(gpioElement)
        }
    }

    private fun colorLabel(
        mViewHolder: GpioHolder,
        gpioElementPair: GpioElementPair,
        left: Boolean
    ) {
        mViewHolder.getImageView(left).setColorFilter(
            ContextCompat.getColor(context, gpioElementPair.getAccentColor(left)),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        mViewHolder.getLabel(left).setBackgroundColor(
            ContextCompat.getColor(context, getLabelColor(gpioElementPair.gpioInUse(left)))
        )
        mViewHolder.getLabel(left).text = gpioElementPair.getLabel(left)
    }

    private fun getLabelColor(gpioInUse: Boolean): Int {
        return if (gpioInUse) {
            R.color.gpio_red
        } else {
            R.color.white
        }
    }

    class GpioHolder(
        val binding: ListItemGpioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun getImageView(left: Boolean): ImageView {
            return if (left) {
                binding.imageView1
            } else {
                binding.imageView2
            }
        }

        fun getLabel(left: Boolean): TextView {
            return if (left) {
                binding.textView
            } else {
                binding.textView2
            }
        }
    }
}