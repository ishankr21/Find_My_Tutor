package com.example.findmytutor.features.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.Student
import com.example.findmytutor.databinding.FragmentProfileBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ProfileStudentFragment : BaseFragment() {

    var imageURI: Uri? = null
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    private lateinit var mProfileFragmentViewModel: ProfileViewModel
    private var spinnerArrayClass: Array<String> = arrayOf()
    private var spinnerArraySchoolBoard: Array<String> = arrayOf()
    var spinnerSubjects:Array<String> = arrayOf()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private var latitude:String=""
    private var longitude:String=""
    var isProfileCompleted=true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        spinnerArrayClass= resources.getStringArray(R.array.classes)
        spinnerArraySchoolBoard= resources.getStringArray(R.array.school_board)
        spinnerSubjects=resources.getStringArray(R.array.subjects)
        spinnerSubjects[0]="Select the subject you help in"

        val bundle=arguments
        isProfileCompleted=bundle!!.getBoolean("isProfileCompleted")
        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileFragmentViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        if (!isProfileCompleted) {

            (activity as MainActivity).hideBottomNavigationView()

        }
        else {

            (activity as MainActivity).setVisibleBottomNavigationView()
            binding.txtGetMyLocation.text="Update Location"
        }
        initSpinners()



        mView = view

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                   if(isProfileCompleted)
                    mView.findNavController().navigate(R.id.action_profileStudentFragment_to_homeStudentsFragment)
                    else
                       requireActivity().finishAffinity()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        binding.profileStudentPhoneEdittext.isEnabled=false
        binding.profileStudentNameEdittext.isEnabled=false

        binding.profileStudentGenderEdittext.isEnabled=false
        binding.registerStudentParentNameEdittext.isEnabled=false
        showProgressDialog("Loading")
        mProfileFragmentViewModel.getStudent()
        mProfileFragmentViewModel.mStudentLiveData.observe(viewLifecycleOwner)
        {
            binding.profileStudentPhoneEdittext.setText(it.mobile)
            binding.profileStudentNameEdittext.setText(it.name)
            binding.profileStudentAgeEdittext.setText(it.age.toString())
            binding.profileStudentGenderEdittext.setText(it.gender)
            binding.registerStudentParentNameEdittext.setText(it.parentName)

            if(it?.emailId!="")
            {
                binding.registerStudentEmailIDEdittext.setText(it.emailId)
            }


            if(it?.leastFavouriteSubject!="")
            {
                var position= 0
                for (i in spinnerSubjects.indices)
                {
                    if(spinnerSubjects[i]==it.leastFavouriteSubject)
                    {
                        position=i
                    }

                }
                binding.spnRegisterStudentBadSubject.setSelection(position)
            }

            if(it?.studentClass!="") {
                binding.spnSelectProfileStudentClass.setSelection(it?.studentClass?.drop(6)!!.toInt())
            }
            if(it.schoolBoard!="")
            {
                when (it.schoolBoard) {
                    "CBSE" -> {
                        binding.spnSelectProfileStudentSchoolBoard.setSelection(1)
                    }
                    "ICSE" -> {
                        binding.spnSelectProfileStudentSchoolBoard.setSelection(2)
                    }
                    else -> {
                        binding.spnSelectProfileStudentSchoolBoard.setSelection(3)
                    }
                }
            }

            if(it.schoolName!="")
            {
                binding.registerStudentSchoolNameEdittext.setText(it.schoolName)
            }
            if(it.latitude!=0.0)
            {

                latitude=it.latitude.toString()
            }
            if(it.longitude!=0.0)
            {
                longitude=it.longitude.toString()
            }
            if (it.profilePicturePath != "") {
                Glide.with(requireContext()).load(it.profilePicturePath)
                    .into(binding.imageViewProfile)

                if (isAdded) {
                    binding.loadingAnimation.visibility=View.GONE
                }
            } else {
                binding.imageViewProfile.setImageResource(R.drawable.ic_user)
                binding.loadingAnimation.let { it1 ->
                    if (isAdded) {
                        it1.visibility=View.GONE
                    }
                }

            }
        }


            dismissProgressDialog()




        binding.fabEditProfilePicture.setOnClickListener {
            showImportImageDialog()
        }

        binding.saveProfileButton.setOnClickListener {
            mProfileFragmentViewModel.getStudent()
            mProfileFragmentViewModel.mStudentLiveData.observe(viewLifecycleOwner)
            { student ->


                if(binding.registerStudentEmailIDEdittext.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Email Id cannot be empty")
                }
                else  if(!binding.registerStudentEmailIDEdittext.text.isValidEmail())
                {
                    showToast(requireContext(),"Email Id invalid!")
                }
                else if(binding.spnSelectProfileStudentClass.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Please provide us your current class.")
                }

                else   if(binding.spnSelectProfileStudentSchoolBoard.selectedItemPosition==0)
                {
                    showToast(requireContext(),"School board cannot be empty")
                }
                else if(binding.registerStudentSchoolNameEdittext.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Please enter your school's name")
                }
                else if(binding.spnRegisterStudentBadSubject.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Please enter your weakest subject")
                }
                else if(binding.profileStudentAgeEdittext.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Age cannot be empty")
                }
                else if(binding.profileStudentAgeEdittext.text.toString().toInt()>100 || binding.profileStudentAgeEdittext.text.toString().toInt()<=5) {
                    Toast.makeText(
                        requireContext(),
                        "Age must be between 6 to 99",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else   if(latitude=="")
                {
                    mProfileFragmentViewModel.getLocusCurrentLocation(requireContext())
                    mProfileFragmentViewModel.addressLiveData.observe(viewLifecycleOwner)
                    { latlong ->
                        if (latlong.first.isNotEmpty()) {
                            latitude = latlong.first
                            longitude = latlong.second
                        }
                    }
                }
                else {
                    val studentFinalData = Student(
                        studentId=student.studentId,
                        mobile = student.mobile,
                        name = student.name,
                        gender = student.gender,
                        emailId = binding.registerStudentEmailIDEdittext.text.toString(),
                        parentName = student.parentName,
                        age = binding.profileStudentAgeEdittext.text.toString().toInt(),
                        profilePicturePath = student.profilePicturePath,
                        studentClass = binding.spnSelectProfileStudentClass.selectedItem.toString(),
                        leastFavouriteSubject = binding.spnRegisterStudentBadSubject.selectedItem.toString(),
                        schoolBoard = binding.spnSelectProfileStudentSchoolBoard.selectedItem.toString(),
                        schoolName = binding.registerStudentSchoolNameEdittext.text.toString(),
                        tokenId = student.tokenId,
                        fcmTokens = student.fcmTokens,
                        latitude = latitude.toDouble(),
                        longitude = longitude.toDouble(),
                        profileIsComplete = true
                    )
                    mProfileFragmentViewModel.storeStudent(studentFinalData)
                    mProfileFragmentViewModel.mStudentDataUpdated.observe(viewLifecycleOwner)
                    {
                        if (it) {
                            showToast(requireContext(), "Data saved successfully!")
                            view.findNavController()
                                .navigate(R.id.action_profileStudentFragment_to_homeStudentsFragment)
                        }
                    }
                }
            }
        }

        binding.profileLogoutButton.setOnClickListener {


             AlertDialog.Builder(requireContext())
                .setTitle("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->

                    FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                    mProfileFragmentViewModel.signOut()
                    mView.findNavController().navigate(R.id.action_profileStudentFragment_to_loginFragment)
                }
                .setNegativeButton("Cancel"){dialog,_->
                    dialog.cancel()

                }
                .show()

        }
        binding.getMyLocation.setOnClickListener {
            getLocation()
        }
        binding.profileBackButton.setOnClickListener {
            if(isProfileCompleted)
                mView.findNavController().navigate(R.id.action_profileStudentFragment_to_homeStudentsFragment)
            else
                requireActivity().finishAffinity()
        }

    }
    private fun getLocation()
    {
        mProfileFragmentViewModel.getLocusCurrentLocation(requireContext())
        mProfileFragmentViewModel.addressLiveData.observe(viewLifecycleOwner)
        {
            if(it.first.isNotEmpty())
            {
                latitude=it.first
                longitude=it.second


                showToast(requireContext(),"Location Obtained Successfully")
            }

        }
    }
    private fun initSpinners() {
        val arrayAdapterSubject = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerSubjects
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

        binding.spnRegisterStudentBadSubject.adapter = arrayAdapterSubject
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


        binding.spnSelectProfileStudentClass.adapter = arrayAdapterClass

        val arrayAdapterSchoolBoard = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArraySchoolBoard
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

        binding.spnSelectProfileStudentSchoolBoard.adapter = arrayAdapterSchoolBoard

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
                        .start(requireContext(), this@ProfileStudentFragment)
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
                            showProgressDialog("Saving")
                        }
                        mProfileFragmentViewModel.uploadPictureToFirebase(resultUri,"Students")
                        mProfileFragmentViewModel.mUserPuploaded.observe(viewLifecycleOwner
                        ) {
                            if (it) {
                                if (isAdded) {
                                    binding.loadingAnimation.visibility=View.GONE

                                }
                                binding.imageViewProfile.setImageURI(resultUri)
                                dismissProgressDialog()
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

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}