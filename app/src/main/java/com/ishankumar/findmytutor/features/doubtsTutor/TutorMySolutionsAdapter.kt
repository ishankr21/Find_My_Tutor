package com.ishankumar.findmytutor.features.doubtsTutor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishankumar.findmytutor.dataClasses.SolutionInfo
import com.ishankumar.findmytutor.databinding.ItemSolutionsGivenByTutorBinding

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TutorMySolutionsAdapter(
    var solutionsArray:ArrayList<SolutionInfo>,
    val context: Context,
    private val itemClickLister: OnSolutionClickListner
): RecyclerView.Adapter<TutorMySolutionsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSolutionsGivenByTutorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(solutionInfo: SolutionInfo) {

            val millis=solutionInfo.solvedOn.toLong()*1000
            val dateInMilli = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
            val formattedDate = sdf.format(dateInMilli)

            binding.tutorMySolutionDateValue.text=formattedDate
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