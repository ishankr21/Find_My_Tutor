package com.example.findmytutor.features.profile

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.findmytutor.dataClasses.Tutor
import com.example.findmytutor.databinding.FragmentProfileTutorBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.zxing.integration.android.IntentIntegrator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

const val QR_CODE_SCAN = 0x0000c0de
class ProfileTutorFragment : BaseFragment() {


    private var imageURI: Uri? = null
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>
    var spinnerArrayClass: Array<String> = arrayOf()
    var spinnerArrayEmployment: Array<String> = arrayOf()
    var spinnerSchoolBoards:Array<String> = arrayOf()
    var spinnerSubjects:Array<String> = arrayOf()
    private lateinit var mProfileFragmentViewModel: ProfileViewModel
    private lateinit var qrCodeScanIntegrator: IntentIntegrator
    private var _binding: FragmentProfileTutorBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private var latitude:String=""
    private var longitude:String=""
    private var upiId:String=""


    var isProfileCompleted=true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val bundle=arguments
        isProfileCompleted=bundle!!.getBoolean("isProfileCompleted")
        _binding = FragmentProfileTutorBinding.inflate(inflater, container, false)
        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileFragmentViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        setUpScanner()
        if (!isProfileCompleted) {

            (activity as MainActivity).hideBottomNavigationView()
            //getLocation()

        }
        else {

            (activity as MainActivity).setVisibleBottomNavigationView()
            binding.txtGetMyLocation.text="Update My Location"
        }
        spinnerArrayClass=resources.getStringArray(R.array.classes)
        spinnerArrayClass[0]="Please select your preferred class"
        spinnerArrayEmployment=resources.getStringArray(R.array.employment_status)
        spinnerSchoolBoards=resources.getStringArray(R.array.school_board)
        spinnerSchoolBoards[0]="Please select your preferred school board"
        spinnerSubjects=resources.getStringArray(R.array.subjects)
        spinnerSubjects[0]="Select the subject you want to teach"
        initSpinners()

        mView = view


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (isProfileCompleted)
                    mView.findNavController().navigate(R.id.action_profileTutorFragment_to_homeTutorsFragment)
                    else
                        requireActivity().finishAffinity()


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.profileTutorPhoneEdittext.isEnabled=false
        binding.profileTutorNameEdittext.isEnabled=false

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
            {
                var position= 0
                for (i in spinnerSubjects.indices)
                {
                    if(spinnerSubjects[i]==it.tutorFavouriteSubject)
                    {
                        position=i
                    }

                }
                binding.spnRegisterTutorFavouriteSubject.setSelection(position)
            }

            if(it?.preferredClass!="") {
                binding.spnSelectProfileTutorClass.setSelection(it?.preferredClass?.drop(6)!!.toInt())
            }


            if(it.employmentStatus!="")
            {
                when (it.employmentStatus) {
                    "Student" -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(3)
                    }
                    "Private Job" -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(1)
                    }
                    "Government Job"->{
                        binding.spnSelectTutorEmploymentStatus.setSelection(2)
                    }
                    else -> {
                        binding.spnSelectTutorEmploymentStatus.setSelection(4)
                    }
                }
            }
            if(it.preferredSchoolBoard!="")
            {
                when (it.preferredSchoolBoard) {
                    "CBSE" -> {
                        binding.spnSelectProfileTutorSchoolBoard.setSelection(1)
                    }
                    "ICSE" -> {
                        binding.spnSelectProfileTutorSchoolBoard.setSelection(2)
                    }
                    else -> {
                        binding.spnSelectProfileTutorSchoolBoard.setSelection(3)
                    }
                }
            }
            if(it.desiredFees!=0.0f)
            {
                binding.registerTutorDesiredFeesEdittext.setText(it.desiredFees.toString())
            }
            if(it.latitude!=0.0)
            {

                latitude=it.latitude.toString()
            }
            if(it.longitude!=0.0)
            {
                longitude=it.longitude.toString()
            }
            if (it.upiId!="")
            {
                upiId=it.upiId
                binding.imgUpiAdded.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_check_circle_24))
                binding.txtGetMyUPI.text="UPDATE UPI"
            }
            if (it.profilePicturePath != "") {
                Glide.with(requireContext()).load(it.profilePicturePath)
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
                if(binding.registerTutorEmailIDEdittext.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Email Id cannot be empty")
                }
                else if(!binding.registerTutorEmailIDEdittext.text.toString().isValidEmail())
                {
                    showToast(requireContext(),"Email Id invalid")
                }
                else if(binding.spnSelectProfileTutorClass.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Please provide us your preferred class.")
                }

                else   if(binding.spnSelectTutorEmploymentStatus.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Employment status cannot be empty")
                }
                else   if(binding.registerTutorDesiredFeesEdittext.text.isNullOrEmpty())
                {
                    showToast(requireContext(),"Please enter your desired fees")
                }
                else   if(binding.registerTutorDesiredFeesEdittext.text.toString().toDouble()>10000.0)
                {
                    showToast(requireContext(),"Please fees cannot be greater that Rs. 10000!")
                }
                else   if(binding.spnRegisterTutorFavouriteSubject.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Please enter your preferred subject")
                }
                else if(binding.spnSelectProfileTutorSchoolBoard.selectedItemPosition==0)
                {
                    showToast(requireContext(),"Please select your preferred school board")
                }
                else if(binding.profileTutorAgeEdittext.text.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Age cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(binding.profileTutorAgeEdittext.text.toString().toInt()>100 || binding.profileTutorAgeEdittext.text.toString().toInt()<18) {
                    Toast.makeText(
                        requireContext(),
                        "Age must be between 18 to 99",
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
                else
                {
                    val tutorFinalData = Tutor(
                        tutorId=it.tutorId,
                        mobile = it.mobile,
                        name = it.name,
                        gender = it.gender,
                        emailId = binding.registerTutorEmailIDEdittext.text.toString(),
                        age = binding.profileTutorAgeEdittext.text.toString().toInt(),
                        profilePicturePath = it.profilePicturePath,
                        preferredClass = binding.spnSelectProfileTutorClass.selectedItem.toString(),
                        tutorFavouriteSubject = binding.spnRegisterTutorFavouriteSubject.selectedItem.toString(),
                        desiredFees = binding.registerTutorDesiredFeesEdittext.text.toString().toFloat(),
                        employmentStatus = binding.spnSelectTutorEmploymentStatus.selectedItem.toString(),
                        tokenId = it.tokenId,
                        fcmTokens = it.fcmTokens,
                        latitude = latitude.toDouble(),
                        longitude = longitude.toDouble(),
                        profileIsComplete = true,
                        rating = it.rating,
                        preferredSchoolBoard = binding.spnSelectProfileTutorSchoolBoard.selectedItem.toString(),
                        upiId = upiId
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
        }

        binding.profileTutorLogoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/tutors")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
            mProfileFragmentViewModel.signOut()
            mView.findNavController().navigate(R.id.action_profileTutorFragment_to_loginFragment)
                }
                .setNegativeButton("Cancel"){dialog,_->
                    dialog.cancel()

                }
                .show()
        }

        binding.profileTutorBackButton.setOnClickListener {
            if(isProfileCompleted)
                mView.findNavController().navigate(R.id.action_profileTutorFragment_to_homeTutorsFragment)
            else
                requireActivity().finishAffinity()
        }

        binding.getMyLocation.setOnClickListener {

            getLocation()
        }



        binding.getMyUpiId.setOnClickListener {
            if(requestPermission())
            {
                startScan()
                showProgressDialog("Please Wait")
                dismissProgressDialog()
            }
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


                showToast(requireContext(),"Location Updated Successfully")
            }

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


        val arrayAdapterSchoolBoard = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerSchoolBoards
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

        binding.spnSelectProfileTutorSchoolBoard.adapter = arrayAdapterSchoolBoard

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

        binding.spnRegisterTutorFavouriteSubject.adapter = arrayAdapterSubject

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
                QR_CODE_SCAN -> {
                    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

                    if (result == null) {

                       dismissProgressDialog()
                       showToast(
                            requireContext(), "Some  Occurred"
                        )
                    } else {
                        val content = result.contents
                        val parts = content.split("&").toMutableList()
                        var mc = "0000"
                        parts[0] = parts[0].drop(10)
                        for (i in parts) {
                            if (i[0] == 'm' && i[1] == 'c') {
                                mc = i.drop(3)
                            }
                        }
                        if (mc == "0000") {
                            showToast(
                                requireContext(),
                                "The scanned UPI ID is not a merchant account, please provide merchant account UPI ID!",
                            )
                            dismissProgressDialog()
                        } else {
                                mProfileFragmentViewModel.addUpiId(content)
                                mProfileFragmentViewModel.upiIdAddedSuccessfully.observe(viewLifecycleOwner)
                                {
                                    upiId=content
                                    if(it)
                                    {
                                        showToast(requireContext(),"UPI added successfully")
                                        binding.imgUpiAdded.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_check_circle_24))
                                        binding.txtGetMyUPI.text="UPDATE UPI"
                                    }
                                    else
                                    {
                                        showToast(requireContext(),"some error occurred")
                                    }

                                    dismissProgressDialog()
                                }
                        }

                    }
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

    private fun requestPermission(): Boolean {
        var check = false
        Dexter.withContext(requireActivity()).withPermissions(
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport?) {
                if (multiplePermissionsReport!!.areAllPermissionsGranted()) {
                    check = true
                } else {
                    showToast(requireContext(),
                        "Please provide permission to access this function",
                    )
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                permissionToken: PermissionToken?
            ) {
                permissionToken!!.continuePermissionRequest()
            }
        }
        ).onSameThread().check()

        if (check) return true
        return false
    }

    private fun setUpScanner() {
        qrCodeScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrCodeScanIntegrator.setOrientationLocked(true)
        qrCodeScanIntegrator.setBeepEnabled(false)
        qrCodeScanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrCodeScanIntegrator.setPrompt("Make Sure the rectangle covers the QR Code")
    }

    private fun startScan() {
        qrCodeScanIntegrator.initiateScan()
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}