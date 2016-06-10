package com.sherdle.universal.nav;

import android.util.Log;

import com.sherdle.universal.R;
import com.sherdle.universal.SettingsFragment;
import com.sherdle.universal.custom.CustomFragment;
import com.sherdle.universal.db.objects.NavItemObject;
import com.sherdle.universal.db.objects.SettingsObject;

import java.util.ArrayList;
import java.util.List;

public class NavListConfig {

    //To use the new drawer (overlaying toolbar)
    public static final boolean USE_NEW_DRAWER = true;

    private static final String TAG = "NavListConfig";

    public static List<NavItem> getAllFragment() {

        List<NavItem> allFragmentList = new ArrayList<NavItem>();

        List<NavItemObject> objects = NavItemObject.listAll(NavItemObject.class);

        for (int j = 0; j < objects.size(); j++) {
            switch (objects.get(j).getFragment()) {
                case "CustomFragment":
                    allFragmentList.add(new NavItem(objects.get(j).getTitle(), objects.get(j).getIcon(), NavItem.ITEM, CustomFragment.class, new String[]{objects.get(j).getData()}, objects.get(j).isVisible()));
                    break;
            }
            Log.d(TAG, "getAllFragment: " + objects.get(j).getFragment());
        }
        return allFragmentList;
    }

    public static List<NavItem> getNavFragment() {

        List<NavItem> navItemList = new ArrayList<NavItem>();

        //DONT MODIFY ABOVE THIS LINE

        //Some sample content is added below, please refer to your documentation for more information about configuring this file properly
        navItemList.add(new NavItem("Section", NavItem.SECTION));

        for (int i = 0; i < getAllFragment().size(); i++) {
            if (getAllFragment().get(i).isVisible()) {
                navItemList.add(getAllFragment().get(i));
            }
        }

//        navItemList.add(new NavItem("Favorites", R.drawable.ic_action_favorite, NavItem.EXTRA, FavFragment.class, null, true));

        //It's suggested to not change the content below this line
        List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);

        if (settingsObjectList.get(0).isOfferwallVisible() || settingsObjectList.get(0).isShareVisible() || settingsObjectList.get(0).isRateVisible() || settingsObjectList.get(0).isSettingsVisible()) {
            navItemList.add(new NavItem("Extra", NavItem.SECTION));
        }
        if (settingsObjectList.get(0).isOfferwallVisible()) {
            navItemList.add(new NavItem("Related apps", R.drawable.ic_apps, NavItem.EXTRA));
        }
        if (settingsObjectList.get(0).isShareVisible()) {
            navItemList.add(new NavItem("Share", R.drawable.ic_share, NavItem.EXTRA));
        }
        if (settingsObjectList.get(0).isRateVisible()) {
            navItemList.add(new NavItem("Rate", R.drawable.ic_rate, NavItem.EXTRA));
        }
        if (settingsObjectList.get(0).isSettingsVisible()) {
            navItemList.add(new NavItem("Settings", R.drawable.ic_settings, NavItem.EXTRA, SettingsFragment.class, null, true));
        }

        //DONT MODIFY BELOW THIS LINE

        return navItemList;

    }

}