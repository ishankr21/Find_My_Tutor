package com.ishankumar.findmytutor.features.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.TransactionInfo
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class MakePaymentViewModel:ViewModel() {
    var transactionStored=MutableLiveData<Boolean>()

    fun storeTransaction(transactionInfo: TransactionInfo)
    {
        transactionStored=FirebaseRepo().storeTransaction(transactionInfo)
    }
}