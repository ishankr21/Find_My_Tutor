package com.example.findmytutor.features.doubtsStudent

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.DoubtInfo

import com.example.findmytutor.databinding.ItemStudentMyDoubtBinding


class StudentMyDoubtAdapter(
    var doubtsArray:ArrayList<DoubtInfo>,
    val context: Context,
    private val itemClickLister: OnRequestClickListner
): RecyclerView.Adapter<StudentMyDoubtAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemStudentMyDoubtBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(doubtInfo: DoubtInfo) {
            binding.studentMyDoubtIdValue.text=doubtInfo.doubtId
            binding.studentMyDoubtNoOfSolutionsValue.text=doubtInfo.noOfSolutions.toString()

            if(doubtInfo.isClosed)
                binding.studentMyDoubtStatusValue.text="Closed"

            binding.studentMyDoubtDateValue.text=doubtInfo.createdOn

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
           ItemStudentMyDoubtBinding.inflate(LayoutInflater.from(context),parent,false)
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