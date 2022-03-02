package com.example.findmytutor.features.doubtsStudent

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo

import com.example.findmytutor.databinding.ItemStudentMyDoubtBinding
import com.example.findmytutor.databinding.ItemStudentViewOfSolutionsGivenByTutorBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentTutorSolutionsAdapter(
    var solutionsArray:ArrayList<SolutionInfo>,
    val context: Context,
    private val itemClickLister: OnSolutionClickListner
): RecyclerView.Adapter<StudentTutorSolutionsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemStudentViewOfSolutionsGivenByTutorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(solutionInfo: SolutionInfo) {


            binding.studentSeeTutorSolutionDoubtIdValue.text=solutionInfo.doubtId
            binding.studentSeeTutorSolutionSolvedByValue.text=solutionInfo.tutorName
            val millis=solutionInfo.solvedOn.toLong()*1000
            val dateInMilli = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
            val formattedDate = sdf.format(dateInMilli)

            binding.studentSeeTutorSolutionDateValue.text=formattedDate
            binding.root.setOnClickListener {
                itemClickLister.onItemClicked(solutionInfo)
            }


        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemStudentViewOfSolutionsGivenByTutorBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doubt=solutionsArray[position]
        holder.bindItems(doubt)
    }

    override fun getItemCount(): Int {
        return solutionsArray.size
    }
    interface OnSolutionClickListner {
        fun onItemClicked(solutionInfo: SolutionInfo)
    }



}