package com.skf.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.skf.weatherapp.R
import com.skf.weatherapp.databinding.ActivityMainBinding
import com.skf.weatherapp.utils.CURRENT_LOCATION
import com.skf.weatherapp.utils.SharedPref
import com.skf.weatherapp.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    lateinit var binding:ActivityMainBinding

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sharedViewModel.update.observe(this,{})
        setUpNavigationWithBottomNav()

        binding.layoutAppbar.tvSearch.setOnClickListener { showSearchView() }

        lifecycleScope.launchWhenResumed {
            if(SharedPref.getString(CURRENT_LOCATION).isEmpty()){
                showSearchView()
            }
        }
    }


    private fun showSearchView() {
        navController.navigate(R.id.searchFragment)
    }

    private fun setUpNavigationWithBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavMenu.setupWithNavController(navController)
    }

}