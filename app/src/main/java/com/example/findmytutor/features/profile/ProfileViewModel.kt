package com.example.findmytutor.features.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo


class ProfileViewModel:ViewModel() {

    var mUserPuploaded: MutableLiveData<Boolean> = MutableLiveData()
    var mStudentLiveData:MutableLiveData<Student> = MutableLiveData()
    var mStudentDataUpdated:MutableLiveData<Boolean> = MutableLiveData()
    var mTutorLiveData:MutableLiveData<Tutor> = MutableLiveData()
    var mTutorDataUploaded:MutableLiveData<Boolean> = MutableLiveData()


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
}