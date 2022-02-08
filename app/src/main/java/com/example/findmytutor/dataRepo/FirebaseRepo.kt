package com.example.findmytutor.dataRepo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepo: FirebaseMessagingService() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var COLLECTION_STUDENT:String = "Students"
    private var COLLECTION_TUTOR:String = "Tutor"
    private var mFirestore = FirebaseFirestore.getInstance()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    fun firebaseSignInWithPhone(credential: PhoneAuthCredential): MutableLiveData<String?> {

        val authenticatedUserLiveData = MutableLiveData<String?>()

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {

                    authenticatedUserLiveData.value = mAuth.currentUser!!.uid

            }
            .addOnFailureListener {
                    authenticatedUserLiveData.value = null
                    Log.d("ishan","${it.message}")

            }

        return authenticatedUserLiveData

    }

    fun createStudent(student: Student,userId:String): MutableLiveData<Boolean> {

        var mStudentCreatedSuccess= MutableLiveData<Boolean>()

        mFirestore.collection(COLLECTION_STUDENT).document(userId).set(student)
            .addOnSuccessListener {
                mStudentCreatedSuccess.value=true
            }
            .addOnFailureListener {
                mStudentCreatedSuccess.value=false
            }





        return mStudentCreatedSuccess

    }

    fun createTutor(tutor: Tutor,userId: String): MutableLiveData<Boolean> {
        var mTutorCreatedSuccess= MutableLiveData<Boolean>()

        mFirestore.collection(COLLECTION_TUTOR).document(userId).set(tutor)
            .addOnSuccessListener {
                mTutorCreatedSuccess.value=true
            }
            .addOnFailureListener {
                mTutorCreatedSuccess.value=false
            }





        return mTutorCreatedSuccess
    }

    fun checkUserExists(phone:String):MutableLiveData<Int>
    {
        val mCheckUserExists = MutableLiveData<Int>()

        mFirestore.collection(COLLECTION_STUDENT)
            .whereEqualTo("mobile", phone).limit(1L).get().addOnCompleteListener { it1 ->

                if (it1.isSuccessful) {
                    Log.d("ishan","$phone+student")
                    if(it1.result.isEmpty)
                    {
                        mFirestore.collection(COLLECTION_TUTOR)
                            .whereEqualTo("mobile", phone).limit(1L).get().addOnCompleteListener {it2->
                                if (it2.isSuccessful) {
                                    if(!it2.result.isEmpty)
                                    mCheckUserExists.value=2
                                    else
                                        mCheckUserExists.value=0
                                } else {
                                    Log.d("user check","fail")

                                }
                            }
                    }
                    else
                    mCheckUserExists.value=1
                } else {
                    Log.d("user check","fail")
                }
            }

        return mCheckUserExists
    }

    fun uploadPictureToFirebase(imageUri: Uri,collection:String): MutableLiveData<Boolean> {

        val userID = mAuth.currentUser!!.uid
        val storageReference =
            FirebaseStorage.getInstance().getReference("/ProfilePictures/$userID")
        val uploadedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl
                    .addOnSuccessListener {
                        mFirestore.collection(collection).document(userID)
                            .update("profilePicturePath", it.toString())
                            .addOnFailureListener {
                                Log.d("ishan","${it.message}")
                            }
                    }
            }
            .addOnProgressListener {
            }
            .addOnCompleteListener {
                uploadedMutableLiveData.value = true

            }
        return uploadedMutableLiveData

    }

    fun checkUserType(): MutableLiveData<Int> {

        val mCheckUserExists = MutableLiveData<Int>()

        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid)
           .get().addOnCompleteListener { it1 ->

                if (it1.isSuccessful) {

                    if(!it1.result.exists())
                    {

                        mCheckUserExists.value=2

                    }
                    else
                        mCheckUserExists.value=1
                } else {
                    mCheckUserExists.value=0
                }
            }

        return mCheckUserExists

    }

    fun getStudent(): MutableLiveData<Student> {
        val mStudentLiveData = MutableLiveData<Student>()
        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                mStudentLiveData.value=it.toObject(Student::class.java)
            }

        return mStudentLiveData
    }


}