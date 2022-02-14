package com.example.findmytutor.features.tutorDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataRepo.FirebaseRepo

class TutorDetailsViewModel:ViewModel() {
    var mStudentLiveData: MutableLiveData<Student> = MutableLiveData()
    var requestsSent:MutableLiveData<Boolean> = MutableLiveData()

    fun getStudent()
    {
        mStudentLiveData= FirebaseRepo().getStudent()
    }

    fun sendRequest(requestTutor: RequestTutor)
    {
        requestsSent=FirebaseRepo().sendTutorRequest(requestTutor)
    }

}