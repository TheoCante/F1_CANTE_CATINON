package fr.audric.tp1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// Adapter associé à la recyclerView qui affiche les images
class ItemAdapter(var images : List<CommonImage>, val classeFunction : UpdateCallbacks) : RecyclerView.Adapter<ItemViewHolder>() {

    // Met à jour la liste d'images
    fun updateElements(newImages: List<CommonImage>) {
        images = newImages
        // Indique un changement du jeu de données à la recyclerview
        notifyDataSetChanged()
    }

    // Crée la case pour afficher dans la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Crée la view dans la liste
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_elt, parent, false)
        // Initialise la case créée avec le ItemViewHolder
        return ItemViewHolder(view)
    }

    // Recycle une case de la liste
    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        // Récupère l'image 'position' de la liste
        val image = images[position]
        // Fixe un Callback sur le clic de la view
        viewHolder.itemView.setOnClickListener{
            classeFunction.onClick(image)
        }
        // Affiche l'image
        viewHolder.update(image,classeFunction)
    }

    // Récupère le nombre d'images
    override fun getItemCount(): Int {
        return images.size
    }
}