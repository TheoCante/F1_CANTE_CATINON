package fr.audric.tp1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.*

class HomeFragment : Fragment(R.layout.home_fragment) {

    lateinit var _progressBar : ProgressBar
    lateinit var textView : TextView
    lateinit var button : Button
    lateinit var input : EditText
    lateinit var ratingBar : RatingBar
    lateinit var seekBar : SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textView = view.findViewById(R.id.textView)
        button = view.findViewById(R.id.button)
        ratingBar = view.findViewById(R.id.ratingBar)
        input = view.findViewById(R.id.input)
        seekBar = view.findViewById(R.id.seekBar)

        val color = Color.RED

        _progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        button.setBackgroundColor(color)
        button.setOnClickListener {
            //button.isEnabled = false
            //_progressBar.visibility = View.VISIBLE
            coroutineProgress()
            //Toast.makeText(this@MainActivity, input.text.toString(), Toast.LENGTH_LONG).show()
        }
        val button2 = view.findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_fragment2)
        }
        Log.i("INFO","Create")
    }
    fun coroutineProgress() = viewLifecycleOwner.lifecycleScope.launch{
        _progressBar.visibility = View.VISIBLE
        button.isEnabled = false
        var n  = 20
        for (i in 1..n) {
            delay(100L)
            _progressBar.progress = i*100/n
            ratingBar.rating = (i.toFloat()*5/n.toFloat())
            seekBar.progress = i*20000/n
        }
        textView.text = input.text.toString()
        _progressBar.visibility = View.INVISIBLE
        button.isEnabled = true
    }

}