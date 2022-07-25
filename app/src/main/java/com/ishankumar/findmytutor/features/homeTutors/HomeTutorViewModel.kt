package com.ishankumar.findmytutor.features.homeTutors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.RequestTutor
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo
import com.google.firebase.auth.FirebaseAuth

class HomeTutorViewModel:ViewModel() {

    var mListOfStudent= MutableLiveData<ArrayList<Student>>()
    var getAllAcceptedStudents= MutableLiveData<ArrayList<RequestTutor>>()
    var mTutorLiveData = MutableLiveData<Tutor>()

    fun getAllStudents(listOfStudentIDs:MutableList<String>)
    {
        mListOfStudent= FirebaseRepo().getAllStudents(listOfStudentIDs)
    }

    fun getAllAcceptedStudents()
    {
        getAllAcceptedStudents=FirebaseRepo().getListOfAllAcceptedStudentsRequestTutor(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun getTutor()
    {
        mTutorLiveData=FirebaseRepo().getTutor()
    }

}