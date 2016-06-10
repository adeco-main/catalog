package com.sherdle.universal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sherdle.universal.BackPressFragment;
import com.sherdle.universal.PermissionsFragment;
import com.sherdle.universal.R;
import com.sherdle.universal.SettingsFragment;
import com.sherdle.universal.ads.BannerAds;
import com.sherdle.universal.ads.InterstitialAds;
import com.sherdle.universal.db.objects.NotificationObject;
import com.sherdle.universal.nav.NavDrawerCallback;
import com.sherdle.universal.nav.NavDrawerFragment;
import com.sherdle.universal.nav.NavItem;
import com.sherdle.universal.nav.NavListConfig;
import com.sherdle.universal.notif.NotificationManager;
import com.sherdle.universal.offers.OfferWallActivity;
import com.sherdle.universal.util.Helper;

import java.util.List;

public class MainActivity extends AppParrentActivity implements
        NavDrawerCallback {

    private NavDrawerFragment mNavigationDrawerFragment;

    //Data to pass to a fragment
    public static String FRAGMENT_DATA = "transaction_data";
    public static String FRAGMENT_CLASS = "transation_target";

    public static boolean TABLET_LAYOUT = true;

    //Permissions Queu
    NavItem queueItem;

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (useTabletMenu()) {
            setContentView(R.layout.activity_main_tablet);
        } else if (NavListConfig.USE_NEW_DRAWER) {
            setContentView(R.layout.activity_main_alternate);
        } else {
            setContentView(R.layout.activity_main);
        }

        initToolbar(R.string.app_name, false);
        initColorTheme(true, false);
        InterstitialAds interstitialAds = new InterstitialAds(this);
        interstitialAds.showInterstitial();

//		if (!useTabletMenu())
//			getSupportActionBar().setDisplayShowHomeEnabled(true);
//		else {
//			getSupportActionBar().setDisplayShowHomeEnabled(false);
//		}

        mNavigationDrawerFragment = (NavDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_drawer);

        if (NavListConfig.USE_NEW_DRAWER == true && !useTabletMenu()) {
            mNavigationDrawerFragment.setup(R.id.scrimInsetsFrameLayout,
                    (DrawerLayout) findViewById(R.id.drawer), toolbar);
            mNavigationDrawerFragment
                    .getDrawerLayout();

            findViewById(R.id.scrimInsetsFrameLayout).getLayoutParams().width = getDrawerWidth();
        } else {
            mNavigationDrawerFragment.setup(R.id.fragment_drawer,
                    (DrawerLayout) findViewById(R.id.drawer), toolbar);

            DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mNavigationDrawerFragment.getView().getLayoutParams();
            params.width = getDrawerWidth();
            mNavigationDrawerFragment.getView().setLayoutParams(params);
        }

        if (useTabletMenu()) {
            mNavigationDrawerFragment
                    .getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mNavigationDrawerFragment
                    .getDrawerLayout().setScrimColor(Color.TRANSPARENT);
        } else {
            mNavigationDrawerFragment
                    .getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        BannerAds.initBannerAds(this, (LinearLayout) findViewById(R.id.adView));

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // Check if we should open a fragment based on the arguments we have
        boolean loadedFragment = false;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(FRAGMENT_CLASS)) {
            try {
                Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) getIntent().getExtras().getSerializable(FRAGMENT_CLASS);
                if (fragmentClass != null) {
                    String[] extra = getIntent().getExtras().getStringArray(FRAGMENT_DATA);

                    Fragment fragment = fragmentClass.newInstance();
                    showFragment(fragment, extra, getTitle().toString());
                    loadedFragment = true;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //If we haven't already loaded an item (or came from rotation and there was already an item)
        //Load the first item
        if (savedInstanceState == null && !loadedFragment) {
            mNavigationDrawerFragment.loadInitialItem();
        }

        // Checking if the user would prefer to show the menu on start
        boolean checkBox = prefs.getBoolean("menuOpenOnStart", true);
        if (checkBox && !useTabletMenu()) {
            mNavigationDrawerFragment.openDrawer();
        }


        // Checking if the user would prefer to notification
        boolean switchPref = prefs.getBoolean(NotificationManager.NOTIFY_ON, true);

        List<NotificationObject> notificationObjectList = NotificationObject.listAll(NotificationObject.class);

        if (notificationObjectList.get(0).getDescription().equals("")) {
            NotificationManager.cancelrNotification(this);
        } else {
            if (switchPref) {
                NotificationManager.regesterNotification(this);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.rss_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                boolean foundfalse = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        foundfalse = true;
                    }
                }
                if (!foundfalse) {
                    openNavigationItem(queueItem);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.permissions_required), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openNavigationItem(NavItem item) {
        try {
            Fragment fragment = item.getFragment().newInstance();

            if (fragment != null) {

                //Verify if we can safely open the fragment by checking for permissions
                if (checkPermissionsHandleIfNeeded(item, fragment) && checkPurchaseHandleIfNeeded(item)) {
                    String[] extra = item.getData();
                    showFragment(fragment, extra, item.getTitle(this));
                } else {
                    //We do nothing, the check method will handle this for us.
                }

            } else {
                Log.v("INFO", "Error creating fragment");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public void showFragment(Fragment fragment, String[] extra, String title) {
        Bundle bundle = new Bundle();

        bundle.putStringArray(FRAGMENT_DATA, extra);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();

        if (!useTabletMenu())
            setTitle(title);
    }

    @SuppressLint("NewApi")
    @Override
    public void onNavigationDrawerItemSelected(int position, NavItem item) {
        switch (item.getTitle(this)) {
            case "Share":
                Helper.initShareApplication(this);
                break;
            case "Rate":
                Helper.initRateApplication(this);
                break;
            case "Related apps":
                Helper.initOpenActivity(this, OfferWallActivity.class);
                break;
            default:
                openNavigationItem(item);
        }
    }

    /**
     * If the item can be opened because it either has been purchased or does not require a purchase to show.
     *
     * @param item Item to check
     * @return true if the item is safe to be opened. False if the item is not safe to open, simply don't open as we will handle.
     */
    private boolean checkPurchaseHandleIfNeeded(NavItem item) {
        String license = getResources().getString(R.string.google_play_license);
        // if item does not require purchase, or app has purchased, or license is null/empty (app has no in app purchases)
        if (item.requiresPurchase() == true
                && !SettingsFragment.getIsPurchased(this)
                && null != license && !license.equals("")) {
            Fragment fragment = new SettingsFragment();
            String[] extra = new String[]{SettingsFragment.SHOW_DIALOG};
            showFragment(fragment, extra, item.getTitle(this));

            return false;
        }

        return true;
    }

    /**
     * Checks if the item can be opened because it has sufficient permissions.
     *
     * @param item     The item to check
     * @param fragment The fragment instance associated to this item.
     * @return true if the item is safe to open
     */
    private boolean checkPermissionsHandleIfNeeded(NavItem item, Fragment fragment) {
        if (fragment instanceof PermissionsFragment && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String[] permissions = ((PermissionsFragment) fragment).requiredPermissions();

            boolean allGranted = true;
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    allGranted = false;
            }

            if (!allGranted) {
                //TODO An explaination before asking
                requestPermissions(permissions, 1);
                queueItem = item;
                return false;
            }

            return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(
                R.id.container);

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else if (activeFragment instanceof BackPressFragment) {
            boolean handled = ((BackPressFragment) activeFragment).handleBackPress();
            if (!handled) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments)
                frag.onActivityResult(requestCode, resultCode, data);
        }
    }

    private int getDrawerWidth() {
        // Navigation Drawer layout width
        int width = getResources().getDisplayMetrics().widthPixels;

        TypedValue tv = new TypedValue();
        int actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        } else {
            actionBarHeight = 0;
        }

        int possibleMinDrawerWidth = width - actionBarHeight;

        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.drawer_width);

        return Math.min(possibleMinDrawerWidth, maxDrawerWidth);
    }


    public boolean useTabletMenu() {
        return (getResources().getBoolean(R.bool.isWideTablet) && TABLET_LAYOUT);
    }

}