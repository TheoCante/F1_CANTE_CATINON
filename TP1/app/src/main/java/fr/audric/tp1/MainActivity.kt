package fr.audric.tp1

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

// L'activite principale
class MainActivity : AppCompatActivity(R.layout.activity_main){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Autorise l'activite a tourner
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

        // Recupere le navigateur de Fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Recupere le controleur du navigateur
        val navController = navHostFragment.navController

        // Recupere le graph du controleur du navigateur
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // Rentre le neavigateur dans la toolBar pour que la fleche de retour revienne bien au bon Fragment
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
    }
}