package com.example.findmytutor.dataClasses

import java.io.Serializable

data class ChattingHelper(
    var senderName:String="",
    var receiverName:String="",
    var senderId:String="",
    var receiverId:String="",
    var sendByStudent:Boolean=true,
    var senderImage:String="",
    var receiverImage:String="",
    var lastMessage:String=""
):Serializable
