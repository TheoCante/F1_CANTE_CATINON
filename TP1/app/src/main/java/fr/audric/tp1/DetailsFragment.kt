package fr.audric.tp1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.*

import androidx.fragment.app.activityViewModels
import android.content.Intent
import android.net.Uri
import coil.load
import com.google.android.material.snackbar.Snackbar
import java.io.File

// Fragment affichant une image sélectionnée depuis l'accueil
class DetailsFragment : Fragment(R.layout.details_fragment) {
    // Récupère le ViewModel
    private val imageViewModel : ImageViewModel by activityViewModels()
    //Récupère les Arguments de la navigation
    private val navArgs:DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // On récupère l'argument de ce Fragment
        val imageStr = navArgs.imageStr

        // On récupère le bouton de sauvegarde
        val buttonSave = view.findViewById<Button>(R.id.button_save)
        buttonSave?.setOnClickListener {
            // Lors du clic sur le bouton on sauvegarde l'image dans un fichier
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default){
                imageViewModel.saveImage(imageStr)
                // On informe l'utilisateur que l'image a ete sauvése
                Snackbar.make(view, R.string.imageSavedMessage, Snackbar.LENGTH_SHORT).show()
            }
            buttonSave.visibility = View.INVISIBLE
        }

        // On récupère le bouton de partage
        val buttonShare: Button = view.findViewById(R.id.button_share)
        buttonShare.setOnClickListener {
            // Lors du clic sur le bouton on lance un Intent de partage avec l'image a l'interieur
            // Creation de l'Intent
            val shareIntent: Intent = Intent().apply {
                // On définit l'action comme un envoi
                action = Intent.ACTION_SEND
                // On met l'image dans l'Intent
                putExtra(Intent.EXTRA_STREAM, Uri.parse(imageStr))
                // On definit le type d'Intent
                type = "image/jpeg"
            }
            // On lance l'Intent
            startActivity(Intent.createChooser(shareIntent, "Partage d'image"))
        }

        // On récupère l'imageView
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        // On affiche l'image dans l'imageView
        if(imageStr.contains("https://")) {// si imageStr est une url
            // Cette image n'est pas en mémoire, on affiche le bouton de sauvegarde
            buttonSave.visibility = View.VISIBLE
            // Affiche l'image depuis internet grace a coil
            imageView.load(imageStr)
        }else{
            // Cette image est déjà en mémoire, on masque le bouton de sauvegarde
            buttonSave.visibility = View.INVISIBLE
            // On récupère le fichier de l'image stockée
            val imageFile =  File(imageView.context.filesDir, imageStr)
            // Coil affiche l'image depuis son fichier
            imageView.load(imageFile)
        }
    }
}