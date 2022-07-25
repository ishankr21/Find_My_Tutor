package com.ishankumar.findmytutor.features.payment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.TransactionInfo
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.databinding.FragmentMakePaymentBinding
import com.ishankumar.findmytutor.features.MainActivity
import com.google.firebase.Timestamp
import java.util.*


class MakePaymentFragment : BaseFragment() {
    private var _binding: FragmentMakePaymentBinding? = null
    private val binding get() = _binding!!
    private var tutor= Tutor()
    private var student= Student()
    private var spinnerArrayMonth: Array<String> = arrayOf()
    private val UPI_PAYMENT = 0
    private lateinit var mMakePaymentViewModel: MakePaymentViewModel
    var transactionInfo=TransactionInfo()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        spinnerArrayMonth= resources.getStringArray(R.array.months)
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentMakePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMakePaymentViewModel = ViewModelProvider(this)[MakePaymentViewModel::class.java]
        val bundle = arguments
        tutor = bundle!!.getSerializable("tutor") as Tutor
        student = bundle.getSerializable("student") as Student



        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_makePaymentFragment_to_tutorDetailsFragment,bundle)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.makePaymentBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_makePaymentFragment_to_tutorDetailsFragment,bundle)
        }



        binding.makePaymentTutorName.text=tutor.name
        binding.makePaymentTutorFees.setText(tutor.desiredFees.toString())
        initSpinners()
        val month=getMonth(Calendar.getInstance().time)+1
        binding.spnSelectFeesMonth.setSelection(month)
        if(tutor.upiId=="")
        {
            binding.makePaymentButton.isEnabled=false
            binding.makePaymentButton.text="UPI ID NOT ADDED PLEASE PAY BY CASH"
        }
        binding.makePaymentButton.setOnClickListener {
            if(!binding.makePaymentTutorFees.text.isNullOrEmpty() && binding.spnSelectFeesMonth.selectedItemPosition!=0)
            {
                 transactionInfo=TransactionInfo(
                    transactionId = "FMT"+System.currentTimeMillis().toString().takeLast(5)+student.studentId.takeLast(3)+tutor.tutorId.takeLast(3),
                    paymentForMonth = binding.spnSelectFeesMonth.selectedItem.toString(),
                    tutorId = tutor.tutorId,
                    tutorName = tutor.name,
                    studentId = student.studentId,
                    studentName = student.name,
                    amount = binding.makePaymentTutorFees.text.toString().toDouble()

                )
                val content = tutor.upiId
                val parts = content.split("&").toMutableList()
                parts[0] = parts[0].drop(13)
                var mc = "0000"

                for (i in parts) {
                    if (i[0] == 'm' && i[1] == 'c') {
                        mc = i.drop(3)
                    }
                }

                payUsingUPI(parts[0], "Vishal", "Payment from find my tutor", "1.0" ,mc)


            }
            else
                showToast(requireContext(),"Please enter al the details!")

        }


    }
    private fun getMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH)
    }
    private fun initSpinners() {

        val arrayAdapterMonth = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArrayMonth
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

        binding.spnSelectFeesMonth.adapter = arrayAdapterMonth



    }
    private fun payUsingUPI(upiId: String, name: String, note: String, amount: String,mc:String) {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("mc", mc)
            .appendQueryParameter("mode", "02")
            .appendQueryParameter("tr", "FMT"+System.currentTimeMillis())
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(requireContext().packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                requireContext(),
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    dataList.add(trxt!!)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d(
                    "UPI",
                    "onActivityResult: " + "Return data is null"
                ) //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {

        var str: String? = data[0]
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
        var paymentCancel = ""
        if (str == null) str = "discard"
        var status = ""
        var approvalRefNo = ""
        val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in response.indices) {
            val equalStr =
                response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (equalStr.size >= 2) {
                if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
                    status = equalStr[1].lowercase(Locale.getDefault())
                } else if (equalStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
                        Locale.getDefault()
                    ) || equalStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(Locale.getDefault())
                ) {
                    approvalRefNo = equalStr[1]
                }
            } else {
                paymentCancel = "Payment cancelled by user."
            }
        }

        if (status == "success") {

                transactionInfo.completedOn= Timestamp.now()
                val b=Bundle()
                b.putString("approvalRefNo",transactionInfo.transactionId)
                b.putString("amount",binding.makePaymentTutorFees.text.toString())
                mMakePaymentViewModel.storeTransaction(transactionInfo)
                mMakePaymentViewModel.transactionStored.observe(viewLifecycleOwner)
                {
                    if(it)
                        findNavController().navigate(R.id.action_makePaymentFragment_to_paymentSuccessfulFragment,b)
                }




        } else if ("Payment cancelled by user." == paymentCancel) {
            Toast.makeText(requireContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT)
                .show()
        } else {

            val b=Bundle()
            b.putString("approvalRefNo",transactionInfo.transactionId)
            b.putString("amount",binding.makePaymentTutorFees.text.toString())
            findNavController().navigate(R.id.action_makePaymentFragment_to_paymentFailedFragment,b)
            Toast.makeText(
                requireContext(),
                "Transaction failed.Please try again",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}