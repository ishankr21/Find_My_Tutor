package com.example.findmytutor.features.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.ActivityMainBinding
import com.example.findmytutor.databinding.FragmentProfileTutorBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ProfileTutorFragment : BaseFragment() {


    var imageURI: Uri? = null
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    var spinnerArrayClass: ArrayList<String> = arrayListOf()
    var spinnerArrayEmployment: ArrayList<String> = arrayListOf()

    private lateinit var mProfileFragmentViewModel: ProfileViewModel
    private var _binding: FragmentProfileTutorBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileTutorBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerArrayClass.add("Select your preferred class to teach")
        for(i in 1..12)
        {
            spinnerArrayClass.add("Class $i")
        }
        spinnerArrayEmployment.add("Select Your Employment Status")
        spinnerArrayEmployment.add("Student")
        spinnerArrayEmployment.add("GOVT. Job")
        spinnerArrayEmployment.add("PRIVATE Job")
        initSpinners()
        mProfileFragmentViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        mView = view

        (activity as MainActivity).setVisibleBottomNavigationView()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    //close app
                    mView.findNavController().navigate(R.id.action_profileTutorFragment_to_homeTutorsFragment)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.profileTutorPhoneEdittext.isEnabled=false
        binding.profileTutorNameEdittext.isEnabled=false
        binding.profileTutorAgeEdittext.isEnabled=false
        binding.profileTutorGenderEdittext.isEnabled=false


        mProfileFragmentViewModel.getTutor()
        mProfileFragmentViewModel.mTutorLiveData.observe(viewLifecycleOwner)
        {
            binding.profileTutorPhoneEdittext.setText(it?.mobile)
            binding.profileTutorNameEdittext.setText(it?.name)
            binding.profileTutorAgeEdittext.setText(it?.age.toString())
            binding.profileTutorGenderEdittext.setText(it?.gender)


            if(it?.emailId!="")
            {
                binding.registerTutorEmailIDEdittext.setText(it?.emailId)
            }


            if(it?.tutorFavouriteSubject!="")
                binding.registerTutorFavouriteSubjectEdittext.setText(it?.tutorFavouriteSubject)

            if(it?.preferredClass!="") {
                binding.spnSelectProfileTutorClass.setSelection(it?.preferredClass?.drop(6)!!.toInt())
            }

            if(it?.pincode!="") {
                binding.registerTutorPincodeEdittext.setText(it?.pincode)
            }
            if(it?.employmentStatus!="")
            {
                when (it?.employmentStatus) {
                    "Student" -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(1)
                    }
                    "GOVT. Job" -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(2)
                    }
                    else -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(3)
                    }
                }
            }

            if(it?.desiredFees!=0.0f)
            {
                binding.registerTutorDesiredFeesEdittext.setText(it?.desiredFees.toString())
            }










            if (it?.profilePicturePath != "") {
                Glide.with(requireContext()).load(it?.profilePicturePath)
                    .into(binding.imageViewProfileTutor)

                if (isAdded) {
                    binding.loadingAnimation.visibility=View.GONE
                }
            } else {
                binding.imageViewProfileTutor.setImageResource(R.drawable.ic_user)
                binding.loadingAnimation.let { it1 ->
                    if (isAdded) {
                        it1.visibility=View.GONE
                    }
                }

            }
        }




        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        binding.fabEditProfilePictureTutor.setOnClickListener {
            showImportImageDialog()
        }

        binding.saveProfileTutorButton.setOnClickListener {
            mProfileFragmentViewModel.getTutor()
            mProfileFragmentViewModel.mTutorLiveData.observe(viewLifecycleOwner)
            {
                val tutorFinalData = Tutor(
                    mobile = it.mobile,
                    name = it.name,
                    gender = it.gender,
                    emailId = binding.registerTutorEmailIDEdittext.text.toString(),
                    age = it.age,
                    profilePicturePath = it.profilePicturePath,
                    preferredClass = binding.spnSelectProfileTutorClass.selectedItem.toString(),
                    tutorFavouriteSubject = binding.registerTutorFavouriteSubjectEdittext.text.toString(),
                    desiredFees = binding.registerTutorDesiredFeesEdittext.text.toString().toFloat(),
                    employmentStatus = binding.spnSelectTutorEmploymentStatus.selectedItem.toString(),
                    pincode = binding.registerTutorPincodeEdittext.text.toString(),
                    tokenId = it.tokenId,
                    fcmTokens = it.fcmTokens
                )
                mProfileFragmentViewModel.storeTutor(tutorFinalData)
                mProfileFragmentViewModel.mTutorDataUploaded.observe(viewLifecycleOwner)
                {
                    if(it)
                    {
                        showToast(requireContext(),"Data saved successfully!")
                        view.findNavController().navigate(R.id.action_profileTutorFragment_to_homeTutorsFragment)
                    }
                }
            }
        }

        binding.profileTutorLogoutButton.setOnClickListener {
            mProfileFragmentViewModel.signOut()
            mView.findNavController().navigate(R.id.action_profileTutorFragment_to_loginFragment)
        }


    }

    private fun initSpinners() {

        val arrayAdapterClass = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArrayClass
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

        binding.spnSelectProfileTutorClass.adapter = arrayAdapterClass

        val arrayAdapterEmployment = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArrayEmployment
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

        binding.spnSelectTutorEmploymentStatus.adapter = arrayAdapterEmployment

    }
    private fun showImportImageDialog() {
        val items = arrayOf("Camera", "Gallery")
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("Select Image")
        dialog.setItems(items) { p0, p1 ->
            if (p1 == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickCamera()
                }
            } else {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickGallery()
                }
            }
        }
        dialog.create().show()
    }
    private fun checkCameraPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result2 = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return result1 and result2
    }

    private fun requestCameraPermission() {
        requestPermissions(cameraPermission, Constants.CAMERA_REQUEST_CODE)
    }
    private fun pickCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "NewImage")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text")
        imageURI = context?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
        startActivityForResult(cameraIntent, Constants.IMAGE_PICK_CAMERA)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        requestPermissions(
            storagePermission,
            Constants.STORAGE_REQUEST_CODE
        )
    }

    private fun pickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.IMAGE_PICK_GALLERY)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted and writeStorageAccepted) {
                        pickCamera()
                    } else {
                        showToast(
                            requireContext(),
                            "Permission Denied "
                        )

                    }
                }
            }
            Constants.STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (writeStorageAccepted) {
                        pickGallery()
                    } else {
                        showToast(
                            requireContext(),
                            "Permission Denied "
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {


            when (requestCode) {
                Constants.IMAGE_PICK_CAMERA -> {
                    CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .start(requireContext(), this@ProfileTutorFragment)
                    //showToast(requireContext(),"${imageURI}")
                }
                Constants.IMAGE_PICK_GALLERY -> {
                    CropImage.activity(data!!.data).setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .start(requireContext(), this)
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.e("Result Code4", resultCode.toString())
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        val resultUri = result.uri

                        if (isAdded) {
                            binding.loadingAnimation.visibility=View.VISIBLE
                        }
                        mProfileFragmentViewModel.uploadPictureToFirebase(resultUri,"Tutor")
                        mProfileFragmentViewModel.mUserPuploaded.observe(viewLifecycleOwner
                        ) {
                            if (it) {
                                if (isAdded) {
                                    binding.loadingAnimation.visibility=View.GONE
                                }
                                binding.imageViewProfileTutor.setImageURI(resultUri)
                            }
                        }
                    }
                }
                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.e("Result Code5", resultCode.toString())
                    val result = CropImage.getActivityResult(data)
                    val error = result.error
                    showToast(
                        requireContext(),
                        " $error"
                    )
                }
            }
        } else {
            //can show progress bar, here make it invisible
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (imageURI != null)
            outState.putString("ImageUri", imageURI.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            imageURI = Uri.parse(savedInstanceState.getString("ImageUri", ""))
        }
    }

}