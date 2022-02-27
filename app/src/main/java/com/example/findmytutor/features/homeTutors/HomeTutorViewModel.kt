package com.example.findmytutor.features.homeTutors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo

class HomeTutorViewModel:ViewModel() {

    var mListOfStudent= MutableLiveData<ArrayList<Student>>()
    var getAllAcceptedStudents= MutableLiveData<ArrayList<String>>()

    fun getAllStudents(listOfStudentIDs:MutableList<String>)
    {
        mListOfStudent= FirebaseRepo().getAllStudents(listOfStudentIDs)
    }

    fun getAllAcceptedStudents()
    {
        getAllAcceptedStudents=FirebaseRepo().getListOfAllAcceptedStudents()
    }

}