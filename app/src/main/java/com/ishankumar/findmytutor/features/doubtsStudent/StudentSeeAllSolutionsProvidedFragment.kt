package com.ishankumar.findmytutor.features.doubtsStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.dataClasses.SolutionInfo
import com.ishankumar.findmytutor.databinding.FragmentStudentSeeAllSolutionsProvidedBinding
import com.ishankumar.findmytutor.features.MainActivity


class StudentSeeAllSolutionsProvidedFragment : Fragment(),
    StudentTutorSolutionsAdapter.OnSolutionClickListner {

    private var _binding: FragmentStudentSeeAllSolutionsProvidedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mDoubtStudentViewModel: DoubtStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentStudentSeeAllSolutionsProvidedBinding.inflate(inflater, container, false)
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
        binding.studentSeesSolutionRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        mDoubtStudentViewModel.getAllTutorSolution()
        mDoubtStudentViewModel.mAllStudentSolutions.observe(viewLifecycleOwner)
        {
            if (it.size==0)
            {
                binding.studentSeesSolutionRecyclerView.visibility=View.GONE
                binding.animEmptyStudentSolutions.visibility=View.VISIBLE
                binding.txtNoStudent.visibility=View.VISIBLE
            }
            else
            {
                binding.studentSeesSolutionRecyclerView.visibility=View.VISIBLE
                binding.animEmptyStudentSolutions.visibility=View.GONE
                binding.txtNoStudent.visibility=View.GONE
                binding.studentSeesSolutionRecyclerView.adapter=StudentTutorSolutionsAdapter(it,requireContext(),this)

            }

        }


    }

    override fun onItemClicked(solutionInfo: SolutionInfo) {
         val bundle=Bundle()
        bundle.putSerializable("solutionInfo",solutionInfo)
        findNavController().navigate(R.id.action_doubtsStudentFragment_to_studentSeesSolutionInDetailsFragment,bundle)
    }

}