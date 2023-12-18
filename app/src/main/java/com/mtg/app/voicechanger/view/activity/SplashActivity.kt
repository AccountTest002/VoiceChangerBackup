package com.mtg.app.voicechanger.view.activity

import android.os.Build
import androidx.core.content.ContextCompat
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.common.control.manager.AppOpenManager
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mtg.app.voicechanger.databinding.ActivitySplashBinding
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.consent_dialog.ConsentDialogManager
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.utils.Common
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.utils.constant.Constants

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private var appPreferences = AppPreferences.instance
    override fun binding() {
        isFullScreen = false
        super.binding()
    }

    override fun initView() {
        setStatusBarColor(ContextCompat.getColor(this, R.color.white))
        SharedPrefs.clearCountTopic(this)
//        ConsentDialogManager.instance?.showConsentDialogSplash(this) {
            handleAds()
//        }
        EventLogger.getInstance()?.logEvent("open_splash")
    }

    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }


    private fun handleAds() {
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
        AdmobManager.getInstance()
            .loadInterAds(this, BuildConfig.inter_splash, object : AdCallback() {
                override fun onResultInterstitialAd(interstitialAd: InterstitialAd) {
                    super.onResultInterstitialAd(interstitialAd)
                    EventLogger.getInstance()?.logEvent("open_splash_with_ad")
                    showInter(interstitialAd)
                }

                override fun onAdFailedToLoad(i: LoadAdError) {
                    super.onAdFailedToLoad(i)
                    EventLogger.getInstance()?.logEvent("open_splash_without_ad")
                    startMain()
                }
            })
    }

    private fun showInter(interstitialAd: InterstitialAd) {
        startMain()
        AdmobManager.getInstance().showInterstitial(this, interstitialAd, object : AdCallback() {
            override fun onAdClosed() {
                super.onAdClosed()
            }
        })

    }

    private fun startMain() {
        if (SharedPrefs.getBoolean(this, Constants.KEY_FIRST_INTRO)) {
            RecordActivity.start(this)
        } else {
            if (!appPreferences.isChooseLanguage) {
                LanguageActivity.start(this)
                this.overridePendingTransition(
                    R.anim.anim_right_left_1,
                    R.anim.anim_right_left_2
                )
            } else {
                RecordActivity.start(this)
                this.overridePendingTransition(
                    R.anim.anim_right_left_1,
                    R.anim.anim_right_left_2
                )
            }
        }

        finish()
    }

    override fun addEvent() {

    }

    override fun onResume() {
        super.onResume()
//        if (AdCache.getInstance().interOpenTopic == null) {
//            AdmobManager.getInstance()
//                .loadInterAds(this, BuildConfig.inter_open_topic, object : AdCallback() {
//                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
//                        super.onResultInterstitialAd(interstitialAd)
//                        AdCache.getInstance().interOpenTopic = interstitialAd
//                    }
//                })
//        }
//        if (AdCache.getInstance().interGuide == null) {
//            AdmobManager.getInstance()
//                .loadInterAds(this, BuildConfig.inter_guide, object : AdCallback() {
//                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
//                        super.onResultInterstitialAd(interstitialAd)
//                        AdCache.getInstance().interGuide = interstitialAd
//                    }
//                })
//        }
    }
}