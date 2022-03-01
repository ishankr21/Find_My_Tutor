package com.example.findmytutor.features.homeTutors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo
import com.google.firebase.auth.FirebaseAuth

class HomeTutorViewModel:ViewModel() {

    var mListOfStudent= MutableLiveData<ArrayList<Student>>()
    var getAllAcceptedStudents= MutableLiveData<ArrayList<String>>()
    var mTutorLiveData = MutableLiveData<Tutor>()

    fun getAllStudents(listOfStudentIDs:MutableList<String>)
    {
        mListOfStudent= FirebaseRepo().getAllStudents(listOfStudentIDs)
    }

    fun getAllAcceptedStudents()
    {
        getAllAcceptedStudents=FirebaseRepo().getListOfAllAcceptedStudents(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun getTutor()
    {
        mTutorLiveData=FirebaseRepo().getTutor()
    }

}