package com.example.findmytutor.features.tutorDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.ChattingHelper
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.dataClasses.RatingInfo
import com.example.findmytutor.databinding.FragmentTutorDetailsBinding

import com.example.findmytutor.databinding.RatingDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.SendNotification
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject


class TutorDetailsFragment : BaseFragment() {
    private var _binding: FragmentTutorDetailsBinding? = null
    private val binding get() = _binding!!
    private var tutor= Tutor()
    private lateinit var mTutorDetailsViewModel: TutorDetailsViewModel
    private var student= Student()
    private var requestTutor=RequestTutor()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).hideBottomNavigationView()
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
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_tutorDetailsFragment_to_homeStudentsFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.tutorDetailsBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_tutorDetailsFragment_to_homeStudentsFragment)
        }
        mTutorDetailsViewModel =
            ViewModelProvider(this)[TutorDetailsViewModel::class.java]
        val bundle = arguments
        tutor = bundle!!.getSerializable("tutor") as Tutor
        student = bundle.getSerializable("student") as Student

        showProgressDialog("Loading")
        mTutorDetailsViewModel.getAllAcceptedStudents(tutor.tutorId)
        mTutorDetailsViewModel.getAllAcceptedStudents.observe(viewLifecycleOwner)
        {
            var pos=-1
            for(i in it)
            {
                if(i.studentId==student.studentId) {
                    pos = 1
                    requestTutor=i
                }
            }
            if(pos==1)
            {
                dismissProgressDialog()
                binding.tutorSendRequest.visibility=View.GONE
                binding.btnRateTutor.visibility=View.VISIBLE
                binding.btnPayTutor.visibility=View.VISIBLE
                binding.tutorDetailsDeleteButton.visibility=View.VISIBLE
            }
            else
            {
                dismissProgressDialog()
            }
        }

        binding.tutorDetailsTutorName.text = tutor.name
        binding.tutorDetailsTutorAge.text = "Age : " + tutor.age.toString()
        binding.tutorDetailsTutorGender.text = "Gender : " + tutor.gender
        binding.tutorDetailsTutorMobileNumber.text = "Mobile No. : " + tutor.mobile.drop(3)
        if (tutor.emailId != "") {
            binding.tutorDetailsTutorEmailId.visibility = View.VISIBLE
            binding.tutorDetailsTutorEmailId.text = "Email id : " + tutor.emailId
        }
        if (tutor.rating.size != 0) {
            var sum=0.0
            for (i in tutor.rating)
                sum+=i
            val rating:Double=(sum/ (tutor.rating.size))

            binding.tutorDetailsTutorRating.visibility = View.VISIBLE
            binding.tutorDetailsTutorRating.text = rating.toString().subSequence(0,3)
        }
        if (tutor.desiredFees != 0.0f) {
            binding.tutorDetailsTutorFees.visibility = View.VISIBLE
            binding.tutorDetailsTutorFees.text = "Fees : " + tutor.desiredFees.toString()
        }
        if (tutor.preferredClass != "") {
            binding.tutorDetailsTutorClass.visibility = View.VISIBLE
            binding.tutorDetailsTutorClass.text = "Preferred Class : " + tutor.preferredClass
        }
        if (tutor.tutorFavouriteSubject != "") {
            binding.tutorDetailsTutorFavouriteSubject.visibility = View.VISIBLE
            binding.tutorDetailsTutorFavouriteSubject.text =
                "Speciality in : " + tutor.tutorFavouriteSubject
        }



        if (tutor.profilePicturePath != "") {
            Glide.with(requireContext()).load(tutor.profilePicturePath)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                .into(binding.tutorDeailsImgTutor)


        } else {
            binding.tutorDeailsImgTutor.setImageResource(R.drawable.ic_user)


        }


        binding.tutorSendRequest.setOnClickListener {
            showProgressDialog("Wait")
            mTutorDetailsViewModel.getAllAcceptedOrPendingStudent(tutor.tutorId)
            mTutorDetailsViewModel.getAllAcceptedOrPendingStudents.observe(viewLifecycleOwner)
            {list->

                dismissProgressDialog()
                if(list.contains(student.studentId))
                {
                    showToast(requireContext(),"Your previous request is pending....please wait before raising new request")
                }
                else
                {
                    val request = RequestTutor(
                        tutorId = tutor.tutorId,
                        studentId = FirebaseAuth.getInstance().uid!!,
                        timeOfRequest = Timestamp.now(),
                        isCompleted = false,
                        isDeclined = false,
                        studentName = student.name,
                        tutorName = tutor.name

                    )

                    mTutorDetailsViewModel.sendRequest(request)
                    mTutorDetailsViewModel.requestsSent.observe(viewLifecycleOwner)
                    { requestSent ->
                        if (requestSent) {
                            showToast(requireContext(), "Request Sent")

                            val topic = "/topics/${tutor.tutorId}"
                            val notification = JSONObject()
                            val notificationBody = JSONObject()
                            notificationBody.put("intentType", "approvalIntent")


                            try {
                                notificationBody.put("title", "You have received request from a student")
                                notificationBody.put(
                                    "message",
                                    "Please click here to approve the student's request"
                                )   //Enter your notification message
                                notification.put("to", topic)
                                notification.put("data", notificationBody)
                                Log.e("TAG", "try")
                            } catch (e: JSONException) {
                                Log.e("TAG", "onCreate: " + e.message)
                            }

                            SendNotification(requireContext()).sendNotification(notification)


                        } else
                            showToast(requireContext(), "Some Error Occurred!")
                    }

                }
            }



        }

        binding.tutorCallButton.setOnClickListener {
            val phone = tutor.mobile
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        binding.tutorMessageButton.setOnClickListener {
            mTutorDetailsViewModel.getStudent()
            mTutorDetailsViewModel.mStudentLiveData.observe(viewLifecycleOwner)
            {
                val chattingHelper = ChattingHelper(
                    studentId = FirebaseAuth.getInstance().currentUser!!.uid,
                    tutorId = tutor.tutorId,
                    studentName = it.name,
                    tutorName = tutor.name,
                    studentImage = it.profilePicturePath,
                    tutorImage = tutor.profilePicturePath,
                    )
                val bundle = Bundle()
                bundle.putBoolean("isStudent",true)
                bundle.putSerializable("chattingHelper", chattingHelper)
                findNavController().navigate(
                    R.id.action_tutorDetailsFragment_to_chatsFragment,
                    bundle
                )
            }
        }
        binding.tutorNavigateButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${tutor.latitude},${tutor.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        binding.btnRateTutor.setOnClickListener {
            val ratingDialogBinding = RatingDialogBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(ratingDialogBinding.root)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.setCanceledOnTouchOutside(true)
            var currentRating=0.0
            ratingDialogBinding.star1.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                currentRating=1.0
            }
            ratingDialogBinding.star2.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                currentRating=2.0
            }
            ratingDialogBinding.star3.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                currentRating=3.0
            }
            ratingDialogBinding.star4.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_outline_24))
                currentRating=4.0
            }
            ratingDialogBinding.star5.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
                currentRating=5.0
            }
            ratingDialogBinding.btnSubmitRating.setOnClickListener {
                if (ratingDialogBinding.etFeedback.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Feedback cannot be empty!")
                }
                else
                {
                    val ratingArray=tutor.rating
                    ratingArray.add(currentRating.toInt())
                    mTutorDetailsViewModel.updateTutorRatings(ratingArray,tutor.tutorId)

                    var sum=0.0
                    for (i in tutor.rating)
                        sum+=i
                    val rating:Double=(sum/ (tutor.rating.size))
                    val ratingInfo= RatingInfo(
                        rating = currentRating.toInt(),
                        feedback = ratingDialogBinding.etFeedback.text.toString(),
                        ratedOn = Timestamp.now(),
                        ratedBy = student.studentId,
                        ratedTo = tutor.tutorId,
                        ratedByName = student.name,
                        ratedToName = tutor.name

                    )
                    mTutorDetailsViewModel.storeReview(ratingInfo)
                    showToast(requireContext(),"Tutor Rated")
                    binding.tutorDetailsTutorRating.visibility = View.VISIBLE
                    binding.tutorDetailsTutorRating.text = rating.toString().subSequence(0,3)
                }

                mAlertDialog.dismiss()
            }



        }


        binding.tutorDetailsDeleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you really want to remove this this tutor?")
                .setPositiveButton("Yes") { dialog, _ ->
                    mTutorDetailsViewModel.deleteTutor(requestTutor)
                        mTutorDetailsViewModel.deleteSuccess.observe(viewLifecycleOwner)
                    {
                        if(it)
                        {
                            showToast(requireContext(),"Tutor Removed")
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_tutorDetailsFragment_to_homeStudentsFragment)
                        }
                        else
                        {
                            showToast(requireContext(),"Some Error Occurred")
                            dialog.dismiss()
                        }
                    }

                }
                .setNegativeButton("Cancel"){dialog,_->
                    dialog.cancel()

                }
                .show()
        }
        binding.btnPayTutor.setOnClickListener {
            val bundle=Bundle()
            bundle.putSerializable("student",student)
            bundle.putSerializable("tutor",tutor)
            findNavController().navigate(R.id.action_tutorDetailsFragment_to_makePaymentFragment,bundle)
        }

    }




}