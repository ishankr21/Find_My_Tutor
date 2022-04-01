package com.example.findmytutor.features.transactionHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.TransactionInfo
import com.example.findmytutor.databinding.FragmentTransactionHistoryBinding
import com.example.findmytutor.features.MainActivity
import com.google.firebase.auth.FirebaseAuth


class TransactionHistoryFragment : BaseFragment() {


    private var _binding: FragmentTransactionHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mTransactionHistoryViewModel: TransactionHistoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentTransactionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTransactionHistoryViewModel=
            ViewModelProvider(this)[TransactionHistoryViewModel::class.java]
        binding.transactionHistoryRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        showProgressDialog("Loading")
        mTransactionHistoryViewModel.checkUserType()
        mTransactionHistoryViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
        { userType ->
            if(userType.first==1)
            {
                mTransactionHistoryViewModel.studentTransactions()
                mTransactionHistoryViewModel.studentTransactionLiveData.observe(viewLifecycleOwner)
                {
                    binding.transactionHistoryRecyclerView.adapter=TransactionHistoryAdapter(it as ArrayList<TransactionInfo>,requireContext(),FirebaseAuth.getInstance().currentUser!!.uid)
                }
            }
            else
            {
                mTransactionHistoryViewModel.tutorTransactions()
                mTransactionHistoryViewModel.tutorTransactionLiveData.observe(viewLifecycleOwner)
                {
                    binding.transactionHistoryRecyclerView.adapter=TransactionHistoryAdapter(it as ArrayList<TransactionInfo>,requireContext(),FirebaseAuth.getInstance().currentUser!!.uid)

                }
            }

            dismissProgressDialog()
        }

    }
}