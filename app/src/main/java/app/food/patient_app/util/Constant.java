package app.food.patient_app.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import app.food.patient_app.R;
import app.food.patient_app.retrofit.ApiClient;
import app.food.patient_app.retrofit.ApiInterface;
import app.food.patient_app.sessionmanager.SessionManager;

public class Constant {


        //  public static final String BASE_URL = "http://192.168.1.200/patient_app_server/";
    public static final String BASE_URL = "http://www.charmhdwallpapers.com/patient_app/";
    // public static final String BASE_URL = "http://51.159.3.125/patient_app/";


    public static ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

    public static ProgressDialog progressBar;
    public static SessionManager session;
    public static String mUserId;
    public static String mUserName;
    public static String mUserMobile;
    public static String mUserFirebaseID;
    public static String mUserEmail;
    public static String mAddress;
    public static String mImages;
    public static String mImagesPath;
    public static String mGender;
    public static String mPassword;
    public static String mDeviceId;
    public static String mHomeLat;
    public static String mHomeLong;
    public static String mLockCounter;
    public static BottomNavigationView navigation;

    public static void progressDialog(Context applicationContext) {
        progressBar = new ProgressDialog(applicationContext);
        progressBar.setCancelable(false);
        progressBar.setTitle(applicationContext.getResources().getString(R.string.progress_dialog_title));
        progressBar.setMessage(applicationContext.getResources().getString(R.string.progress_dialog_msg));
        progressBar.show();

    }


    public static String currentDate() {
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        return mCurrentDate;
    }


    public static void setSession(Context applicationContext) {

        session = new SessionManager(applicationContext);
        HashMap<String, String> user = session.getUserDetails();
        mUserId = user.get(SessionManager.KEY_ID);
        mUserName = user.get(SessionManager.KEY_NAME);
        mUserMobile = user.get(SessionManager.KEY_MOBILE);
        mUserFirebaseID = user.get(SessionManager.KEY_FIREBASEID);
        mUserEmail = user.get(SessionManager.KEY_EMAIL);
        //mAddress = user.get(SessionManager.KEY_ADDRESS);
        mImages = user.get(SessionManager.KEY_IMAGE);
        // mGender = user.get(SessionManager.KEY_GENDER);
        mPassword = user.get(SessionManager.KEY_PASSWORD);
        mDeviceId = user.get(SessionManager.KEY_DEVICEID);
        mImagesPath = user.get(SessionManager.KEY_IMAGES);
    }

    public static String secondTominORHR(float sum) {
        String Duration_call = "";
        String result;
        if (sum >= 0 && sum < 3600) {

            result = String.valueOf(sum / 60);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int minutes = Integer.parseInt(decimal);
            float seconds = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            if (minutes == 0) {
                Duration_call = formatter.format(seconds) + " secs";
            } else {
                Duration_call = minutes + "m " + formatter.format(seconds) + "s";
            }
        } else if (sum >= 3600) {

            result = String.valueOf(sum / 3600);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int hours = Integer.parseInt(decimal);
            float minutes = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            Duration_call = hours + "h " + formatter.format(minutes) + "m";

        }

        return Duration_call;
    }
}
