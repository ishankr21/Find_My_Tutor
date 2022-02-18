package com.example.findmytutor.features.doubtsStudent

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataRepo.FirebaseRepo

class DoubtStudentViewModel:ViewModel() {

    var mStudentLiveData=MutableLiveData<Student>()
    var mDoubtStorageSuccess=MutableLiveData<Boolean>()
    var mAllStudentDoubts=MutableLiveData<ArrayList<DoubtInfo>>()
    var mMarkAsDoneSuccess=MutableLiveData<Boolean>()
    var mAllStudentSolutions=MutableLiveData<ArrayList<SolutionInfo>>()


    fun getStudentData()
    {
        mStudentLiveData=FirebaseRepo().getStudent()
    }

    fun storeStudentDoubt(imageUri: Uri?,doubtInfo: DoubtInfo)
    {
        mDoubtStorageSuccess=FirebaseRepo().storeDoubt(imageUri,doubtInfo)
    }

    fun getAllStudentDoubt()
    {
        mAllStudentDoubts=FirebaseRepo().getAllDoubtOfCurrentStudent()
    }

    fun getAllTutorSolution()
    {
        mAllStudentSolutions=FirebaseRepo().getAllTutorSolutions()
    }

    fun markAsDone(doubtId:String)
    {
        mMarkAsDoneSuccess=FirebaseRepo().markDoubtAsDone(doubtId)
    }
}