package app.food.patient_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import app.food.patient_app.R;
import app.food.patient_app.data.DataManager;
import app.food.patient_app.service.AppService;
import app.food.patient_app.service.ConnectionCheckService;
import app.food.patient_app.sessionmanager.SessionManager;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    private final int SPLASH_TIME = 500;
    SessionManager sessionManager;
    Intent intent;
    int Flag;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final String TAG = "SplashActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(SplashActivity.this);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        checkLoginSession();
        initEvents();
        Intent intent = new Intent(SplashActivity.this, ConnectionCheckService.class);
        intent.setAction(ConnectionCheckService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);
        setContentView(R.layout.activity_splash);

    }


    private void checkLoginSession() {
        if (sessionManager.checkLogin()) {
            Flag = 1;
        } else {
            Flag = 0;

        }
        checkAndroidVersion();

    }

    private void checkAndroidVersion() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {

            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            openNewScreen();
                        }
                    }, SPLASH_TIME);
        }

    }

    private void openNewScreen() {
        if (Flag == 1) {

            intent = new Intent(SplashActivity.this, Login_Regi_Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
        } else {

            intent = new Intent(SplashActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) +
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) +
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CALL_LOG)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_CONTACTS)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_SECURE_SETTINGS)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.RECEIVE_SMS)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_SMS)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_SMS)+
                ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_CONTACTS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_CALL_LOG)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_SECURE_SETTINGS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.RECEIVE_SMS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_SMS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.BODY_SENSORS)){

                Snackbar.make(SplashActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.READ_CALL_LOG,
                                                Manifest.permission.READ_CONTACTS,
                                                Manifest.permission.READ_PHONE_STATE,
                                                Manifest.permission.WRITE_SECURE_SETTINGS,
                                                Manifest.permission.RECEIVE_SMS,
                                                Manifest.permission.READ_SMS,
                                                Manifest.permission.BODY_SENSORS},

                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_SECURE_SETTINGS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.BODY_SENSORS},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            openNewScreen();
                        }
                    }, SPLASH_TIME);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean location = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile && location) {
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        openNewScreen();
                                    }
                                }, SPLASH_TIME);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                                        Manifest.permission.CAMERA,
                                                        Manifest.permission.READ_CALL_LOG,
                                                        Manifest.permission.READ_CONTACTS,
                                                        Manifest.permission.READ_PHONE_STATE,
                                                        Manifest.permission.WRITE_SECURE_SETTINGS,
                                                        Manifest.permission.RECEIVE_SMS,
                                                        Manifest.permission.READ_SMS,
                                                        Manifest.permission.BODY_SENSORS},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initEvents() {

        if (!DataManager.getInstance().hasPermission(getApplicationContext())) {

            Intent intent = new Intent(SplashActivity.this, AppService.class);
            intent.putExtra(AppService.SERVICE_ACTION, AppService.SERVICE_ACTION_CHECK);
            startService(intent);
            finish();
        } else {
            //delayEnter();
        }
    }
    private void delayEnter() {
        new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}

