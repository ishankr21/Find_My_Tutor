package com.example.findmytutor.features.studentDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.findmytutor.databinding.FragmentStudentDetailsBinding
import com.example.findmytutor.databinding.RatingDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


class StudentDetailsFragment :  BaseFragment() {
    private var _binding: FragmentStudentDetailsBinding? = null
    private val binding get() = _binding!!
    private var tutor= Tutor()
    private lateinit var mStudentDetailsViewModel: StudentDetailsViewModel
    private var student= Student()
    private var requestTutor = RequestTutor()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mStudentDetailsViewModel = ViewModelProvider(this)[StudentDetailsViewModel::class.java]
        val bundle = arguments
        tutor = bundle!!.getSerializable("tutor") as Tutor
        student = bundle.getSerializable("student") as Student
        requestTutor = bundle.getSerializable("requestTutor") as RequestTutor
        showProgressDialog("Loading")
        binding.studentName.text = "Name : " + student.name
        binding.studentAge.text = "Age : " + student.age
        binding.studentParentName.text = "Parent Name : " + student.parentName
        binding.studentSex.text = "Gender : " + student.gender
        binding.studentPhone.text = "Mobile : " + student.mobile
        val sdf = SimpleDateFormat("dd/MM/yy   h:mm a")
        val date = requestTutor.timeOfAcceptance!!.toDate()
        binding.studentDetailsSince.text = "Your Student Since : " + sdf.format(date)
        Glide.with(requireContext()).load(student.profilePicturePath)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
            .into(binding.studentDetailsImgStudnet)
        binding.distanceFromYou.text =
            "Distance from you : " + (Utils.calculateDistanceBetweenStudentTutor(
                tutor.latitude,
                tutor.longitude,
                student.latitude,
                student.longitude
            ) / 1000.0).roundToInt().toString() + " km."

        dismissProgressDialog()

        binding.studentCallButton.setOnClickListener {
            val phone = student.mobile
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        binding.studentMessageButton.setOnClickListener {

            val chattingHelper = ChattingHelper(
                studentId = FirebaseAuth.getInstance().currentUser!!.uid,
                tutorId = tutor.tutorId,
                studentName = student.name,
                tutorName = tutor.name,
                studentImage = student.profilePicturePath,
                tutorImage = tutor.profilePicturePath,
            )
            val bundle = Bundle()
            bundle.putBoolean("isStudent", false)
            bundle.putSerializable("chattingHelper", chattingHelper)
            findNavController().navigate(
                R.id.action_tutorDetailsFragment_to_chatsFragment,
                bundle
            )

        }
        binding.studentNavigateButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${student.latitude},${student.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        binding.studentDetailsDeleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you really want to remove this student?")
                .setPositiveButton("Yes") { dialog, _ ->
                    mStudentDetailsViewModel.disapproveRequest(requestTutor)
                    mStudentDetailsViewModel.disapprovalRequestSuccess.observe(viewLifecycleOwner)
                    {
                        if (it) {
                            showToast(requireContext(), "Student Removed")
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_studentDetailsFragment_to_homeTutorsFragment)
                        } else {
                            showToast(requireContext(), "Some Error Occurred")
                            dialog.dismiss()
                        }
                    }

                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()

                }
                .show()
        }

        binding.btnGivePerformanceReview.setOnClickListener {
            val ratingDialogBinding = RatingDialogBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(ratingDialogBinding.root)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.setCanceledOnTouchOutside(true)
            var currentRating = 0.0
            ratingDialogBinding.star1.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                currentRating = 1.0
            }
            ratingDialogBinding.star2.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                currentRating = 2.0
            }
            ratingDialogBinding.star3.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                ratingDialogBinding.star5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                currentRating = 3.0
            }
            ratingDialogBinding.star4.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_outline_24
                    )
                )
                currentRating = 4.0
            }
            ratingDialogBinding.star5.setOnClickListener {
                ratingDialogBinding.star1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                ratingDialogBinding.star5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                currentRating = 5.0
            }
            ratingDialogBinding.btnSubmitRating.setOnClickListener {
                if(ratingDialogBinding.etFeedback.text.isNullOrEmpty())
                    showToast(requireContext(),"Feedback cannot be empty!")
                else
                {
                    val ratingInfo= RatingInfo(
                        rating = currentRating.toInt(),
                        feedback = ratingDialogBinding.etFeedback.text.toString(),
                        ratedOn = Timestamp.now(),
                        ratedBy = tutor.tutorId,
                        ratedTo = student.studentId,
                        ratedByName = tutor.name,
                        ratedToName = student.name

                    )
                    mStudentDetailsViewModel.storeReview(ratingInfo)
                    showToast(requireContext(),"Rating submitted")
                }
                mAlertDialog.dismiss()
            }

        }
    }
}