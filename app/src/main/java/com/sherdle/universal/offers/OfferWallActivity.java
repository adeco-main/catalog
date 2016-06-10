package com.sherdle.universal.offers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sherdle.universal.R;
import com.sherdle.universal.activity.AppParrentActivity;
import com.sherdle.universal.db.ManagerORM;
import com.sherdle.universal.db.objects.CategoryObject;

import java.util.HashMap;
import java.util.List;

public class OfferWallActivity extends AppParrentActivity {

    private static final int LAYOUT = R.layout.activity_offerwall;
    private static final String TAG = "OfferWallActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initTabsMap();
        initTabs(tabs);
        initToolbar(R.string.offerwal_activity, true);
        initColorTheme(true, true);
    }

    private void initTabsMap() {
        ManagerORM managerORM = new ManagerORM();
        managerORM.clearCategoriesORM();
        managerORM.setCategoriesORM();
        tabs = new HashMap<>();
        List<CategoryObject> categoryList = CategoryObject.listAll(CategoryObject.class);
        for (int i = 0; i < categoryList.size(); i++) {
            String s = categoryList.get(i).getTitle();
            OfferWallFragment offerWallFragment = OfferWallFragment.getInstance();
            offerWallFragment.setTitle(s);
            tabs.put(i, offerWallFragment);
            Log.d(TAG, "initTabsMap: " + i + " " + s);
        }
    }

    @Override
    public void onBackPressed() {
        OfferWallActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

}
