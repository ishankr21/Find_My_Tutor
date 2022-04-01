package com.example.findmytutor.features.transactionHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.TransactionInfo
import com.example.findmytutor.dataRepo.FirebaseRepo

class TransactionHistoryViewModel:ViewModel() {
    var studentTransactionLiveData=MutableLiveData<MutableList<TransactionInfo>>()
    var tutorTransactionLiveData=MutableLiveData<MutableList<TransactionInfo>>()
    var mExistingUserLiveData: MutableLiveData<Pair<Int,Boolean>> = MutableLiveData<Pair<Int,Boolean>>()

    fun  checkUserType() {
        mExistingUserLiveData = FirebaseRepo().checkUserType()
    }

    fun studentTransactions()
    {
        studentTransactionLiveData=FirebaseRepo().getAllTransactionsStudent()
    }

    fun tutorTransactions()
    {
        tutorTransactionLiveData=FirebaseRepo().getAllTransactionsTutor()
    }

}