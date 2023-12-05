package com.mtg.app.voicechanger.view.activity

import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mtg.app.voicechanger.databinding.ActivityPolicyWebviewBinding
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.consent_dialog.ConsentDialogManager
import com.mtg.app.voicechanger.eventlogger.EventLogger
import com.mtg.app.voicechanger.utils.constant.Constants

class PolicyWebViewActivity :
    BaseActivity<ActivityPolicyWebviewBinding>(ActivityPolicyWebviewBinding::inflate) {

    override fun initView() {
        val web: WebView = findViewById(R.id.webView)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.contains("play.google.com")) {
                    EventLogger.firebaseLog(this@PolicyWebViewActivity, "gdpr_policy")
                    ConsentDialogManager.instance
                        ?.showConsentDialogWebView(this@PolicyWebViewActivity)
                    true
                } else {
                    false
                }
            }
        }
        Constants.getPolicyURL()?.let { web.loadUrl(it) }
    }

    override fun addEvent() {}

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PolicyWebViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}