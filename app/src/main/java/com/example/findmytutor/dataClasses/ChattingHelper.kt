package com.example.findmytutor.dataClasses

import com.google.firebase.Timestamp
import java.io.Serializable

data class ChattingHelper(
    var senderName:String="",
    var receiverName:String="",
    var senderId:String="",
    var receiverId:String="",
    var sendByStudent:Boolean=true,
    var senderImage:String="",
    var receiverImage:String="",
    var lastMessage:String="",
    var lastContact:Timestamp?=null
):Serializable
