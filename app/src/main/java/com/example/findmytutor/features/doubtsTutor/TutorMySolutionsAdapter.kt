package com.example.findmytutor.features.doubtsTutor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.databinding.ItemSolutionsGivenByTutorBinding

import com.example.findmytutor.databinding.ItemStudentMyDoubtBinding
import com.example.findmytutor.databinding.ItemStudentViewOfSolutionsGivenByTutorBinding


class TutorMySolutionsAdapter(
    var solutionsArray:ArrayList<SolutionInfo>,
    val context: Context,
    private val itemClickLister: OnSolutionClickListner
): RecyclerView.Adapter<TutorMySolutionsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSolutionsGivenByTutorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(solutionInfo: SolutionInfo) {

            binding.tutorMySolutionDateValue.text=solutionInfo.solvedOn
            binding.tutorMySolutionDoubtIdValue.text=solutionInfo.doubtId
            binding.tutorMySolutionSolutionIdValue.text=solutionInfo.solutionId

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
            ItemSolutionsGivenByTutorBinding.inflate(LayoutInflater.from(context),parent,false)
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