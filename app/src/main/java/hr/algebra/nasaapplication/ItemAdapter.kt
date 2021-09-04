package hr.algebra.nasaapplication

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasaapplication.framework.startActivity
import hr.algebra.nasaapplication.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(private val items: MutableList<Item>, private val context: Context) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem: ImageView = itemView.findViewById(R.id.ivItem)
        private val tvItem: TextView = itemView.findViewById(R.id.tvItem)

        fun bind(item: Item) {
            tvItem.text = item.title
            Picasso.get()
                .load(File(item.picturePath))
                .transform(RoundedCornersTransformation(50, 5))
                .error(R.drawable.nasa)
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            itemView.apply {
                setOnLongClickListener {
                    AlertDialog.Builder(context).apply {
                        setTitle(context.getString(R.string.delete))
                        setMessage("'${items[position].title}?'")
                        setIcon(R.drawable.delete)
                        setCancelable(true)
                        setPositiveButton(context.getString(R.string.yes)) { _, _ -> deleteItem(position) }
                        setNegativeButton(context.getString(R.string.cancel), null)
                        show()
                    }
                    true
                }
                setOnClickListener {
                    context.startActivity<ItemPagerActivity>(ITEM_POSITION, position)
                }
            }
            bind(items[position])
        }
    }


private fun deleteItem(position: Int) {
    val item = items[position]
    context.contentResolver.delete(
        ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, item._id!!),
        null, null
    )
    File(item.picturePath).delete()
    items.removeAt(position)
    notifyDataSetChanged() // notify observer
}

override fun getItemCount() = items.size

}