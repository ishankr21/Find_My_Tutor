package com.example.findmytutor.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment:Fragment() {

    public fun showToast(context:Context,message:String)
    {
        Toast.makeText(context, message,Toast.LENGTH_LONG).show()

    }
}