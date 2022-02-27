package com.example.findmytutor.dataClasses



data class FilterSearchStudent(
    var subjectIndex:Int=0,
    var classIndex:Int=0,
    var ratingIndex:Int=0,
    var schoolBoardIndex:Int=0,
    var maxFees:Double=10000.0,
    var minFees:Double=0.0
)
