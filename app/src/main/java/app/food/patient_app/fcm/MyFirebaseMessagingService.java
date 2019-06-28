package app.food.patient_app.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import app.food.patient_app.R;
import app.food.patient_app.activity.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    int NOTIFICATION_ID;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {
            final JSONObject data = json.getJSONObject("data");
            Log.e(TAG, "handleDataMessage: "+data );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        final int icon = R.mipmap.ic_launcher;
        Bitmap icon1 = BitmapFactory.decodeResource(getApplicationContext().getResources(), icon);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            CharSequence name = "Product";
            String description = "Notifications regarding our products";

            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MAX);


            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setShowBadge(true);
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);
            Intent notificationIntent = new Intent(getApplication(), SplashActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.putExtra("message", "yes");
            notificationIntent.putExtra("senderId", "");
            notificationIntent.putExtra("firstName", "");
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")

                    .setSmallIcon(icon)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setChannelId("id_product")
                    .setAutoCancel(true)
                    .setSound(Uri.parse(String.valueOf(Notification.DEFAULT_SOUND)))
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setContentText("")
                    .setWhen(System.currentTimeMillis())

                    .build();
            NotificationManagerCompat notificationManager1 =
                    NotificationManagerCompat.from(this);
            notificationManager1.notify(NOTIFICATION_ID, notificationBuilder);

        }
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Intent notificationIntent = new Intent(getApplication(), SplashActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("message", "yes");
        notificationIntent.putExtra("senderId", "");
        notificationIntent.putExtra("firstName", "");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setTicker("").setWhen(0)
                        .setAutoCancel(true)
                        .setSound(Uri.parse(String.valueOf(Notification.DEFAULT_SOUND)))
                        //  .setStyle(inboxStyle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentIntent(contentIntent)
                        .setWhen(System.currentTimeMillis())
                        .setContentText("");
        Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                .bigText("").build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, notification);
        Intent iq = new Intent();
        iq.setAction("countrefresh");
        this.sendBroadcast(iq);


        Intent i = new Intent();
        i.setAction("appendChatScreenMsg");
        i.putExtra("sender_id", "");
        i.putExtra("message", "");
        i.putExtra("firstName", "");
        this.sendBroadcast(i);
    }
}