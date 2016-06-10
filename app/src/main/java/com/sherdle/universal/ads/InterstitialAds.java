package com.sherdle.universal.ads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sherdle.universal.db.objects.SettingsObject;
import com.sherdle.universal.util.Helper;

import java.util.List;

public class InterstitialAds {

    private Context context;
    private InterstitialAd interstitialAdView;
    private ProgressDialog mProgressDialog;
    List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);
    private String interstitialID = settingsObjectList.get(0).getInterstitialID();
    private static final String TAG = "InterstitialAds";

    public InterstitialAds(Context context) {
        this.context = context;
    }

    public void showInterstitial() {
        interstitialAdView = new InterstitialAd(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (interstitialAdView != null && interstitialAdView.isLoaded()) {
                    interstitialAdView.show();
                }
            }
        });
        mProgressDialog.show();
        interstitialAdView.setAdUnitId(interstitialID);
        AdRequest interstitialRequest = new AdRequest.Builder().build();
        interstitialAdView.loadAd(interstitialRequest);
        interstitialAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                new Thread() {
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                }.start();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mProgressDialog.dismiss();
            }
        });
    }


    public void showInterstitial(final String url) {
        interstitialAdView = new InterstitialAd(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (interstitialAdView != null && interstitialAdView.isLoaded()) {
                    interstitialAdView.show();
                }
            }
        });
        mProgressDialog.show();
        interstitialAdView.setAdUnitId(interstitialID);
        AdRequest interstitialRequest = new AdRequest.Builder().build();
        interstitialAdView.loadAd(interstitialRequest);
        interstitialAdView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(((Helper.onVerificationUrl(url))))));
            }

            @Override
            public void onAdLoaded() {
                new Thread() {
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                }.start();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mProgressDialog.dismiss();
            }
        });
    }

    public void showInterstitial(final Class className) {
        interstitialAdView = new InterstitialAd(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(mProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (interstitialAdView != null && interstitialAdView.isLoaded()) {
                    interstitialAdView.show();
                }
            }
        });
        mProgressDialog.show();
        interstitialAdView.setAdUnitId(interstitialID);
        AdRequest interstitialRequest = new AdRequest.Builder().build();
        interstitialAdView.loadAd(interstitialRequest);
        interstitialAdView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Activity activity = (Activity) context;
                activity.startActivity(new Intent(context, className));
                activity.overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }

            @Override
            public void onAdLoaded() {
                new Thread() {
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                }.start();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mProgressDialog.dismiss();
            }
        });
    }

}
