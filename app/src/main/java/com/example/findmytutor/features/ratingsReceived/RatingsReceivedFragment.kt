package com.example.findmytutor.features.ratingsReceived

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.dataClasses.RatingInfo
import com.example.findmytutor.dataClasses.RequestTutor
import com.example.findmytutor.databinding.FragmentRatingsGivenBinding
import com.example.findmytutor.databinding.FragmentRatingsReceivedBinding
import com.example.findmytutor.databinding.RatingDialogBinding
import com.example.findmytutor.databinding.RatingReceivedDialogBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.ratingsGiven.RatingsAdapter
import com.example.findmytutor.features.ratingsGiven.RatingsGivenFragment
import com.example.findmytutor.features.ratingsGiven.RatingsGivenViewModel


class RatingsReceivedFragment : BaseFragment(), RatingsReceivedAdapter.OnRatingClickedListner {


    private var _binding: FragmentRatingsReceivedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mRatingsReceivedViewModel: RatingsReceivedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentRatingsReceivedBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRatingsReceivedViewModel =
            ViewModelProvider(this)[RatingsReceivedViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    showProgressDialog("Wait")
                    mRatingsReceivedViewModel.checkUserType()
                    mRatingsReceivedViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        dismissProgressDialog()
                        if(it.first==2)
                            findNavController().navigate(R.id.action_ratingsReceivedFragment_to_homeTutorsFragment)
                        else
                            findNavController().navigate(R.id.action_ratingsReceivedFragment_to_homeStudentsFragment)
                    }


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        mRatingsReceivedViewModel.getRatingsReceived()
        mRatingsReceivedViewModel.mRatingsReceivedByMeLiveData.observe(viewLifecycleOwner)
        {
            binding.ratingsReceivedRecyclerView.layoutManager= LinearLayoutManager(requireContext())
            binding.ratingsReceivedRecyclerView.adapter= RatingsReceivedAdapter(it,requireContext(),this)
        }
        binding.ratingsReceivedBackButton.setOnClickListener {
            showProgressDialog("Wait")
            mRatingsReceivedViewModel.checkUserType()
            mRatingsReceivedViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
            {
                dismissProgressDialog()
                if(it.first==2)
                    findNavController().navigate(R.id.action_ratingsReceivedFragment_to_homeTutorsFragment)
                else
                    findNavController().navigate(R.id.action_ratingsReceivedFragment_to_homeStudentsFragment)
            }
        }
    }

    override fun onItemClicked(ratingInfo: RatingInfo) {
        val ratingDialogBinding = RatingReceivedDialogBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(ratingDialogBinding.root)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(true)
        ratingDialogBinding.etFeedback.text=ratingInfo.feedback
        val rating=ratingInfo.rating
        if(rating==1)
            ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
        else if(rating==2)
        {
            ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
        }
        else if(rating==3)
        {

            ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
        }
        else if(rating==4)
        {
            ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
        }
        else if(rating==5)
        {
            ratingDialogBinding.star1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star2.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star3.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star4.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))
            ratingDialogBinding.star5.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_star_24))

        }

    }

}