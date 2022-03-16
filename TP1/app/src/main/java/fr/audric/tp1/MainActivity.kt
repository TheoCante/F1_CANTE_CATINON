package fr.audric.tp1

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

// L'activité principale
class MainActivity : AppCompatActivity(R.layout.activity_main){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Autorise l'activité à tourner
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

        // Récupère le navigateur de Fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Récupère le controlleur du navigateur
        val navController = navHostFragment.navController

        // Récupère le graph du controlleur du navigateur
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // Associe le navigateur à la ToolBar pour que la flèche de retour revienne bien au bon Fragment
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
    }
}