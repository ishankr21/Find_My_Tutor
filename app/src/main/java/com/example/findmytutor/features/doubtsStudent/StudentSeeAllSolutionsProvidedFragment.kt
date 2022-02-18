package com.example.findmytutor.features.doubtsStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.databinding.FragmentStudentAskDoubtBinding
import com.example.findmytutor.databinding.FragmentStudentSeeAllSolutionsProvidedBinding


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
            binding.studentSeesSolutionRecyclerView.adapter=StudentTutorSolutionsAdapter(it,requireContext(),this)
        }
    }

    override fun onItemClicked(solutionInfo: SolutionInfo) {
        TODO("Not yet implemented")
    }

}