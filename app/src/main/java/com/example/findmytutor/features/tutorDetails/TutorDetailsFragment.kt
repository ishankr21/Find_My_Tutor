package com.example.findmytutor.features.tutorDetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentTutorDetailsBinding
import com.example.findmytutor.databinding.FragmentVerifyOtpBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.profile.ProfileViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


class TutorDetailsFragment : BaseFragment() {
    private var _binding: FragmentTutorDetailsBinding? = null
    private val binding get() = _binding!!
    private var tutor= Tutor()
    private lateinit var mTutorDetailsViewModel: TutorDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTutorDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigationView()
        mTutorDetailsViewModel =
            ViewModelProvider(this)[TutorDetailsViewModel::class.java]
        val bundle=arguments
        tutor=bundle!!.getSerializable("tutor") as Tutor


        binding.tutorDetailsTutorName.text=tutor.name
        binding.tutorDetailsTutorAge.text="Age : "+tutor.age.toString()
        binding.tutorDetailsTutorGender.text="Gender : "+tutor.gender
        binding.tutorDetailsTutorMobileNumber.text="Mobile No. : "+tutor.mobile.drop(3)
        if(tutor.emailId!="")
        {
            binding.tutorDetailsTutorEmailId.visibility=View.VISIBLE
            binding.tutorDetailsTutorEmailId.text="Email id : "+tutor.emailId
        }
        if(tutor.rating!=0)
        {
            binding.tutorDetailsTutorRating.visibility=View.VISIBLE
            binding.tutorDetailsTutorRating.text=tutor.rating.toString()
        }
        if(tutor.desiredFees!=0.0f)
        {
            binding.tutorDetailsTutorFees.visibility=View.VISIBLE
            binding.tutorDetailsTutorFees.text="Fees : "+tutor.desiredFees.toString()
        }
        if(tutor.preferredClass!="")
        {
            binding.tutorDetailsTutorClass.visibility=View.VISIBLE
            binding.tutorDetailsTutorClass.text="Preferred Class : "+tutor.preferredClass
        }
        if(tutor.tutorFavouriteSubject!="")
        {
            binding.tutorDetailsTutorFavouriteSubject.visibility=View.VISIBLE
            binding.tutorDetailsTutorFavouriteSubject.text="Speciality in : "+tutor.tutorFavouriteSubject
        }


        if (tutor.profilePicturePath != "") {
            Glide.with(requireContext()).load(tutor.profilePicturePath)
                .apply(RequestOptions.bitmapTransform( RoundedCorners(50)))
                .into(binding.tutorDeailsImgTutor)



        } else {
            binding.tutorDeailsImgTutor.setImageResource(R.drawable.ic_user)


        }


        binding.tutorSendRequest.setOnClickListener {
            mTutorDetailsViewModel.getStudent()
            mTutorDetailsViewModel.mStudentLiveData.observe(viewLifecycleOwner)
            {
                val request= RequestTutor(
                    tutorId = tutor.tutorId,
                    studentId = FirebaseAuth.getInstance().uid!!,
                    timeOfRequest = Timestamp.now(),
                    isCompleted = false,
                    isDeclined = false,
                    studentName = it.name,
                    tutorName = tutor.name

                )

                mTutorDetailsViewModel.sendRequest(request)
                mTutorDetailsViewModel.requestsSent.observe(viewLifecycleOwner)
                {requestSent->
                    if(requestSent)
                    {
                        showToast(requireContext(),"Request Sent")
                    }
                    else
                        showToast(requireContext(),"Some Error Occurred!")
                }

            }

        }

    }

}