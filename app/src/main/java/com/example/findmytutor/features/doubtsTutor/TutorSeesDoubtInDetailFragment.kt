package com.example.findmytutor.features.doubtsTutor

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.findmytutor.R
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.SolutionInfo
import com.example.findmytutor.databinding.FragmentTutorSeesDoubtInDetailBinding
import com.example.findmytutor.databinding.ImageDialogBinding
import com.example.findmytutor.features.MainActivity


class TutorSeesDoubtInDetailFragment : Fragment() {

    private var _binding: FragmentTutorSeesDoubtInDetailBinding? = null
    private val binding get() = _binding!!
    private var doubtInfoCame = DoubtInfo()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentTutorSeesDoubtInDetailBinding.inflate(inflater, container, false)
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
        binding.txtTitleValue.text=doubtInfoCame.doubtTitle
        binding.txtDescriptionValue.text=doubtInfoCame.doubtDescription
        if(doubtInfoCame.doubtImagePath!="")
        {
            Glide.with(requireContext())
                .load(doubtInfoCame.doubtImagePath)
                .into(binding.doubtSeenByTutorImage)
        }

        binding.doubtSeenByTutorImage.setOnClickListener {
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

        binding.doubtSolveBtn.setOnClickListener {
            val bundle=Bundle()
            val solutionInfo=SolutionInfo(
                doubtId = doubtInfoCame.doubtId,
                studentId = doubtInfoCame.studentId
            )
            bundle.putSerializable("solutionInfo",solutionInfo)
            findNavController().navigate(R.id.action_tutorSeesDoubtInDetailFragment_to_tutorCreateSolutionFragment,bundle)
        }
    }

}