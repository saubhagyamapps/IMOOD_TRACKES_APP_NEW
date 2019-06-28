package app.food.patient_app.sessionmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.StreamTokenizer;
import java.util.HashMap;

import app.food.patient_app.activity.LoginActivity;

public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "patientapp";
    public static final String IS_LOGIN = "isloggedin";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIREBASEID = "firebaseId";
    public static final String KEY_DEVICEID = "DeviceId";
    public static final String KEY_IMAGES = "imagepath";
    public static final String KEY_IMAGE = "image";


    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    public void CreateLoginSession(String id, String name, String email, String mobile,
                                   String password, String firebaseId, String deviceId,String image) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE,mobile);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FIREBASEID, firebaseId);
        editor.putString(KEY_DEVICEID, deviceId);
        editor.putString(KEY_IMAGE, image);
        editor.apply();
    }

    public void ImagesPath(String imgesPath) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_IMAGES, imgesPath);
        editor.apply();
    }

    public boolean checkLogin() {
        if (!this.isLoggedIn()) {
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE, preferences.getString(KEY_MOBILE, null));
        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
        user.put(KEY_FIREBASEID, preferences.getString(KEY_FIREBASEID, null));
        user.put(KEY_DEVICEID, preferences.getString(KEY_DEVICEID, null));
        user.put(KEY_IMAGES, preferences.getString(KEY_IMAGES, null));
        user.put(KEY_IMAGE, preferences.getString(KEY_IMAGE, null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
      /*  Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
