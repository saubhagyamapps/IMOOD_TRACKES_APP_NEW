package app.food.patient_app.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import app.food.patient_app.data.DataManager;
import app.food.patient_app.service.AppService;

public class PermissionActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initEvents() {

        if (!DataManager.getInstance().hasPermission(getApplicationContext())) {

            Intent intent = new Intent(PermissionActivity.this, AppService.class);
            intent.putExtra(AppService.SERVICE_ACTION, AppService.SERVICE_ACTION_CHECK);
            startService(intent);
        } else {
           /* Intent intent = new Intent(PermissionActivity.this, SplashActivity.class);
            startActivity(intent);*/
        }
    }

}
