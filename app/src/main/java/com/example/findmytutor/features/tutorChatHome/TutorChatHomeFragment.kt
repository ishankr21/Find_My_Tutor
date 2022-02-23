package com.example.findmytutor.features.tutorChatHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.databinding.FragmentChatsBinding
import com.example.findmytutor.databinding.FragmentTutorChatHomeBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.chats.ChatViewModel


class TutorChatHomeFragment : Fragment(), ChatHomeTutorAdapter.OnChatClickListner {

    private var _binding: FragmentTutorChatHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mTutorChatHomeViewModel: TutorChatHomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentTutorChatHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTutorChatHomeViewModel =
            ViewModelProvider(this)[TutorChatHomeViewModel::class.java]
        binding.chatHomeTutorRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        mTutorChatHomeViewModel.getAllChats()
        mTutorChatHomeViewModel.mChattingHelperLiveData.observe(viewLifecycleOwner)
        {

            binding.chatHomeTutorRecyclerView.adapter=ChatHomeTutorAdapter(it,requireContext(),this)
        }
    }

    override fun onItemClicked(chattingHelper: ChattingHelper) {
        val bundle=Bundle()
        chattingHelper.sendByStudent=false
        bundle.putSerializable("chattingHelper",chattingHelper)
        findNavController().navigate(R.id.action_tutorChatHomeFragment_to_chatsFragment,bundle)
    }

}