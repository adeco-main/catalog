package com.sherdle.universal.ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.sherdle.universal.custom.CustomFragment;
import com.sherdle.universal.custom.entity.Element;
import com.sherdle.universal.db.objects.SettingsObject;

import java.util.List;

public class NativeAds implements Element {

    private static final String TAG = "NativeAdmob";
    private NativeExpressAdView adView;
    private Context context;
    List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);

    public NativeAds(Context context) {
        adView = new NativeExpressAdView(context);
        this.context = context;
        initNativeAds();
    }

    private void initNativeAds() {
        int width = settingsObjectList.get(0).getNativeWidth();
        int height = settingsObjectList.get(0).getNativeHeight();

        if (width != 0 && height != 0) {
            adView.setAdSize(new AdSize(width, height));
        } else {
            adView.setAdSize(new AdSize(getFullWidth(), 250));
        }

        String ADMOB_AD_UNIT_ID = settingsObjectList.get(0).getNativeID();
        adView.setAdUnitId(ADMOB_AD_UNIT_ID);

//         Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        // Add the NativeExpressAdView to the view hierarchy.

        // Start loading the ad.
        adView.loadAd(adRequestBuilder.build());
    }

    private int getFullWidth() {
        Activity activity = (Activity) context;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = context.getResources().getDisplayMetrics().density;
        float dpHeight = (outMetrics.heightPixels / density);
        float dpWidth = (outMetrics.widthPixels / density);

        int inWidth = (int) (dpWidth) - (CustomFragment.marginLeft + CustomFragment.marginRight);
        Log.d(TAG, "buildNativeAdmobFragment: " + dpWidth + " " + inWidth);
        return inWidth;
    }

    public NativeExpressAdView getAdView() {
        return adView;
    }
}