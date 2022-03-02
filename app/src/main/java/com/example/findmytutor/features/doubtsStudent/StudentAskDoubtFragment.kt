package com.example.findmytutor.features.doubtsStudent

import android.app.AlertDialog
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
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.databinding.DoubtsFilterDialogBinding
import com.example.findmytutor.databinding.FragmentStudentAskDoubtBinding
import com.example.findmytutor.features.MainActivity


class StudentAskDoubtFragment : BaseFragment(), StudentMyDoubtAdapter.OnRequestClickListner {
    private var _binding: FragmentStudentAskDoubtBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter :StudentMyDoubtAdapter


    private lateinit var mDoubtStudentViewModel: DoubtStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentStudentAskDoubtBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabCreateANewDoubt.setOnClickListener {
            val bundle=Bundle()
            val doubtInfo=DoubtInfo()
            bundle.putSerializable("doubtInfo",doubtInfo)
            findNavController().navigate(R.id.action_doubtsStudentFragment_to_createNewDoubtFragment,bundle)
        }
        mDoubtStudentViewModel =
            ViewModelProvider(this)[DoubtStudentViewModel::class.java]
        binding.studentsDoubtRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        showProgressDialog("Loading")
        mDoubtStudentViewModel.getAllStudentDoubt(requireContext())
        mDoubtStudentViewModel.mAllStudentDoubts.observe(viewLifecycleOwner)
        {

            dismissProgressDialog()
            if(it.size==0)
            {
                binding.studentsDoubtRecyclerView.visibility=View.GONE
                binding.animEmptyStudentDoubts.visibility=View.VISIBLE
                binding.txtNoStudent.visibility=View.VISIBLE
            }
            else
            {
                binding.studentsDoubtRecyclerView.visibility=View.VISIBLE
                binding.animEmptyStudentDoubts.visibility=View.GONE
                binding.txtNoStudent.visibility=View.GONE
                adapter=StudentMyDoubtAdapter(it,requireContext(),this)
                binding.studentsDoubtRecyclerView.adapter=adapter

            }

        }

        binding.btnFilter.setOnClickListener {
            val filterBinding= DoubtsFilterDialogBinding.inflate(LayoutInflater.from(requireContext()))
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(filterBinding.root)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.setCanceledOnTouchOutside(true)
            filterBinding.statusActive.setOnClickListener {
                mDoubtStudentViewModel.getAllOpenDoubts(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }




            }
            filterBinding.statusClosed.setOnClickListener {
                mDoubtStudentViewModel.getAllClosedDoubts(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }


            }
            filterBinding.dateLatestFirst.setOnClickListener {
                mDoubtStudentViewModel.getLatestFirst(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }

            }
            filterBinding.nosHighToLow.setOnClickListener {
                mDoubtStudentViewModel.getNoOfSolutionsHighToLow(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }

            }
            filterBinding.nosLowToHigh.setOnClickListener {
                mDoubtStudentViewModel.getNoOfSolutionsLowToHigh(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }

            }
            filterBinding.dateOldestFirst.setOnClickListener {
                mDoubtStudentViewModel.getOldestFirst(requireContext())
                mDoubtStudentViewModel.filterList.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }

            }
            filterBinding.clearFilter.setOnClickListener {
                mDoubtStudentViewModel.getLocalDbDoubts(requireContext())
                mDoubtStudentViewModel.doubtArrayFromLocalDb.observe(viewLifecycleOwner)
                {
                    mAlertDialog.dismiss()
                    adapter.updateDoubtsList(it)

                }

            }

        }

    }


    override fun onItemClicked(doubtInfo: DoubtInfo) {

        val bundle=Bundle()
        bundle.putSerializable("doubtInfo",doubtInfo)
        findNavController().navigate(R.id.action_doubtsStudentFragment_to_createNewDoubtFragment,bundle)

    }

}