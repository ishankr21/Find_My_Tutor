package com.example.findmytutor.features.chats

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.Messages
import com.example.findmytutor.databinding.ItemStudentMyDoubtBinding
import com.example.findmytutor.databinding.ReceiverBinding
import com.example.findmytutor.databinding.SentBinding

class ChatsAdapter(
    var messagesArray:ArrayList<Messages>,
    val context: Context,
    var userId:String
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class SenderViewHolder(val binding: SentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItems(messages: Messages) {
            binding.textSentMessage.text=messages.message
        }
    }
    inner class ReceiverViewHolder(val binding: ReceiverBinding): RecyclerView.ViewHolder(binding.root) {
            fun bindItems(messages: Messages) {

                binding.txtReceivedMessage.text=messages.message
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==1) {
            SenderViewHolder(
                SentBinding.inflate(LayoutInflater.from(context),parent,false)
            )
        } else {
            ReceiverViewHolder(
                ReceiverBinding.inflate(LayoutInflater.from(context),parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message=messagesArray[position]
        if(message.senderId!=userId)
        {
            val viewHolder=holder as ReceiverViewHolder
            viewHolder.bindItems(message)
        }
        else
        {
            val viewHolder=holder as SenderViewHolder
            viewHolder.bindItems(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message=messagesArray[position]
        return if(message.senderId!=userId) {
            2
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return messagesArray.size
    }
}