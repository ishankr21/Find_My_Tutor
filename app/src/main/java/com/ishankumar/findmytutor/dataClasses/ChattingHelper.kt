package com.ishankumar.findmytutor.dataClasses

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ChattingHelper(
    var studentName:String="",
    var tutorName:String="",
    var studentId:String="",
    var tutorId:String="",
    var studentImage:String="",
    var tutorImage:String="",
    var lastMessage:String="",
    var lastContact:Timestamp?=null
):Serializable
