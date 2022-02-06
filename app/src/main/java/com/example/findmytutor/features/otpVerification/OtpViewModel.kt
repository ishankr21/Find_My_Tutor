package com.example.findmytutor.features.otpVerification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataRepo.FirebaseRepo
import com.google.firebase.auth.PhoneAuthCredential

class OtpViewModel : ViewModel(){
    var isSignInSuccess = MutableLiveData<Boolean>()
    var mCreatedUserLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun signInWithPhone(authCredential: PhoneAuthCredential)
    {
        isSignInSuccess= FirebaseRepo().firebaseSignInWithPhone(authCredential)
    }


    fun createNewStudent(student: Student) {
        mCreatedUserLiveData = FirebaseRepo().createStudent(student)



    }
}