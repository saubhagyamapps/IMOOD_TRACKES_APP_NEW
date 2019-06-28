package app.food.patient_app.lockcount;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import app.food.patient_app.R;
import app.food.patient_app.activity.LockAPICallClass;

public final class IndicatorService extends Service {

    final private Handler mHandler = new Handler();
    String countere;
    Handler handler1;
    static int count = 0;
    boolean mFlag = false;
    private static final String TAG = "IndicatorService";
    private final Runnable mHandlerRunnable = new Runnable() {

        @Override
        public void run() {
            handler1 = new Handler();

               Toast toast = Toast.makeText(IndicatorService.this, "", Toast.LENGTH_LONG);
            View view1 = toast.getView();
            toast.getView().setPadding(20, 20, 20, 20);
            view1.setBackgroundResource(R.color.transparent);

              toast.show();
       //     Toast.makeText(IndicatorService.this, "afaf", Toast.LENGTH_SHORT).show();
            KeyguardManager myKM = (KeyguardManager) IndicatorService.this.getSystemService(Context.KEYGUARD_SERVICE);
            if (myKM.inKeyguardRestrictedInputMode()) {
                mFlag = true;
            } else {
                if (mFlag) {
                    mFlag = false;
                    count++;
                    new LockAPICallClass(IndicatorService.this);
                }

            }

            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "Product";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                Notification notification = new Notification.Builder(IndicatorService.this, CHANNEL_ID)
                        .setContentTitle(countere)
                        .setContentText(String.valueOf(count))
                        .setSmallIcon(R.drawable.ic_notifacation_icon)
                        .setChannelId(CHANNEL_ID)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        //   .setOngoing(true)
                        //.setVisibility(Notification.VISIBILITY_SECRET)
                        //     .setLocalOnly(true)
                        .build();
                NotificationManager mNotificationManager =
                        (NotificationManager) IndicatorService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);

                mNotificationManager.notify(notifyID, notification);
            }
            mHandler.postDelayed(this, 1000);


        }
    };

    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mHandlerRunnable);

        Log.e(TAG, "onDestroyServices");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.removeCallbacks(mHandlerRunnable);
        mHandler.post(mHandlerRunnable);
        return START_REDELIVER_INTENT;
    }

}
