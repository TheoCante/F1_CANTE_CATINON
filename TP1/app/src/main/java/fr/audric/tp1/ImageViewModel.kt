package fr.audric.tp1

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

// Le view Model permettant de transemettre les infos entre les Fragments
class ImageViewModel(application: Application) : AndroidViewModel(application) {
    //  Une LiveData d'erreur pour prevenir des erreurs lors des traitements pour le ViewModel
    val errors = MutableLiveData<Exception?>()

    // Creation d'un ImageManager avec la base de donnes contenant toutes les images stockes
    private val imageManager = ImageManager(application)

    // Creation d'une LiveData qui contiendra toutes les images generees
    private val generatedImagesLiveData = MutableLiveData<List<GeneratedImage>>(emptyList())

    //  Fusion des 2 Liste d'Image Stored et Generated pour tous les afficher
    val elementsLiveData: LiveData<List<CommonImage>> = imageManager.watchSavedImages().switchMap { storedImages ->
        generatedImagesLiveData.map { generatedImages ->
            // On concatene la liste des images generees a la liste des images stockees
            storedImages + generatedImages.filter {generatedImage ->
                //si l'image stocke est la meme que celle deja genere on l'enleve
                !storedImages.any {
                    it.imageName == generatedImage.url.removePrefix(imageManager.urlPrefix)
                }
            }
        }
    }

    // On genere une image et on la met dans la liste des images generees
    fun genElement() = viewModelScope.launch {
        try {
            // On appelle l'API pour genere une url d'Image
            val image = imageManager.genereImage()
            // On recupere la liste des elements deja affiche
            val alreadyGeneratedImages = generatedImagesLiveData.value.orEmpty()
            // On verifie que la liste ne contient pas l'image genere (pas possible normalement)
            if (!alreadyGeneratedImages.contains(image)) {
                // On concatene l'image a la liste des images deja affiche
                generatedImagesLiveData.value = alreadyGeneratedImages + image
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // On met l'erreur dans la LiveData en cas d'erreur
            errors.value = e
        }
    }

    // On sauvegarde l'image decrit par l'url
    suspend fun saveImage(imageUrl: String){
        imageManager.saveImage(imageUrl)
    }

    // On supprime les erreurs
    fun clearError(){
        errors.value = null
    }
}