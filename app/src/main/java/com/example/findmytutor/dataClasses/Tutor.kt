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
    var pincode:String="",
    var profilePicturePath:String="",
    var employmentStatus:String="",
    var preferredClass:String="",
    var desiredFees:Float=0.0f,
    var tutorFavouriteSubject:String="",
    var rating:Int=0,
    var tokenId:String="",
    var fcmTokens:ArrayList<String> = arrayListOf(),
    var profileIsComplete:Boolean=false,
    var latitude:Double=0.0,
    var longitude:Double=0.0
):Serializable
