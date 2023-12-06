package com.newmysmileQR.Utility.Notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.newmysmileQR.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NotificationUtils {


    public static final String CHANNEL_ONE_ID = "com.school365.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Playing notification sound
    public static void playNotificationSound(Context mContext, String type) {
        try {
            if(type.equals("other")){
                type = "general";
            }
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/" + type);
            MediaPlayer mp = MediaPlayer.create(mContext, alarmSound);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createChannels(Context context) {

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager(context).createNotificationChannel(notificationChannel);
        }
    }

    public static NotificationCompat.Builder getNotification(Context context, String title, String body) {
            return new NotificationCompat.Builder(context, CHANNEL_ONE_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setBigContentTitle(title))
                    .setAutoCancel(true);
    }

    public static NotificationCompat.Builder getNotification(Context context, String title, String body,Bitmap bitmap) {
            return new NotificationCompat.Builder(context, CHANNEL_ONE_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null))
                    .setAutoCancel(true);
    }


    public static void notify(Context context,int id, NotificationCompat.Builder notification) {
        getManager(context).notify(id, notification.build());
    }

    private static NotificationManager getManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}