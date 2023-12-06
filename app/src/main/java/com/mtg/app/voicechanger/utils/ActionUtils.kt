package com.mtg.app.voicechanger.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.common.control.interfaces.RateCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.dialog.CustomRateAppDialog

object ActionUtils {

    fun openLink(c: Context, url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url.replace("HTTPS", "https"))
            c.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(c, "No browser!", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendFeedback(context: Context) {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(Constants.EMAIL))
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.app_name_store) + " Feedback"
        )
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        emailIntent.selector = selectorIntent
//        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email using..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    fun showRateDialog(context: Activity, isFinish: Boolean, callback: (Boolean) -> Unit) {
        val dialog = CustomRateAppDialog(context)
        dialog.setCallback(object : RateCallback {
            override fun onMaybeLater() {
                if (isFinish) {
                    SharedPrefs.increaseCountRate(context)
                    context.finishAffinity()
                }
            }

            override fun onSubmit(review: String) {
                Toast.makeText(context, context.getString(R.string.thank_you), Toast.LENGTH_SHORT)
                    .show()
                SharedPrefs.setRated(context)
                if (isFinish) {
                    context.finishAffinity()
                }
                callback(true)

            }

            override fun onRate() {
                rateInApp(context)
                SharedPrefs.setRated(context)
                callback(true)

            }

            override fun starRate(v: Float) {

            }

            override fun onDismiss() {

            }
        })
        dialog.show()
    }

    private fun rateInApp(context: Activity) {
        val manager: ReviewManager = ReviewManagerFactory.create(context)
        val request: com.google.android.play.core.tasks.Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task: com.google.android.play.core.tasks.Task<ReviewInfo> ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo = task.result
                reviewInfo.let { manager.launchReviewFlow(context, it) }
            }
        }
    }
}