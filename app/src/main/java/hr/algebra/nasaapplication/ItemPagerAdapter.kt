package hr.algebra.nasaapplication

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasaapplication.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(private val items: MutableList<Item>, private val context: Context) :
    RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val explanation: TextView = itemView.findViewById(R.id.tvExplanation)
        private val pictureItem: ImageView = itemView.findViewById(R.id.ivItemPager)
        private val date: TextView = itemView.findViewById(R.id.tvDate)
        private val read: ImageView = itemView.findViewById(R.id.ivRead)

        fun bind(item: Item) {
            title.text = item.title
            explanation.text = item.explanation
            Picasso.get()
                .load(File(item.picturePath))
                .transform(RoundedCornersTransformation(50, 5))
                .error(R.drawable.flat_earth)
                .into(pictureItem)
            date.text = item.date
            read.setImageResource(if (item.read) R.drawable.green_flag else R.drawable.red_flag)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPagerAdapter.ViewHolder {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.item_pager, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemPagerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val read = holder.itemView.findViewById<ImageView>(R.id.ivRead)
        read.setOnClickListener {
                item.read = !item.read
                context.contentResolver.update(
                    ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, item._id!!),
                    ContentValues().apply {
                        put(Item::read.name, item.read)
                    },
                    null, null
                )
            notifyItemChanged(position)
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}