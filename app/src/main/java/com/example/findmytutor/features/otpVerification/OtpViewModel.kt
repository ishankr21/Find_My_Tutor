package com.example.findmytutor.features.otpVerification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataRepo.FirebaseRepo
import com.google.firebase.auth.PhoneAuthCredential

class OtpViewModel : ViewModel(){
    var isSignInSuccess = MutableLiveData<String?>()
    var mCreatedUserLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var mCreatedTutorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var tutorData:MutableLiveData<Tutor> = MutableLiveData()
    var studentData:MutableLiveData<Student> = MutableLiveData()


    fun signInWithPhone(authCredential: PhoneAuthCredential)
    {
        isSignInSuccess= FirebaseRepo().firebaseSignInWithPhone(authCredential)
    }


    fun createNewStudent(student: Student,userID:String) {
        mCreatedUserLiveData = FirebaseRepo().createStudent(student,userID)



    }

    fun createNewTutor(tutor: Tutor,userID: String) {
        mCreatedTutorLiveData = FirebaseRepo().createTutor(tutor,userID)



    }

    fun getTutorWithMobileNumber(mobile:String)
    {
        tutorData=FirebaseRepo().getTutorByMobileNumber(mobile)
    }

    fun updateFcmListTutor(tutor: Tutor)
    {
        FirebaseRepo().updateFcmTokenListOfTutor(tutor)
    }
    fun updateFcmListStudent(student: Student)
    {
        FirebaseRepo().updateFcmTokenListOfStudent(student)
    }

    fun getStudentWithMobileNumber(mobile:String)
    {
        studentData=FirebaseRepo().getStudentByMobileNumber(mobile)
    }
}