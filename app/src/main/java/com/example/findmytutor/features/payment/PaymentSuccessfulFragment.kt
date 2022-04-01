package com.example.findmytutor.features.payment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.databinding.FragmentPaymentSuccessfulBinding
import com.example.findmytutor.features.MainActivity
import java.util.*


class PaymentSuccessfulFragment : Fragment() {

    private var _binding: FragmentPaymentSuccessfulBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentPaymentSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val transactionId = bundle!!.getSerializable("approvalRefNo") as String
        val amount = bundle.getSerializable("amount") as String
        binding.txtTransactionId.text = "Transaction Id : $transactionId"
        binding.txtTransactionAmount.text= "Amount Paid : $amount"
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.txtTransactionDate.text= "Transaction date : $day / $month /$year"
        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_paymentSuccessfulFragment_to_homeStudentsFragment)

        }


    }

}