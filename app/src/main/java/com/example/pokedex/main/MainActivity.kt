package com.example.pokedex.main

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.pokedex.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.ViewCompat.animate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) // Finish splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
    }

    private fun initNavigation() {
        // The NavController
        val navController = findNavController(R.id.navHostFragment)

        // Connect the navHostFragment with the BottomNavigationView.
        NavigationUI.setupWithNavController(navView, navController)

        // Connect the navHostFragment with the Toolbar.
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        appBarConfiguration.topLevelDestinations.add(R.id.dexF)
//        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNavigationBar(true)
                R.id.dexFragment -> showBottomNavigationBar(false)
//                R.id.ratedFragment -> showBottomNavigationBar(false)
            }
        }
        animate(navView).apply {
            interpolator = AccelerateInterpolator()
            alpha(1f)
            duration = 5000
            start()
        }
    }

    private fun showBottomNavigationBar(visible: Boolean) {
        when (visible) {
            true -> navView.visibility = View.VISIBLE
            false -> navView.visibility = View.GONE
        }
    }
}
