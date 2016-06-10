package com.sherdle.universal.json;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sherdle.universal.R;
import com.sherdle.universal.db.objects.OfferObject;
import com.sherdle.universal.db.objects.SettingsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JSONRemoteParser {

    private static final String TAG = "RemoteController";
    Context context;
    RequestQueue queue;
    List<SettingsObject> settingsObjectList = SettingsObject.listAll(SettingsObject.class);


    public JSONRemoteParser(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void sendConversion(boolean isFirstRun) {
        if (isFirstRun) {
            String url = settingsObjectList.get(0).getHost() + "/api/conversion/" + context.getPackageName() + "/" + context.getString(R.string.app_name).replace(" ", "");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getAdRequest();
                            Log.d(TAG, "Update Count Download:" + response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Response Error", error);
                        }
                    });
            queue.add(stringRequest);
        } else {
            getAdRequest();
        }
    }

    private void getAdRequest() {
        String url = settingsObjectList.get(0).getHost() + "/api/ad/" + context.getPackageName();
        Log.d(TAG, "getAdRequest: getHost " + settingsObjectList.get(0).getHost());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            String idBanner = "";
                            String idInterstitial = "";
                            String idNative = "";
                            int widthNative = 0;
                            int heightNative = 0;
                            String idApp = "";
                            String idAnalytics = "";
                            if (!data.isNull("application_ad_banner_id")) {
                                idBanner = data.optString("application_ad_banner_id");
                                if (!idBanner.equals("")) {
                                    Log.d(TAG, "onResponse: " + idBanner);
                                    settingsObjectList.get(0).setBannerID(idBanner);
                                }
                            }
                            if (!data.isNull("application_ad_interstitial_id")) {
                                idInterstitial = data.optString("application_ad_interstitial_id");
                                if (!idInterstitial.equals("")) {
                                    Log.d(TAG, "onResponse: " + idInterstitial);
                                    settingsObjectList.get(0).setInterstitialID(idInterstitial);
                                }
                            }
                            if (!data.isNull("application_ad_native_id")) {
                                idNative = data.optString("application_ad_native_id");
                                if (!idNative.equals("")) {
                                    Log.d(TAG, "onResponse: " + idNative);
                                    settingsObjectList.get(0).setNativeID(idNative);
                                }
                            }
                            if (!data.isNull("application_ad_native_width")) {
                                widthNative = data.optInt("application_ad_native_width");
                                if (widthNative != 0) {
                                    Log.d(TAG, "onResponse: " + widthNative);
                                    settingsObjectList.get(0).setNativeWidth(widthNative);
                                }
                            }
                            if (!data.isNull("application_ad_native_height")) {
                                heightNative = data.optInt("application_ad_native_height");
                                if (heightNative != 0) {
                                    Log.d(TAG, "onResponse: " + heightNative);
                                    settingsObjectList.get(0).setNativeHeight(heightNative);
                                }
                            }
                            if (!data.isNull("application_analytics_id")) {
                                idAnalytics = data.optString("application_analytics_id");
                                if (!idAnalytics.equals("")) {
                                    Log.d(TAG, "onResponse: " + idAnalytics);
                                    settingsObjectList.get(0).setAnalyticsID(idAnalytics);
                                }
                            }
                            settingsObjectList.get(0).save();
                            Log.d(TAG, "Recive Ad params"
                                    + " BannerID: " + idBanner
                                    + " InterstitialID: " + idInterstitial
                                    + " NativeID: " + idNative
                                    + " NativeWidth: " + widthNative
                                    + " NativeHeight: " + heightNative
                                    + " AppID: " + idApp
                                    + " AnalyticsID " + idAnalytics);
                        } catch (JSONException e) {
                            Log.d(TAG, "JSON Exception", e);
                        }
                        Log.d(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Response Error", error);
                    }
                });
        queue.add(stringRequest);
    }

    public void setPromoteOffers() {
        String url = settingsObjectList.get(0).getHost() + "/api/offers/";
        Log.d(TAG, "setPromoteOffers: getHost " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray offersArray = new JSONArray(response);
                            for (int i = 0; i != offersArray.length(); i++) {
                                JSONObject data = (JSONObject) offersArray.get(i);
                                JSONObject category = data.optJSONObject("offer_category");
                                OfferObject offer = new OfferObject(
                                        data.optString("offer_name"),
                                        data.optString("offer_url_thumb"),
                                        data.optInt("offer_rating"),
                                        data.optString("offer_download_link"),
                                        data.optInt("offer_priority"),
                                        category.optString("offer_category_name"));
                                offer.save();
                                Log.d("setPromoteOffers: ",
                                        data.optString("offer_name")
                                                + data.optString("offer_url_thumb")
                                                + data.optInt("offer_rating")
                                                + data.optString("offer_download_link")
                                                + data.optInt("offer_priority")
                                                + category.optString("offer_category_name"));
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "JSON Exception", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Response Error", error);
                    }
                });
        queue.add(stringRequest);
    }
}