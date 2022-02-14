package com.example.findmytutor.features.requests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.databinding.FragmentTutorRequestsBinding
import com.example.findmytutor.features.homeStudents.HomeStudentViewModel
import com.example.findmytutor.features.homeStudents.TutorAdapter
import com.example.findmytutor.features.splashScreen.SplashScreenViewModel


class TutorRequestsFragment : BaseFragment(), TutorRequestsAdapter.OnRequestClickListner {

    private var _binding: FragmentTutorRequestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorRequestsAdapter: TutorRequestsAdapter
    private lateinit var mTutorRequestsViewModel: TutorRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTutorRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTutorRequestsViewModel =
            ViewModelProvider(this)[TutorRequestsViewModel::class.java]
        mTutorRequestsViewModel.getAllRequests()
        binding.tutorApproveRequestsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mTutorRequestsViewModel.mRequestsLiveData.observe(viewLifecycleOwner)
        {
            val adapter=TutorRequestsAdapter(it,requireContext(),this)
            binding.tutorApproveRequestsRecyclerView.adapter=adapter
        }

    }

    override fun onItemClicked(requestTutor: RequestTutor) {
        showToast(requireContext(),"Blablalba")
    }


}