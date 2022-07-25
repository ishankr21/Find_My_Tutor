package com.ishankumar.findmytutor.features.askHelp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.base.BaseFragment
import com.ishankumar.findmytutor.databinding.FragmentSendIssueMailBinding
import com.ishankumar.findmytutor.features.MainActivity
import com.ishankumar.findmytutor.features.splashScreen.SplashScreenViewModel


class SendIssueMailFragment : BaseFragment() {

    private var _binding: FragmentSendIssueMailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSplashScreenViewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).hideBottomNavigationView()
        _binding = FragmentSendIssueMailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSplashScreenViewModel =
            ViewModelProvider(this)[SplashScreenViewModel::class.java]
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    showProgressDialog("Wait")
                    mSplashScreenViewModel.checkUserType()
                    mSplashScreenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        dismissProgressDialog()
                        if(it.first==2)
                        findNavController().navigate(R.id.action_sendIssueMailFragment_to_homeTutorsFragment)
                        else
                            findNavController().navigate(R.id.action_sendIssueMailFragment_to_homeStudentsFragment)
                    }


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        binding.sendIssueMail.setOnClickListener {
            if(binding.issueDescriptionValue.text.isNullOrEmpty() || binding.issueTitleValue.text.isNullOrEmpty())
            {
                showToast(requireContext(),"Please fill all the details")
            }
            else
            {
                val emailIntent = Intent(Intent.ACTION_SEND)
                val TO = arrayOf("kumarishaan01@gmail.com,anshumali.1132000@gmail.com")
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type = "text/plain"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, binding.issueTitleValue.text.toString())
                emailIntent.putExtra(Intent.EXTRA_TEXT, binding.issueDescriptionValue.text.toString())


                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"))


                binding.issueEmailBackButton.performClick()

            }

        }

        binding.issueEmailBackButton.setOnClickListener {
            showProgressDialog("Wait")
            mSplashScreenViewModel.checkUserType()
            mSplashScreenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
            {
                dismissProgressDialog()
                if(it.first==2)
                    findNavController().navigate(R.id.action_sendIssueMailFragment_to_homeTutorsFragment)
                else
                    findNavController().navigate(R.id.action_sendIssueMailFragment_to_homeStudentsFragment)
            }
        }
    }

}