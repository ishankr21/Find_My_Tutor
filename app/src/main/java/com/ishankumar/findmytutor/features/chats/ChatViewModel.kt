package com.ishankumar.findmytutor.features.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.ChattingHelper
import com.ishankumar.findmytutor.dataClasses.Messages
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class ChatViewModel:ViewModel() {

    var allMessages=MutableLiveData<ArrayList<Messages>>()
    fun sendMessage(messages: Messages,chattingHelper: ChattingHelper)
    {
        FirebaseRepo().sendMessage(messages,chattingHelper)
    }

    fun getAllMessages(chatId:String)
    {
        allMessages=FirebaseRepo().getMessages(chatId)
    }
}