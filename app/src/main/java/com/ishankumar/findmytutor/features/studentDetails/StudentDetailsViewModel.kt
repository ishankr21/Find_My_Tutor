package com.ishankumar.findmytutor.features.studentDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.RequestTutor
import com.ishankumar.findmytutor.dataClasses.RatingInfo
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class StudentDetailsViewModel: ViewModel() {
    var disapprovalRequestSuccess= MutableLiveData<Boolean>()

    fun disapproveRequest(requestTutor: RequestTutor)
    {
        disapprovalRequestSuccess= FirebaseRepo().disapproveStudentRequest(requestTutor)

    }
    fun storeReview(ratingInfo: RatingInfo)
    {
        FirebaseRepo().storeReview(ratingInfo)
    }
}