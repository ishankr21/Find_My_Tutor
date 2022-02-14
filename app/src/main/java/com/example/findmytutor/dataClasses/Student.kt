package com.example.findmytutor.dataClasses

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Student(
    var studentId:String="",
    var mobile:String="",
    var name:String="",
    var gender:String="",
    var emailId:String="",
    var parentName:String="",
    var age:Int=0,
    var profilePicturePath:String="",
    var studentClass:String="",
    var leastFavouriteSubject:String="",
    var schoolBoard:String="",
    var schoolName:String="",
    var tokenId:String="",
    var fcmTokens:ArrayList<String> = arrayListOf()
): Serializable
