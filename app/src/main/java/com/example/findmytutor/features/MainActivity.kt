package com.example.findmytutor.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.findmytutor.R
import com.example.findmytutor.databinding.ActivityMainBinding
import com.example.findmytutor.features.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var mMainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivityViewModel =
            ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)
       setBottomNavigationMenu()

        bottomNavigationView.setOnItemSelectedListener{ menuItem ->

                    navController.navigate(menuItem.itemId)
                    return@setOnItemSelectedListener true
                }









    }

    fun setVisibleBottomNavigationView() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
    //to hide bottom navigation
    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }
    fun setBottomNavigationMenu()
    {
        mMainActivityViewModel.checkUserType()
        mMainActivityViewModel.mExistingUserLiveData.observe(this)
        {
            if(it==2)
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.menu_tutor_bottom_navigation)


            }
            else
            {
                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.menu_student_bottom_navigation)

            }
        }
    }

}