package com.example.findmytutor.features.requests

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.ItemRequestTutorBinding
import java.text.SimpleDateFormat

class TutorRequestsAdapter(
    var requestsArray:ArrayList<RequestTutor>,
    val context:Context,
    private val itemClickLister: OnRequestClickListner
):RecyclerView.Adapter<TutorRequestsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:ItemRequestTutorBinding):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bindItems(request: RequestTutor) {
                binding.itemRequestTutorStudentName.text="Student Name : "+request.studentName
            val date = request.timeOfRequest!!.toDate()

            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
            binding.itemRequestTutorStudentRequestDate.text="Requested on : "+simpleDateFormat.format(date)
            if(request.deleteByStudent)
            {
                binding.animStatus.setAnimation(R.raw.alert_icon_exclamation)
                binding.itemRequestTutorStudentRequestStatus.text =
                    "Status : Deleted by student"
                binding.root.setOnClickListener {
                    Toast.makeText(context,"This request was deleted by the student",Toast.LENGTH_LONG).show()
                }
            }
            else {
                if (request.isCompleted && request.isDeclined) {
                    binding.animStatus.setAnimation(R.raw.red_status)
                    binding.itemRequestTutorStudentRequestStatus.text =
                        "Status : Request was declined by you"
                } else if (request.isCompleted && !request.isDeclined) {
                    binding.animStatus.setAnimation(R.raw.status)
                    binding.itemRequestTutorStudentRequestStatus.text =
                        "Status : Request was accepted by you"
                } else {
                    binding.animStatus.setAnimation(R.raw.yellow_status)
                    binding.itemRequestTutorStudentRequestStatus.text =
                        "Status : Request approval pending"
                }
                binding.root.setOnClickListener {
                    itemClickLister.onItemClicked(request)
                }
            }

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TutorRequestsAdapter.ViewHolder {
        return ViewHolder(
            ItemRequestTutorBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TutorRequestsAdapter.ViewHolder, position: Int) {
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


