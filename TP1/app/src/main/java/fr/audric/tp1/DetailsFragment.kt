package fr.audric.tp1

import android.R.id
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import androidx.fragment.app.activityViewModels
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import android.content.Intent

import android.R.id.message
import android.net.Uri


class DetailsFragment : Fragment(R.layout.details_fragment) {

    lateinit var _progressBar : ProgressBar
    lateinit var textView : TextView
    lateinit var imageView : ImageView
    lateinit var urlImage : String
    val stringsViewModel : StringsViewModel by activityViewModels()
    val navArgs:DetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val elementPosition = navArgs.elementPosition

        Log.i("Position : ", "" + elementPosition)
        urlImage = (stringsViewModel.elements.value)?.get(elementPosition).toString()
        textView = view.findViewById(R.id.textView)
        imageView = view.findViewById(R.id.imageView)

        _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val dbmanager = DbManager(activity as Context)

        val button2 = view.findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default){
                dbmanager.saveImage((stringsViewModel.elements.value)?.get(elementPosition)!!)
            }
        }
        val buttonShare: Button = view.findViewById(R.id.button_share)
        buttonShare.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, Uri.parse(urlImage))
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        Log.i("INFO","Create")
        coroutineDownloadImage(urlImage)
    }
    fun coroutineDownloadImage(urlImage: String) = viewLifecycleOwner.lifecycleScope.launch{
        _progressBar.visibility = View.VISIBLE
        val client = HttpClient(OkHttp)
        val v = stringsViewModel.elements.value
        Log.i("array",""+v)
        val httpResponse: HttpResponse = client.get(urlImage)
        val byteArray: ByteArray = httpResponse.receive()
        client.close()

        /*val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        imageView.setImageBitmap(bmp)*/

        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.width, imageView.height, false))

        _progressBar.visibility = View.INVISIBLE


    }

}