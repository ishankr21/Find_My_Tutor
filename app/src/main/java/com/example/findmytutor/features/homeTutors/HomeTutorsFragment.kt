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
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.databinding.FragmentHomeTutorsBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.homeStudents.HomeStudentViewModel
import com.example.findmytutor.features.homeStudents.TutorAdapter


class HomeTutorsFragment : BaseFragment(), StudentAdapter.OnItemClickListener {

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
        showProgressDialog("Loading")
        mHomeTutorViewModel.getTutor()
        mHomeTutorViewModel.mTutorLiveData.observe(viewLifecycleOwner)
        {tutor->
           mHomeTutorViewModel.getAllAcceptedStudents()
            mHomeTutorViewModel.getAllAcceptedStudents.observe(viewLifecycleOwner)
            {list->
                dismissProgressDialog()
                if(list.size>0)
                {
                    binding.studentsRecyclerView.visibility=View.VISIBLE
                    binding.animEmptyTutorHome.visibility=View.GONE
                    binding.txtNoStudent.visibility=View.GONE
                    mHomeTutorViewModel.getAllStudents(list)
                    mHomeTutorViewModel.mListOfStudent.observe(viewLifecycleOwner)
                    {

                        binding.studentsRecyclerView.adapter=StudentAdapter(it,requireContext(),this,tutor)
                        binding.studentsRecyclerView.hideShimmer()
                    }
                }
                else
                {
                    binding.studentsRecyclerView.visibility=View.GONE
                    binding.animEmptyTutorHome.visibility=View.VISIBLE
                    binding.txtNoStudent.visibility=View.VISIBLE
                }


            }

        }


    }

    override fun onItemClicked(student: Student) {
        TODO("Not yet implemented")
    }
}