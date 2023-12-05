package com.mtg.app.voicechanger.view.activity

import android.content.Context
import android.content.Intent
import androidx.core.view.GravityCompat
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.common.control.manager.AppOpenManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mtg.app.voicechanger.AdCache
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.databinding.ActivityMainBinding
import com.mtg.app.voicechanger.utils.ActionUtils
import com.mtg.app.voicechanger.utils.Common
import com.mtg.app.voicechanger.utils.CommonUtils
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.LanguageUtils
import com.mtg.app.voicechanger.utils.hide
import com.mtg.app.voicechanger.utils.setSize


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun binding() {
        super.binding()
        isRegisterReceiver = true
    }

    override fun initView() {
        setupDrawerNavigation()
        setSize()
        if (SharedPrefs.isRated(this)) hideRate()
//        createDatabase(repository, this)
//        fakeFavorite()

        AdmobManager.getInstance()
            .loadCollapsibleBanner(this, BuildConfig.banner_main, binding.frAd)
//        AppOpenManager.getInstance().hideNativeOrBannerWhenShowOpenApp(this, binding.frAd)
    }

    private fun setSize() {
        if (Common.screenWidth / Common.screenHeight > 108 / 216) {
            binding.imgBackground.layoutParams.height = Common.screenHeight
        } else {
            binding.imgBackground.layoutParams.width = Common.screenWidth
        }
        binding.tvAppName.setSize(20)
        binding.tvRate.setSize(10)
    }

    override fun addEvent() {
        binding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            EventLogger.getInstance()?.logEvent("click_main_setting")
        }

        binding.btnHome.setOnClickListener {
            binding.viewpagerMain.setCurrentItem(0, false)
            EventLogger.getInstance()?.logEvent("click_main_all")
        }
        binding.btnFav.setOnClickListener {
            binding.viewpagerMain.setCurrentItem(1, false)
            EventLogger.getInstance()?.logEvent("click_main_favorite")
        }

        binding.btnRate.setOnClickListener {
            ActionUtils.showRateDialog(this, false, callback = {
                if (it) hideRate()
            })
        }

    }

    private fun setupDrawerNavigation() {
        binding.navContent.tvVersion.text = "Ver ${BuildConfig.VERSION_NAME}"

        binding.navContent.btnLanguage.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_language")
            startActivity(Intent(this, LanguageActivity::class.java))
        }
        binding.navContent.btnRateNavigation.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_rate")
            ActionUtils.showRateDialog(this, false, callback = {
                if (it) hideRate()
            })
        }
        binding.navContent.btnShare.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_share")
            CommonUtils.shareApp(this)
        }
        binding.navContent.btnFeedback.setOnClickListener {
            ActionUtils.sendFeedback(this)
        }
        binding.navContent.btnPrivacy.setOnClickListener {
            PolicyWebViewActivity.start(this)
//            CommonUtils.showPolicy(this)
        }
    }

    private fun hideRate() {
        binding.btnRate.hide()
        binding.navContent.btnRateNavigation.hide()
        binding.tvRate.hide()
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().hideNativeOrBannerWhenShowOpenApp(this, binding.frAd)
//        SharedPrefs.clearCountOpenSound(this)

        loadInterAds()
        binding.navContent.imgFlag.setImageResource(LanguageUtils.getFlagResourceID(context = this))
//        loadAds()
    }

    private fun loadInterAds() {
        if (AdCache.getInstance().interOpenFavorite == null) {
            AdmobManager.getInstance()
                .loadInterAds(this, BuildConfig.inter_open_favorite, object : AdCallback() {
                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
                        super.onResultInterstitialAd(interstitialAd)
                        AdCache.getInstance().interOpenFavorite = interstitialAd
                    }
                })
        }
        if (AdCache.getInstance().interOpenTopic == null) {
            AdmobManager.getInstance()
                .loadInterAds(this, BuildConfig.inter_open_topic, object : AdCallback() {
                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
                        super.onResultInterstitialAd(interstitialAd)
                        AdCache.getInstance().interOpenTopic = interstitialAd
                    }
                })
        }
//        if (AdCache.getInstance().interBackTopic == null) {//preload : case click back before ads loaded
//            AdmobManager.getInstance()
//                .loadInterAds(this, BuildConfig.inter_back_topic, object : AdCallback() {
//                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
//                        super.onResultInterstitialAd(interstitialAd)
//                        AdCache.getInstance().interBackTopic = interstitialAd
//                    }
//                })
//        }
    }
}