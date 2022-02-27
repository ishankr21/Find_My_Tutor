package com.example.findmytutor.features.homeStudents

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.TutorItemLayoutBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.util.*
import kotlin.collections.ArrayList

class TutorAdapter(
    var items:ArrayList<Tutor>,
    var context: Context,
    private val itemClickListener: OnItemClickListener
):RecyclerView.Adapter<TutorAdapter.ViewHolder>(), Filterable {
    var completeTutorList:ArrayList<Tutor> = arrayListOf()
    init{
        completeTutorList.addAll(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TutorItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tutor = completeTutorList[position]
        holder.bindItems(tutor,itemClickListener)
    }


    override fun getItemCount(): Int {
        return completeTutorList.size
    }
    inner class ViewHolder(val binding: TutorItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

                @SuppressLint("SetTextI18n")
                fun bindItems(tutor: Tutor, clickListener: OnItemClickListener)
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
                    if(tutor.profilePicturePath!="")
                    {

                        Glide.with(itemView.context)
                            .load(tutor.profilePicturePath)
                            .placeholder(shimmerDrawable)
                            .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
                            .into(binding.imgTutor)
                    }
                    binding.tutorName.text = tutor.name
                    binding.tutorAge.text= "Age : ${tutor.age}"
                    if (tutor.preferredClass!="")
                    {
                        binding.tutorClass.text= "Preferred Class : "+tutor.preferredClass
                    }
                    if(tutor.tutorFavouriteSubject!="")
                        binding.tutorSubject.text= "Speciality in : "+tutor.tutorFavouriteSubject
                    if(tutor.rating.size!=0) {
                        var sum=0.0
                        for (i in tutor.rating)
                            sum+=i
                        val rating:Double=(sum/ (tutor.rating.size))
                        binding.tutorRating.text = "${rating.toString().subSequence(0,3)}/5"
                    }
                    binding.root.setOnClickListener{
                        clickListener.onItemClicked(tutor)
                    }
                }


    }
    override fun getFilter(): Filter {
        return object :Filter()
        {
            override fun performFiltering(charSequence: CharSequence): FilterResults {


                var filteredList:ArrayList<Tutor> = arrayListOf()
                if (charSequence.isEmpty()) {
                    filteredList=(items)
                } else {
                    val filterPattern =
                        charSequence.toString().lowercase(Locale.ENGLISH).trim { it <= ' ' }
                    for (tutor in items) {
                        if (tutor.name.lowercase(Locale.ENGLISH).contains(filterPattern)
                        ) {
                            filteredList.add(tutor)
                        }
                    }
                    completeTutorList=filteredList
                }
                val results = FilterResults()
                results.values = filteredList

                return results
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

                completeTutorList = filterResults?.values as ArrayList<Tutor>

                notifyDataSetChanged()
            }
        }
    }



    fun updateTutorList(tutorList:ArrayList<Tutor>) {
        completeTutorList.clear()
        completeTutorList.addAll(tutorList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(tutor: Tutor)
    }

}