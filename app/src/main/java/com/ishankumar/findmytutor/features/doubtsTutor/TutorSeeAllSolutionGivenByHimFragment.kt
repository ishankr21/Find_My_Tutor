package com.ishankumar.findmytutor.features.doubtsTutor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.dataClasses.SolutionInfo
import com.ishankumar.findmytutor.databinding.FragmentTutorSeeAllSolutionGivenByHimBinding
import com.ishankumar.findmytutor.features.MainActivity


class TutorSeeAllSolutionGivenByHimFragment : BaseFragment(),
    TutorMySolutionsAdapter.OnSolutionClickListner {


    private var _binding: FragmentTutorSeeAllSolutionGivenByHimBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDoubtTutorViewModel: DoubtTutorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentTutorSeeAllSolutionGivenByHimBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDoubtTutorViewModel =
            ViewModelProvider(this)[DoubtTutorViewModel::class.java]
        binding.tutorSeesHisSolutionsRecyclerView.layoutManager= LinearLayoutManager(requireContext())

        showProgressDialog("Loading")
        mDoubtTutorViewModel.getTutorSolutions()
        mDoubtTutorViewModel.mAllTutorSolutions.observe(viewLifecycleOwner)
        {
            dismissProgressDialog()
            if(it.size==0)
            {
                binding.tutorSeesHisSolutionsRecyclerView.visibility=View.GONE
                binding.animEmptyStudentDoubts.visibility=View.VISIBLE
                binding.txtNoStudent.visibility=View.VISIBLE
            }
            else
            {
                binding.tutorSeesHisSolutionsRecyclerView.visibility=View.VISIBLE
                binding.animEmptyStudentDoubts.visibility=View.GONE
                binding.txtNoStudent.visibility=View.GONE
                binding.tutorSeesHisSolutionsRecyclerView.adapter=
                    TutorMySolutionsAdapter(it,requireContext(),this)
            }

        }
    }

    override fun onItemClicked(solutionInfo: SolutionInfo) {
        showProgressDialog("Loading")
        mDoubtTutorViewModel.getDoubt(solutionInfo.doubtId)
        mDoubtTutorViewModel.doubtInfo.observe(viewLifecycleOwner)
        {
            dismissProgressDialog()
            val bundle=Bundle()
            bundle.putSerializable("doubtInfo",it)
            bundle.putSerializable("solutionInfo",solutionInfo)
            findNavController().navigate(R.id.action_doubtsTutorFragment_to_tutorCreateSolutionFragment,bundle)
        }

    }


}