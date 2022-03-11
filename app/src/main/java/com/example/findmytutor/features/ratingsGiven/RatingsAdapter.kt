package com.example.findmytutor.features.ratingsGiven


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.RatingInfo
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.ItemRatingsBinding
import java.text.SimpleDateFormat

class RatingsAdapter(
    var ratingsArray:ArrayList<RatingInfo>,
    val context: Context,
    private val itemClickLister: RatingsAdapter.OnRatingClickedListner
): RecyclerView.Adapter<RatingsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRatingsBinding):RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(ratingInfo: RatingInfo) {
            val data=ratingInfo.ratedOn!!.toDate()
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")

            binding.ratingValue.text="Rating : "+ratingInfo.rating.toString()+"/5"
            binding.ratedByName.text="Rated To : "+ratingInfo.ratedToName
            binding.ratedOnDate.text="Rated On : "+sdf.format(data)
            binding.ratingViewDetails.setOnClickListener {
                itemClickLister.onItemClicked(ratingInfo)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRatingsBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rating=ratingsArray[position]
        holder.bindItems(rating)
    }

    override fun getItemCount(): Int {
        return ratingsArray.size
    }
    interface OnRatingClickedListner {
        fun onItemClicked(ratingInfo: RatingInfo)
    }
}
