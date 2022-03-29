package com.example.findmytutor.features.ratingsReceived

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RatingInfo
import com.example.findmytutor.dataRepo.FirebaseRepo

class RatingsReceivedViewModel:ViewModel() {
    var mRatingsReceivedByMeLiveData= MutableLiveData<ArrayList<RatingInfo>>()
    var mExistingUserLiveData: MutableLiveData<Pair<Int,Boolean>> = MutableLiveData<Pair<Int,Boolean>>()

    fun  checkUserType() {
        mExistingUserLiveData = FirebaseRepo().checkUserType()
    }

    fun getRatingsReceived()
    {
        mRatingsReceivedByMeLiveData= FirebaseRepo().getRatingsReceivedByMe()
    }
}