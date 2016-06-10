package com.sherdle.universal.notif;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.sherdle.universal.R;
import com.sherdle.universal.activity.MainActivity;
import com.sherdle.universal.db.objects.NotificationObject;

import java.util.Calendar;
import java.util.List;

public class NotificationManager {

    private static final String TAG = "NotificationManager";
    private static final int NOTIFY_ID = 101;
    public static AlarmManager manager;
    public static Intent alarmIntent;
    public static PendingIntent pendingIntent;
    public static String NOTIFY_ON = "notify_on";

    public NotificationManager() {
    }

    public static void regesterNotification(Context context) {
        List<NotificationObject> notificationObjectList = NotificationObject.listAll(NotificationObject.class);
        alarmIntent = new Intent(context, AlarmReceiver.class);
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        manager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.setTimeInMillis(System.currentTimeMillis());
        alarmStartTime.set(Calendar.HOUR_OF_DAY, notificationObjectList.get(0).getTime());
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 00);

        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1); //Первое уведомление будет через 1 день
        }

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        // Saving for preferences
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor= prefs.edit();

        editor.putBoolean(NOTIFY_ON, true);
        editor.commit();

        Log.v("INFO", "Push Notifications Enabled");
        Log.d(TAG, alarmStartTime.getTime().toString());
        Log.d(TAG, "Regester notification " + notificationObjectList.get(0).getTime() + notificationObjectList.get(0).getDescription());
    }

    public static void pushNotification(Context context) {
        List<NotificationObject> notificationObjectList = NotificationObject.listAll(NotificationObject.class);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationObjectList.get(0).getDescription());

        Notification notification = builder.getNotification();
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public static void cancelrNotification(Context context) {
        // If the alarm has been set, cancel it.
        if (manager!= null) {
            manager.cancel(pendingIntent);
        } else {
            manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            manager.cancel(pendingIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        // Saving for preferences
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor= prefs.edit();

        editor.putBoolean(NOTIFY_ON, false);
        editor.commit();
        editor.apply();

        Log.v("INFO", "Push Notifications Disabled");
    }

}