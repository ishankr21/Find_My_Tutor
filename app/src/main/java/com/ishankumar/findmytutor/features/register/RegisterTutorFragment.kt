package com.ishankumar.findmytutor.features.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.databinding.FragmentRegisterTutorBinding
import java.util.regex.Pattern


class RegisterTutorFragment : BaseFragment() {
    private var _binding: FragmentRegisterTutorBinding? = null
    private val binding get() = _binding!!
    var spinnerArrayGender: ArrayList<String> = arrayListOf()
    private lateinit var mRegisterViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterTutorBinding.inflate(inflater, container, false)
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



        spinnerArrayGender.add("Select Gender")
        spinnerArrayGender.add("FEMALE")
        spinnerArrayGender.add("MALE")
        initSpinners()
        binding.spnSelectTutorGender.onItemSelectedListener =
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

        binding.registerTutorButton.setOnClickListener {
            var array= arrayListOf<String>("1111111111","2222222222","3333333333","44444444444","5555555555")
            if(array.contains(binding.registerTutorPhoneEdittext.text.toString())||isValidPhoneNumber(binding.registerTutorPhoneEdittext.text.toString())) {
                if (binding.registerTutorPhoneEdittext.text.isNullOrEmpty() || binding.registerTutorAgeEdittext.text.isNullOrEmpty() || binding.registerTutorNameEdittext.text.isNullOrEmpty()
                    || binding.spnSelectTutorGender.selectedItemPosition == 0||binding.registerTutorNameEdittext.text.trim().length==0
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the details!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(binding.registerTutorAgeEdittext.text.toString().toInt()>100 || binding.registerTutorAgeEdittext.text.toString().toInt()<18) {
                    Toast.makeText(
                        requireContext(),
                        "Age must be between 18 to 99",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {

                    mRegisterViewModel.checkUserExists("+91" + binding.registerTutorPhoneEdittext.text.toString())
                    mRegisterViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        if (it == 0) {
                            val tutor = Tutor(
                                name = binding.registerTutorNameEdittext.text.toString(),
                                mobile = "+91" + binding.registerTutorPhoneEdittext.text.toString(),
                                age = binding.registerTutorAgeEdittext.text.toString().toInt(),
                                gender = spinnerArrayGender[binding.spnSelectTutorGender.selectedItemPosition]

                            )
                            val student = Student()
                            val bundle = Bundle()
                            bundle.putSerializable("student", student)
                            bundle.putSerializable("tutor", tutor)
                            bundle.putBoolean("isRegistration", true)
                            view.findNavController().navigate(
                                R.id.action_registerFragment_to_verifyOtpFragment2,
                                bundle
                            )
                        } else {
                            showToast(requireContext(), "Phone number already registered!")
                        }
                    }
                }
            }
            else
            {
                showToast(requireContext(),"Incorrect Phone Number")
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

        binding.spnSelectTutorGender.adapter = arrayAdapterGender

    }
    private fun isValidPhoneNumber(number:String) : Boolean {
        val patterns =  Pattern.compile("[6-9][0-9]{9}")
        val matcher=patterns.matcher(number)
        return matcher.matches()
    }
}
    