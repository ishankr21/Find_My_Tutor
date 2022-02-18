package com.example.findmytutor.features.doubtsStudent

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.databinding.FragmentCreateNewDoubtBinding
import com.example.findmytutor.databinding.ImageDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.utilities.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.text.SimpleDateFormat


class CreateNewDoubtFragment : BaseFragment() {
    var imageURI: Uri? = null
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    private lateinit var mDoubtStudentViewModel: DoubtStudentViewModel
    var resultUri:Uri?=null
    private var _binding: FragmentCreateNewDoubtBinding? = null
    private val binding get() = _binding!!
    private var doubtInfoCame=DoubtInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentCreateNewDoubtBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val bundle=arguments
            doubtInfoCame=bundle!!.getSerializable("doubtInfo") as DoubtInfo


        if(doubtInfoCame.doubtId!="")
        {
            binding.btnAddImage.visibility=View.GONE
            binding.btnCreateDoubt.visibility=View.GONE
            binding.btnChangeImage.visibility=View.VISIBLE
            binding.btnEndDoubt.visibility=View.VISIBLE
            binding.btnSaveEditDoubt.visibility=View.VISIBLE
            showProgressDialog("Loading")
            binding.doubtTitle.setText(doubtInfoCame.doubtTitle)
            binding.doubtDescription.setText(doubtInfoCame.doubtDescription)

            if(doubtInfoCame.doubtImagePath!="")
            {
                Glide.with(requireContext())
                    .load(doubtInfoCame.doubtImagePath)
                    .placeholder(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_image_24))
                    .into(binding.doubtImage)
                dismissProgressDialog()
            }
            else
                dismissProgressDialog()

        }





        mDoubtStudentViewModel =
            ViewModelProvider(this)[DoubtStudentViewModel::class.java]
        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        binding.btnAddImage.setOnClickListener {
            showImportImageDialog()
        }
        binding.btnChangeImage.setOnClickListener {

            showImportImageDialog()
        }
        binding.doubtImage.setOnClickListener {
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
            if(doubtInfoCame.doubtImagePath!="")
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
                    .load(doubtInfoCame.doubtImagePath)
                    .into(zoomPinchDialogBinding.pinchToZoomImageView)
            }
        }
        binding.btnCreateDoubt.setOnClickListener {
            if(binding.doubtTitle.text.isNullOrEmpty()||binding.doubtDescription.text.isNullOrEmpty())
            {
                showToast(requireContext(),"Please enter title and description!")
            }
            else if(binding.doubtDescription.text.toString().length>500)
            {
                showToast(requireContext(),"Description length greater than 500")
            }
            else
            {
                showProgressDialog("Saving")
                mDoubtStudentViewModel.getStudentData()
                mDoubtStudentViewModel.mStudentLiveData.observe(viewLifecycleOwner)
                {
                    val date = Timestamp.now().toDate()

                    val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")

                    val doubtInfo=DoubtInfo(
                        doubtId = "FMTDBT"+ (System.currentTimeMillis()/1000).toString(),
                        createdOn =  simpleDateFormat.format(date).toString(),
                        studentId = FirebaseAuth.getInstance().currentUser!!.uid,
                        studentName = it.name,
                        doubtTitle = binding.doubtTitle.text.toString(),
                        doubtDescription = binding.doubtDescription.text.toString(),
                        isClosed = false
                    )
                    mDoubtStudentViewModel.storeStudentDoubt(resultUri,doubtInfo)
                    mDoubtStudentViewModel.mDoubtStorageSuccess.observe(viewLifecycleOwner)
                    {storageSuccess->
                        if(storageSuccess)
                        {
                            dismissProgressDialog()
                            showToast(requireContext(),"Data stored successfully")
                            findNavController().navigate(R.id.action_createNewDoubtFragment_to_homeStudentsFragment)
                        }
                        else {
                            dismissProgressDialog()
                            showToast(requireContext(), "Some error occurred!")
                        }
                    }
                }

            }
        }




        binding.btnSaveEditDoubt.setOnClickListener {
            if(binding.doubtTitle.text.isNullOrEmpty()||binding.doubtDescription.text.isNullOrEmpty())
            {
                showToast(requireContext(),"Please enter title and description!")
            }
            else if(binding.doubtDescription.text.toString().length>500)
            {
                showToast(requireContext(),"Description length greater than 500")
            }
            else
            {
                //showToast(requireContext(),"${resultUri}")
                showProgressDialog("Saving")

                    val doubtInfo=DoubtInfo(
                        doubtId =doubtInfoCame.doubtId ,
                        createdOn = doubtInfoCame.createdOn,
                        studentId = FirebaseAuth.getInstance().currentUser!!.uid,
                        studentName = doubtInfoCame.studentName,
                        doubtTitle = binding.doubtTitle.text.toString(),
                        doubtDescription = binding.doubtDescription.text.toString(),
                        isClosed = false,
                        doubtImagePath = doubtInfoCame.doubtImagePath,
                        noOfSolutions = doubtInfoCame.noOfSolutions
                    )
                    mDoubtStudentViewModel.storeStudentDoubt(resultUri,doubtInfo)
                    mDoubtStudentViewModel.mDoubtStorageSuccess.observe(viewLifecycleOwner)
                    { storageSuccess ->
                        if (storageSuccess) {
                            dismissProgressDialog()
                            showToast(requireContext(), "Data stored successfully")
                            findNavController().navigate(R.id.action_createNewDoubtFragment_to_homeStudentsFragment)
                        } else {
                            dismissProgressDialog()
                            showToast(requireContext(), "Some error occurred!")
                        }

                    }
                    }
        }

        binding.btnEndDoubt.setOnClickListener {
            mDoubtStudentViewModel.markAsDone(doubtInfoCame.doubtId)
            mDoubtStudentViewModel.mMarkAsDoneSuccess.observe(viewLifecycleOwner)
            {
                if(it)
                {
                    showToast(requireContext(),"Doubt Closed")
                    findNavController().navigate(R.id.action_createNewDoubtFragment_to_homeStudentsFragment)
                }
                else
                {
                    showToast(requireContext(),"Some error occurred!")
                }
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
                        .start(requireContext(), this@CreateNewDoubtFragment)
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


                        binding.doubtImage.setImageURI(resultUri)

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