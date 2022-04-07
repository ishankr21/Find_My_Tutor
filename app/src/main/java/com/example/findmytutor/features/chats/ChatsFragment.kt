package com.example.findmytutor.features.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.dataClasses.Messages
import com.example.findmytutor.databinding.FragmentChatsBinding
import com.example.findmytutor.features.MainActivity
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
    private var chatId:String=""
    private var isStudent:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        isStudent=bundle.getBoolean("isStudent")


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {



                        if(isStudent)
                            findNavController().navigate(R.id.action_chatsFragment_to_studentChatHomeFragment)
                        else
                            findNavController().navigate(R.id.action_chatsFragment_to_tutorChatHomeFragment)



                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        binding.chattingPageBackButton.setOnClickListener {
            if(isStudent)
                findNavController().navigate(R.id.action_chatsFragment_to_studentChatHomeFragment)
            else
                findNavController().navigate(R.id.action_chatsFragment_to_tutorChatHomeFragment)
        }
        mChatsViewModel =
            ViewModelProvider(this)[ChatViewModel::class.java]

            chatId=chattingHelper.studentId.takeLast(5)+chattingHelper.tutorId.takeLast(5)


        if (isStudent)
        {
           binding.chattingPageTitleText.text=chattingHelper.tutorName
        }
        else
        {
            binding.chattingPageTitleText.text=chattingHelper.studentName
        }
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


                val senderId: String
                val receiverId:String
                val senderName:String
                val receiverName:String
                if (isStudent)
                {
                    senderId=chattingHelper.studentId
                    receiverId=chattingHelper.tutorId
                    senderName=chattingHelper.studentName
                    receiverName=chattingHelper.tutorName
                }
                else
                {
                    senderId=chattingHelper.tutorId
                    receiverId=chattingHelper.studentId
                    receiverName=chattingHelper.studentName
                    senderName=chattingHelper.tutorName
                }
                chattingHelper.lastMessage=binding.chattingPageTextBox.text.toString()
                chattingHelper.lastContact= Timestamp.now()
                val message=Messages(
                    sentByStudent = isStudent,
                    senderId =  senderId,
                    receiverId =  receiverId,
                    senderName =senderName,
                    receiverName = receiverName,
                    message = binding.chattingPageTextBox.text.toString(),
                    messageId = chatId,
                    time = Timestamp.now()
                )

                mChatsViewModel.sendMessage(message,chattingHelper)

                val topic = "/topics/${receiverId}"
                val notification = JSONObject()
                val notificationBody = JSONObject()

                if(isStudent)
                notificationBody.put("intentType", "messageSentByStudent")
                else {

                    notificationBody.put("intentType", "messageSentByTutor")
                }

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