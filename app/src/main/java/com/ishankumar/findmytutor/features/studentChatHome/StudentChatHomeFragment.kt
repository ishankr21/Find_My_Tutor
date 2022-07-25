package com.ishankumar.findmytutor.features.studentChatHome

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
import com.ishankumar.findmytutor.databinding.FragmentStudentChatHomeBinding
import com.ishankumar.findmytutor.features.MainActivity


class StudentChatHomeFragment : Fragment(), ChatHomeStudentAdapter.OnChatClickListner {


    private var _binding: FragmentStudentChatHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mStudentChatHomeViewModel: StudentChatHomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentStudentChatHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStudentChatHomeViewModel =
            ViewModelProvider(this)[StudentChatHomeViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_studentChatHomeFragment_to_homeStudentsFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.chatHomeStudentPageBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentChatHomeFragment_to_homeStudentsFragment)
        }
        binding.chatHomeStudentRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        mStudentChatHomeViewModel.getAllChats()
        mStudentChatHomeViewModel.mChattingHelperLiveData.observe(viewLifecycleOwner)
        {
            if(it.size==0)
            {
                binding.chatHomeStudentRecyclerView.visibility=View.GONE
                binding.animEmptyStudentHome.visibility=View.VISIBLE
                binding.txtNoStudent.visibility=View.VISIBLE
            }
            else
            {
                binding.chatHomeStudentRecyclerView.visibility=View.VISIBLE
                binding.animEmptyStudentHome.visibility=View.GONE
                binding.txtNoStudent.visibility=View.GONE
                binding.chatHomeStudentRecyclerView.adapter= ChatHomeStudentAdapter(it,requireContext(),this)
            }

        }
    }

    override fun onItemClicked(chattingHelper: ChattingHelper) {
        val bundle=Bundle()
        bundle.putBoolean("isStudent",true)
        bundle.putSerializable("chattingHelper",chattingHelper)
        findNavController().navigate(R.id.action_studentChatHomeFragment_to_chatsFragment,bundle)
    }


}