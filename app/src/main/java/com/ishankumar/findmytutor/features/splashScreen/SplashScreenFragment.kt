package com.ishankumar.findmytutor.features.splashScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.ishankumar.findmytutor.R
import com.ishankumar.findmytutor.databinding.FragmentSplashScreenBinding
import com.ishankumar.findmytutor.features.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSplashScreenViewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSplashScreenViewModel =
            ViewModelProvider(this)[SplashScreenViewModel::class.java]
        (activity as MainActivity).hideBottomNavigationView()

        lifecycleScope.launch {
            delay(3000)
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    mSplashScreenViewModel.checkUserType()
                    mSplashScreenViewModel.mExistingUserLiveData.observe(viewLifecycleOwner)
                    {
                        binding.splashScreenProgressBar.visibility=View.GONE
                        if (it.first==2) {


                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/tutors")
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            (activity as MainActivity).setBottomNavigationMenu(it.first)
                            (activity as MainActivity).setNavigationDrawerMenu(it.first)
                            if(it.second)
                            view.findNavController().navigate(R.id.action_splashScreenFragment_to_homeTutorsFragment)
                            else
                            {
                                val bundle=Bundle()
                                bundle.putSerializable("isProfileCompleted",it.second)
                                view.findNavController().navigate(R.id.action_splashScreenFragment_to_profileTutorFragment,bundle)
                            }
                        }
                            else {
                            (activity as MainActivity).setBottomNavigationMenu(it.first)
                            (activity as MainActivity).setNavigationDrawerMenu(it.first)
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/${FirebaseAuth.getInstance().currentUser!!.uid}")

                            if(it.second)
                                view.findNavController().navigate(R.id.action_splashScreenFragment_to_homeStudentsFragment)
                            else
                            {
                                val bundle=Bundle()
                                bundle.putSerializable("isProfileCompleted",it.second)
                                view.findNavController().navigate(R.id.action_splashScreenFragment_to_profileStudentFragment,bundle)
                            }


                            }
                        }
                }
                else
                view.findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)


        }
    }


}