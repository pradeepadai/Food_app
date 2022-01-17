package com.example.foododeringanddeliveryapp.API

import android.R
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.net.wifi.WifiConfiguration.GroupCipher.strings
import android.net.wifi.WifiConfiguration.KeyMgmt.strings
import android.provider.Settings.Global.getString
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
 
    private const val BASE_URL = "http://10.0.2.2:90/"
    //private const val BASE_URL = "http://192.168.10.217:90/"

    var token:String?=null
    private val okhttp =
        OkHttpClient.Builder()

    //create retrofit builder
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp.build())

    //Create retrofit instance
    private val retrofit = retrofitBuilder.build()
    //Generic function
    fun <T> buildService(serviceType: Class<T>):T{
        return retrofit.create(serviceType)
    }
    fun loadImagePath(): String {
        val arr = BASE_URL.split("/").toTypedArray()
        return arr[0] + "/" + arr[1] + arr[2] + "/"
    }
}