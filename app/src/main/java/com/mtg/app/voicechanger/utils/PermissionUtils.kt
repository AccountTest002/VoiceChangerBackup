package com.mtg.app.voicechanger.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Intent
import android.content.DialogInterface
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.utils.constant.Constants.TAG
import java.lang.Exception

object PermissionUtils {
    const val REQUEST_PERMISSION_RECORD = 10
    const val REQUEST_PERMISSION_READ_WRITE = 11
    const val REQUEST_PERMISSION_READ_AUDIO = 32134

    fun checkPermissionRecord(context: Context): Boolean {
        val record = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        return record == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionReadWriteFile(context: Context): Boolean {
        val write =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

        return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestWriteSetting(context: Context) {
        try {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            context.startActivity(intent)
        }
    }

    @JvmStatic
    fun openDialogAccessAllFile(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.dialog_request_permission)
            .setTitle(R.string.app_name_store)
        builder.setPositiveButton(R.string.settings) { dialog: DialogInterface?, id: Int ->
            requestAccessAllFile(
                activity
            )
        }
        builder.setNegativeButton(R.string.cancel) { dialog: DialogInterface?, id: Int -> activity.finish() }
        builder.setCancelable(false)
        val dialogRequest = builder.create()
        dialogRequest.show()
    }

    private fun requestAccessAllFile(activity: Activity) {
        try {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            activity.startActivityForResult(intent, REQUEST_PERMISSION_RECORD)
        } catch (e: Exception) {
            Log.d(TAG, "requestAccessAllFile: error " + e.message)
        }
    }


    fun checkReadAudioPms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestReadAudioPms(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                REQUEST_PERMISSION_READ_AUDIO
            )
        } else {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_READ_AUDIO
            )
        }
    }
    fun requestReadAudioPmsSettings(act: Activity) {
        try {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            act.startActivityForResult(intent, REQUEST_PERMISSION_READ_AUDIO)
        } catch (e: Exception) {
            Log.d(TAG, "requestPms: error " + e.message)
        }
    }

}