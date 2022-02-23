package com.example.findmytutor.features.chats

import android.os.Bundle
import android.os.Message
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.dataClasses.Messages
import com.example.findmytutor.databinding.FragmentChatsBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.doubtsStudent.DoubtStudentViewModel
import com.example.findmytutor.utilities.SendNotification
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject


class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mChatsViewModel: ChatViewModel
    var chattingHelper=ChattingHelper()
    var chatId:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle=arguments
        chattingHelper=bundle!!.getSerializable("chattingHelper") as ChattingHelper
        mChatsViewModel =
            ViewModelProvider(this)[ChatViewModel::class.java]

            chatId=chattingHelper.senderId.takeLast(5)+chattingHelper.receiverId.takeLast(5)



        binding.chatsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mChatsViewModel.getAllMessages(chatId)
        mChatsViewModel.allMessages.observe(viewLifecycleOwner)
        {
            binding.chatsRecyclerView.adapter=ChatsAdapter(it,requireContext(),FirebaseAuth.getInstance().currentUser!!.uid)
            binding.chatsRecyclerView.scrollToPosition(it.size-1)
        }
        binding.chattingPageSendButton.setOnClickListener {
            if(binding.chattingPageTextBox.text.isNotEmpty())
            {
                var senderId=""
                var receiverId=""
                var senderName=""
                var receiverName=""
                if (chattingHelper.sendByStudent)
                {
                    senderId=chattingHelper.senderId
                    receiverId=chattingHelper.receiverId
                    senderName=chattingHelper.senderName
                    receiverName=chattingHelper.receiverName
                }
                else
                {
                    senderId=chattingHelper.receiverId
                    receiverId=chattingHelper.senderId
                    receiverName=chattingHelper.senderName
                    senderName=chattingHelper.receiverName
                }
                val message=Messages(
                    sentByStudent = chattingHelper.sendByStudent,
                    senderId =  senderId,
                    receiverId =  receiverId,
                    senderName =senderName,
                    receiverName = receiverName,
                    message = binding.chattingPageTextBox.text.toString(),
                    messageId = chattingHelper.senderId.takeLast(5)+chattingHelper.receiverId.takeLast(5),
                    time = Timestamp.now())

                mChatsViewModel.sendMessage(message,chattingHelper)

                val topic = "/topics/${receiverId}"
                val notification = JSONObject()
                val notificationBody = JSONObject()
                if(chattingHelper.sendByStudent)
                notificationBody.put("intentType", "messageSentByStudent")
                else
                    notificationBody.put("intentType", "messageSentByTutor")


                try {
                    notificationBody.put("title", "You received a message from $senderName")
                    notificationBody.put(
                        "message",
                        "${binding.chattingPageTextBox.text}"
                    )   //Enter your notification message
                    notification.put("to", topic)
                    notification.put("data", notificationBody)
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                SendNotification(requireContext()).sendNotification(notification)


                binding.chattingPageTextBox.setText("")
            }
        }

    }

}