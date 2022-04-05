package com.example.findmytutor.features.doubtsStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.databinding.FragmentDoubtsStudentBinding
import com.example.findmytutor.databinding.FragmentRegisterBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.register.RegisterStudentFragment
import com.example.findmytutor.features.register.RegisterTutorFragment
import com.example.findmytutor.utilities.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class DoubtsStudentFragment : Fragment() {

    private var _binding: FragmentDoubtsStudentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentDoubtsStudentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                      findNavController().navigate(R.id.action_doubtsStudentFragment_to_homeStudentsFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val viewPager2Adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2Adapter.addFragment(StudentAskDoubtFragment(), "Your Doubts")
        viewPager2Adapter.addFragment(StudentSeeAllSolutionsProvidedFragment(), "Solutions")

        binding.doubtStudentPageViewPager2.adapter = viewPager2Adapter

        binding.doubtStudentPageTabLayout.let {
            binding.doubtStudentPageViewPager2?.let { it1 ->
                TabLayoutMediator(
                    it, it1
                ) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "Your Doubts"
                        }
                        1 -> {
                            tab.text = "Solutions"
                        }

                    }
                }.attach()
            }
        }

        binding.doubtStudentBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_doubtsStudentFragment_to_homeStudentsFragment)
        }

    }
}