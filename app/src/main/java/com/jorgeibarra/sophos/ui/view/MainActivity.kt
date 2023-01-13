package com.jorgeibarra.sophos.ui.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Sophos)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.activitiyToolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        when(this.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.activitiyToolbar.setTitleTextColor(resources.getColor(R.color.primaryLightColor))
                binding.activitiyToolbar.overflowIcon = getDrawable(R.drawable.ic_menu)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}

        }

        /*supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)*/


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.NavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }


}