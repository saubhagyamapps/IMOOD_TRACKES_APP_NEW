package app.food.patient_app.lockscreen;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class LockService extends Service {
    private CounterReceiver lockReceiver = new CounterReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","LocakService");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockReceiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(lockReceiver);
        super.onDestroy();
        Log.d("log","onDestroy service");
    }
}