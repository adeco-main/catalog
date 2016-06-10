package com.sherdle.universal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sherdle.universal.R;
import com.sherdle.universal.db.ManagerORM;
import com.sherdle.universal.db.objects.SettingsObject;
import com.sherdle.universal.json.JSONLocalParser;
import com.sherdle.universal.json.JSONRemoteParser;
import com.sherdle.universal.util.Helper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppParrentActivity {

    private static final String TAG = "LoadingActivity";
    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Helper.isOnline(this)) {
            Helper.noConnectionDialog(this);
        } else {
            initManagerORM();
            initLocalParseJSON();
            initLaunchSettings();
            setContentView(R.layout.activity_loading);
            initShowSplash();
        }
    }

    private void initLocalParseJSON() {
        try {
            JSONLocalParser jsonParser = new JSONLocalParser(this);
            jsonParser.getNotification();
            jsonParser.getSettingsApplication();
            jsonParser.getColorTheme();
            jsonParser.getNavigationItemList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initManagerORM() {
        ManagerORM managerORM = new ManagerORM();
        managerORM.clearNotificationORM();
        managerORM.clearOfferORM();
        managerORM.clearNavItemORM();
        managerORM.clearColorORM();
        managerORM.clearSettingsORM();
    }

    private void initLaunchSettings() {
        SharedPreferences settings = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        boolean isFirstRun = settings.getBoolean("firstStartLoading", true);
        initializeImageLoader();
        initGoogleAnalytics(isFirstRun);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstStartLoading", false);
        editor.apply();

        JSONRemoteParser jsonRemote = new JSONRemoteParser(this);
        jsonRemote.sendConversion(isFirstRun);
        jsonRemote.setPromoteOffers();

    }

    private void initShowSplash() {
        int drawableResourceId = this.getResources().getIdentifier("splash", "drawable", this.getPackageName());
        if (drawableResourceId > 0) {
            ((ImageView) findViewById(R.id.splash)).setImageResource(drawableResourceId);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    openMainActivity();
                }
            }, SPLASH_TIMEOUT);
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private ImageLoader initializeImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.empty_image)
                    .decodingOptions(decodingOptions)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(options)
                    .build();
            imageLoader.init(config);
        }
        return imageLoader;
    }

    private void initGoogleAnalytics(boolean isFirstRun) {
        List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        Tracker tracker = analytics.newTracker(settingsObjectList.get(0).getAnalyticsID());
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.setScreenName(getClass().getName());
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Application")
                .setAction("Launch")
                .setLabel(getPackageName())
                .build());
        if (isFirstRun) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Application")
                    .setAction("Download")
                    .setLabel(getPackageName())
                    .build());
        }
        Log.d(TAG, "initGoogleAnalytics: " + getPackageName());
    }

}