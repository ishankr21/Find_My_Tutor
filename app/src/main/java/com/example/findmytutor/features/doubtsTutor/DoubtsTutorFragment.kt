package com.example.findmytutor.features.doubtsTutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.databinding.FragmentDoubtsTutorBinding
import com.example.findmytutor.databinding.FragmentRegisterBinding
import com.example.findmytutor.features.doubtsStudent.StudentAskDoubtFragment
import com.example.findmytutor.features.doubtsStudent.StudentSeeAllSolutionsProvidedFragment
import com.example.findmytutor.utilities.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class DoubtsTutorFragment : Fragment() {

    private var _binding: FragmentDoubtsTutorBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDoubtsTutorBinding.inflate(inflater, container, false)
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

                    view.findNavController().navigate(R.id.action_doubtsTutorFragment_to_homeTutorsFragment)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val viewPager2Adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2Adapter.addFragment(TutorsSeeAllDoubtsFragment(), "All Doubts")
        viewPager2Adapter.addFragment(TutorSeeAllSolutionGivenByHimFragment(), "Your Solutions")

        binding.doubtTutorPageViewPager2.adapter = viewPager2Adapter

        binding.doubtTutorPageTabLayout.let {
            binding.doubtTutorPageViewPager2?.let { it1 ->
                TabLayoutMediator(
                    it, it1
                ) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "All Doubts"
                        }
                        1 -> {
                            tab.text = "Your Solutions"
                        }

                    }
                }.attach()
            }
        }
        binding.doubtTutorBackButton.setOnClickListener {
            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {

                        view.findNavController().navigate(R.id.action_doubtsTutorFragment_to_homeTutorsFragment)

                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
        
    }
}