package com.ishankumar.findmytutor.features.tutorChatHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.dataClasses.ChattingHelper
import com.ishankumar.findmytutor.databinding.FragmentTutorChatHomeBinding
import com.ishankumar.findmytutor.features.MainActivity


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


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_tutorChatHomeFragment_to_homeTutorsFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.chatHomeTutorPageBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_tutorChatHomeFragment_to_homeTutorsFragment)
        }


        mTutorChatHomeViewModel =
            ViewModelProvider(this)[TutorChatHomeViewModel::class.java]
        binding.chatHomeTutorRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        mTutorChatHomeViewModel.getAllChats()
        mTutorChatHomeViewModel.mChattingHelperLiveData.observe(viewLifecycleOwner)
        {
            if(it.size==0)
            {
                binding.chatHomeTutorRecyclerView.visibility=View.GONE
                binding.animEmptyTutorChat.visibility=View.VISIBLE
                binding.txtNoStudent.visibility=View.VISIBLE
            }
            else
            {
                binding.chatHomeTutorRecyclerView.visibility = View.VISIBLE
                binding.animEmptyTutorChat.visibility = View.GONE
                binding.txtNoStudent.visibility = View.GONE
                binding.chatHomeTutorRecyclerView.adapter =
                    ChatHomeTutorAdapter(it, requireContext(), this)
            }
        }
    }

    override fun onItemClicked(chattingHelper: ChattingHelper) {
        val bundle=Bundle()
        bundle.putBoolean("isStudent",false)
        bundle.putSerializable("chattingHelper",chattingHelper)
        findNavController().navigate(R.id.action_tutorChatHomeFragment_to_chatsFragment,bundle)
    }

}