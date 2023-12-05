package com.mtg.app.voicechanger.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.utils.constant.Constants.TAG
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays

object Common {

    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

}