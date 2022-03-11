package com.example.findmytutor.dataClasses

import com.google.firebase.Timestamp


data class RatingInfo(
    var rating:Int=0,
    var feedback:String="",
    var ratedOn: Timestamp? =null,
    var ratedBy:String="",
    var ratedTo:String="",
    var ratedByName:String="",
    var ratedToName:String=""
)
