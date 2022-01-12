package fr.audric.tp1

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main){
    //val stringsViewModel : StringsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onStart() {
        super.onStart()
        Log.i("INFO","Start")
    }

    override fun onPause() {
        super.onPause()
        Log.i("INFO","Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("INFO","Stop")
    }

    override fun onResume() {
        super.onResume()
        Log.i("INFO","Resume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("INFO","Destroy")
    }



}

class StringsViewModel : ViewModel() {
    private val elements: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    init {
        elements.value = ArrayList<String>()
    }
    fun getElements(): LiveData<ArrayList<String>> {
        return elements
    }

    fun addElement(adapter : Fragment2.ItemAdapter) {
        val client = HttpClient(OkHttp)
        viewModelScope.launch {
            val httpResponse: HttpResponse = client.get("https://inspirobot.me/api?generate=true")
            val stringBody: String = httpResponse.receive()
            client.close()
            elements.value?.add(stringBody)
            adapter.notifyDataSetChanged()
        }
    }
}