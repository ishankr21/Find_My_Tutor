package com.example.findmytutor.features

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

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
       setBottomNavigationMenu(2)

        bottomNavigationView.setOnItemSelectedListener{ menuItem ->

                    navController.navigate(menuItem.itemId)
                    return@setOnItemSelectedListener true
                }


        if (intent.extras != null) {



            if (intent.extras!!["intentType"].toString() == "approvalIntent") {

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.tutorRequestsFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "doubtCreatedIntent") {

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.doubtsTutorFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "doubtSolvedIntent") {

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.doubtsStudentFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "messageSentByStudent") {

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.tutorChatHomeFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "messageSentByTutor") {

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.studentChatHomeFragment)
                navController.graph=graph


            }


        }






    }

    fun setVisibleBottomNavigationView() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
    //to hide bottom navigation
    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }
    fun setBottomNavigationMenu(it:Int)
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