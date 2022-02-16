package com.example.findmytutor.dataClasses

import com.google.firebase.Timestamp

data class RequestTutor(

    var studentId:String="",
    var tutorId:String="",
    var timeOfRequest: Timestamp?=null,
    var isCompleted:Boolean=false,
    var isDeclined:Boolean=false,
    var studentName:String="",
    var tutorName:String="",
    var requestId:String=""

    )
