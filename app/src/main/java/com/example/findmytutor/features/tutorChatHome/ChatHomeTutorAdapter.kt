package com.example.findmytutor.features.tutorChatHome

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.databinding.ChatItemBinding

class ChatHomeTutorAdapter(
    var chatsArray:ArrayList<ChattingHelper>,
    val context: Context,
    var itemClickListner: OnChatClickListner

):RecyclerView.Adapter<ChatHomeTutorAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {

            fun bindItem(chattingHelper: ChattingHelper)
            {
                    binding.receiverName.text=chattingHelper.senderName
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