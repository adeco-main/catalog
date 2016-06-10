package com.sherdle.universal.util;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Helper {
	
	public static final boolean IS_DEBUG = false;
	private static final String TAG = "Helper";
	
	public static void noConnectionDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("No network connection")
				.setMessage("Please turn on mobile internet")
				.setCancelable(false)
				.setNegativeButton("ОК",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
								dialog.cancel();
							}
						});
		android.support.v7.app.AlertDialog alert = builder.create();
		alert.show();
     }

    public static boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}
    
    @SuppressLint("NewApi")
	public static void revealView(View toBeRevealed, View frame){

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				// get the center for the clipping circle
				int cx = (frame.getLeft() + frame.getRight()) / 2;
				int cy = (frame.getTop() + frame.getBottom()) / 2;

				// get the final radius for the clipping circle
				int finalRadius = Math.max(frame.getWidth(), frame.getHeight());

				// create the animator for this view (the start radius is zero)
				Animator anim = ViewAnimationUtils.createCircularReveal(
						toBeRevealed, cx, cy, 0, finalRadius);

				// make the view visible and start the animation
				toBeRevealed.setVisibility(View.VISIBLE);
				anim.start();
			} else {
				toBeRevealed.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Makes high numbers readable (e.g. 5000 -> 5K)
	public static String formatValue(double value) {
		if (value > 0){
			int power; 
		    String suffix = " kmbt";
		    String formattedNumber = "";

		    NumberFormat formatter = new DecimalFormat("#,###.#");
		    power = (int)StrictMath.log10(value);
		    value = value/(Math.pow(10,(power/3)*3));
		    formattedNumber=formatter.format(value);
		    formattedNumber = formattedNumber + suffix.charAt(power/3);
		    return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;  
		} else {
			return "0";
		}
	}

    public static String getDataFromUrl(String url){
        // Making HTTP request
        Log.v("INFO", "Requesting: " + url);

        StringBuffer chaine = new StringBuffer("");
        try {
            URL urlCon = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlCon
                    .openConnection();
            connection.setRequestProperty("User-Agent", "Universal/2.0 (Android)");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return chaine.toString();
    }

    //Get JSON from an url and parse it to a JSON Object.
	public static JSONObject getJSONObjectFromUrl(String url) {
		String data = getDataFromUrl(url);

		try {
			return new JSONObject(data);
		} catch (Exception e) {
            Log.e("INFO", "Error parsing JSON. Printing stacktrace now");
			e.printStackTrace();
		}

		return null;
	}

    //Get JSON from an url and parse it to a JSON Array.
    public static JSONArray getJSONArrayFromUrl(String url) {
        String data = getDataFromUrl(url);

        try {
            return new JSONArray(data);
        } catch (Exception e) {
            Log.e("INFO", "Error parsing JSON. Printing stacktrace now");
            e.printStackTrace();
        }

        return null;
    }

	public static String onVerificationUrl(String url) {

		if (!url.contains("http://") && !url.contains("https://")) {
			url = "http://" + url;
		}
		Log.d(TAG, "onVerificationUrl: " + url);
		return url;
	}

	public static void initOpenActivity(Context context, Class className){
		Activity activity = (Activity) context;
		activity.startActivity(new Intent(context, className));
		activity.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}


	public static void initShareApplication(Context context) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Get in on Google Play https://play.google.com/store/apps/details?id=" + context.getPackageName());
		sendIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(sendIntent, "Share it"));
		Log.d(TAG, "Share Choise");
	}

	public static void initRateApplication(Context context) {
		context.startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
		Log.d(TAG, "Rate Choise");
	}

}
