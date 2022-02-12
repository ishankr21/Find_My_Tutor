package com.example.findmytutor.features.homeStudents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class HomeStudentViewModel:ViewModel() {

    var mListOfTutors= MutableLiveData<ArrayList<Tutor>>()

    fun getAllTutors()
    {
        mListOfTutors= FirebaseRepo().getAllTutor()
    }

}