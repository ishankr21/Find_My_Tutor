package com.example.findmytutor.features.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentLoginBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Utils
import java.util.regex.Pattern


class LoginFragment :BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mLoginViewModel: LoginFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        (activity as MainActivity).hideBottomNavigationView()

        mLoginViewModel = ViewModelProvider(this)[LoginFragmentViewModel::class.java]


        binding.signUpTextviewButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.loginButton.setOnClickListener {




                Utils.hideKeyboard(requireActivity())
                binding.loginProgressBarLoadingAnimation.visibility=View.VISIBLE

            var array= arrayListOf<String>("1111111111","2222222222","3333333333","4444444444","5555555555")
            if(array.contains(binding.loginPhoneEdittext.text.toString())||isValidPhoneNumber(binding.loginPhoneEdittext.text.toString())) {
                val phone = "+91" + binding.loginPhoneEdittext.text.toString().trim()
                mLoginViewModel.checkUserExists(phone)
                mLoginViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                {
                    binding.loginProgressBarLoadingAnimation.visibility = View.GONE
                    when (it) {
                        0 -> {
                            showToast(requireContext(),"Sorry user does not exist, please register")
                        }
                        1 -> {
                            val student=Student(mobile = phone)
                            val tutor= Tutor()
                            val bundle=Bundle()
                            bundle.putSerializable("student",student)
                            bundle.putSerializable("tutor",tutor)
                            bundle.putBoolean("isRegistration",false)

                            view.findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment,bundle)

                        }
                        else -> {
                            val student=Student()
                            val tutor= Tutor(mobile = phone)
                            val bundle=Bundle()
                            bundle.putSerializable("student",student)
                            bundle.putSerializable("tutor",tutor)
                            bundle.putBoolean("isRegistration",false)

                            view.findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment,bundle)
                        }
                    }

                }






            }
            else
            {
                showToast(requireContext(),"Incorrect phone number! Please enter again")
                binding.loginProgressBarLoadingAnimation.visibility= View.GONE
            }



            }

    }

    private fun isValidPhoneNumber(number:String) : Boolean {
        val patterns =  Pattern.compile("[6-9][0-9]{9}")
        val matcher=patterns.matcher(number)
        return matcher.matches()
    }

}