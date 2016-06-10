package com.sherdle.universal.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sherdle.universal.AbstractTabFragment;
import com.sherdle.universal.R;
import com.sherdle.universal.db.objects.ColorStyleObject;
import com.sherdle.universal.offers.TabAdapter;

import java.util.List;
import java.util.Map;

public class AppParrentActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected TabAdapter adapter;
    protected Map<Integer, AbstractTabFragment> tabs;
    private static final String TAG = "AppParrentActivity";
    private boolean doubleBackToExitPressedOnce = false;

    public AppParrentActivity() {
    }


    protected void initOpenActivity(Class className) {
        startActivity(new Intent(this, className));
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }


    protected void initTabs(Map<Integer, AbstractTabFragment> tabs) {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabAdapter(getSupportFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void initToolbar(int title, boolean homeButtonEnable) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        if (homeButtonEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                }
            });
        }
    }

    protected void initColorTheme(boolean isToolbar, boolean isTablayout) {
        List<ColorStyleObject> colorStyleList = ColorStyleObject.listAll(ColorStyleObject.class);

        Log.d(TAG, "initColorTheme: " + colorStyleList.get(0).getDrawerHeadColor());
        Log.d(TAG, "initColorTheme: " + colorStyleList.get(0).getStatusBarColor());
        Log.d(TAG, "initColorTheme: " + colorStyleList.get(0).getTabLayoutColor());
        Log.d(TAG, "initColorTheme: " + colorStyleList.get(0).getToolbarColor());

        if (isToolbar)
        toolbar.setBackgroundColor(Color.parseColor(colorStyleList.get(0).getToolbarColor()));
        if (isTablayout)
        tabLayout.setBackgroundColor(Color.parseColor(colorStyleList.get(0).getTabLayoutColor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(colorStyleList.get(0).getStatusBarColor()));
            window.setNavigationBarColor(Color.parseColor(colorStyleList.get(0).getStatusBarColor()));
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
