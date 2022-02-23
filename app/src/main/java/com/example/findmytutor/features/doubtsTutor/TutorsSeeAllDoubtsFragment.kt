package com.example.findmytutor.features.doubtsTutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.databinding.FragmentTutorsSeeAllDoubtsBinding
import com.example.findmytutor.features.MainActivity


class TutorsSeeAllDoubtsFragment : Fragment(), TutorSeesAllDoubtsAdapter.OnRequestClickListner {

    private var _binding: FragmentTutorsSeeAllDoubtsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDoubtTutorViewModel: DoubtTutorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentTutorsSeeAllDoubtsBinding.inflate(inflater, container, false)
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
        binding.tutorSeesAllDoubtsRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        mDoubtTutorViewModel.getAllDoubts()
        mDoubtTutorViewModel.mAllDoubts.observe(viewLifecycleOwner)
        {
            binding.tutorSeesAllDoubtsRecyclerView.adapter=
                TutorSeesAllDoubtsAdapter(it,requireContext(),this)
        }
    }

    override fun onItemClicked(doubtInfo: DoubtInfo) {
        val bundle=Bundle()
        bundle.putSerializable("doubtInfo",doubtInfo)
        findNavController().navigate(R.id.action_doubtsTutorFragment_to_tutorSeesDoubtInDetailFragment,bundle)
    }
}