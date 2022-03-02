package com.example.findmytutor.features.doubtsStudent

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.databinding.ItemStudentMyDoubtBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentMyDoubtAdapter(
    private var doubtsArray:ArrayList<DoubtInfo>,
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
            else
                binding.studentMyDoubtStatusValue.text="Active"
            val millis=doubtInfo.createdOn.toLong()*1000
            val dateInMilli = Date(millis)
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
            val formattedDate = sdf.format(dateInMilli)

            binding.studentMyDoubtDateValue.text=formattedDate

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
    fun updateDoubtsList(listOfDoubts:MutableList<DoubtInfo>)
    {
        Log.d("doubt","from adapter ${listOfDoubts.size}")
        doubtsArray.clear()
        doubtsArray.addAll(listOfDoubts)
        notifyDataSetChanged()
    }



}