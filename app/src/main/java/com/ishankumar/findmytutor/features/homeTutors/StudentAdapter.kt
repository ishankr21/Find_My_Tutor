package com.ishankumar.findmytutor.features.homeTutors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ishankumar.findmytutor.dataClasses.Student
import com.ishankumar.findmytutor.dataClasses.Tutor
import com.ishankumar.findmytutor.databinding.ItemStudentBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import kotlin.math.roundToInt

class StudentAdapter(
var items:ArrayList<Student>,
var context: Context,
private val itemClickListener: StudentAdapter.OnItemClickListener,
val tutor:Tutor
): RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(student: Student, clickListener: StudentAdapter.OnItemClickListener)
        {
            val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.95f) //the alpha of the underlying children
                .setHighlightAlpha(0.8f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable()
            shimmerDrawable.setShimmer(shimmer)
            binding.txtStudentDistanceFromTutor.text = "Distance "+ (com.ishankumar.findmytutor.utilities.Utils.calculateDistanceBetweenStudentTutor(tutor.latitude,tutor.longitude,student.latitude,student.longitude)/1000.0).roundToInt().toString()+" km."
            if(student.profilePicturePath!="")
            {

                Glide.with(itemView.context)
                    .load(student.profilePicturePath)
                    .placeholder(shimmerDrawable)
                    .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
                    .into(binding.imgStudent)
            }
            binding.txtStudentName.text = student.name
            binding.txtStudentAge.text= "Age : ${student.age}"

                binding.txtStudentClass.text= "Class : "+student.studentClass



            binding.root.setOnClickListener{
                clickListener.onItemClicked(student)
            }
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {

        return ViewHolder(
            ItemStudentBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {



            val student = items[position]
            holder.bindItems(student,itemClickListener)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClicked(student: Student)
    }

}