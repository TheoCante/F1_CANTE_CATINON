package fr.audric.tp1

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File

// Notre ViewHolder customise
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var _imageView: ImageView
    var _button: Button

    init {
        _button = itemView.findViewById(R.id.recycle_item_button)
        _imageView = itemView.findViewById(R.id.recycle_item_image)
    }

    fun update(image: CommonImage,classeFunction : UpdateCallbacks) {
        _button.setOnClickListener {
            classeFunction.onClick(image)
        }
        if(image is StoredImage) {
            val imageFile =  File(_imageView.context.filesDir, image.imageName)
            _imageView.load(imageFile)
        } else if(image is GeneratedImage) {
            _imageView.load(R.drawable.unknown)
        }
    }
}