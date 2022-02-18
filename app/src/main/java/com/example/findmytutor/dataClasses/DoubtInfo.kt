package com.example.findmytutor.dataClasses

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@IgnoreExtraProperties

data class DoubtInfo(

    var doubtId: String ="",
    var createdOn:String="",
    var studentId:String="",
    var studentName:String="",
    var doubtTitle:String="",
    var doubtDescription:String="",
    var isClosed:Boolean=false,
    var doubtImagePath:String="",
    var noOfSolutions:Int=0

):Serializable
