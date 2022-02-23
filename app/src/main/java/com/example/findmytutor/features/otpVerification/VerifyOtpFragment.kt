package com.example.findmytutor.features.otpVerification

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentVerifyOtpBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit


class VerifyOtpFragment : BaseFragment() {

    private var _binding: FragmentVerifyOtpBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View
    private var mVerificationId:String=""
    private lateinit var mOtpViewModel: OtpViewModel
    private var isComingFromRegistration=false
    private var student=Student()
    private var tutor=Tutor()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigationView()
        mOtpViewModel = ViewModelProvider(this).get(OtpViewModel::class.java)

        val bundle=arguments
        student=bundle!!.getSerializable("student") as Student
        tutor=bundle!!.getSerializable("tutor") as Tutor
        isComingFromRegistration = bundle.getBoolean("isRegistration")

        val content = SpannableString("Resend OTP")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.resendOtpButton.text = content

        val timer = object: CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (isAdded) {
                    binding.resendOtpTimerTv.visibility = View.VISIBLE
                    binding.resendOtpButton.visibility = View.GONE
                    binding.resendOtpTimerTv.text = "Resend OTP in ${millisUntilFinished / 1000} seconds"
                }
            }

            override fun onFinish() {
                if (isAdded) {
                    binding.resendOtpTimerTv.visibility = View.GONE
                    binding.resendOtpButton.visibility = View.VISIBLE
                }
            }

        }
        timer.start()
        if(isComingFromRegistration)
        {
           if(tutor.pincode=="")
               sendVerificationCode(student.mobile, requireActivity(), view, isComingFromRegistration)
           else
               sendVerificationCode(tutor.mobile, requireActivity(), view, isComingFromRegistration)
        }
        else
        {
            if(tutor.mobile=="")
            sendVerificationCode(student.mobile, requireActivity(), view, isComingFromRegistration)
            else
            sendVerificationCode(tutor.mobile, requireActivity(), view, isComingFromRegistration)
        }

        binding.verifyOtpButton.setOnClickListener {
            Utils.hideKeyboard(requireActivity())

            if(binding.otpEdittext.text.isNullOrEmpty())
            {
                showToast(requireContext(),"Please enter the otp")
            }
            else
            {
                if (isAdded) {
                    binding.otpVerificationProgressbar.visibility=View.VISIBLE
                }
                verifyOtp(binding.otpEdittext.text.toString().trim()).observe(viewLifecycleOwner
                ) {
                    if (isAdded) {

                        binding.otpVerificationProgressbar.visibility=View.GONE
                    }
                }
            }



        }
        binding.resendOtpButton.setOnClickListener {

            if(isComingFromRegistration)
            {
                if(tutor.pincode=="")
                    sendVerificationCode(student.mobile, requireActivity(), view, isComingFromRegistration)
                else
                    sendVerificationCode(tutor.mobile, requireActivity(), view, isComingFromRegistration)
            }
            else
            {
                if(tutor.mobile=="")
                    sendVerificationCode(student.mobile, requireActivity(), view, isComingFromRegistration)
                else
                    sendVerificationCode(tutor.mobile, requireActivity(), view, isComingFromRegistration)
            }
            Toast.makeText(requireContext(),"Otp Sent Successfully",Toast.LENGTH_SHORT).show()
            it.visibility = View.GONE
            timer.start()
        }
    }
    private fun sendVerificationCode(
        phone: String, activity: Activity, view: View,
        isComingFromRegistration: Boolean
    ) {
             mView=view

        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //automatic verification
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode

            if (code != null) {



            }
        }

        override fun onVerificationFailed(e: FirebaseException) {


            e.printStackTrace()
        }

        //code sent to device but no auto verification
        override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {

            super.onCodeSent(s, forceResendingToken)
            mVerificationId = s

        }
    }

    private fun verifyOtp(otp: String): MutableLiveData<Boolean> {
        val verifyOtpMutableLiveData = MutableLiveData<Boolean>()

        if (mVerificationId != "")
        {

            val credential = PhoneAuthProvider.getCredential(mVerificationId, otp)
            mOtpViewModel.signInWithPhone(credential)
            binding.verifyOtpButton.isEnabled = false
            mOtpViewModel.isSignInSuccess.observe(viewLifecycleOwner)
            { authenticatedUser ->

                if (authenticatedUser!=null) {

                    if (!isComingFromRegistration) {

                        if(student.mobile=="")
                        {
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/tutors")
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            (activity as MainActivity).setBottomNavigationMenu(2)

                            mOtpViewModel.getTutorWithMobileNumber(tutor.mobile)
                            mOtpViewModel.tutorData.observe(viewLifecycleOwner)
                            {
                                if(it.mobile!="")
                                {
                                    mOtpViewModel.updateFcmListTutor(it)
                                    mView.findNavController()
                                        .navigate(R.id.action_verifyOtpFragment_to_homeTutorsFragment)
                                }
                                else
                                {
                                    showToast(requireContext(),"Some Error Occurred")
                                }
                            }


                        }
                        else
                        {

                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            (activity as MainActivity).setBottomNavigationMenu(1)
                            mOtpViewModel.getStudentWithMobileNumber(student.mobile)
                            mOtpViewModel.studentData.observe(viewLifecycleOwner)
                            {
                                if(it.mobile!="")
                                {
                                    mOtpViewModel.updateFcmListStudent(it)
                                    mView.findNavController()
                                        .navigate(R.id.action_verifyOtpFragment_to_homeStudentsFragment)
                                }
                                else
                                {
                                    showToast(requireContext(),"Some Error Occurred")
                                }
                            }
                        }

                    } else {
                        if(tutor.pincode!="")
                        {
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/tutors")
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            (activity as MainActivity).setBottomNavigationMenu(2)
                            tutor.tutorId=authenticatedUser
                            mOtpViewModel.createNewTutor(tutor,authenticatedUser)
                            mOtpViewModel.mCreatedTutorLiveData.observe(viewLifecycleOwner)
                            {
                                if(it)
                                {
                                    Toast.makeText(requireContext(),"Registration Successful!",Toast.LENGTH_LONG).show()
                                    mView.findNavController()
                                        .navigate(R.id.action_verifyOtpFragment_to_homeTutorsFragment)
                                }
                            }
                        }
                        else
                        {

                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            (activity as MainActivity).setBottomNavigationMenu(1)
                            student.studentId=authenticatedUser
                            mOtpViewModel.createNewStudent(student,authenticatedUser)
                            mOtpViewModel.mCreatedUserLiveData.observe(viewLifecycleOwner)
                            {
                                if(it)
                                {
                                    Toast.makeText(requireContext(),"Registration Successful!",Toast.LENGTH_LONG).show()
                                    mView.findNavController()
                                        .navigate(R.id.action_verifyOtpFragment_to_homeStudentsFragment)
                                }
                            }
                        }

                    }

                } else {
                    verifyOtpMutableLiveData.value = false
                    Toast.makeText(requireContext(),"Incorrect otp",Toast.LENGTH_SHORT).show()
                }
                binding.verifyOtpButton.isEnabled = true
            }
        }
        else
        {
            binding.verifyOtpButton.isEnabled = true
            verifyOtpMutableLiveData.value = false

            Toast.makeText(requireContext(),"Incorrect otp",Toast.LENGTH_SHORT).show()
        }

        return verifyOtpMutableLiveData
    }

}