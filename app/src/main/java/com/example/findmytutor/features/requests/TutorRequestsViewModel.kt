package com.example.findmytutor.features.requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class TutorRequestsViewModel:ViewModel() {
    var mRequestsLiveData = MutableLiveData<ArrayList<RequestTutor>>()


    fun getAllRequests()
    {
        mRequestsLiveData=FirebaseRepo().getAllTutorRequests()
    }



}