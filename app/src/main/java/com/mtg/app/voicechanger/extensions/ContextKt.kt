package com.mtg.app.voicechanger.extensions

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mtg.app.voicechanger.R
import kotlin.jvm.internal.Intrinsics


object ContextKt {
    fun isCameraPermissionGranted(context: Context?): Boolean {
        return true || context?.let { ContextCompat.checkSelfPermission(it, "android.permission.CAMERA") } === 0
    }

    fun  onCreateBottomSheetDialogShow(context: Context?, i: Int, z: Boolean, i2: Int, obj: Any?): BottomSheetDialog? {
        var z = z
        if (i2 and 2 != 0) {
            z = true
        }
        return onCreateBottomSheetDialog(context, i, z)
    }

    fun onCreateBottomSheetDialog(context: Context?, i: Int, z: Boolean): BottomSheetDialog? {
        val bottomSheetDialog = context?.let { BottomSheetDialog(it, R.style.BottomSheetDialogStyle) }
        bottomSheetDialog?.setContentView(i)
        bottomSheetDialog?.setCancelable(z)
        bottomSheetDialog?.window?.setLayout(-1, -2)
        bottomSheetDialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        return bottomSheetDialog
    }

    fun  /* synthetic */`onCreateDialog$default`(context: Context?, i: Int, z: Boolean, i2: Int, obj: Any?): Dialog? {
        var z = z
        if (i2 and 2 != 0) {
            z = true
        }
        return onCreateDialog(context, i, z)
    }

    private fun onCreateDialog(context: Context?, i: Int, z: Boolean): Dialog? {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(i)
        dialog?.setCancelable(z)
        dialog?.window?.setLayout(-1, -2)
        dialog?.window?.setGravity(17)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        return dialog
    }

//    fun loadBackgroundScreen(view: View, path: String?) {
//        Intrinsics.checkNotNullParameter(view, "<this>")
//        Intrinsics.checkNotNullParameter(path, "path")
//        Glide.with(view).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).error(android.R.drawable.detail_bg).into(object : CustomTarget<Drawable?>() {
//            // com.bumptech.glide.request.target.Target
//            override fun onLoadCleared(drawable: Drawable?) {}
//
//            // com.bumptech.glide.request.target.Target
//            /* bridge */ /* synthetic */   fun onResourceReady(obj: Any?, transition: Transition?) {
//                onResourceReady(obj as Drawable?, transition as Transition<in Drawable?>?)
//            }
//
//            fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable?>?) {
//                Intrinsics.checkNotNullParameter(resource, "resource")
//                view.background = resource
//            }
//        } as RequestBuilder<*>)
//    }

    fun loadBackgroundScreen(view: View, path: Any?) {
        Glide.with(view)
            .load(path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.img_background_gradient)
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(drawable: Drawable?) {}

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    view.background = resource
                }
            })
    }


    fun onCreateProgressDialog(context: Context, i: Int): Dialog {
        Intrinsics.checkNotNullParameter(context, "<this>")
        val progressDialog = ProgressDialog(context)
        progressDialog.setIndeterminate(true)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage(context.getString(i))
        return progressDialog
    }

    fun getUrlFromDrawable(context: Context, i: Int): String {
        Intrinsics.checkNotNullParameter(context, "<this>")
        return "android.resource://" + context.getPackageName() + "/drawable/" + i
    }

    fun isNotificationPermissionGranted(context: Context?): Boolean {
        Intrinsics.checkNotNullParameter(context, "<this>")
        return Build.VERSION.SDK_INT < 33 || context?.let { ContextCompat.checkSelfPermission(it, "android.permission.POST_NOTIFICATIONS") } === 0
    }
}