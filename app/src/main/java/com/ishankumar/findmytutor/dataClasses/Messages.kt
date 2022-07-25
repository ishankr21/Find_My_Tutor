package com.ishankumar.findmytutor.dataClasses

import com.google.firebase.Timestamp

data class Messages(
    var senderId:String="",
    var receiverId:String="",
    var message:String="",
    var time:Timestamp?=null,
    var senderName:String="",
    var receiverName:String="",
    var messageId:String="",
    var sentByStudent:Boolean=true
)
