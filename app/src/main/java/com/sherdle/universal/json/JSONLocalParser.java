package com.sherdle.universal.json;

import android.content.Context;
import android.util.Log;

import com.sherdle.universal.ads.NativeAds;
import com.sherdle.universal.custom.entity.ButtonElement;
import com.sherdle.universal.custom.entity.Element;
import com.sherdle.universal.custom.entity.ImageElement;
import com.sherdle.universal.custom.entity.ListElement;
import com.sherdle.universal.custom.entity.NativeOfferElement;
import com.sherdle.universal.custom.entity.SettingsElement;
import com.sherdle.universal.custom.entity.TextElement;
import com.sherdle.universal.db.objects.ColorStyleObject;
import com.sherdle.universal.db.objects.NavItemObject;
import com.sherdle.universal.db.objects.NotificationObject;
import com.sherdle.universal.db.objects.SettingsObject;
import com.sherdle.universal.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONLocalParser {

    private static final String TAG = "JSONLocalParser";

    private static final String JSON_FILE_NAME = "data.json";
    private final Context appContext;

    public JSONLocalParser(Context appContext) throws IOException, JSONException {
        this.appContext = appContext;
    }

    private JSONObject getDataStorageContent() throws IOException, JSONException {
        byte[] buffer;
        InputStream is = appContext.getAssets().open(JSON_FILE_NAME);
        int n = is.available();
        buffer = new byte[n];
        is.read(buffer);
        is.close();
        String bufferString = new String(buffer);
        return new JSONObject(bufferString);
    }


    public void getNavigationItemList() throws IOException, JSONException {
        JSONObject data = getDataStorageContent();
        if (!data.isNull("navigation_items")) {
            JSONArray navigationItems = data.getJSONArray("navigation_items");
            for (int i = 0; i != navigationItems.length(); i++) {
                JSONObject item = (JSONObject) navigationItems.get(i);

                String title = "";
                int idIcon = 0;
                boolean visible = false;

                if (!item.isNull("title")) {
                    title = item.optString("title");
                }
                if (!item.isNull("icon")) {
                    idIcon = appContext.getResources().getIdentifier(item.optString("icon"), "drawable", appContext.getPackageName());
                    Log.d(TAG, "Recive Icon Id " + idIcon);
                }
                if (!item.isNull("visible")) {
                    visible = item.optBoolean("visible");
                }
                if (!item.isNull("fragment")) {
                    String fragmentAlias = item.optString("fragment");

                    if (fragmentAlias.equals("CustomFragment")) {
                        if (!item.isNull("elements")) {
                            String arrayData = item.getJSONArray("elements").toString();
                            NavItemObject navItem = new NavItemObject(title, idIcon, fragmentAlias, arrayData, visible);
                            navItem.save();
                            Log.d(TAG, "getNavigationItemList: " + "CustomFragment");
                        }
                    }
                }
            }
        }
    }

    public List<Element> getCustomElementList(String jsonCustomElements) throws IOException, JSONException {
        List<Element> resultList = new ArrayList<>();
        JSONArray elementsArray = new JSONArray(jsonCustomElements);
        for (int i = 0; i != elementsArray.length(); i++) {
            JSONObject element = (JSONObject) elementsArray.get(i);
            Element newElement = null;
            String id = element.optString("id");
            switch (id) {
                case "settings_element":
                    newElement = new SettingsElement(
                            element.optString("background_color"),
                            element.getString("background_image"));
                    break;
                case "text_element":
                    newElement = new TextElement(
                            element.optString("text_content"),
                            element.optString("text_color"),
                            element.optInt("text_size"),
                            element.getString("text_link"));
                    break;
                case "button_element":
                    newElement = new ButtonElement(
                            element.optString("button_text"),
                            element.optInt("button_text_size"),
                            element.optString("button_text_color"),
                            element.optString("button_color"),
                            element.optString("button_url"),
                            element.optString("button_fragment"),
                            element.optString("button_gravity"));
                    break;
                case "image_element":
                    newElement = new ImageElement(
                            element.optString("image_url"),
                            element.optString("link_url"),
                            element.optString("image_gravity"));
                    break;
                case "native_ads_element":
                    newElement = new NativeAds(appContext);
                    break;
                case "list_element":
                    newElement = new ListElement(
                            element.optString("list_title"),
                            element.optString("list_description"),
                            element.optString("list_image_url"),
                            element.optString("list_link_url"),
                            element.optString("list_fragment"),
                            element.optString("list_text_button"),
                            element.optInt("list_text_size"),
                            element.optString("list_button_text_color"),
                            element.optString("list_button_color"),
                            element.optInt("list_type"));
                    break;
                case "native_offer_element":
                    newElement = new NativeOfferElement();
                    break;
            }
            if (newElement != null) {
                resultList.add(newElement);
                Log.d(TAG, "Get Element: " + newElement.getClass() + " / " + newElement.toString());
            }
        }
        return resultList;
    }

    public void getNotification() throws IOException, JSONException {
        JSONObject data = getDataStorageContent();
        String text = "";
        int hour = 19;
        if (!data.isNull("notification")) {
            JSONObject jsonObject = data.getJSONObject("notification");
            if (!jsonObject.optString("text").equals("") && !jsonObject.isNull("text")) {
                text = jsonObject.optString("text");
            }
            if (jsonObject.optInt("hours") > 0 && jsonObject.optInt("hours") < 24 && !jsonObject.isNull("hours")) {
                hour = jsonObject.optInt("hours");
            }
        }
        Log.d(TAG, "getNotification: " + "  text: " + text + " hour: " + hour);
        NotificationObject notificationObject = new NotificationObject(text, hour);
        notificationObject.save();
    }

    public void getColorTheme() throws IOException, JSONException {
        JSONObject data = getDataStorageContent();
        String toolbar = "#3F51B5", tablayout = "#3F51B5", statusbar = "#3F51B5", drawerhead = "#3F51B5";
        if (!data.isNull("style")) {
            JSONObject jsonObject = data.getJSONObject("style");
            if (!jsonObject.optString("toolbar").equals("") && !jsonObject.isNull("toolbar")) {
                toolbar = jsonObject.optString("toolbar");
            }
            if (!jsonObject.optString("tablayout").equals("") && !jsonObject.isNull("tablayout")) {
                tablayout = jsonObject.optString("tablayout");
            }
            if (!jsonObject.optString("statusbar").equals("") && !jsonObject.isNull("statusbar")) {
                statusbar = jsonObject.optString("statusbar");
            }
            if (!jsonObject.optString("drawerhead").equals("") && !jsonObject.isNull("drawerhead")) {
                drawerhead = jsonObject.optString("drawerhead");
            }
        }
        ColorStyleObject colorStyle = new ColorStyleObject(
                toolbar,
                tablayout,
                statusbar,
                drawerhead);
        colorStyle.save();
    }

    public void getSettingsApplication() throws IOException, JSONException {
        JSONObject data = getDataStorageContent();
        String bannerID = "", interstitialID = "", nativeID = "", analyticsID = "", host;
        boolean offerwallVisible = true, shareVisible = true, rateVisible = true, settingsVisible = true;
        if (!data.isNull("settings")) {
            JSONObject jsonObject = data.getJSONObject("settings");
            if (!jsonObject.optString("admob_banner_id").equals("") && !jsonObject.isNull("admob_banner_id")) {
                bannerID = jsonObject.optString("admob_banner_id");
            }
            if (!jsonObject.optString("admob_interstitial_id").equals("") && !jsonObject.isNull("admob_interstitial_id")) {
                interstitialID = jsonObject.optString("admob_interstitial_id");
            }
            if (!jsonObject.optString("admob_native_id").equals("") && !jsonObject.isNull("admob_native_id")) {
                nativeID = jsonObject.optString("admob_native_id");
            }
            if (!jsonObject.optString("analytics_id").equals("") && !jsonObject.isNull("analytics_id")) {
                analyticsID = jsonObject.optString("analytics_id");
            }
            if (!jsonObject.isNull("offerwall_visible")) {
                offerwallVisible = jsonObject.optBoolean("offerwall_visible");
            }
            if (!jsonObject.isNull("share_visible")) {
                shareVisible = jsonObject.optBoolean("share_visible");
            }
            if (!jsonObject.isNull("rate_visible")) {
                rateVisible = jsonObject.optBoolean("rate_visible");
            }
            if (!jsonObject.isNull("settings_visible")) {
                settingsVisible = jsonObject.optBoolean("settings_visible");
            }
        }
        host = (Helper.IS_DEBUG ? "http://test.ru/" : "http://185.26.120.195:8001");
        SettingsObject settingsObject = new SettingsObject(
                bannerID,
                interstitialID,
                nativeID,
                analyticsID,
                host,
                0,
                0,
                offerwallVisible,
                shareVisible,
                rateVisible,
                settingsVisible);
        settingsObject.save();
    }
}
