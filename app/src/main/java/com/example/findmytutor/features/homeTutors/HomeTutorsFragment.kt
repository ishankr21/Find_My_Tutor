package com.example.findmytutor.features.homeTutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.findmytutor.R
import com.example.findmytutor.features.MainActivity


class HomeTutorsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        return inflater.inflate(R.layout.fragment_homw_tutors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
    }
}