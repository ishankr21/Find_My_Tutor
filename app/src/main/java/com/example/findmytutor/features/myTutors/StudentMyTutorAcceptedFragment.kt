package com.example.findmytutor.features.myTutors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.FragmentStudentMyTutorAcceptedBinding
import com.example.findmytutor.databinding.FragmentStudentMyTutorsBinding
import com.example.findmytutor.features.register.RegisterViewModel


class StudentMyTutorAcceptedFragment : BaseFragment(), StudentMyTutorAdapter.OnRequestClickListner {

    private var _binding: FragmentStudentMyTutorAcceptedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mStudentMyTutorViewModel: StudentMyTutorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentMyTutorAcceptedBinding.inflate(inflater, container, false)
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
        mStudentMyTutorViewModel.getAllAcceptedRequests()
        mStudentMyTutorViewModel.mAcceptedRequestsLiveData.observe(viewLifecycleOwner)
        {

            if(it.size>0)
            {
                binding.myTutorsAcceptedRecyclerView.visibility=View.VISIBLE
                binding.animNoResultsFound.visibility=View.GONE
                binding.txtNoResultsFound.visibility=View.GONE
                val adapter=StudentMyTutorAdapter(it,requireContext(),this)
                binding.myTutorsAcceptedRecyclerView.layoutManager=LinearLayoutManager(requireContext())
                binding.myTutorsAcceptedRecyclerView.adapter=adapter
            }
            else
            {
                binding.myTutorsAcceptedRecyclerView.visibility=View.GONE
                binding.animNoResultsFound.visibility=View.VISIBLE
                binding.txtNoResultsFound.visibility=View.VISIBLE
            }

        }
    }

    override fun onItemClicked(requestTutor: RequestTutor) {
        showProgressDialog("Please Wait")
        mStudentMyTutorViewModel.getStudent()
        mStudentMyTutorViewModel.mStudentLiveData.observe(viewLifecycleOwner)
        {student->
            mStudentMyTutorViewModel.getAllTutorId(requestTutor.tutorId)
            mStudentMyTutorViewModel.mTutorData.observe(viewLifecycleOwner)
            {
                dismissProgressDialog()
                val bundle=Bundle()
                bundle.putSerializable("tutor",it)
                bundle.putSerializable("student",student)
                findNavController().navigate(R.id.action_studentMyTutorsFragment_to_tutorDetailsFragment,bundle)
            }
        }

    }

}