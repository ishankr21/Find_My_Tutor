package com.example.findmytutor.features.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataRepo.FirebaseRepo

class SplashScreenViewModel:ViewModel() {


    var mExistingUserLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun  checkUserType() {
        mExistingUserLiveData = FirebaseRepo().checkUserType()
    }
}