package fr.audric.tp1

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

// Le view Model permettant de transmettre les infos entre les Fragments
class ImageViewModel(application: Application) : AndroidViewModel(application) {
    //  Une LiveData d'erreur pour prévenir des erreurs lors des traitements pour le ViewModel
    val errors = MutableLiveData<Exception?>()

    // Création d'un ImageManager avec la base de données contenant toutes les images stockées
    private val imageManager = ImageManager(application)

    // Création d'une LiveData qui contiendra toutes les images générées
    private val generatedImagesLiveData = MutableLiveData<List<GeneratedImage>>(emptyList())

    //  Fusion des 2 listes d'ImageStored et ImageGenerated pour toutes les afficher
    val elementsLiveData: LiveData<List<CommonImage>> = imageManager.watchSavedImages().switchMap { storedImages ->
        generatedImagesLiveData.map { generatedImages ->
            // On concatène la liste des images générées à la liste des images stockées
            storedImages + generatedImages.filter {generatedImage ->
                // Si l'image stockée est la même que celle déjà générée on l'enlève
                !storedImages.any {
                    it.imageName == generatedImage.url.removePrefix(imageManager.urlPrefix)
                }
            }
        }
    }

    // On génère une image et on la met dans la liste des images générées
    fun genElement() = viewModelScope.launch {
        try {
            // On appelle l'API pour générer une url d'Image
            val image = imageManager.genereImage()
            // On récupère la liste des éléments déjà affichée
            val alreadyGeneratedImages = generatedImagesLiveData.value.orEmpty()
            // On vérifie que la liste ne contient pas l'image générée (pas possible normalement)
            if (!alreadyGeneratedImages.contains(image)) {
                // On concatène l'image à la liste des images dejà affichées
                generatedImagesLiveData.value = alreadyGeneratedImages + image
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // On met l'erreur dans la LiveData en cas d'erreur
            errors.value = e
        }
    }

    // On sauvegarde l'image décrite par l'url
    suspend fun saveImage(imageUrl: String){
        imageManager.saveImage(imageUrl)
    }

    // On supprime les erreurs
    fun clearError(){
        errors.value = null
    }
}