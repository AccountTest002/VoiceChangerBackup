package com.mtg.app.voicechanger.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.utils.constant.Constants.EMAIL
import com.mtg.app.voicechanger.utils.constant.Constants.POLICY_URL
import com.mtg.app.voicechanger.utils.constant.Constants.PUBLISH_NAME
import com.mtg.app.voicechanger.utils.constant.Constants.SUBJECT
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    fun setUserProperties(properties: String?, value: String?, context: Context?) {
        try {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
            firebaseAnalytics.setUserProperty(properties!!, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun logEvent(context: Context?, value: String) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        try {
            Log.d("android_log", "logEvent: $value")
            val bundle = Bundle()
            bundle.putString("EVENT", value)
            firebaseAnalytics.logEvent(value, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun renameFile(dir: File, newName: String?): Boolean {
        val from = File(dir.path)
        val to = File(dir.path.replace(dir.name, newName!!))
        return from.renameTo(to)
    }

    fun deleteDocument(file: File) {
        if (file.delete()) {
            println("ok")
        } else {
            println("not_ok")
        }
    }

    fun shareApp(context: Context) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "https://play.google.com/store/apps/details?id=" + context.packageName
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "Share to"))
    }

    fun support(context: Context) {
        val mailIntent = Intent(Intent.ACTION_VIEW)
        val data = Uri.parse("mailto:?SUBJECT=$SUBJECT&body=&to=$EMAIL")
        mailIntent.data = data
        context.startActivity(Intent.createChooser(mailIntent, "Send mail..."))
    }

    fun rateApp(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.packageName)
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }
    }

    fun openWeb(context: Context, url: String?) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showPolicy(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(POLICY_URL)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun moreApp(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=pub:" + PUBLISH_NAME)
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/developer?id=" + PUBLISH_NAME)
                )
            )
        }
    }

    fun log(text: String?) {
        Log.d(Constants.TAG, text!!)
    }

    fun saveFile(fin: InputStream, savePath: String, nameFile: String) {
        val file = File(savePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        var fout: FileOutputStream? = null
        try {
            fout = FileOutputStream(File("$savePath/$nameFile"))
            var lenght = 0
            val buff = ByteArray(1024)
            lenght = fin.read(buff)
            while (lenght > 0) {
                fout.write(buff, 0, lenght)
                lenght = fin.read(buff)
            }
            fin.close()
            fout.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @JvmOverloads
    fun formatTime(duration: Long, isHour: Boolean = false): String {
        var formatter = SimpleDateFormat("HH:mm:ss")
        if (!isHour) {
            formatter = SimpleDateFormat("mm:ss")
        }
        val date = Date(duration)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(duration: Long): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val date = Date(duration)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    } //    public void shareFile(Context context, File file) {

    //        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    //        StrictMode.setVmPolicy(builder.build());
    //        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
    //        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
    //        intentShareFile.putExtra(Intent.EXTRA_STREAM,
    //                Uri.parse("file://" + file.getAbsolutePath()));
    //        context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
    //    }


}