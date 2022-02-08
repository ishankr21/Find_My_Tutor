package com.example.findmytutor.features.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataRepo.FirebaseRepo


class ProfileViewModel:ViewModel() {

    var mUserPuploaded: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var mStudentLiveData:MutableLiveData<Student> = MutableLiveData()

    fun uploadPictureToFirebase(imageUri: Uri,collection:String) {
        mUserPuploaded =
           FirebaseRepo().uploadPictureToFirebase(imageUri,collection)
    }

    fun getStudent()
    {
        mStudentLiveData= FirebaseRepo().getStudent()
    }

}