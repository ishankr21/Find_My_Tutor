package com.ishankumar.findmytutor.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class LoginFragmentViewModel:ViewModel() {

    var mExistingUserLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun  checkUserExists(phone: String) {
        mExistingUserLiveData = FirebaseRepo().checkUserExists(phone)
    }

}