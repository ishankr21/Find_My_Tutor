package com.ishankumar.findmytutor.features.tutorChatHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishankumar.findmytutor.dataClasses.ChattingHelper
import com.ishankumar.findmytutor.dataRepo.FirebaseRepo

class TutorChatHomeViewModel:ViewModel() {
    var mChattingHelperLiveData= MutableLiveData<ArrayList<ChattingHelper>>()

    fun getAllChats()
    {
        mChattingHelperLiveData=FirebaseRepo().getPeopleIHaveChatWith()
    }
}