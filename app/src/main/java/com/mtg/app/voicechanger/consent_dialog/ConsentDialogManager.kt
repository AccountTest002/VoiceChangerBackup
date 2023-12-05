package com.mtg.app.voicechanger.consent_dialog

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ump.ConsentForm
import com.google.android.ump.FormError
import com.mtg.app.voicechanger.consent_dialog.base.BaseDialogConsentManager
import com.mtg.app.voicechanger.consent_dialog.dialog.BillingDialog
import com.mtg.app.voicechanger.consent_dialog.remote_config.RemoteConfigManager
import com.mtg.app.voicechanger.consent_dialog.remote_config.RemoteConfigManager.BooleanCallback
import com.mtg.app.voicechanger.consent_dialog.remote_config.RemoteConfigManager.NumberCallback
import com.mtg.app.voicechanger.utils.constant.Const
import com.mtg.app.voicechanger.view.activity.MainActivity

class ConsentDialogManager : BaseDialogConsentManager() {
    private var rejectSplashCount: Long = 0
    private var buttonClickCount: Long = 0
    fun showConsentDialogSplash(
        activity: Activity?,
        listener: ConsentForm.OnConsentFormDismissedListener
    ) {
        RemoteConfigManager.instance!!.loadIsShowConsent(
            activity!!,
            object : BooleanCallback {
                override fun onResult(value: Boolean) {
                    if (value) {
                        loadDialogForm(activity, null)
                        requestConsentInfor(activity, {
                            setRequiredConsentFirstOpen(activity)
                            if (!consentRequired(activity)) {
                                listener.onConsentFormDismissed(null)
                                return@requestConsentInfor
                            }
                            if (canRequestAds(activity)) {
                                listener.onConsentFormDismissed(null)
                                return@requestConsentInfor
                            }
                            showDialogAtSplash(activity, listener)
                        }) { formError: FormError? ->
                            listener.onConsentFormDismissed(null)
                            rejectSplashCount = 0
                        }
                    } else {
                        listener.onConsentFormDismissed(null)
                    }
                }
            })
    }

    private fun showDialogAtSplash(
        activity: Activity?,
        listener: ConsentForm.OnConsentFormDismissedListener
    ) {
        showDialog(activity!!, false) { formError: FormError? ->
            if (canRequestAds(activity)) {
                listener.onConsentFormDismissed(null)
                return@showDialog
            }
            BillingDialog.instance!!.setCallback { key: String, data: Array<Any?>? ->
                if (key == Const.KEY_CANCEL) {
                    val dismissedListener =
                        ConsentForm.OnConsentFormDismissedListener { error: FormError? ->
                            RemoteConfigManager.instance!!.loadReshowGDPRSplashCount(
                                activity, object : NumberCallback {
                                    override fun onResult(value: Long) {
                                        if (canRequestAds(activity)) {
                                            BillingDialog.instance!!.dismiss()
                                            listener.onConsentFormDismissed(null)
                                            rejectSplashCount = 0
                                        } else {
                                            rejectSplashCount++
                                            if (rejectSplashCount == value) {
                                                BillingDialog.instance!!.dismiss()
                                                rejectSplashCount = 0
                                                listener.onConsentFormDismissed(null)
                                            }
                                        }
                                    }
                                })
                        }
                    showDialog(activity, true, dismissedListener)
                }
            }
            BillingDialog.instance!!.show((activity as AppCompatActivity?)!!)
        }
    }

    fun showConsentDialogOnButtonClick(activity: Activity): Boolean {
        return if (RemoteConfigManager.instance!!.isShowConsent) {
            if (!consentRequired(activity)) {
                return false
            }
            if (canRequestAds(activity)) {
                false
            } else showOnButtonClick(activity, null)
        } else {
            false
        }
    }

    private fun showOnButtonClick(
        activity: Activity,
        dismissedListener: ConsentForm.OnConsentFormDismissedListener?
    ): Boolean {
        buttonClickCount++
        return if (buttonClickCount == RemoteConfigManager.instance!!.limitFunctionClickCount()) {
            BillingDialog.instance!!.setCallback { key: String, data: Array<Any?>? ->
                if (key == Const.KEY_CANCEL) {
                    val onDismiss = ConsentForm.OnConsentFormDismissedListener {
                        buttonClickCount = 0
                        if (canRequestAds(activity)) {
                            BillingDialog.instance!!.dismiss()
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activity.startActivity(intent)
                        } else {
                            BillingDialog.instance!!.dismiss()
                        }
                    }
                    showDialog(activity, true, onDismiss)
                }
            }
            BillingDialog.instance!!.show((activity as AppCompatActivity))
            true
        } else {
            false
        }
    }

    fun showConsentDialogWebView(activity: Activity) {
        RemoteConfigManager.instance!!.loadIsShowConsent(
            activity,
            object : BooleanCallback {
                override fun onResult(value: Boolean) {
                    if (value) {
                        showDialogAtWebView(activity)
                    }
                }
            })
    }

    private fun showDialogAtWebView(activity: Activity) {
        if (!consentRequired(activity)) {
            return
        }
        loadDialogForm(activity, null)
        val onDismiss = ConsentForm.OnConsentFormDismissedListener {
            if (canRequestAds(activity)) {
                BillingDialog.instance!!.dismiss()
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
            }
        }
        showDialog(activity, true, onDismiss)
    }

    companion object {
        private var INSTANCE: ConsentDialogManager? = null
        val instance: ConsentDialogManager?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = ConsentDialogManager()
                }
                return INSTANCE
            }
    }
}