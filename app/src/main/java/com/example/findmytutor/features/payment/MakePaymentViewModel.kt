package com.example.findmytutor.features.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.TransactionInfo
import com.example.findmytutor.dataRepo.FirebaseRepo

class MakePaymentViewModel:ViewModel() {
    var transactionStored=MutableLiveData<Boolean>()

    fun storeTransaction(transactionInfo: TransactionInfo)
    {
        transactionStored=FirebaseRepo().storeTransaction(transactionInfo)
    }
}