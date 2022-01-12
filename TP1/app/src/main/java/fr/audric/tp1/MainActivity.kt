package fr.audric.tp1

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity(R.layout.activity_main){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        /*val newFragment: Fragment = HomeFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()*/
        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val action = SpecifyAmountFragmentDirections.actionSpecifyAmountFragmentToConfirmationFragment()
        view.findNavController().navigate(action)*/

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

    fun addElement(str: String) {
        elements.value?.add(str)
    }

}