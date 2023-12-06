package com.mtg.app.voicechanger.view.activity

import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.common.control.manager.AppOpenManager
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mtg.app.voicechanger.databinding.ActivitySplashBinding
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.consent_dialog.ConsentDialogManager
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.utils.Common
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.utils.constant.Const

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private var appPreferences = AppPreferences.instance
    override fun binding() {
        isFullScreen = true
        super.binding()
    }

    override fun initView() {
        setImageBackground()
//        setLanguage(LanguageUtils.getCurrentLanguageCode(this))
        SharedPrefs.clearCountTopic(this)
//        Handler(Looper.getMainLooper()).postDelayed({ handleAds() }, 2000)
        ConsentDialogManager.instance?.showConsentDialogSplash(this) {
            handleAds()
        }
        EventLogger.getInstance()?.logEvent("open_splash")
    }

    private fun setImageBackground() {
        if (Common.screenWidth / Common.screenHeight > 108 / 216) {
            binding.imgBackground.layoutParams.height = Common.screenHeight
        } else {
            binding.imgBackground.layoutParams.width = Common.screenWidth
        }
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
        val languageToLoad = SharedPrefs.getString(this, Const.SHARE_PREF_LANGUAGE, "default")
//        if (languageToLoad == "default") {
//            startActivity(Intent(this, LanguageActivity::class.java))
//        } else {
//            //todo
//            if (SharedPrefs.getBoolean(this, Const.IS_SKIP_ONBOARD)) {
//                startActivity(Intent(this, MainActivity::class.java))
//            } else {
//                startActivity(Intent(this@SplashActivity, OnBoardActivity::class.java))
//            }
//        }
        if (SharedPrefs.getBoolean(this, Const.KEY_FIRST_INTRO)) {
            RecordActivity.start(this)
        } else {
            if (!appPreferences.isChooseLanguage) {
                LanguageActivity.start(this)
            } else {
                RecordActivity.start(this)
            }
        }

        finish()
    }

    override fun onStart() {
        super.onStart()
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this@SplashActivity, OnBoardActivity::class.java)) //check onboard first time in OnboardActivity
//        }, 2000)
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