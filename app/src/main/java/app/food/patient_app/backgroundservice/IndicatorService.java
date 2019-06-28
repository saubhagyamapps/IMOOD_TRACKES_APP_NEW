package app.food.patient_app.backgroundservice;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.activity.LockAPICallClass;
import app.food.patient_app.backgroundservice.settings.Settings;
import app.food.patient_app.data.AppItem;
import app.food.patient_app.data.DataManager;
import app.food.patient_app.util.AppUtil;


public final class IndicatorService extends Service {
    private KeyguardManager mKeyguardManager;

    private long mLastRxBytes = 0;
    private long mLastTxBytes = 0;
    private long mLastTime = 0;
    private static final String TAG = "IndicatorService";
    private Speed mSpeed;
    int Count = 0;
    private IndicatorNotification mIndicatorNotification;

    private boolean mNotificationCreated = false;
    private long mTotal;
    private boolean mNotificationOnLockScreen;
    private PackageManager mPackageManager;
    final private Handler mHandler = new Handler();
    static int count = 0;
    boolean mFlag = false;
    private final Runnable mHandlerRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            long currentRxBytes = TrafficStats.getTotalRxBytes();
            long currentTxBytes = TrafficStats.getTotalTxBytes();
            long usedRxBytes = currentRxBytes - mLastRxBytes;
            long usedTxBytes = currentTxBytes - mLastTxBytes;
            long currentTime = System.currentTimeMillis();
            long usedTime = currentTime - mLastTime;

            mLastRxBytes = currentRxBytes;
            mLastTxBytes = currentTxBytes;
            mLastTime = currentTime;

            mSpeed.calcSpeed(usedTime, usedRxBytes, usedTxBytes);
            long mTotal = 0;
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
           // Log.e(TAG, "Background Service is Ruining !!!!!!!!!!!!!! " );

            mIndicatorNotification.updateNotification(mSpeed);
            mHandler.postDelayed(this, 1000);

        }
    };


    private final BroadcastReceiver mScreenBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(TAG, "ACTION_SCREEN_OFF: ");
                Toast.makeText(context, "Lock --->", Toast.LENGTH_SHORT).show();
                //    pauseNotifying();
                Count++;
                if (!mNotificationOnLockScreen) {
                    mIndicatorNotification.hideNotification();
                }
                mIndicatorNotification.updateNotification(mSpeed);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Toast.makeText(context, "UnLock --->", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ACTION_SCREEN_ON: ");
                if (mNotificationOnLockScreen || !mKeyguardManager.isKeyguardLocked()) {
                    mIndicatorNotification.showNotification();
                    restartNotifying();
                }
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                mIndicatorNotification.showNotification();
                restartNotifying();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();

        pauseNotifying();
        unregisterReceiver(mScreenBroadcastReceiver);

        removeNotification();
    }

    public void onCreate() {
        super.onCreate();
        mPackageManager = getPackageManager();
        // startForeground(1,new Notification());
        mLastRxBytes = TrafficStats.getTotalRxBytes();
        mLastTxBytes = TrafficStats.getTotalTxBytes();
        mLastTime = System.currentTimeMillis();

        mSpeed = new Speed(this);

        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);


        mIndicatorNotification = new IndicatorNotification(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleConfigChange(intent.getExtras());

        createNotification();

        restartNotifying();

        return START_REDELIVER_INTENT;
    }

    private void createNotification() {
        if (!mNotificationCreated) {
            mIndicatorNotification.start(this);
            mNotificationCreated = true;
        }
    }

    private void removeNotification() {
        if (mNotificationCreated) {
            mIndicatorNotification.stop(this);
            mNotificationCreated = false;
        }
    }

    private void pauseNotifying() {
        mHandler.removeCallbacks(mHandlerRunnable);
    }

    private void restartNotifying() {
        mHandler.removeCallbacks(mHandlerRunnable);
        mHandler.post(mHandlerRunnable);
    }

    private void handleConfigChange(Bundle config) {
        // Show/Hide on lock screen
        IntentFilter screenBroadcastIntentFilter = new IntentFilter();
        screenBroadcastIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenBroadcastIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mNotificationOnLockScreen = config.getBoolean(Settings.KEY_NOTIFICATION_ON_LOCK_SCREEN, false);

        if (!mNotificationOnLockScreen) {
            screenBroadcastIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
            screenBroadcastIntentFilter.setPriority(999);
        }

        if (mNotificationCreated) {
            unregisterReceiver(mScreenBroadcastReceiver);
        }
        registerReceiver(mScreenBroadcastReceiver, screenBroadcastIntentFilter);

        // Speed unit, bps or Bps
        boolean isSpeedUnitBits = config.getString(Settings.KEY_INDICATOR_SPEED_UNIT, "Bps").equals("bps");
        mSpeed.setIsSpeedUnitBits(isSpeedUnitBits);

        // Pass it to notification
        mIndicatorNotification.handleConfigChange(config);
    }
}
