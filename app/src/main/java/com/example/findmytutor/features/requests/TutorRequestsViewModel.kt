package com.example.findmytutor.features.requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class TutorRequestsViewModel:ViewModel() {
    var mRequestsLiveData = MutableLiveData<ArrayList<RequestTutor>>()
    var studentLiveData= MutableLiveData<Student>()
    var approvalRequestSuccess= MutableLiveData<Boolean>()
    var disapprovalRequestSuccess= MutableLiveData<Boolean>()
    var mTutorLiveData=MutableLiveData<Tutor>()


    fun getAllRequests()
    {
        mRequestsLiveData=FirebaseRepo().getAllTutorRequests()
    }

    fun getStudentById(studentId:String)
    {
        studentLiveData=FirebaseRepo().getStudentById(studentId)
    }

    fun approveRequest(requestTutor: RequestTutor)
    {
        approvalRequestSuccess=FirebaseRepo().approveStudentRequest(requestTutor)
    }

    fun disapproveRequest(requestTutor: RequestTutor)
    {
        disapprovalRequestSuccess=FirebaseRepo().disapproveStudentRequest(requestTutor)

    }

    fun getTutor()
    {
        mTutorLiveData=FirebaseRepo().getTutor()
    }


}