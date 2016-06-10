package com.sherdle.universal.db;


import android.util.Log;

import com.sherdle.universal.db.objects.CategoryObject;
import com.sherdle.universal.db.objects.ColorStyleObject;
import com.sherdle.universal.db.objects.NavItemObject;
import com.sherdle.universal.db.objects.NotificationObject;
import com.sherdle.universal.db.objects.OfferObject;
import com.sherdle.universal.db.objects.SettingsObject;

import java.util.HashSet;
import java.util.List;

public class ManagerORM {

    public static final String TAG = "ManagerORM";

    public void clearNavItemORM(){
        List<NavItemObject> navItemObjectList = NavItemObject.listAll(NavItemObject.class);
        NavItemObject.deleteAll(NavItemObject.class);
    }

    public void clearOfferORM(){
        List<OfferObject> offerList = OfferObject.listAll(OfferObject.class);
        OfferObject.deleteAll(OfferObject.class);
    }

    public void clearCategoriesORM(){
        List<CategoryObject> categoryList = CategoryObject.listAll(CategoryObject.class);
        CategoryObject.deleteAll(CategoryObject.class);
    }

    public void clearColorORM(){
        List<ColorStyleObject> colorStyleList = ColorStyleObject.listAll(ColorStyleObject.class);
        ColorStyleObject.deleteAll(ColorStyleObject.class);
    }

    public void clearNotificationORM(){
        List<NotificationObject> notificationObjectList = NotificationObject.listAll(NotificationObject.class);
        NotificationObject.deleteAll(NotificationObject.class);
    }

    public void clearSettingsORM(){
        List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);
        SettingsObject.deleteAll(SettingsObject.class);
    }

    public void setCategoriesORM(){
        List<OfferObject> offerList = OfferObject.listAll(OfferObject.class);
        HashSet<String> hashSet = new HashSet<>();
        for (int i = 0; i < offerList.size(); i++) {
            String title = offerList.get(i).getCategory();
            Log.d(TAG, "setCategoriesORM: " + offerList.get(i).getName() + " "+ offerList.get(i).getCategory());
            hashSet.add(title);
        }

        for (String temp : hashSet) {
            CategoryObject category = new CategoryObject(temp);
            category.save();
        }
    }


}
