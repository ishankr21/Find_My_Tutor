package com.ishankumar.findmytutor.dataClasses

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class SolutionInfo(

    var solutionId:String="",
    var doubtId:String="",
    var solutionImagePath:String="",
    var solvedOn:String="",
    var solutionDescription:String="",
    var tutorId:String="",
    var tutorName:String="",
    var studentId:String=""

):Serializable
