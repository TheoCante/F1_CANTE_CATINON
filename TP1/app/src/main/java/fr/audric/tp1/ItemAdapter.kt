package fr.audric.tp1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(var images : List<CommonImage>, val classeFunction : UpdateCallbacks) : RecyclerView.Adapter<ItemViewHolder>() {
    fun updateElements(newImages: List<CommonImage>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_elt, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        val image = images[position]
        viewHolder.itemView.setOnClickListener{
            classeFunction.onClick(image)
        }
        viewHolder.update(image,classeFunction)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}