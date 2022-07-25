package com.ishankumar.findmytutor.features.ratingsReceived


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishankumar.findmytutor.dataClasses.RatingInfo
import com.ishankumar.findmytutor.databinding.ItemRatingsBinding
import java.text.SimpleDateFormat

class RatingsReceivedAdapter(
    var ratingsArray:ArrayList<RatingInfo>,
    val context: Context,
    private val itemClickLister: RatingsReceivedAdapter.OnRatingClickedListner
): RecyclerView.Adapter<RatingsReceivedAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRatingsBinding):RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(ratingInfo: RatingInfo) {
            val data=ratingInfo.ratedOn!!.toDate()
            val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")

            binding.ratingValue.text="Rating : "+ratingInfo.rating.toString()+"/5"
            binding.ratedByName.text="Rated By : "+ratingInfo.ratedByName
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
