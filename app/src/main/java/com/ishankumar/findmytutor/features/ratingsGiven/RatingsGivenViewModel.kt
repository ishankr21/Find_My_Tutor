package com.ishankumar.findmytutor.features.ratingsGiven

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.RatingInfo
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class RatingsGivenViewModel:ViewModel() {
    var mRatingsGivenByMeLiveData= MutableLiveData<ArrayList<RatingInfo>>()
    var mExistingUserLiveData: MutableLiveData<Pair<Int,Boolean>> = MutableLiveData<Pair<Int,Boolean>>()

    fun  checkUserType() {
        mExistingUserLiveData = FirebaseRepo().checkUserType()
    }

    fun getRatingsGivenByMe()
    {
        mRatingsGivenByMeLiveData=FirebaseRepo().getRatingsGivenByMe()
    }
}