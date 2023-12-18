package com.mtg.app.voicechanger;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class AdCache {

    private static AdCache instance;
    private int interSoundCount = 0;

    private InterstitialAd interOpenEditSound;
    private InterstitialAd interOpenMyWorks;
    private InterstitialAd interDetailGun;
    private InterstitialAd interOpenRecord;
    private InterstitialAd interGuide;
    private RewardedAd rewardedAd;

    private InterstitialAd interBackTopic;
    private InterstitialAd interSaved;

    private AdCache() {
    }

    public static AdCache getInstance() {
        if (instance == null) {
            instance = new AdCache();
        }
        return instance;
    }

    public InterstitialAd getInterDetailGun() {
        return interDetailGun;
    }

    public InterstitialAd getInterOpenMyWorks() {
        return interOpenMyWorks;
    }

    public void setInterOpenMyWorks(InterstitialAd interOpenSoundPlay) {
        this.interOpenMyWorks = interOpenSoundPlay;
    }

    public void setInterDetailGun(InterstitialAd interDetailGun) {
        this.interDetailGun = interDetailGun;
    }

    public InterstitialAd getInterOpenEditSound() {
        return interOpenEditSound;
    }

    public void setInterOpenEditSound(InterstitialAd interOpenEditSound) {
        this.interOpenEditSound = interOpenEditSound;
    }

    public RewardedAd getRewardedAd() {
        return rewardedAd;
    }

    public void setRewardedAd(RewardedAd rewardedAd) {
        this.rewardedAd = rewardedAd;
    }

    public InterstitialAd getInterGuide() {
        return interGuide;
    }

    public void setInterGuide(InterstitialAd interGuide) {
        this.interGuide = interGuide;
    }

    public InterstitialAd getInterBackTopic() {
        return interBackTopic;
    }

    public void setInterBackTopic(InterstitialAd interBackTopic) {
        this.interBackTopic = interBackTopic;
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
