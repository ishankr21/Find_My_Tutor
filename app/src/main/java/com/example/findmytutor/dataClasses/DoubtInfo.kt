package com.example.findmytutor.dataClasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@IgnoreExtraProperties
@Entity(tableName = "doubt_info")
data class DoubtInfo(
    @PrimaryKey(autoGenerate = true)
    var rowId:Int=0,
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
