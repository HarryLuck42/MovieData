package com.corp.luqman.movielisting.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        settingNavigation()
    }

    private fun settingNavigation(){
        val navController = findNavController(R.id.nav_movie_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.moviesFragment,
            R.id.movieNowPlayingFragment,
            R.id.movieUpcomingFragment,
            R.id.favoriteMoviesFragment
        ), binding.mainDrawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_movie_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}