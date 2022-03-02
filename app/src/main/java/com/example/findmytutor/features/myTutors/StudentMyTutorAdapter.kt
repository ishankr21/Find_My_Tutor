package com.example.findmytutor.features.myTutors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.ItemRequestTutorBinding
import java.text.SimpleDateFormat

class StudentMyTutorAdapter(
    var requestsArray:ArrayList<RequestTutor>,
    val context:Context,
    private val itemClickLister: OnRequestClickListner
):RecyclerView.Adapter<StudentMyTutorAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:ItemRequestTutorBinding):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(request: RequestTutor) {
                binding.itemRequestTutorStudentName.text="Tutor Name : "+request.tutorName
            val date = request.timeOfRequest!!.toDate()

            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
            binding.itemRequestTutorStudentRequestDate.text="Requested on : "+simpleDateFormat.format(date)
            if(request.deleteByStudent)
            {
                binding.animStatus.setAnimation(R.raw.alert_icon_exclamation)
                binding.itemRequestTutorStudentRequestStatus.text =
                    "Status : Deleted by student"
            }
            else
            {
                if (request.isCompleted && request.isDeclined) {
                    binding.animStatus.setAnimation(R.raw.red_status)
                    binding.itemRequestTutorStudentRequestStatus.text =
                        "Status : Request was declined by the tutor"
                }
                else if(request.isCompleted && !request.isDeclined) {
                    binding.animStatus.setAnimation(R.raw.status)
                    binding.itemRequestTutorStudentRequestStatus.text = "Status : Request was accepted"
                }
                else {
                    binding.animStatus.setAnimation(R.raw.yellow_status)
                    binding.itemRequestTutorStudentRequestStatus.text =
                        "Status : Request approval pending"
                }
            }

            binding.root.setOnClickListener {
                itemClickLister.onItemClicked(request)
            }


        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentMyTutorAdapter.ViewHolder {
        return ViewHolder(
            ItemRequestTutorBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: StudentMyTutorAdapter.ViewHolder, position: Int) {
        val request=requestsArray[position]
        holder.bindItems(request)
    }

    override fun getItemCount(): Int {
        return requestsArray.size
    }
    interface OnRequestClickListner {
        fun onItemClicked(requestTutor: RequestTutor)
    }

}


