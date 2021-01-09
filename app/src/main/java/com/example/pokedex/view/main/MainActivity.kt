package com.example.pokedex.view.main

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat.animate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.pokedex.R
import com.example.pokedex.view.dex.PokeDexFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) // Finish splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
        initViews()
    }

    /**
     * Set's event listeners
     *
     */
    private fun initViews() {
        setListeners()
    }

    /**
     * Initiates navigation, appbar
     *
     */
    private fun initNavigation() {
        // The NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Connect the navHostFragment with the BottomNavigationView.
        NavigationUI.setupWithNavController(navView, navController)

        // Connect the navHostFragment with the Toolbar.
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration.topLevelDestinations.add(R.id.pokeDexFragment)
//        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            println("NavChange: $destination")
            when (destination.id) {
                R.id.pokeDexFragment -> {
                    showBackButton(false)
                    setActionBarTitle(getString(R.string.app_name))
                    showBottomNavigationBar(true)
                }
                    R.id.search -> {
                    showBackButton(false)
                    showBottomNavigationBar(true)
                }
                R.id.myPokemonFragment -> {
                    showBackButton(false)
                    setActionBarTitle(getString(R.string.menu_my_pokemon))
                    showBottomNavigationBar(true)
                }
                R.id.detailFragment -> {
                    showBackButton(true)
                    showBottomNavigationBar(false) }
            }
        }

        // Fade in bottom navigation
        displayBottomNav()
    }

    /**
     * Public function to alter ActionBar title
     *
     * @param title String title
     */
    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * Sets back button status
     *
     * @param show Show back button or not
     */
    private fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    /**
     * Catch back button click
     *
     * @return success
     */
    override fun onSupportNavigateUp(): Boolean {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.pokeDexFragment) {
            val bundle = bundleOf("withSearch" to false)
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_pokeDex, bundle)
        } else {
            onBackPressed()
        }
        return true
    }

    /**
     * Fades in the bottom nav bar
     *
     */
    private fun displayBottomNav() {
        animate(navView).apply {
            interpolator = AccelerateInterpolator()
            alpha(1f)
            duration = 2500
            start()
        }
    }

    /**
     * Shows or hides the button nav
     *
     * @param visible Visible or not param
     */
    private fun showBottomNavigationBar(visible: Boolean) {
        when (visible) {
            true -> navView.visibility = View.VISIBLE
            false -> navView.visibility = View.GONE
        }
    }

    /**
     * Sets all necessary event listeners on UI elements.
     *
     */
    private fun setListeners() {
        navView.menu.getItem(1).setOnMenuItemClickListener { onNavigateSearchButtonClick() }
    }

    private fun onNavigateSearchButtonClick(): Boolean {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.pokeDexFragment) {
            val pokeDexFragment = supportFragmentManager.primaryNavigationFragment!!
            .childFragmentManager.fragments.first() as PokeDexFragment
            pokeDexFragment.toggleSearchBar()
        } else {
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_pokeDex)
        }
        return true
    }
}
