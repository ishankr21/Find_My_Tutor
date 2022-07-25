package com.ishankumar.findmytutor.features.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class RegisterViewModel: ViewModel() {

    var mExistingUserLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun  checkUserExists(phone: String) {
        mExistingUserLiveData = FirebaseRepo().checkUserExists(phone)
    }
}