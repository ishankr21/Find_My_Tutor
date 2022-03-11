package com.example.findmytutor.features.studentDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.RatingInfo
import com.example.findmytutor.dataRepo.FirebaseRepo

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