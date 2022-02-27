package com.example.findmytutor.features.homeTutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.databinding.FragmentHomeTutorsBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.homeStudents.HomeStudentViewModel
import com.example.findmytutor.features.homeStudents.TutorAdapter


class HomeTutorsFragment : Fragment(), StudentAdapter.OnItemClickListener {

    private var _binding: FragmentHomeTutorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var mHomeTutorViewModel: HomeTutorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentHomeTutorsBinding.inflate(inflater, container, false)
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

                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)

        mHomeTutorViewModel = ViewModelProvider(this)[HomeTutorViewModel::class.java]

        binding.studentsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.studentsRecyclerView.showShimmer()
        mHomeTutorViewModel.getAllAcceptedStudents()
        mHomeTutorViewModel.getAllAcceptedStudents.observe(viewLifecycleOwner)
        {list->
            mHomeTutorViewModel.getAllStudents(list)
            mHomeTutorViewModel.mListOfStudent.observe(viewLifecycleOwner)
            {

                binding.studentsRecyclerView.adapter=StudentAdapter(it,requireContext(),this)
                binding.studentsRecyclerView.hideShimmer()
            }

        }

    }

    override fun onItemClicked(student: Student) {
        TODO("Not yet implemented")
    }
}