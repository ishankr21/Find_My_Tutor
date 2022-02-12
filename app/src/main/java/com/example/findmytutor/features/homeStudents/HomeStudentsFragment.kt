package com.example.findmytutor.features.homeStudents

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.features.MainActivity

class HomeStudentsFragment : BaseFragment(), TutorAdapter.OnItemClickListener {

    private var _binding: FragmentHomeStudentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorListAdapter: TutorAdapter
    private lateinit var mHomeStudentViewModel: HomeStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentHomeStudentsBinding.inflate(inflater, container, false)
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
        mHomeStudentViewModel = ViewModelProvider(this)[HomeStudentViewModel::class.java]

        binding.studentHomeRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.studentHomeRecyclerView.showShimmer()
        binding.studentHomeRecyclerView.setHasFixedSize(true)

        binding.homeStudentSearchAutoCompleteTextView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                tutorListAdapter.filter.filter(p0)
                return false
            }

        })


        mHomeStudentViewModel.getAllTutors()
        mHomeStudentViewModel.mListOfTutors.observe(viewLifecycleOwner)
        {
            tutorListAdapter= TutorAdapter(it,requireContext(),this@HomeStudentsFragment)
            binding.studentHomeRecyclerView.adapter=tutorListAdapter
            binding.studentHomeRecyclerView.hideShimmer()
        }

    }

    override fun onItemClicked() {
        TODO("Not yet implemented")
    }



}