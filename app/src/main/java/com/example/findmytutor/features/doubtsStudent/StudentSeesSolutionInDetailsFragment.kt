package com.example.findmytutor.features.doubtsStudent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.databinding.FragmentStudentSeesSolutionInDetailsBinding
import com.example.findmytutor.features.MainActivity


class StudentSeesSolutionInDetailsFragment : BaseFragment() {


    private var _binding: FragmentStudentSeesSolutionInDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mDoubtStudentViewModel: DoubtStudentViewModel
    var solutionInfo=SolutionInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentStudentSeesSolutionInDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDoubtStudentViewModel =
            ViewModelProvider(this)[DoubtStudentViewModel::class.java]
        val bundle=arguments
        solutionInfo=bundle!!.getSerializable("solutionInfo") as SolutionInfo
        binding.txtTutorNameValue.text=solutionInfo.tutorName
        binding.txtDescriptionValue.text=solutionInfo.solutionDescription
        if(solutionInfo.solutionImagePath!="")
        {
            Glide.with(requireContext())
                .load(solutionInfo.solutionImagePath)
                .into(binding.solutionSeenByStudentImage)
        }

        binding.btnGoToTutor.setOnClickListener{
            showProgressDialog("Please Wait")
            mDoubtStudentViewModel.getAllTutorId(solutionInfo.tutorId)
            mDoubtStudentViewModel.mTutorData.observe(viewLifecycleOwner)
            {
                mDoubtStudentViewModel.getStudentData()
                mDoubtStudentViewModel.mStudentLiveData.observe(viewLifecycleOwner)
                {student->
                    dismissProgressDialog()
                    val bundle=Bundle()
                    bundle.putSerializable("tutor",it)
                    bundle.putSerializable("student",student)

                    findNavController().navigate(R.id.action_studentSeesSolutionInDetailsFragment_to_tutorDetailsFragment,bundle)
                }


            }
        }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_studentSeesSolutionInDetailsFragment_to_doubtsStudentFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.solutionSeenByStudentBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentSeesSolutionInDetailsFragment_to_doubtsStudentFragment)
        }
    }


}