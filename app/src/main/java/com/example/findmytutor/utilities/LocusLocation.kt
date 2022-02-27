package com.example.findmytutor.utilities


import android.content.Context
import android.location.Address

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.birjuvachhani.locus.Locus

import java.io.IOException
import java.util.*

class LocusLocation {



    private var mLastLocation : Location? = null
    var address : Address? = null




    fun getLatLongByLocus(context: Context) : MutableLiveData<Pair<String,String>> {

        val addressLiveData = MutableLiveData<Pair<String,String>>()

        Locus.configure {
            this.rationaleTitle = "Location Permission"
            this.rationaleText = "We need your location to provide you proper services!"
        }

        Locus.getCurrentLocation(context){
            try {
                    it.location?.let {locusLocation->

                        addressLiveData.value=(Pair(locusLocation.latitude.toString(),locusLocation.longitude.toString()))
                    }
                it.error.let {
                    addressLiveData.value=(Pair("",""))
                }

            } catch (e: IOException){
                e.printStackTrace()
            }
        }
        return addressLiveData
    }

}