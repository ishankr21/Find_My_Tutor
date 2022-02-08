package com.example.findmytutor.features.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentRegisterTutorBinding



class RegisterTutorFragment : Fragment() {
    private var _binding: FragmentRegisterTutorBinding? = null
    private val binding get() = _binding!!
    var spinnerArrayGender: ArrayList<String> = arrayListOf()

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
        binding.registerTutorButton.setOnClickListener {

            view.findNavController().navigate(R.id.action_registerFragment_to_verifyOtpFragment2)
        }

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
            if (binding.registerTutorPhoneEdittext.text.isNullOrEmpty()||binding.registerTutorAgeEdittext.text.isNullOrEmpty()||binding.registerTutorNameEdittext.text.isNullOrEmpty()
                || binding.registerTutorPincodeEdittext.text.isNullOrEmpty()||binding.spnSelectTutorGender.selectedItemPosition==0)
            {
                Toast.makeText(requireContext(),"Please fill all the details!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val tutor= Tutor(
                    name = binding.registerTutorNameEdittext.text.toString(),
                    mobile = "+91"+binding.registerTutorPhoneEdittext.text.toString(),
                    age = binding.registerTutorAgeEdittext.text.toString().toInt(),
                    gender = spinnerArrayGender[binding.spnSelectTutorGender.selectedItemPosition],
                    pincode = binding.registerTutorPincodeEdittext.text.toString()
                )
                val student=Student()
                val bundle=Bundle()
                bundle.putSerializable("student",student)
                bundle.putSerializable("tutor",tutor)
                bundle.putBoolean("isRegistration",true)
                view.findNavController().navigate(R.id.action_registerFragment_to_verifyOtpFragment2,bundle)

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

}
    