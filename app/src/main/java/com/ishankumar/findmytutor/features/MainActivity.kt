package com.ishankumar.findmytutor.features


import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.databinding.ActivityMainBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var mMainActivityViewModel: MainActivityViewModel
    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivityViewModel =
            ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toggle= ActionBarDrawerToggle(this,binding.drawerLayout,binding.activityMainToolBar,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(binding.activityMainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=""

        lockDrawer()


//         networkConnection= NetworkConnection(applicationContext)
//        networkConnection.observe(this, Observer { isConnected->
//            if(isConnected)
//            {
//                val snackbar = Snackbar.make(
//                        binding.navMainFragment,
//                        "Connected!",
//                        Snackbar.LENGTH_LONG
//                    )
//                snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.green))
//                snackbar.show()
//            }
//            else
//            {
//                val snackbar = Snackbar.make(
//                    binding.navMainFragment,
//                    "Not Connected! App will not work properly!",
//                    Snackbar.LENGTH_LONG
//                )
//                snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.just_red))
//                snackbar.show()
//            }
//        })
        
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)
       setBottomNavigationMenu(2)

        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
                    if(menuItem.itemId==R.id.profileTutorFragment||menuItem.itemId==R.id.profileStudentFragment)
                    {
                        val bundle=Bundle()
                        bundle.putSerializable("isProfileCompleted",true)
                        navController.navigate(menuItem.itemId,bundle)
                    }
                        else
                    navController.navigate(menuItem.itemId)
                    return@setOnItemSelectedListener true
                }


        if (intent.extras != null) {



            if (intent.extras!!["intentType"].toString() == "approvalIntent") {
                setBottomNavigationMenu(2)
                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.tutorRequestsFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "doubtCreatedIntent") {
                setBottomNavigationMenu(2)
                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.doubtsTutorFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "doubtSolvedIntent") {
                setBottomNavigationMenu(1)
                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.doubtsStudentFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "messageSentByStudent") {
                setBottomNavigationMenu(2)

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.tutorChatHomeFragment)
                navController.graph=graph


            }
            else  if (intent.extras!!["intentType"].toString() == "messageSentByTutor") {
                setBottomNavigationMenu(1)

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.studentChatHomeFragment)
                navController.graph=graph


            }


        }





        //handling navigation drawer clicks
        binding.navigationView.setNavigationItemSelectedListener {
            if(it.itemId==R.id.signOutT)
            {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/tutors")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                        mMainActivityViewModel.signOut()
                        navController.navigate(R.id.action_homeTutorsFragment_to_loginFragment)
                    }
                    .setNegativeButton("Cancel"){dialog,_->
                        dialog.cancel()

                    }
                    .show()

            }
            else if(it.itemId==R.id.signOut)
            {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to log out?")
                    .setPositiveButton("Yes") { _, _ ->

                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                        mMainActivityViewModel.signOut()
                        navController.navigate(R.id.action_homeStudentsFragment_to_loginFragment)
                    }
                    .setNegativeButton("Cancel"){dialog,_->
                        dialog.cancel()

                    }
                    .show()
            }
            else  if(it.itemId==R.id.profileTutorFragment||it.itemId==R.id.profileStudentFragment)
            {
                val bundle=Bundle()
                bundle.putSerializable("isProfileCompleted",true)
                navController.navigate(it.itemId,bundle)
            }
            else
            {
                navController.navigate(it.itemId)

            }
              true
        }


    }

    //setting visibility of the navigation drawer
    fun setNavigationDrawerVisible()
    {
        binding.activityMainToolBar.visibility=View.VISIBLE
    }
    fun setNavigationDrawerDisappear()
    {
        binding.activityMainToolBar.visibility=View.GONE
    }

    //setting visibility of bottom navigation
    fun setVisibleBottomNavigationView() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }



    fun
            setBottomNavigationMenu(it:Int)
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
    fun setNavigationDrawerMenu(it:Int)
    {

        if(it==2)
        {
            binding.navigationView.menu.clear()
            binding.navigationView.inflateMenu(R.menu.menu_navigation_drawer_tutor)


        }
        else
        {
            binding.navigationView.menu.clear()
            binding.navigationView.inflateMenu(R.menu.menu_navigation_drawer_student)

        }

    }
    fun lockDrawer()
    {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    fun unlockDrawer()
    {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    fun getHeader():View
    {
        return binding.navigationView.getHeaderView(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)

    }


}