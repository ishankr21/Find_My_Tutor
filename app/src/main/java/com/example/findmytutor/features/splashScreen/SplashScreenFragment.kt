package com.example.findmytutor.features.splashScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {
    private lateinit var mSplashScreenViewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSplashScreenViewModel =
            ViewModelProvider(this)[SplashScreenViewModel::class.java]
        (activity as MainActivity).hideBottomNavigationView()

        lifecycleScope.launch {
            delay(3000)
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    mSplashScreenViewModel.checkUserType()
                    mSplashScreenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        if (it==2) {
                            (activity as MainActivity).setBottomNavigationMenu(it)
                            view.findNavController()
                                .navigate(R.id.action_splashScreenFragment_to_homeTutorsFragment)
                        }
                            else {
                            (activity as MainActivity).setBottomNavigationMenu(it)
                            view.findNavController()
                                .navigate(R.id.action_splashScreenFragment_to_homeStudentsFragment)

                            }
                        }
                }
                else
                view.findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)


        }
    }


}