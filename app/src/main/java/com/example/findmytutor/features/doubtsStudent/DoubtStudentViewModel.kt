package com.example.findmytutor.features.doubtsStudent

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo
import com.example.findmytutor.dataRepo.LocalDataRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoubtStudentViewModel:ViewModel() {

    var mStudentLiveData=MutableLiveData<Student>()
    var mDoubtStorageSuccess=MutableLiveData<Boolean>()
    var mAllStudentDoubts=MutableLiveData<ArrayList<DoubtInfo>>()
    var mMarkAsDoneSuccess=MutableLiveData<Boolean>()
    var mAllStudentSolutions=MutableLiveData<ArrayList<SolutionInfo>>()
    var mTutorData = MutableLiveData<Tutor>()
    var doubtArrayFromLocalDb:LiveData<MutableList<DoubtInfo>> = MutableLiveData()
    var filterList:LiveData<MutableList<DoubtInfo>> = MutableLiveData()



    fun getAllTutorId(tutorId:String)
    {
        mTutorData=FirebaseRepo().getTutorById(tutorId)
    }


    fun getStudentData()
    {
        mStudentLiveData=FirebaseRepo().getStudent()
    }

    fun storeStudentDoubt(imageUri: Uri?,doubtInfo: DoubtInfo)
    {
        mDoubtStorageSuccess=FirebaseRepo().storeDoubt(imageUri,doubtInfo)
    }

    fun getAllStudentDoubt(context: Context)
    {
        mAllStudentDoubts=FirebaseRepo().getAllDoubtOfCurrentStudent(context)
    }

    fun getAllTutorSolution()
    {
        mAllStudentSolutions=FirebaseRepo().getAllTutorSolutions()
    }

    fun markAsDone(doubtId:String)
    {
        mMarkAsDoneSuccess=FirebaseRepo().markDoubtAsDone(doubtId)
    }

    fun getLocalDbDoubts(context: Context)
    {
        doubtArrayFromLocalDb=LocalDataRepo(context).getAllDoubts()
    }

    fun getAllOpenDoubts(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getAllOpenDoubt()

    }

    fun getAllClosedDoubts(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getAllClosedDoubt()

    }
    fun getLatestFirst(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getLatestDoubtFirst()

    }
    fun getOldestFirst(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getOldestDoubtFirst()

    }
    fun getNoOfSolutionsHighToLow(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getNoOfSolutionsHighToLow()

    }
    fun getNoOfSolutionsLowToHigh(context: Context)= CoroutineScope(Dispatchers.IO).launch{
        filterList=LocalDataRepo(context).getNoOfSolutionsLowToHigh()

    }
}