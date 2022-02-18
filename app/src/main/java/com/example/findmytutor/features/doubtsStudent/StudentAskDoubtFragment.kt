package com.example.findmytutor.features.doubtsStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.databinding.FragmentStudentAskDoubtBinding


class StudentAskDoubtFragment : Fragment(), StudentMyDoubtAdapter.OnRequestClickListner {
    private var _binding: FragmentStudentAskDoubtBinding? = null
    private val binding get() = _binding!!


    private lateinit var mDoubtStudentViewModel: DoubtStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentAskDoubtBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabCreateANewDoubt.setOnClickListener {
            val bundle=Bundle()
            val doubtInfo=DoubtInfo()
            bundle.putSerializable("doubtInfo",doubtInfo)
            findNavController().navigate(R.id.action_doubtsStudentFragment_to_createNewDoubtFragment,bundle)
        }
        mDoubtStudentViewModel =
            ViewModelProvider(this)[DoubtStudentViewModel::class.java]
        binding.studentsDoubtRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mDoubtStudentViewModel.getAllStudentDoubt()
        mDoubtStudentViewModel.mAllStudentDoubts.observe(viewLifecycleOwner)
        {
            binding.studentsDoubtRecyclerView.adapter=StudentMyDoubtAdapter(it,requireContext(),this)
        }
    }

    override fun onItemClicked(doubtInfo: DoubtInfo) {

        val bundle=Bundle()
        bundle.putSerializable("doubtInfo",doubtInfo)
        findNavController().navigate(R.id.action_doubtsStudentFragment_to_createNewDoubtFragment,bundle)

    }

}