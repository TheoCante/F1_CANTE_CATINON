package fr.audric.tp1

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File

// ViewHolder customisé
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Récupère l'imageView de la case du RecyclerView
    private val _imageView: ImageView = itemView.findViewById(R.id.recycle_item_image)
    // Récupère le bouton de la case du RecyclerView
    private val _button: Button = itemView.findViewById(R.id.recycle_item_button)

    fun update(image: CommonImage,classeFunction : UpdateCallbacks) {
        // Fixe un Callback sur le clic du bouton
        _button.setOnClickListener {
            classeFunction.onClick(image)
        }
        // Si l'image est stockée
        if(image is StoredImage) {
            // On cherche l'image dans le système de fichier
            val imageFile =  File(_imageView.context.filesDir, image.imageName)
            // Affiche l'image dans l'imageView grace à coil
            _imageView.load(imageFile)
        } else {
            // Affiche le placeholder 'inconnu'
            _imageView.load(R.drawable.unknown)
        }
    }
}