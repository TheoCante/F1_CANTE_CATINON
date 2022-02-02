package fr.audric.tp1

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class StringsViewModel(application: Application) : AndroidViewModel(application) {
    private val _elements: MutableLiveData<List<Image>> = MutableLiveData<List<Image>>()
    val elements: LiveData<List<Image>> = _elements

    val errors = MutableLiveData<Exception?>()
    val imageManager = ImageManager(application)

    init {
        viewModelScope.launch{
          _elements.value = imageManager.getSavedImages()
        }
    }

    fun genElement() = viewModelScope.launch {
        try {
            val str = imageManager.genereImage()!!
            if (_elements.value?.find { it == str } == null)
                _elements.value = _elements.value.orEmpty() + str
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errors.value = e
        }
    }

    fun clearError(){
        errors.value = null
    }
}