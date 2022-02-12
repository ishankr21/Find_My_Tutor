package com.example.findmytutor.base

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import dmax.dialog.SpotsDialog

open class BaseFragment:Fragment() {
    private var progressDialog: AlertDialog? =null

     fun showToast(context:Context,message:String)
    {
        Toast.makeText(context, message,Toast.LENGTH_LONG).show()

    }
    fun showProgressDialog(message: String) {
        progressDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage(message)
            .build().apply { show() }
    }

    fun dismissProgressDialog() {
        if ( progressDialog!=null && progressDialog!!.isShowing) {
            progressDialog?.dismiss()
        }
    }
}