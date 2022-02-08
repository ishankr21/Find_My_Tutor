package com.example.findmytutor.dataClasses

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Tutor(
    var mobile:String="",
    var name:String="",
    var gender:String="",
    var emailId:String="",
    var age:Int=0,
    var pincode:String="",
    var profilePicturePath:String=""
):Serializable
