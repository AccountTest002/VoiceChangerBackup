package com.mtg.app.voicechanger.utils;



import android.content.Context;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.mtg.app.voicechanger.BuildConfig;

public class AudienceNetworkInitializeHelper implements AudienceNetworkAds.InitListener {
    public static void initialize(Context context) {
        if (!AudienceNetworkAds.isInitialized(context)) {
            if (BuildConfig.DEBUG) {
                AdSettings.turnOnSDKDebugger(context);
            }

            AudienceNetworkAds.buildInitSettings(context).withInitListener(new AudienceNetworkInitializeHelper()).initialize();
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }
}
