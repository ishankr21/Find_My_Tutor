package com.example.findmytutor.features.myTutors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class StudentMyTutorViewModel: ViewModel() {

    var mAcceptedRequestsLiveData = MutableLiveData<ArrayList<RequestTutor>>()
    var mRejectedRequestsLiveData = MutableLiveData<ArrayList<RequestTutor>>()
    var mTutorData = MutableLiveData<Tutor>()

    fun getAllAcceptedRequests()
    {
        mAcceptedRequestsLiveData=FirebaseRepo().getAllAcceptedStudentRequests()
    }

    fun getAllRejectedRequests()
    {
        mRejectedRequestsLiveData=FirebaseRepo().getAllRejectedStudentRequests()
    }

    fun getAllTutorId(tutorId:String)
    {
        mTutorData=FirebaseRepo().getTutorById(tutorId)
    }
}