package fr.audric.tp1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import androidx.fragment.app.activityViewModels
import android.content.Intent

import android.net.Uri


class DetailsFragment : Fragment(R.layout.details_fragment) {

    lateinit var _progressBar : ProgressBar
    lateinit var textView : TextView
    lateinit var imageView : ImageView
    lateinit var image : Image
    val stringsViewModel : StringsViewModel by activityViewModels()
    val navArgs:DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val elementPosition = navArgs.elementPosition

        image = (stringsViewModel.elements.value)?.get(elementPosition)!!
        textView = view.findViewById(R.id.textView)
        imageView = view.findViewById(R.id.imageView)

        _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val buttonSave = view.findViewById<Button>(R.id.button_save)
        buttonSave?.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default){
                stringsViewModel.imageManager.saveImage((stringsViewModel.elements.value)?.get(elementPosition)!!)
            }
        }
        val buttonShare: Button = view.findViewById(R.id.button_share)
        buttonShare.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, Uri.parse(stringsViewModel.imageManager.getImageUrl(image)))
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }
        coroutineDownloadImage(image)
    }

    private fun coroutineDownloadImage(image: Image) = viewLifecycleOwner.lifecycleScope.launch{
        _progressBar.visibility = View.VISIBLE
        var byteArray: ByteArray? = null
        do{
            byteArray = stringsViewModel.imageManager.getByteArray(image)
            delay(100)
        }while (byteArray == null)
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(bmp)
        _progressBar.visibility = View.INVISIBLE
    }

}