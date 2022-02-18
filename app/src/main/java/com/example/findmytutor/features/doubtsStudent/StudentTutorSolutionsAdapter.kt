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


class StudentTutorSolutionsAdapter(
    var solutionsArray:ArrayList<SolutionInfo>,
    val context: Context,
    private val itemClickLister: OnSolutionClickListner
): RecyclerView.Adapter<StudentTutorSolutionsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemStudentViewOfSolutionsGivenByTutorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(solutionInfo: SolutionInfo) {

            binding.studentSeeTutorSolutionDateValue.text=solutionInfo.solvedOn
            binding.studentSeeTutorSolutionDoubtIdValue.text=solutionInfo.doubtId
            binding.studentSeeTutorSolutionSolvedByValue.text=solutionInfo.tutorName

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