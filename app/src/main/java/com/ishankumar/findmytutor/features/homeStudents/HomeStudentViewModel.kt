package com.ishankumar.findmytutor.features.homeStudents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

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