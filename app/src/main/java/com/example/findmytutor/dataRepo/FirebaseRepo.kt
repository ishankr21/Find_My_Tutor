package com.example.findmytutor.dataRepo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.findmytutor.dataClasses.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseRepo: FirebaseMessagingService() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var COLLECTION_STUDENT:String = "Students"
    private var mFirestore = FirebaseFirestore.getInstance()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    fun firebaseSignInWithPhone(credential: PhoneAuthCredential): MutableLiveData<Boolean> {

        val authenticatedUserLiveData = MutableLiveData<Boolean>()

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {

                    authenticatedUserLiveData.value = true

            }
            .addOnFailureListener {
                    authenticatedUserLiveData.value = false
                    Log.d("ishan","${it.message}")

            }

        return authenticatedUserLiveData

    }

    fun createStudent(student: Student): MutableLiveData<Boolean> {

        var mStudentCreatedSuccess= MutableLiveData<Boolean>()

        mFirestore.collection(COLLECTION_STUDENT).add(student)
            .addOnSuccessListener {
                mStudentCreatedSuccess.value=true
            }
            .addOnFailureListener {
                mStudentCreatedSuccess.value=false
            }





        return mStudentCreatedSuccess

    }


}