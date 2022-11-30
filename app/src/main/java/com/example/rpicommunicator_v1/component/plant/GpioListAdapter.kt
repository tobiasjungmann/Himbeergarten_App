
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.rpicommunicator_v1.component.general.GpioElement
import com.example.rpicommunicator_v1.databinding.ListItemGpioBinding

class MyAdapter(
    private val context: Context,
    private val arrayList: java.util.ArrayList<GpioElement>
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val element=arrayList[position]
        val binding =
            ListItemGpioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.imageView1.setColorFilter(
            ContextCompat.getColor(context,element.accentLeft ),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.textView.text = arrayList[position].labelLeft

        binding.imageView2.setColorFilter(
            ContextCompat.getColor(context, element.accentRight),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.textView2.text = element.labelRight
        return binding.root
    }
}