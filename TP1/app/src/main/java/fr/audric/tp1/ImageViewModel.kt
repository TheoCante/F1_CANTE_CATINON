package fr.audric.tp1

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    val errors = MutableLiveData<Exception?>()
    private val imageManager = ImageManager(application)

    private val generatedImagesLiveData = MutableLiveData<List<GeneratedImage>>(emptyList())

    val elementsLiveData: LiveData<List<CommonImage>> = imageManager.watchSavedImages().switchMap {
    storedImages ->
        generatedImagesLiveData.map { generatedImages ->
            storedImages + generatedImages.filter {generatedImage ->
                !storedImages.any { it.imageName == generatedImage.url.removePrefix(imageManager.urlPrefix) }
            }

        }
    }

    fun genElement() = viewModelScope.launch {
        try {
            val image = imageManager.genereImage()
            Log.d("StringsViewModel", "image $image")
            val storedImages = generatedImagesLiveData.value.orEmpty()
            if (!storedImages.contains(image)) {
                generatedImagesLiveData.value = storedImages + image
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errors.value = e
        }
    }

    suspend fun saveImage(imageUrl: String){
        imageManager.saveImage(imageUrl)
    }

    fun clearError(){
        errors.value = null
    }
}