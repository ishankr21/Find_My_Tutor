package com.example.findmytutor.features.homeStudents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class HomeStudentViewModel:ViewModel() {

    var mListOfTutors= MutableLiveData<ArrayList<Tutor>>()
    var mStudentLiveData= MutableLiveData<Student>()

    fun getAllTutors()
    {
        mListOfTutors= FirebaseRepo().getAllTutor()
    }

    fun getCurrentStudent()
    {
        mStudentLiveData=FirebaseRepo().getStudent()
    }

}