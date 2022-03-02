package com.example.findmytutor.dataClasses

import com.google.firebase.Timestamp
import java.io.Serializable

data class RequestTutor(

    var studentId:String="",
    var tutorId:String="",
    var timeOfRequest: Timestamp?=null,
    var timeOfAcceptance: Timestamp?=null,
    var isCompleted:Boolean=false,
    var isDeclined:Boolean=false,
    var studentName:String="",
    var tutorName:String="",
    var requestId:String="",
    var deleteByStudent:Boolean=false

    ):Serializable
