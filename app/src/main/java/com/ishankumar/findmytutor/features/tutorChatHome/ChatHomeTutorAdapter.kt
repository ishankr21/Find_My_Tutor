package com.ishankumar.findmytutor.features.tutorChatHome

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ishankumar.findmytutor.dataClasses.ChattingHelper
import com.ishankumar.findmytutor.databinding.ChatItemBinding
import com.ishankumar.findmytutor.utilities.Decode
import java.text.SimpleDateFormat

class ChatHomeTutorAdapter(
    var chatsArray:ArrayList<ChattingHelper>,
    val context: Context,
    var itemClickListner: OnChatClickListner

):RecyclerView.Adapter<ChatHomeTutorAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {

            fun bindItem(chattingHelper: ChattingHelper)
            {

                val date = chattingHelper.lastContact!!.toDate()

                val simpleDateFormat = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                binding.txtLastMessageTime.text=simpleDateFormat.format(date)
                if(chattingHelper.studentImage!="")
                {
                    Glide.with(context)
                        .load(chattingHelper.studentImage)
                        .into(binding.imageViewChatPhoto)

                }
                binding.txtLastMessage.text= Decode.decode(chattingHelper.lastMessage)
                    binding.receiverName.text=chattingHelper.studentName
                    binding.root.setOnClickListener {
                        itemClickListner.onItemClicked(chattingHelper)
                    }
             }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ChatItemBinding.inflate(LayoutInflater.from(context),parent,false)
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat=chatsArray[position]
        holder.bindItem(chat)

    }



    override fun getItemCount(): Int {
        return chatsArray.size
    }
    interface OnChatClickListner {
        fun onItemClicked(chattingHelper: ChattingHelper)
    }

}