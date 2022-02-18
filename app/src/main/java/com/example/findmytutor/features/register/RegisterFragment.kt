package com.example.findmytutor.features.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.databinding.FragmentRegisterBinding
import com.example.findmytutor.utilities.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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

                    view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        binding.registerBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val viewPager2Adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2Adapter.addFragment(RegisterStudentFragment(), "Student")
        viewPager2Adapter.addFragment(RegisterTutorFragment(), "Tutor")

        binding.registerPageViewPager2.adapter = viewPager2Adapter

        binding.registerPageTabLayout.let {
            binding.registerPageViewPager2?.let { it1 ->
                TabLayoutMediator(
                    it, it1
                ) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "Student"
                        }
                        1 -> {
                            tab.text = "Tutor"
                        }

                    }
                }.attach()
            }
        }



    }


}