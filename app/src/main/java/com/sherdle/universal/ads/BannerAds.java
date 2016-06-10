package com.sherdle.universal.ads;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sherdle.universal.SettingsFragment;
import com.sherdle.universal.db.objects.SettingsObject;

import java.util.List;

public class BannerAds {

    public static void initBannerAds(Context context, LinearLayout linearLayout){

        List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);
        String bannerID = settingsObjectList.get(0).getBannerID();

        if (settingsObjectList.get(0).getNativeID().equals("")) {
            if (bannerID != null && !bannerID.equals("") && !SettingsFragment.getIsPurchased(context)) {

                linearLayout.setVisibility(View.VISIBLE);
                AdView adView = new AdView(context);
                adView.setAdSize(AdSize.SMART_BANNER);
                adView.setAdUnitId(bannerID);
                linearLayout.addView(adView);
                AdRequest bannerRequest = new AdRequest.Builder().build();
                adView.loadAd(bannerRequest);
            }
        }
    }
}
