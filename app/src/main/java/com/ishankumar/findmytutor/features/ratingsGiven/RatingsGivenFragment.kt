package com.ishankumar.findmytutor.features.ratingsGiven

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.dataClasses.RatingInfo
import com.ishankumar.findmytutor.databinding.FragmentRatingsGivenBinding
import com.ishankumar.findmytutor.databinding.RatingReceivedDialogBinding
import com.ishankumar.findmytutor.features.MainActivity


class RatingsGivenFragment : BaseFragment(), RatingsAdapter.OnRatingClickedListner {

    private var _binding: FragmentRatingsGivenBinding? = null
    private val binding get() = _binding!!
    private lateinit var mRatingsGivenViewModel: RatingsGivenViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentRatingsGivenBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRatingsGivenViewModel =
            ViewModelProvider(this)[RatingsGivenViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    showProgressDialog("Wait")
                    mRatingsGivenViewModel.checkUserType()
                    mRatingsGivenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        dismissProgressDialog()
                        if(it.first==2)
                            findNavController().navigate(R.id.action_ratingsGivenFragment_to_homeTutorsFragment)
                        else
                            findNavController().navigate(R.id.action_ratingsGivenFragment_to_homeStudentsFragment)
                    }


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        mRatingsGivenViewModel.getRatingsGivenByMe()
        mRatingsGivenViewModel.mRatingsGivenByMeLiveData.observe(viewLifecycleOwner)
        {
            if(it.size==0)
            {
                binding.ratingsGivenRecyclerView.visibility=View.GONE
                binding.animNoResultsFound.visibility=View.VISIBLE
                binding.txtNoResultsFound.visibility=View.VISIBLE
            }
            else
            {

            binding.ratingsGivenRecyclerView.visibility=View.VISIBLE
                binding.animNoResultsFound.visibility=View.GONE
                binding.txtNoResultsFound.visibility=View.GONE
            binding.ratingsGivenRecyclerView.layoutManager=LinearLayoutManager(requireContext())
            binding.ratingsGivenRecyclerView.adapter=RatingsAdapter(it,requireContext(),this)
            }
        }
        binding.ratingsGivenBackButton.setOnClickListener {
            showProgressDialog("Wait")
            mRatingsGivenViewModel.checkUserType()
            mRatingsGivenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
            {
                dismissProgressDialog()
                if(it.first==2)
                    findNavController().navigate(R.id.action_ratingsGivenFragment_to_homeTutorsFragment)
                else
                    findNavController().navigate(R.id.action_ratingsGivenFragment_to_homeStudentsFragment)
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