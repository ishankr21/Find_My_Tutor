package com.example.findmytutor.features.doubtsTutor


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.dataClasses.Tutor

import com.example.findmytutor.dataRepo.FirebaseRepo

class DoubtTutorViewModel: ViewModel() {


    var mAllDoubts = MutableLiveData<ArrayList<DoubtInfo>>()
    var mSolutionStorageSuccess = MutableLiveData<Boolean>()
    var mTutorLiveDetails = MutableLiveData<Tutor>()
    var mAllTutorSolutions=MutableLiveData<ArrayList<SolutionInfo>>()


    fun getAllDoubts() {
        mAllDoubts = FirebaseRepo().getAllDoubts()
    }

    fun storeTutorSolution(imageUri: Uri?, solutionInfo: SolutionInfo,update:Boolean)
    {
        mSolutionStorageSuccess=FirebaseRepo().storeSolution(imageUri,solutionInfo,update)
    }

    fun getTutorDetails()
    {
        mTutorLiveDetails=FirebaseRepo().getTutor()
    }
    fun getTutorSolutions()
    {
        mAllTutorSolutions=FirebaseRepo().getMySolutions()
    }
}