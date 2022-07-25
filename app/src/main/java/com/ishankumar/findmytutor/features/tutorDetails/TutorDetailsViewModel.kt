package com.ishankumar.findmytutor.features.tutorDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.RequestTutor
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.RatingInfo
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class TutorDetailsViewModel:ViewModel() {
    var mStudentLiveData: MutableLiveData<Student> = MutableLiveData()
    var requestsSent:MutableLiveData<Boolean> = MutableLiveData()
    var getAllAcceptedStudents= MutableLiveData<ArrayList<RequestTutor>>()
    var getAllAcceptedOrPendingStudents= MutableLiveData<ArrayList<String>>()
    var deleteSuccess= MutableLiveData<Boolean>()

    fun getStudent()
    {
        mStudentLiveData= FirebaseRepo().getStudent()
    }

    fun sendRequest(requestTutor: RequestTutor)
    {
        requestsSent=FirebaseRepo().sendTutorRequest(requestTutor)
    }

    fun updateTutorRatings(ratings:ArrayList<Int>,tutorId:String)
    {
        FirebaseRepo().updateRatingOfTutor(tutorId,ratings)
    }
    fun getAllAcceptedStudents(tutorId: String)
    {
        getAllAcceptedStudents=FirebaseRepo().getListOfAllAcceptedStudentsRequestTutor(tutorId)
    }

    fun getAllAcceptedOrPendingStudent(tutorId: String)
    {
        getAllAcceptedOrPendingStudents=FirebaseRepo().getListOfAllAcceptedOrRequestPendingStudents(tutorId)
    }

    fun deleteTutor(requestTutor: RequestTutor)
    {
        deleteSuccess=FirebaseRepo().studentDeleteTutor(requestTutor)
    }

    fun storeReview(ratingInfo: RatingInfo)
    {
        FirebaseRepo().storeReview(ratingInfo)
    }
}