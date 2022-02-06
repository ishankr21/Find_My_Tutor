package com.example.findmytutor.features.splashScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.features.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigationView()

        lifecycleScope.launch {
            delay(3000)

                view.findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)


        }
    }


}