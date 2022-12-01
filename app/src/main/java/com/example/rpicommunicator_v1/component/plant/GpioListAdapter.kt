
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.database.plant.GpioElement
import com.example.rpicommunicator_v1.component.general.GpioElementPair
import com.example.rpicommunicator_v1.component.plant.PlantViewModel


class MyAdapter(
    private val context: Context,
    private val arrayList: ArrayList<GpioElementPair>,
    private val plantViewModel: PlantViewModel
) : BaseAdapter() {

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    private class ViewHolder(item: View?) {

        var labelLeft: TextView? = null
        var labelRight: TextView? = null
        var imageLeft: ImageView? = null
        var imageRight: ImageView? = null

        init {
            this.labelLeft = item?.findViewById(R.id.textView) as TextView
            this.labelRight = item.findViewById(R.id.textView2) as TextView
            this.imageLeft = item.findViewById(R.id.imageView1) as ImageView
            this.imageRight = item.findViewById(R.id.imageView2) as ImageView
        }
        fun getImageView(left: Boolean): ImageView?{
            return if (left){
                imageLeft
            }else{
                imageRight
            }
        }
        fun getLabel(left: Boolean): TextView?{
            return if (left){
                labelLeft
            }else{
                labelRight
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val mViewHolder: ViewHolder?
        if (convertView == null) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_gpio, parent, false)
            mViewHolder = ViewHolder(view)
            view.tag = mViewHolder
            mViewHolder.labelRight?.setOnClickListener { v ->
                gpioClicked(arrayList[position].right,v as TextView)
             }
            mViewHolder.labelLeft?.setOnClickListener { v ->
                gpioClicked(arrayList[position].left,v as TextView)
            }
        } else {
            view = convertView
            mViewHolder = view.tag as ViewHolder
        }

        colorLabel(mViewHolder,arrayList[position],true)
        colorLabel(mViewHolder,arrayList[position],false)
        return view;
    }

    private fun gpioClicked(gpioElement: GpioElement, label: TextView) {
        if (gpioElement.userId==-1){
            label.setBackgroundColor(
                ContextCompat.getColor(context,R.color.gpio_red))
            // todo update all other values - remove color from unselected element
            plantViewModel.gpioSelectedForElement(gpioElement)
        }
    }

    private fun colorLabel(
        mViewHolder: ViewHolder,
        gpioElementPair: GpioElementPair,
        left: Boolean
    ) {
        mViewHolder.getImageView(left)?.setColorFilter(
            ContextCompat.getColor(context, gpioElementPair.getAccentColor(left)),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        mViewHolder.getLabel(left)?.setBackgroundColor(
            ContextCompat.getColor(context,getLabelColor(gpioElementPair.gpioInUse(left))))
        mViewHolder.getLabel(left)?.text=gpioElementPair.getLabel(left)
    }

    private fun getLabelColor(gpioInUse: Boolean): Int {
        return if (gpioInUse){
            R.color.gpio_red
        }else{
            R.color.white
        }
    }
}