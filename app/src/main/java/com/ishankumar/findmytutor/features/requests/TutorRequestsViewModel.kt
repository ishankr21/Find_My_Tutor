package com.ishankumar.findmytutor.features.requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.RequestTutor
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

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