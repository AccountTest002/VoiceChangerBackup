package com.mtg.app.voicechanger

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.common.control.AppConfig
import com.common.control.MyApplication
import com.common.control.dialog.PermissionStorageDialog
import com.common.control.manager.AppOpenManager
import com.common.control.model.PurchaseModel
import com.facebook.FacebookSdk
import com.mtg.app.voicechanger.consent_dialog.dialog.BillingDialog
import com.mtg.app.voicechanger.consent_dialog.remote_config.RemoteConfigManager
import com.mtg.app.voicechanger.utils.AudienceNetworkInitializeHelper
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.view.activity.SplashActivity


class MyApplication : MyApplication(), Application.ActivityLifecycleCallbacks {
    companion object{
        const val PRODUCT_LIFETIME_OLD = "PRODUCT_LIFETIME_OLD"
        const val PRODUCT_LIFETIME = "PRODUCT_LIFETIME"
    }
    private val lsActivity = ArrayList<Activity>()

    override fun onApplicationCreate() {
        RemoteConfigManager.instance?.loadRemote()
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionStorageDialog::class.java)
        registerActivityLifecycleCallbacks(this)
        EventLogger.init(applicationContext)
        AudienceNetworkInitializeHelper.initialize(this)
        FacebookSdk.sdkInitialize(this);
        AppPreferences(this)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)
        val removableActivities = ArrayList<Activity>()
        if (activity is SplashActivity) {
            if (lsActivity.isNotEmpty()) {
                for (a in lsActivity) {
                    a.finish()
                    removableActivities.add(a)
                }
            }
        }
        lsActivity.removeAll(removableActivities.toSet())
        lsActivity.add(activity)
    }

    override fun hasAdjust(): Boolean {
        return false
    }

    override fun getAdjustAppToken(): String? {
        return null
    }

    override fun hasAds(): Boolean {
        return true
    }

    override fun isShowDialogLoadingAd(): Boolean {
        return true
    }

    override fun isShowAdsTest(): Boolean {
        return BuildConfig.TEST_AD || BuildConfig.DEBUG
    }

    override fun enableAdsResume(): Boolean {
        return true
    }

    override fun getOpenAppAdId(): String {
        return BuildConfig.open_app
    }

    override fun isInitBilling(): Boolean {
        return true
    }

    override fun getPurchaseList(): MutableList<PurchaseModel> {
        return mutableListOf(
            PurchaseModel(BillingDialog.PRODUCT_ID, PurchaseModel.ProductType.INAPP),
        )
    }

    override fun getAppConfig(): AppConfig {
        return AppConfig.AppConfigBuilder().setShowLogIdAd(true).build()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }


}