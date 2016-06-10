package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class SettingsObject extends SugarRecord {

    private String bannerID;
    private String interstitialID;
    private String nativeID;
    private String analyticsID;
    private String host;
    private int nativeWidth;
    private int nativeHeight;
    private boolean offerwallVisible;
    private boolean shareVisible;
    private boolean rateVisible;
    private boolean settingsVisible;

    public SettingsObject() {
    }

    public SettingsObject(String bannerID, String interstitialID, String nativeID, String analyticsID, String host, int nativeWidth, int nativeHeight, boolean offerwallVisible, boolean shareVisible, boolean rateVisible, boolean settingsVisible) {
        this.bannerID = bannerID;
        this.interstitialID = interstitialID;
        this.nativeID = nativeID;
        this.analyticsID = analyticsID;
        this.host = host;
        this.nativeWidth = nativeWidth;
        this.nativeHeight = nativeHeight;
        this.offerwallVisible = offerwallVisible;
        this.shareVisible = shareVisible;
        this.rateVisible = rateVisible;
        this.settingsVisible = settingsVisible;
    }

    public String getBannerID() {
        return bannerID;
    }

    public void setBannerID(String bannerID) {
        this.bannerID = bannerID;
    }

    public String getInterstitialID() {
        return interstitialID;
    }

    public void setInterstitialID(String interstitialID) {
        this.interstitialID = interstitialID;
    }

    public String getAnalyticsID() {
        return analyticsID;
    }

    public void setAnalyticsID(String analyticsID) {
        this.analyticsID = analyticsID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public int getNativeWidth() {
        return nativeWidth;
    }

    public void setNativeWidth(int nativeWidth) {
        this.nativeWidth = nativeWidth;
    }

    public int getNativeHeight() {
        return nativeHeight;
    }

    public void setNativeHeight(int nativeHeight) {
        this.nativeHeight = nativeHeight;
    }

    public boolean isOfferwallVisible() {
        return offerwallVisible;
    }

    public boolean isShareVisible() {
        return shareVisible;
    }

    public boolean isRateVisible() {
        return rateVisible;
    }

    public boolean isSettingsVisible() {
        return settingsVisible;
    }
}
