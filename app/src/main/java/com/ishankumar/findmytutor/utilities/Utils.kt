package com.ishankumar.findmytutor.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.maps.android.SphericalUtil
import com.google.android.gms.maps.model.LatLng

object Utils {



    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun calculateDistanceBetweenStudentTutor(
        tutorLatitude: Double,
        tutorLongitude: Double,
        studentLatitude: Double,
        studentLongitude: Double,
    ): Double {



        val distDouble = SphericalUtil.computeDistanceBetween(
            LatLng(tutorLatitude,tutorLongitude),
            LatLng(studentLatitude,studentLongitude)
        )

      Log.d("dist", distDouble.toString())

        return distDouble

    }
}