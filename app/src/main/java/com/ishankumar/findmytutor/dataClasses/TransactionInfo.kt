package com.ishankumar.findmytutor.dataClasses

import com.google.firebase.Timestamp
import java.io.Serializable

data class TransactionInfo(

    var transactionId:String="",
    var paymentForMonth:String="",
    var tutorId:String="",
    var tutorName:String="",
    var studentId:String="",
    var studentName:String="",
    var amount:Double=0.0,
    var completedOn:Timestamp?=null
):Serializable
