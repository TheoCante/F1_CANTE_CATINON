package fr.audric.tp1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class StringsViewModel(application: Application) : AndroidViewModel(application) {
    private val _elements: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val elements: LiveData<List<String>> = _elements

    fun addElement() {
        val client = HttpClient(OkHttp)
        viewModelScope.launch {
            val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
            val stringBody: String = httpResponse.receive()
            client.close()
            _elements.value = _elements.value.orEmpty() + stringBody
        }
    }

    fun addElement(str : String) {
        if(_elements.value?.find { it == str } == null)
            _elements.value = _elements.value.orEmpty() + str

    }
}