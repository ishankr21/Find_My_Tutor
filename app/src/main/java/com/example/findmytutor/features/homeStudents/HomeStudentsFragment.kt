package com.example.findmytutor.features.homeStudents


import android.annotation.SuppressLint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.FilterSearchStudent
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FilterBottomSheetDialogBinding
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding

import com.example.findmytutor.features.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.RangeSlider

import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeStudentsFragment : BaseFragment(), TutorAdapter.OnItemClickListener {

    private var _binding: FragmentHomeStudentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorListAdapter: TutorAdapter
    private lateinit var mHomeStudentViewModel: HomeStudentViewModel
    var tutorList:ArrayList<Tutor> = arrayListOf()
    var classArray:Array<String> = arrayOf()
    var subjectArray:Array<String> = arrayOf()
    var ratingArray:Array<String> = arrayOf()
    var schoolBoardArray:Array<String> = arrayOf()
    var filterSearchStudent=FilterSearchStudent()
    var student=Student()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).setVisibleBottomNavigationView()
        (activity as MainActivity).setNavigationDrawerVisible()
        (activity as MainActivity).unlockDrawer()
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
         classArray=resources.getStringArray(R.array.classes)
         subjectArray=resources.getStringArray(R.array.subjects)
          ratingArray=resources.getStringArray(R.array.rating)
          schoolBoardArray=resources.getStringArray(R.array.school_board )
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        mHomeStudentViewModel = ViewModelProvider(this)[HomeStudentViewModel::class.java]

        binding.studentHomeRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.studentHomeRecyclerView.showShimmer()
        binding.studentHomeRecyclerView.setHasFixedSize(true)



        showProgressDialog("Loading")
        mHomeStudentViewModel.getCurrentStudent()
        mHomeStudentViewModel.mStudentLiveData.observe(viewLifecycleOwner)
        {stud->

            student=stud
            if(filterSearchStudent.default==0)
            {

                filterSearchStudent.classIndex= classArray.indexOf(student.studentClass)
                filterSearchStudent.subjectIndex=subjectArray.indexOf(student.leastFavouriteSubject)
                filterSearchStudent.schoolBoardIndex=schoolBoardArray.indexOf(student.schoolBoard)

            }
            val navHeaderBinding= (activity as MainActivity).getHeader()
            navHeaderBinding.findViewById<TextView>(R.id.drawerProfileName).text=student.name
            Glide.with(requireContext())
                .load(student.profilePicturePath)
                .into(navHeaderBinding.findViewById(R.id.drawerProfileImage))
            dismissProgressDialog()

            mHomeStudentViewModel.getAllTutors()
            mHomeStudentViewModel.mListOfTutors.observe(viewLifecycleOwner)
            {

                if (it.size==0)
                {
                    binding.animNoResultsFound.visibility=View.VISIBLE
                    binding.studentHomeRecyclerView.visibility=View.GONE
                    binding.txtNoResultsFound.visibility=View.VISIBLE
                }
                else
                {
                    tutorList=it
                    tutorListAdapter= TutorAdapter(it,requireContext(),this@HomeStudentsFragment,student)
                    binding.studentHomeRecyclerView.adapter=tutorListAdapter
                    binding.studentHomeRecyclerView.hideShimmer()
                    binding.animNoResultsFound.visibility=View.GONE
                    binding.txtNoResultsFound.visibility=View.GONE
                    binding.studentHomeRecyclerView.visibility=View.VISIBLE
                }

            }
        }

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
        binding.btnSelectYourFilters.setOnClickListener {

            val bottomSheetDialogBinding = FilterBottomSheetDialogBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            if(filterSearchStudent.default==0)
            {

                filterSearchStudent.classIndex= classArray.indexOf(student.studentClass)
                filterSearchStudent.subjectIndex=subjectArray.indexOf(student.leastFavouriteSubject)
                filterSearchStudent.schoolBoardIndex=schoolBoardArray.indexOf(student.schoolBoard)

            }
            val bottomSheetDialog=BottomSheetDialog(requireContext())
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
            initSpinners(bottomSheetDialogBinding)
            if(filterSearchStudent.classIndex!=0)
                bottomSheetDialogBinding.spnSelectClass.setSelection(filterSearchStudent.classIndex)
            if(filterSearchStudent.subjectIndex!=0)
                bottomSheetDialogBinding.spnSelectSubject.setSelection(filterSearchStudent.subjectIndex)
            if(filterSearchStudent.schoolBoardIndex!=0)
                bottomSheetDialogBinding.spnSelectSchoolBoard.setSelection(filterSearchStudent.schoolBoardIndex)
            if(filterSearchStudent.ratingIndex!=0)
                bottomSheetDialogBinding.spnSelectRating.setSelection(filterSearchStudent.ratingIndex)
            val initialList:MutableList<Float> = mutableListOf(filterSearchStudent.minFees.toFloat(),filterSearchStudent.maxFees.toFloat())

            bottomSheetDialogBinding.tutorFeeFilterSlider.values = initialList
            bottomSheetDialogBinding.tutorFeeFilterSlider.valueFrom=0.0F
            bottomSheetDialogBinding.tutorFeeFilterSlider.valueTo=10000.0F
            var minFee=filterSearchStudent.minFees.toFloat()
            var maxFee=filterSearchStudent.maxFees.toFloat()

            bottomSheetDialogBinding.tutorFeeFilterSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {

                @SuppressLint("RestrictedApi")
                override fun onStartTrackingTouch(slider: RangeSlider) {

                }
                @SuppressLint("RestrictedApi")
                override fun onStopTrackingTouch(slider: RangeSlider) {

                }

            })

            bottomSheetDialogBinding.tutorFeeFilterSlider.addOnChangeListener { rangeSlider, value, fromUser ->

                minFee=rangeSlider.values[0]
                maxFee=rangeSlider.values[1]


            }


            bottomSheetDialogBinding.tutorFeeFilterSlider.setLabelFormatter { value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("INR")
                format.format(value.toDouble())
            }

            bottomSheetDialog.show()

            bottomSheetDialogBinding.txtClearFilter.setOnClickListener {
                bottomSheetDialogBinding.spnSelectClass.setSelection(0)
                bottomSheetDialogBinding.spnSelectSubject.setSelection(0)
                bottomSheetDialogBinding.spnSelectSchoolBoard.setSelection(0)
                bottomSheetDialogBinding.spnSelectRating.setSelection(0)
                val initialList:MutableList<Float> = mutableListOf(0.0F,10000.0F)

                bottomSheetDialogBinding.tutorFeeFilterSlider.values = initialList
                filterSearchStudent=FilterSearchStudent()
            }

            bottomSheetDialogBinding.btnApplySeletedFilters.setOnClickListener {
                filterSearchStudent=FilterSearchStudent(
                    bottomSheetDialogBinding.spnSelectSubject.selectedItemPosition,
                    bottomSheetDialogBinding.spnSelectClass.selectedItemPosition,
                    bottomSheetDialogBinding.spnSelectRating.selectedItemPosition,
                    bottomSheetDialogBinding.spnSelectSchoolBoard.selectedItemPosition,
                    minFees = minFee.toDouble(),
                    maxFees = maxFee.toDouble(),
                    default = 1
                )


                val tempTutorArrayList= ArrayList<Tutor>()

                for(i in tutorList)
                {
                    var sum=0.0
                    for (j in i.rating)
                        sum+=j
                    val rating:Double=(sum/ (i.rating.size))

                    if((filterSearchStudent.subjectIndex==0 || (filterSearchStudent.subjectIndex!=0 && i.tutorFavouriteSubject==subjectArray[filterSearchStudent.subjectIndex]))
                        &&(filterSearchStudent.classIndex==0 || (filterSearchStudent.classIndex!=0 && i.preferredClass==classArray[filterSearchStudent.classIndex]))
                                && (filterSearchStudent.ratingIndex==0||(filterSearchStudent.ratingIndex!=0 && rating>=ratingArray[filterSearchStudent.ratingIndex].dropLast(1).toDouble()))
                                && (filterSearchStudent.schoolBoardIndex==0 || (filterSearchStudent.schoolBoardIndex!=0 && i.preferredSchoolBoard==subjectArray[filterSearchStudent.schoolBoardIndex]))
                        && (i.desiredFees>=filterSearchStudent.minFees && i.desiredFees<=filterSearchStudent.maxFees)
                    )
                        tempTutorArrayList.add(i)

                }

                bottomSheetDialog.dismiss()
                if (tempTutorArrayList.size==0)
                {
                    binding.animNoResultsFound.visibility=View.VISIBLE
                    binding.studentHomeRecyclerView.visibility=View.GONE
                    binding.txtNoResultsFound.visibility=View.VISIBLE
                }
                else
                {
                    binding.animNoResultsFound.visibility=View.GONE
                    binding.txtNoResultsFound.visibility=View.GONE
                    binding.studentHomeRecyclerView.visibility=View.VISIBLE
                }
                tutorListAdapter.updateTutorList(tempTutorArrayList)


            }
        }

    }

    private fun initSpinners(bottomSheetDialogBinding: FilterBottomSheetDialogBinding) {


        classArray[0]="Class"
        subjectArray[0]="Subject"
        ratingArray[0]="Rating"
        schoolBoardArray[0]="School Board"
        val arrayAdapterClass = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            subjectArray
        ) {

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = view as TextView
                item.run {
                    this.isSingleLine = false
                }

                return item
            }
        }


        bottomSheetDialogBinding.spnSelectSubject.adapter = arrayAdapterClass

        val arrayAdapterSchoolClass = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            classArray
        ) {

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = view as TextView
                item.run {
                    this.isSingleLine = false
                }

                return item
            }
        }

        bottomSheetDialogBinding.spnSelectClass.adapter = arrayAdapterSchoolClass

        val arrayAdapterSchoolRating = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            ratingArray
        ) {

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = view as TextView
                item.run {
                    this.isSingleLine = false
                }

                return item
            }
        }

        bottomSheetDialogBinding.spnSelectRating.adapter = arrayAdapterSchoolRating

        val arrayAdapterSchoolBoard = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            schoolBoardArray
        ) {

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = view as TextView
                item.run {
                    this.isSingleLine = false
                }

                return item
            }
        }

        bottomSheetDialogBinding.spnSelectSchoolBoard.adapter = arrayAdapterSchoolBoard
    }

    override fun onItemClicked(tutor: Tutor) {
        val bundle = Bundle()
        bundle.putSerializable("tutor", tutor)
        bundle.putSerializable("student", student)
        findNavController().navigate(
            R.id.action_homeStudentsFragment_to_tutorDetailsFragment,
            bundle
        )


    }

    override fun onPause() {
        (activity as MainActivity).setNavigationDrawerDisappear()
        (activity as MainActivity).lockDrawer()
        super.onPause()
    }

    override fun onResume() {
        (activity as MainActivity).setNavigationDrawerVisible()
        (activity as MainActivity).unlockDrawer()
        super.onResume()
    }

}