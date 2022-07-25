package com.ishankumar.findmytutor.features.payment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.databinding.FragmentPaymentFailedBinding
import com.ishankumar.findmytutor.features.MainActivity
import java.util.*


class PaymentFailedFragment : Fragment() {


    private var _binding: FragmentPaymentFailedBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentPaymentFailedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {

                findNavController().navigate(R.id.action_paymentFailedFragment_to_homeStudentsFragment)


            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val bundle = arguments
        val transactionId = bundle!!.getSerializable("approvalRefNo") as String
        val amount = bundle.getSerializable("amount") as String
        binding.txtTransactionId.text = "Transaction Id : $transactionId"
        binding.txtTransactionAmount.text= "Amount : $amount"
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.txtTransactionDate.text= "Transaction date : $day / $month /$year"

        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFailedFragment_to_homeStudentsFragment)

        }

    }
}