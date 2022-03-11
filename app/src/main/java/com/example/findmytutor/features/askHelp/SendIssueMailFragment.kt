package com.example.findmytutor.features.askHelp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.findmytutor.R
import com.example.findmytutor.base.BaseFragment
import com.example.findmytutor.databinding.FragmentSendIssueMailBinding
import com.example.findmytutor.features.MainActivity
import com.example.findmytutor.features.splashScreen.SplashScreenViewModel


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
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("kumarishaan01@gmail.com"))
                email.putExtra(Intent.EXTRA_SUBJECT, binding.issueTitleValue.text)
                email.putExtra(Intent.EXTRA_TEXT, binding.issueDescriptionValue.text)



                email.type = "message/rfc822"

                startActivity(Intent.createChooser(email, "Choose an Email client :"))
                findNavController().navigate(R.id.action_sendIssueMailFragment_to_homeTutorsFragment)
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