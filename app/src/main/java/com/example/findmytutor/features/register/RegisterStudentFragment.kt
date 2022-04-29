package com.example.findmytutor.features.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentRegisterStudentBinding
import java.util.regex.Pattern


class RegisterStudentFragment : BaseFragment() {
    private var _binding: FragmentRegisterStudentBinding? = null
    private val binding get() = _binding!!
    var spinnerArrayGender: ArrayList<String> = arrayListOf()
    private lateinit var mRegisterViewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterStudentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRegisterViewModel =
            ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.registerStudentButton.setOnClickListener {

            view.findNavController().navigate(R.id.action_registerFragment_to_verifyOtpFragment2)
        }

        spinnerArrayGender.add("Select Gender")
        spinnerArrayGender.add("FEMALE")
        spinnerArrayGender.add("MALE")
        initSpinners()
        binding.spnSelectStudentGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {

                }

            }

            binding.registerStudentButton.setOnClickListener {
                var array= arrayListOf<String>("1111111111","2222222222","3333333333","44444444444","5555555555")
                if(array.contains(binding.registerStudentPhoneEdittext.text.toString())||isValidPhoneNumber(binding.registerStudentPhoneEdittext.text.toString()))
                {
                    if (binding.registerStudentPhoneEdittext.text.isNullOrEmpty()||binding.registerStudentAgeEdittext.text.isNullOrEmpty()||binding.registerStudentNameEdittext.text.isNullOrEmpty()
                        || binding.registerStudentParentNameEdittext.text.isNullOrEmpty()||binding.spnSelectStudentGender.selectedItemPosition==0||binding.registerStudentNameEdittext.text.trim().length==0
                        ||binding.registerStudentParentNameEdittext.text.trim().length==0
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Please fill all the details!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if(binding.registerStudentAgeEdittext.text.toString().toInt()>100 || binding.registerStudentAgeEdittext.text.toString().toInt()<5) {
                        Toast.makeText(
                            requireContext(),
                            "Age must be between 6 to 99",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else
                    {
                        mRegisterViewModel.checkUserExists("+91"+binding.registerStudentPhoneEdittext.text.toString())
                        mRegisterViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                        {
                            if(it==0)
                            {
                                val student=Student(
                                    name = binding.registerStudentNameEdittext.text.toString(),
                                    mobile = "+91"+binding.registerStudentPhoneEdittext.text.toString(),
                                    age = binding.registerStudentAgeEdittext.text.toString().toInt(),
                                    gender = spinnerArrayGender[binding.spnSelectStudentGender.selectedItemPosition],
                                    parentName = binding.registerStudentParentNameEdittext.text.toString()
                                )
                                val tutor=Tutor()
                                val bundle=Bundle()
                                bundle.putSerializable("student",student)
                                bundle.putSerializable("tutor",tutor)
                                bundle.putBoolean("isRegistration",true)
                                view.findNavController().navigate(R.id.action_registerFragment_to_verifyOtpFragment2,bundle)
                            }
                            else
                            {
                                showToast(requireContext(),"Phone number already registered!")
                            }
                        }

                    }
                }
                else
                {
                    showToast(requireContext(),"Incorrect phone number! Please enter again")
                }

            }

    }


    private fun initSpinners() {

        val arrayAdapterGender = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArrayGender
        ) {

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = view as TextView
                item.run {
                    this.isSingleLine = false
                }

                return item
            }
        }

        binding.spnSelectStudentGender.adapter = arrayAdapterGender

    }

    private fun isValidPhoneNumber(number:String) : Boolean {
        val patterns =  Pattern.compile("[6-9][0-9]{9}")
        val matcher=patterns.matcher(number)
        return matcher.matches()
    }



}