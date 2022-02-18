package com.example.findmytutor.features.doubtsTutor

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.databinding.FragmentTutorCreateSolutionBinding
import com.example.findmytutor.databinding.ImageDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.text.SimpleDateFormat


class TutorCreateSolutionFragment : BaseFragment() {

    private var _binding: FragmentTutorCreateSolutionBinding? = null
    private val binding get() = _binding!!
    private var solutionInfoCame=SolutionInfo()
    var imageURI: Uri? = null
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    private lateinit var mDoubtTutorViewModel: DoubtTutorViewModel
    var resultUri: Uri?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentTutorCreateSolutionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle=arguments
        solutionInfoCame=bundle!!.getSerializable("solutionInfo") as SolutionInfo
        mDoubtTutorViewModel =
            ViewModelProvider(this)[DoubtTutorViewModel::class.java]


        if (solutionInfoCame.solutionId!="")
        {

            binding.tutorCreateSolutionBtnAddImage.text = "Change Image"
            binding.tutorCreateSolutionDoubtDescription.setText(solutionInfoCame.solutionDescription)
            if(solutionInfoCame.solutionImagePath!="") {
                Glide.with(requireContext())
                    .load(solutionInfoCame.solutionImagePath)
                    .placeholder(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_image_24
                        )
                    )
                    .into(binding.tutorCreateSolutionSolutionImage)

            }

        }

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        binding.tutorCreateSolutionBtnAddImage.setOnClickListener {
            showImportImageDialog()
        }

        binding.tutorCreateSolutionBtnSubmit.setOnClickListener {
            if(binding.tutorCreateSolutionDoubtDescription.text.isNullOrEmpty())
            {
                showToast(requireContext(),"Explanation cannot be empty!")
            }
            else if(binding.tutorCreateSolutionDoubtDescription.toString().length>500)
            {
                showToast(requireContext(),"Explanation length greater than 500")
            }
            else
            {
                showProgressDialog("Saving")
                mDoubtTutorViewModel.getTutorDetails()
                mDoubtTutorViewModel.mTutorLiveDetails.observe(viewLifecycleOwner)
                {
                    val date = Timestamp.now().toDate()

                    val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
                    var solutionInfo= SolutionInfo(
                        doubtId =solutionInfoCame.doubtId ,
                        solvedOn = simpleDateFormat.format(date).toString(),
                        solutionId = "FMTSOL"+ (System.currentTimeMillis()/1000).toString(),
                        tutorId = FirebaseAuth.getInstance().currentUser!!.uid,
                        tutorName = it.name,
                        solutionDescription = binding.tutorCreateSolutionDoubtDescription.text.toString(),
                        studentId = solutionInfoCame.studentId
                    )
                    if(solutionInfoCame.solutionId!="")
                    {
                        solutionInfo.solutionImagePath=solutionInfoCame.solutionImagePath
                        solutionInfo.solvedOn=solutionInfoCame.solvedOn
                        solutionInfo.solutionId=solutionInfoCame.solutionId

                    }


                    mDoubtTutorViewModel.storeTutorSolution(resultUri,solutionInfo)
                    mDoubtTutorViewModel.mSolutionStorageSuccess.observe(viewLifecycleOwner)
                    {
                            storageSuccess ->
                        if (storageSuccess) {
                            dismissProgressDialog()
                            showToast(requireContext(), "Data stored successfully")
                            findNavController().navigate(R.id.action_tutorCreateSolutionFragment_to_doubtsTutorFragment)
                        } else {
                            dismissProgressDialog()
                            showToast(requireContext(), "Some error occurred!")
                        }
                    }
                }


            }
        }


        binding.tutorCreateSolutionSolutionImage.setOnClickListener {
            if(resultUri!=null)
            {
                val zoomPinchDialogBinding = ImageDialogBinding.inflate(
                    LayoutInflater.from(requireContext())
                )
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(zoomPinchDialogBinding.root)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.setCanceledOnTouchOutside(true)
                mAlertDialog.window?.setLayout(1024,1024)
                zoomPinchDialogBinding.pinchToZoomImageView.setImageURI(resultUri)
            }
            if(solutionInfoCame.solutionImagePath!="")
            {
                val zoomPinchDialogBinding = ImageDialogBinding.inflate(
                    LayoutInflater.from(requireContext())
                )
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(zoomPinchDialogBinding.root)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.setCanceledOnTouchOutside(true)
                mAlertDialog.window?.setLayout(1024,1024)
                Glide.with(requireContext())
                    .load(solutionInfoCame.solutionImagePath)
                    .into(zoomPinchDialogBinding.pinchToZoomImageView)
            }
        }

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
                        .start(requireContext(), this@TutorCreateSolutionFragment)
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
                        resultUri = result.uri


                        binding.tutorCreateSolutionSolutionImage.setImageURI(resultUri)

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