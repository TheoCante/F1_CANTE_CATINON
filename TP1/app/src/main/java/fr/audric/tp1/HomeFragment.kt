package fr.audric.tp1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import android.widget.*
import androidx.core.app.Person.fromBundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.lang.Integer.min
import java.net.URL


class HomeFragment : Fragment(R.layout.home_fragment) {

    lateinit var _progressBar : ProgressBar
    lateinit var textView : TextView
    lateinit var imageView : ImageView
    val stringsViewModel : StringsViewModel by activityViewModels()
    val navArgs:HomeFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val elementPosition = navArgs.elementPosition

        Log.i("Position : ", "" + elementPosition)

        textView = view.findViewById(R.id.textView)
        imageView = view.findViewById(R.id.imageView)

        _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val button2 = view.findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_fragment2)
        }
        Log.i("INFO","Create")
        coroutineProgress(elementPosition)
    }
    fun coroutineProgress(position: Int) = viewLifecycleOwner.lifecycleScope.launch{
        _progressBar.visibility = View.VISIBLE

        val client = HttpClient(OkHttp)
        val v = stringsViewModel.getElements().value
        Log.i("array",""+v)
        val url = (stringsViewModel.getElements().value)?.get(position)
        val httpResponse: HttpResponse = client.get(url!!)
        val byteArray: ByteArray = httpResponse.receive()
        client.close()

        /*val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        imageView.setImageBitmap(bmp)*/

        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.width, imageView.height, false))

        _progressBar.visibility = View.INVISIBLE


    }

}