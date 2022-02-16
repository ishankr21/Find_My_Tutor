package com.example.findmytutor.features.homeStudents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.features.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

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

    override fun onItemClicked(tutor: Tutor) {
        val bundle = Bundle()
        bundle.putSerializable("tutor", tutor)
        findNavController().navigate(
            R.id.action_homeStudentsFragment_to_tutorDetailsFragment,
            bundle
        )
//        showToast(requireContext(),"${tutor.tutorId}")

    }



}