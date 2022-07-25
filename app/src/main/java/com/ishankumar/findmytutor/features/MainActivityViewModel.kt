package com.ishankumar.findmytutor.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class MainActivityViewModel:ViewModel() {

    var mExistingUserLiveData: MutableLiveData<Pair<Int,Boolean>> = MutableLiveData<Pair<Int,Boolean>>()

    fun  checkUserType() {
        mExistingUserLiveData = FirebaseRepo().checkUserType()
    }
    fun signOut()
    {
        FirebaseRepo().userSignOut()
    }
}