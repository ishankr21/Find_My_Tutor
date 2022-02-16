package com.example.findmytutor.features.requests

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.FragmentHomeStudentsBinding
import com.example.findmytutor.databinding.FragmentTutorRequestsBinding
import com.example.findmytutor.databinding.TutorAcceptStudentRequestDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.homeStudents.HomeStudentViewModel
import com.example.findmytutor.features.homeStudents.TutorAdapter
import com.example.findmytutor.features.splashScreen.SplashScreenViewModel


class TutorRequestsFragment : BaseFragment(), TutorRequestsAdapter.OnRequestClickListner {

    private var _binding: FragmentTutorRequestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorRequestsAdapter: TutorRequestsAdapter
    private lateinit var mTutorRequestsViewModel: TutorRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTutorRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setVisibleBottomNavigationView()
        mTutorRequestsViewModel =
            ViewModelProvider(this)[TutorRequestsViewModel::class.java]
        mTutorRequestsViewModel.getAllRequests()
        binding.tutorApproveRequestsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mTutorRequestsViewModel.mRequestsLiveData.observe(viewLifecycleOwner)
        {
            val adapter=TutorRequestsAdapter(it,requireContext(),this)
            binding.tutorApproveRequestsRecyclerView.adapter=adapter
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onItemClicked(requestTutor: RequestTutor) {

        val approveStudentRequestDialogBinding = TutorAcceptStudentRequestDialogBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(approveStudentRequestDialogBinding.root)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)

        showProgressDialog("Loading")
        mTutorRequestsViewModel.getStudentById(requestTutor.studentId)
        mTutorRequestsViewModel.studentLiveData.observe(viewLifecycleOwner)
        {student->
            approveStudentRequestDialogBinding.studentDetailsStudentName.text=student.name
            approveStudentRequestDialogBinding.studentDetailsstudentAge.text="Age : "+student.age.toString()
            approveStudentRequestDialogBinding.studentDetailsstudentGender.text="Gender : "+student.gender
            approveStudentRequestDialogBinding.studentDetailsstudentMobileNumber.text="Mobile No. : "+student.mobile.drop(3)
            if(student.emailId!="")
            {
                approveStudentRequestDialogBinding.studentDetailsstudentEmailId.visibility=View.VISIBLE
                approveStudentRequestDialogBinding.studentDetailsstudentEmailId.text="Email id : "+student.emailId
            }
            if(student.parentName!="")
            {
                approveStudentRequestDialogBinding.studentDetailsstudentParentName.visibility=View.VISIBLE
                approveStudentRequestDialogBinding.studentDetailsstudentParentName.text="Parent name : "+student.parentName
            }
            if(student.studentClass!="")
            {
                approveStudentRequestDialogBinding.studentDetailsstudentClass.visibility=View.VISIBLE
                approveStudentRequestDialogBinding.studentDetailsstudentClass.text="Student class : "+student.studentClass
            }
            if(student.leastFavouriteSubject!="")
            {
                approveStudentRequestDialogBinding.studentDetailsstudentLeastFavouriteSubject.visibility=View.VISIBLE
                approveStudentRequestDialogBinding.studentDetailsstudentLeastFavouriteSubject.text="Problem in subject : "+student.leastFavouriteSubject
            }
            if(student.schoolName!="")
            {
                approveStudentRequestDialogBinding.studentDetailsstudentSchoolName.visibility=View.VISIBLE
                approveStudentRequestDialogBinding.studentDetailsstudentSchoolName.text="School Name : "+student.schoolName
            }


            if (student.profilePicturePath != "") {
                Glide.with(requireContext()).load(student.profilePicturePath)
                    .apply(RequestOptions.bitmapTransform( RoundedCorners(50)))
                    .into(approveStudentRequestDialogBinding.studentDeailsImgstudent)



            } else {
                approveStudentRequestDialogBinding.studentDeailsImgstudent.setImageResource(R.drawable.ic_user)


            }

            dismissProgressDialog()

            approveStudentRequestDialogBinding.txtYesApprove.setOnClickListener {
                mTutorRequestsViewModel.approveRequest(requestTutor)
                mTutorRequestsViewModel.approvalRequestSuccess.observe(viewLifecycleOwner)
                {
                    if(it)
                    {
                        showToast(requireContext(),"Request Approved")
                        mAlertDialog.dismiss()
                    }
                    else
                    {
                        showToast(requireContext(),"Some Error Occurred")
                        mAlertDialog.dismiss()
                    }
                }

            }

            approveStudentRequestDialogBinding.txtNotApprove.setOnClickListener {

                mTutorRequestsViewModel.disapproveRequest(requestTutor)
                mTutorRequestsViewModel.disapprovalRequestSuccess.observe(viewLifecycleOwner)
                {
                    if(it)
                    {
                        showToast(requireContext(),"Request Declined")
                        mAlertDialog.dismiss()
                    }
                    else
                    {
                        showToast(requireContext(),"Some Error Occurred")
                        mAlertDialog.dismiss()
                    }
                }
            }
        }
        
    }


}