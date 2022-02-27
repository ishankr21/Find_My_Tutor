package com.example.findmytutor.dataClasses

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Tutor(
    var tutorId:String="",
    var mobile:String="",
    var name:String="",
    var gender:String="",
    var emailId:String="",
    var age:Int=0,
    var profilePicturePath:String="",
    var employmentStatus:String="",
    var preferredClass:String="",
    var desiredFees:Float=0.0f,
    var tutorFavouriteSubject:String="",
    var rating:ArrayList<Int> = arrayListOf(),
    var tokenId:String="",
    var fcmTokens:ArrayList<String> = arrayListOf(),
    var profileIsComplete:Boolean=false,
    var latitude:Double=0.0,
    var longitude:Double=0.0,
    var preferredSchoolBoard:String=""
):Serializable
