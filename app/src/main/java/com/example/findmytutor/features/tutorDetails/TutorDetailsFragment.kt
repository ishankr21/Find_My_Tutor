package com.example.findmytutor.features.tutorDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentTutorDetailsBinding
import com.example.findmytutor.features.MainActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject


class TutorDetailsFragment : BaseFragment() {
    private var _binding: FragmentTutorDetailsBinding? = null
    private val binding get() = _binding!!
    private var tutor= Tutor()
    private lateinit var mTutorDetailsViewModel: TutorDetailsViewModel
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAYBYkWKg:APA91bH3oEjFVGuvqkbQVwuC32AdhL0oAz8ii4JjBSZtSesjElQ-nCRebMQYcn_F37IfDGEhm2Gg97c970hlBL5-ebFhA5nHB88jKlGRH9VEFSOzHgs9IGqYkh8SRwG4idwyH5bkuW--"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }

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
        val bundle = arguments
        tutor = bundle!!.getSerializable("tutor") as Tutor


        binding.tutorDetailsTutorName.text = tutor.name
        binding.tutorDetailsTutorAge.text = "Age : " + tutor.age.toString()
        binding.tutorDetailsTutorGender.text = "Gender : " + tutor.gender
        binding.tutorDetailsTutorMobileNumber.text = "Mobile No. : " + tutor.mobile.drop(3)
        if (tutor.emailId != "") {
            binding.tutorDetailsTutorEmailId.visibility = View.VISIBLE
            binding.tutorDetailsTutorEmailId.text = "Email id : " + tutor.emailId
        }
        if (tutor.rating != 0) {
            binding.tutorDetailsTutorRating.visibility = View.VISIBLE
            binding.tutorDetailsTutorRating.text = tutor.rating.toString()
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
            mTutorDetailsViewModel.getStudent()
            mTutorDetailsViewModel.mStudentLiveData.observe(viewLifecycleOwner)
            {
                val request = RequestTutor(
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
                { requestSent ->
                    if (requestSent) {
                        showToast(requireContext(), "Request Sent")
                        //topic is set as the id of the receiver
                        val topic = "/topics/${tutor.tutorId}"
                        val notificationData = hashMapOf<String, String>()
                        notificationData["intentType"] = "approvalIntent"
                        val notification = JSONObject()
                        val notificationBody = JSONObject()
                        notificationBody.put("intentType", "approvalIntent")


                        try {
                            notificationBody.put("title", "You have received request from a student")
                            notificationBody.put(
                                "message",
                                "Please click here to approve the student's \n request"
                            )   //Enter your notification message
                            notification.put("to", topic)
                            notification.put("data", notificationBody)
                            Log.e("TAG", "try")
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }

                        sendNotification(notification)


                    } else
                        showToast(requireContext(), "Some Error Occurred!")
                }

            }

        }
    }



private fun sendNotification(notification: JSONObject) {
    Log.e("TAG", "sendNotification")
    val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
        Response.Listener<JSONObject> { response ->
            Log.i("TAG", "onResponse: $response")

        },
        Response.ErrorListener {
            Toast.makeText(requireContext(), "Request error", Toast.LENGTH_LONG).show()
            Log.i("TAG", "onErrorResponse: Didn't work")
        }) {

        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["Authorization"] = serverKey
            params["Content-Type"] = contentType
            return params
        }
    }
    requestQueue.add(jsonObjectRequest)
}

}