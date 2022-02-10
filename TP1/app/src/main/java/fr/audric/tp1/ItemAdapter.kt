package fr.audric.tp1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// l'Adapter associe a la recyclerView qui affiche les images
class ItemAdapter(var images : List<CommonImage>, val classeFunction : UpdateCallbacks) : RecyclerView.Adapter<ItemViewHolder>() {

    // Met a jour la liste d'images
    fun updateElements(newImages: List<CommonImage>) {
        images = newImages
        // Indique le changement des images
        notifyDataSetChanged()
    }

    // Cree la case pour afficher dans la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Cree la view dans la liste
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_elt, parent, false)
        //Initialise la case Cree avec le ItemViewHolder
        return ItemViewHolder(view)
    }

    // recycle la case pour afficher dans la liste
    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        // Recupere l'image 'position' de la liste
        val image = images[position]
        // Fixe un Callback sur le clic de la view
        viewHolder.itemView.setOnClickListener{
            classeFunction.onClick(image)
        }
        // Affiche l'image grace a update
        viewHolder.update(image,classeFunction)
    }

    // Recupere le nombre d'image
    override fun getItemCount(): Int {
        return images.size
    }
}