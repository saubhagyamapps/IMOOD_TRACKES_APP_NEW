package app.food.patient_app.backgroundservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;

import app.food.patient_app.backgroundservice.settings.Settings;


public final class IndicatorServiceHelper {
    private static final String TAG = "IndicatorServiceHelper";

    private static Intent getServiceIntent(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        Intent serviceIntent = new Intent(context, IndicatorService.class);

        for (Map.Entry<String, ?> entry : sharedPref.getAll().entrySet()) {
            if (entry.getValue() instanceof Boolean) {
                serviceIntent.putExtra(entry.getKey(), (boolean) (Object) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                serviceIntent.putExtra(entry.getKey(), (String) entry.getValue());
            }
        }

        return serviceIntent;
    }

    public static void startService(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(getServiceIntent(context));
            Log.e(TAG, "startService:->startForegroundService ");
        } else {
            Log.e(TAG, "startService:->startService");
            context.startService(getServiceIntent(context));
        }


        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(Settings.KEY_INDICATOR_STARTED, true)
                .apply();
    }

    public static void stopService(Context context) {
        context.stopService(new Intent(context, IndicatorService.class));

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(Settings.KEY_INDICATOR_STARTED, false)
                .apply();
    }
}
