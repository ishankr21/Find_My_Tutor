package com.example.findmytutor.features.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo
import com.example.findmytutor.utilities.LocusLocation


class ProfileViewModel:ViewModel() {

    var mUserPuploaded: MutableLiveData<Boolean> = MutableLiveData()
    var mStudentLiveData:MutableLiveData<Student> = MutableLiveData()
    var mStudentDataUpdated:MutableLiveData<Boolean> = MutableLiveData()
    var mTutorLiveData:MutableLiveData<Tutor> = MutableLiveData()
    var mTutorDataUploaded:MutableLiveData<Boolean> = MutableLiveData()
    var addressLiveData=MutableLiveData<Pair<String,String>>()


    fun uploadPictureToFirebase(imageUri: Uri,collection:String) {
        mUserPuploaded =
           FirebaseRepo().uploadPictureToFirebase(imageUri,collection)
    }

    fun getStudent()
    {
        mStudentLiveData= FirebaseRepo().getStudent()
    }

    fun storeStudent(student: Student)
    {
        mStudentDataUpdated = FirebaseRepo().saveStudent(student)
    }

    fun getTutor()
    {
        mTutorLiveData= FirebaseRepo().getTutor()
    }

    fun storeTutor(tutor: Tutor)
    {
        mTutorDataUploaded = FirebaseRepo().saveTutor(tutor)
    }

    fun signOut()
    {
        FirebaseRepo().userSignOut()
    }

    fun getLocusCurrentLocation(context: Context) {
        addressLiveData = LocusLocation().getLatLongByLocus(context)
    }

}