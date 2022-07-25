package com.ishankumar.findmytutor.features.myTutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.dataClasses.RequestTutor
import com.ishankumar.findmytutor.databinding.FragmentStudentMyTutorRejectedBinding


class StudentMyTutorRejectedFragment :  BaseFragment(), StudentMyTutorAdapter.OnRequestClickListner {
    private var _binding: FragmentStudentMyTutorRejectedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mStudentMyTutorViewModel: StudentMyTutorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStudentMyTutorRejectedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mStudentMyTutorViewModel =
            ViewModelProvider(this)[StudentMyTutorViewModel::class.java]
        mStudentMyTutorViewModel.getAllRejectedRequests()
        mStudentMyTutorViewModel.mRejectedRequestsLiveData.observe(viewLifecycleOwner)
        {
            if(it.size>0)
            {
                binding.myTutorsRejectedRecyclerView.visibility=View.VISIBLE
                binding.animNoResultsFound.visibility=View.GONE
                binding.txtNoResultsFound.visibility=View.GONE
                val adapter=StudentMyTutorAdapter(it,requireContext(),this)
                binding.myTutorsRejectedRecyclerView.layoutManager= LinearLayoutManager(requireContext())
                binding.myTutorsRejectedRecyclerView.adapter=adapter
            }
            else{
                binding.myTutorsRejectedRecyclerView.visibility=View.GONE
                binding.animNoResultsFound.visibility=View.VISIBLE
                binding.txtNoResultsFound.visibility=View.VISIBLE
            }

        }
    }

    override fun onItemClicked(requestTutor: RequestTutor) {
        showProgressDialog("Please Wait")

        mStudentMyTutorViewModel.getStudent()
        mStudentMyTutorViewModel.mStudentLiveData.observe(viewLifecycleOwner)
        { student ->
            mStudentMyTutorViewModel.getAllTutorId(requestTutor.tutorId)
            mStudentMyTutorViewModel.mTutorData.observe(viewLifecycleOwner)
            {
                dismissProgressDialog()
                val bundle = Bundle()
                bundle.putSerializable("tutor", it)
                bundle.putSerializable("student",student)
                findNavController().navigate(
                    R.id.action_studentMyTutorsFragment_to_tutorDetailsFragment,
                    bundle
                )
            }
        }
    }
}