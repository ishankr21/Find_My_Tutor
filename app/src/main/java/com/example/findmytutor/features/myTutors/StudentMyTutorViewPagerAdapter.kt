package com.example.findmytutor.features.myTutors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class StudentMyTutorViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitle = ArrayList<String>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(offerFragment : Fragment, offerTitle : String){
        fragmentList.add(offerFragment)
        fragmentTitle.add(offerTitle)
    }
}