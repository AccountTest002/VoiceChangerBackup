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

    fun smartCheckColor(stringColor: String): Int {
        return try {
            Color.parseColor(stringColor)
        } catch (_: Exception) {
            return Color.TRANSPARENT
        }
    }

    /**
     * read name file in asset
     */
    fun getNameFromAssets(context: Context, folder:String): List<String> {
        var list: List<String> = ArrayList()
        val assetManager: AssetManager
        try {
            assetManager = context.assets
            list = Arrays.asList(*assetManager.list(folder)!!)
        } catch (ignored: IOException) {
        }
        return list
    }

    /**
     * input: item_01_Hair_clipper.mp3
     */
    fun handleName(nameIn:String):String{
        return nameIn
//            .replaceBefore("_","")
//            .replaceBefore("_","")
            .removeRange(0,7)
            .replace(".mp3","")
            .replace("_"," ")
            .replace("-"," ")
            .trim()
            .split("\\s+".toRegex())
            .joinToString(" ") { it.capitalize() }
    }


    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(android.util.Base64.encode(md.digest(), 0))

                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }
}