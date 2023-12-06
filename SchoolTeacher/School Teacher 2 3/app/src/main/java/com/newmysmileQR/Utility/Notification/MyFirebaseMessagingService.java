package com.newmysmileQR.Utility.Notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.newmysmileQR.R;
import com.newmysmileQR.SplashActivity;
import com.newmysmileQR.Utility.Preference;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;

        /*if (remoteMessage.getNotification() != null) {
            Notification mNotification = new Notification();
            mNotification.body = remoteMessage.getNotification().getBody();
            mNotification.title = remoteMessage.getNotification().getTitle();
            mNotification.userType = "2";
            handleNotification(mNotification);
        }*/

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Notification mNotification = new Notification();
            mNotification.title = data.get("title");
            mNotification.body = data.get("body");
            mNotification.url = data.get("url");
            mNotification.customData = gson.fromJson(data.get("customData"), Notification.Data.class);
            showNotification(mNotification);
        }
    }

    private void handleNotification(Notification mNotification) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", mNotification.body);
            pushNotification.putExtra("title", mNotification.title);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            showNotification(mNotification);
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    @Override
    public void onNewToken(String refreshedToken) {
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        super.onNewToken(refreshedToken);
    }

    private void showNotification(Notification mNotification) {
        try {
            if (mNotification.customData.userType != Preference.getUser(getApplicationContext()).getUserType())
                return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationUtils.createChannels(getApplicationContext());
            }

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder;

            if (mNotification.url != null && !mNotification.url.equalsIgnoreCase("")) {
                builder = NotificationUtils.getNotification(
                        getApplicationContext(),
                        mNotification.title,
                        mNotification.body,
                        NotificationUtils.getBitmapFromURL(mNotification.url)).setContentIntent(pendingIntent);
            } else {
                builder = NotificationUtils.getNotification(
                        getApplicationContext(),
                        mNotification.title,
                        mNotification.body).setContentIntent(pendingIntent);
            }

            NotificationUtils.notify(getApplicationContext(), 0, builder);
//            NotificationUtils.playNotificationSound(getApplicationContext(), mNotification.results.mType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

}