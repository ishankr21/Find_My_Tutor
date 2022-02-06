package com.example.findmytutor.dataClasses

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Student(
    var mobile:String="",
    var name:String="",
    var gender:String="",
    var emailId:String="",
    var parentName:String="",
    var age:Int=0
): Serializable
