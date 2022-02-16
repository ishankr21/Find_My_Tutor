package com.example.findmytutor.features.myTutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findmytutor.databinding.FragmentStudentMyTutorsBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.register.RegisterStudentFragment
import com.example.findmytutor.features.register.RegisterTutorFragment
import com.google.android.material.tabs.TabLayoutMediator


class StudentMyTutorsFragment : Fragment() {


    private var _binding: FragmentStudentMyTutorsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentStudentMyTutorsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager2Adapter = StudentMyTutorViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2Adapter.addFragment(StudentMyTutorAcceptedFragment(), "Accepted")
        viewPager2Adapter.addFragment(StudentMyTutorRejectedFragment(), "Rejected/Pending")

        binding.myTutorPageViewPager2.adapter = viewPager2Adapter

        binding.myTutorPageTabLayout.let {
            binding.myTutorPageViewPager2.let { it1 ->
                TabLayoutMediator(
                    it, it1
                ) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "Accepted"
                        }
                        1 -> {
                            tab.text = "Rejected/Pending"
                        }

                    }
                }.attach()
            }
        }
    }

}