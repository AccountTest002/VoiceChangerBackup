package com.mtg.app.voicechanger;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class AdCache {

    private static AdCache instance;

    private InterstitialAd interOpenEditSound;
    private InterstitialAd interOpenMyWorks;
    private InterstitialAd interOpenRecord;

    private InterstitialAd interSaved;

    private AdCache() {
    }

    public static AdCache getInstance() {
        if (instance == null) {
            instance = new AdCache();
        }
        return instance;
    }


    public InterstitialAd getInterOpenMyWorks() {
        return interOpenMyWorks;
    }

    public void setInterOpenMyWorks(InterstitialAd interOpenSoundPlay) {
        this.interOpenMyWorks = interOpenSoundPlay;
    }


    public InterstitialAd getInterOpenEditSound() {
        return interOpenEditSound;
    }

    public void setInterOpenEditSound(InterstitialAd interOpenEditSound) {
        this.interOpenEditSound = interOpenEditSound;
    }

    public InterstitialAd getInterSaved() {
        return interSaved;
    }

    public void setInterSaved(InterstitialAd interSaved) {
        this.interSaved = interSaved;
    }

    public InterstitialAd getInterRecord() {
        return interOpenRecord;
    }

    public void setInterRecord(InterstitialAd interRecord) {
        this.interOpenRecord = interRecord;
    }
}
