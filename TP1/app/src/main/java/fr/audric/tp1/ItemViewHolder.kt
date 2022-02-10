package fr.audric.tp1

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File

// Notre ViewHolder customise
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Recupere l'imageView de la case du RecyclerView
    private val _imageView: ImageView = itemView.findViewById(R.id.recycle_item_image)
    // Recupere le bouton de la case du RecyclerView
    private val _button: Button = itemView.findViewById(R.id.recycle_item_button)

    fun update(image: CommonImage,classeFunction : UpdateCallbacks) {
        // Fixe un Callback sur le clic du bouton
        _button.setOnClickListener {
            classeFunction.onClick(image)
        }
        // Si l'image est stockee
        if(image is StoredImage) {
            // On cherche l'image dans le systeme de fichier
            val imageFile =  File(_imageView.context.filesDir, image.imageName)
            // Affiche l'image dans l'imageView grace a coil
            _imageView.load(imageFile)
        } else {
            // Affiche l'image inconnu
            _imageView.load(R.drawable.unknown)
        }
    }
}