package com.example.findmytutor.features.requests

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.FragmentTutorRequestsBinding
import com.example.findmytutor.databinding.TutorAcceptStudentRequestDialogBinding
import com.example.findmytutor.features.MainActivity
import kotlin.math.roundToInt


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
        (activity as MainActivity).setVisibleBottomNavigationView()
        _binding = FragmentTutorRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTutorRequestsViewModel =
            ViewModelProvider(this)[TutorRequestsViewModel::class.java]
        mTutorRequestsViewModel.getAllRequests()
        binding.tutorApproveRequestsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        mTutorRequestsViewModel.mRequestsLiveData.observe(viewLifecycleOwner)
        {
            if (it.size > 0) {
                binding.animNoResultsFound.visibility = View.GONE
                binding.txtNoResultsFound.visibility = View.GONE
                binding.tutorApproveRequestsRecyclerView.visibility = View.VISIBLE
                val adapter = TutorRequestsAdapter(it, requireContext(), this)
                binding.tutorApproveRequestsRecyclerView.adapter = adapter
            }
            else
            {
                binding.animNoResultsFound.visibility = View.VISIBLE
                binding.txtNoResultsFound.visibility = View.VISIBLE
                binding.tutorApproveRequestsRecyclerView.visibility = View.GONE
            }

        }
        binding.tutorApproveRequestsBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_tutorRequestsFragment_to_homeTutorsFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClicked(requestTutor: RequestTutor) {

        val approveStudentRequestDialogBinding = TutorAcceptStudentRequestDialogBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(approveStudentRequestDialogBinding.root)
        if(requestTutor.isCompleted && !requestTutor.isDeclined)
        {
            approveStudentRequestDialogBinding.txtYesApprove.visibility=View.GONE
            approveStudentRequestDialogBinding.txtNotApprove.visibility=View.GONE
            approveStudentRequestDialogBinding.txtViewMore.visibility=View.VISIBLE
        }
        else
        {
            approveStudentRequestDialogBinding.txtYesApprove.visibility=View.VISIBLE
            approveStudentRequestDialogBinding.txtNotApprove.visibility=View.VISIBLE
            approveStudentRequestDialogBinding.txtViewMore.visibility=View.GONE
        }
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)

        showProgressDialog("Loading")
        mTutorRequestsViewModel.getTutor()
        mTutorRequestsViewModel.mTutorLiveData.observe(viewLifecycleOwner)
        {tutor->
            mTutorRequestsViewModel.getStudentById(requestTutor.studentId)
            mTutorRequestsViewModel.studentLiveData.observe(viewLifecycleOwner)
            {student->

                val age = SpannableStringBuilder().bold { append("Age: ") }.append(student.age.toString())
                approveStudentRequestDialogBinding.studentDetailsstudentAge.text=age
                approveStudentRequestDialogBinding.studentDetailsStudentName.text=student.name

                val gender = SpannableStringBuilder().bold { append("Gender: ") }.append(student.gender)
                approveStudentRequestDialogBinding.studentDetailsstudentGender.text=gender

                val mobile = SpannableStringBuilder().bold { append("Mobile: ") }.append(student.mobile)
                approveStudentRequestDialogBinding.studentDetailsstudentMobileNumber.text=mobile


                val email = SpannableStringBuilder().bold { append("Email Id: ") }.append(student.emailId)
                approveStudentRequestDialogBinding.studentDetailsstudentEmailId.text=email

                val parentName = SpannableStringBuilder().bold { append("Parent Name: ") }.append(student.parentName)
                approveStudentRequestDialogBinding.studentDetailsstudentParentName.text=parentName


                val schoolName = SpannableStringBuilder().bold { append("School Name: ") }.append(student.schoolName)
                approveStudentRequestDialogBinding.studentDetailsstudentSchoolName.text=schoolName

               val distanceValue =(com.example.findmytutor.utilities.Utils.calculateDistanceBetweenStudentTutor(tutor.latitude,tutor.longitude,student.latitude,student.longitude)/1000.0).roundToInt().toString()+" km."
                val distance = SpannableStringBuilder().bold { append("Distance From You: ") }.append(distanceValue)
                approveStudentRequestDialogBinding.studentDetailsstudentDistance.text=distance

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

                approveStudentRequestDialogBinding.txtViewMore.setOnClickListener {
                    mAlertDialog.dismiss()
                    val bundle=Bundle()
                    bundle.putSerializable("tutor",tutor)
                    bundle.putSerializable("student",student)
                    bundle.putSerializable("requestTutor",requestTutor)
                    findNavController().navigate(R.id.action_tutorRequestsFragment_to_studentDetailsFragment,bundle)
                }
            }
        }

        
    }


}