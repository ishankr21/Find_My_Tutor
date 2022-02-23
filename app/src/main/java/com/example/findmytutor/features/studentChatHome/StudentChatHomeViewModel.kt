package com.example.findmytutor.features.studentChatHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.dataRepo.FirebaseRepo

class StudentChatHomeViewModel:ViewModel() {
    var mChattingHelperLiveData= MutableLiveData<ArrayList<ChattingHelper>>()

    fun getAllChats()
    {
        mChattingHelperLiveData=FirebaseRepo().getPeopleIHaveChatWith()
    }
}