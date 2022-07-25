package com.ishankumar.findmytutor.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ishankumar.findmytutor.BuildConfig
import com.ishankumar.findmytutor.R
import org.json.JSONObject

class SendNotification(val context: Context) {
//    private val FCM_API = "https://fcm.googleapis.com/v1/projects/myproject-find-my-tutor-f2b29/messages:send"
//    private val code= BuildConfig.API_KEY
//    private val serverKey =
//        "key=$code"
//    private val contentType = "application/json"
//    private val requestQueue: RequestQueue by lazy {
//        Volley.newRequestQueue(context)
//    }
//
  fun sendNotification(notification: JSONObject) {

      //TODO send Notifications

//        Log.e("TAG", "sendNotification")
//        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
//            Response.Listener<JSONObject> { response ->
//                Log.i("TAG", "onResponse: $response")
//
//            },
//            Response.ErrorListener {
//                Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show()
//                Log.i("TAG", "onErrorResponse: Didn't work")
//            }) {
//
//            override fun getHeaders(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params["Authorization"] = serverKey
//                params["Content-Type"] = contentType
//                return params
//            }
//        }
//        requestQueue.add(jsonObjectRequest)
   }
}