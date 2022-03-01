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
    private var doubtsArray:ArrayList<DoubtInfo> = arrayListOf()
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
        mDoubtStudentViewModel.getAllStudentDoubt()
        mDoubtStudentViewModel.mAllStudentDoubts.observe(viewLifecycleOwner)
        {
            doubtsArray=it
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

                val tempArray= arrayListOf<DoubtInfo>()
//                for(i in doubtsArray)
//                {
//                    if(!i.isClosed)
//                        tempArray.add(i)
//                }
//
//                    adapter.updateDoubtsList(tempArray)
                mAlertDialog.dismiss()
            }
            filterBinding.statusClosed.setOnClickListener {
                val tempArray= arrayListOf<DoubtInfo>()
                for(i in doubtsArray)
                {
                    if(i.isClosed)
                        tempArray.add(i)
                }

                adapter.updateDoubtsList(tempArray)
                mAlertDialog.dismiss()
            }
            filterBinding.dateLatestFirst.setOnClickListener {
                val tempArray= doubtsArray
                tempArray.sortWith(compareBy<DoubtInfo> { it.createdOn }.reversed())


                adapter.updateDoubtsList(tempArray)
                mAlertDialog.dismiss()
            }
            filterBinding.dateOldestFirst.setOnClickListener {
                val tempArray= doubtsArray
                tempArray.sortWith(compareBy<DoubtInfo> { it.createdOn })


                adapter.updateDoubtsList(tempArray)
                mAlertDialog.dismiss()
            }
            filterBinding.clearFilter.setOnClickListener {

                adapter.updateDoubtsList(doubtsArray)
                mAlertDialog.dismiss()
            }

        }

    }


    override fun onItemClicked(doubtInfo: DoubtInfo) {

        val bundle=Bundle()
        bundle.putSerializable("doubtInfo",doubtInfo)
        findNavController().navigate(R.id.action_doubtsStudentFragment_to_createNewDoubtFragment,bundle)

    }

}