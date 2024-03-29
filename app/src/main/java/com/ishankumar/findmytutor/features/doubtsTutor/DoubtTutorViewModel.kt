package com.ishankumar.findmytutor.features.doubtsTutor


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.DoubtInfo
import com.ishankumar.findmytutor.dataClasses.SolutionInfo
import com.ishankumar.findmytutor.dataClasses.Tutor

import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class DoubtTutorViewModel: ViewModel() {


    var mAllDoubts = MutableLiveData<ArrayList<DoubtInfo>>()
    var mSolutionStorageSuccess = MutableLiveData<Boolean>()
    var mTutorLiveDetails = MutableLiveData<Tutor>()
    var mAllTutorSolutions=MutableLiveData<ArrayList<SolutionInfo>>()
    var doubtInfo=MutableLiveData<DoubtInfo>()


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

    fun getDoubt(doubtId:String)
    {
        doubtInfo=FirebaseRepo().getDoubt(doubtId)
    }
}