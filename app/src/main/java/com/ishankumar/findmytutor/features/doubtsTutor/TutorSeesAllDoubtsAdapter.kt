package com.ishankumar.findmytutor.features.doubtsTutor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishankumar.findmytutor.dataClasses.DoubtInfo
import com.ishankumar.findmytutor.databinding.ItemTutorSeeStudentDoubtsBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TutorSeesAllDoubtsAdapter(
    var doubtsArray:ArrayList<DoubtInfo>,
    val context: Context,
    private val itemClickLister: OnRequestClickListner
): RecyclerView.Adapter<TutorSeesAllDoubtsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTutorSeeStudentDoubtsBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(doubtInfo: DoubtInfo) {
            binding.tutorSeesStudentAllDoubtIdValue.text=doubtInfo.doubtId
            binding.tutorSeesStudentAllDoubtByValue.text=doubtInfo.studentName



            val millis=doubtInfo.createdOn.toLong()*1000
            val dateInMilli = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
            val formattedDate = sdf.format(dateInMilli)

            binding.tutorSeesStudentAllDoubtDateValue.text=formattedDate

            binding.root.setOnClickListener {
                itemClickLister.onItemClicked(doubtInfo)
            }


        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemTutorSeeStudentDoubtsBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doubt=doubtsArray[position]
        holder.bindItems(doubt)
    }

    override fun getItemCount(): Int {
        return doubtsArray.size
    }
    interface OnRequestClickListner {
        fun onItemClicked(doubtInfo: DoubtInfo)
    }



}