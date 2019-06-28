package app.food.patient_app.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import app.food.patient_app.R;
import app.food.patient_app.backgroundservice.IndicatorServiceHelper;
import app.food.patient_app.fragment.DashBoardFragment;
import app.food.patient_app.fragment.EditProfilFragment;
import app.food.patient_app.fragment.GetCurrentLocationFragment;
import app.food.patient_app.fragment.GoogleFitDataFragment;
import app.food.patient_app.fragment.LineChartFragment;
import app.food.patient_app.fragment.MoodAndNewPicsFragment;
import app.food.patient_app.fragment.MoodAndSocialGraphFragment;
import app.food.patient_app.fragment.MoodCalendarFragment;
import app.food.patient_app.fragment.MoodSettingsFragment;
import app.food.patient_app.fragment.MoodandCaloriesFragment;
import app.food.patient_app.fragment.MoodandHome_Work_TimeFragment;
import app.food.patient_app.fragment.MoodandNewContactFragment;
import app.food.patient_app.fragment.MoodandPhoneLockFragment;
import app.food.patient_app.fragment.MoodandSocial_CallFragment;
import app.food.patient_app.fragment.MoodsAndCallsFragment;
import app.food.patient_app.fragment.ResetPasswordFragment;
import app.food.patient_app.fragment.SocialGraphViewPagerFragment;
import app.food.patient_app.fragment.SocialIntegrationFragment;
import app.food.patient_app.fragment.StepGraphViewPagerFragment;
import app.food.patient_app.fragment.WorkAndHomeGraphFragment;
import app.food.patient_app.fragment.WorkLocationFragment;
import app.food.patient_app.lockcount.IndicatorService;
import app.food.patient_app.lockcount.MyJobService;
import app.food.patient_app.model.CallInsertModel;
import app.food.patient_app.model.CalllogsListModel;
import app.food.patient_app.model.RemainingCallModel;
import app.food.patient_app.receiver.AlarmReceiver;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.ui.MainActivity;
import app.food.patient_app.util.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.food.patient_app.util.Constant.navigation;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "NavigationActivity";
    private static final String MyOnClick = "myOnClickTag";
    SessionManager sessionManager;
    Fragment fragment;
    Fragment fragment2;
    CalllogsListModel calllogsListModel;
    ArrayList<CalllogsListModel> calllogsListModels = new ArrayList<>();
    JSONObject singObj;
    String mName;
    ArrayList<HashMap<String, String>> contactList;
    String mMonthId;
    NavigationView navigationView;
    TextView txtName, txtEmail;
    CircleImageView imgUser;
    SharedPreferences prefs = null;
    private String url;

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Constant.setSession(getApplicationContext());
        System.gc();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        Constant.setSession(NavigationActivity.this);
        //scheduleJob();


        contactList = new ArrayList<>();
        Constant.setSession(getApplicationContext());
        RemainningCallAPI();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        navigation = findViewById(R.id.navigation);
        setBottomNavigation();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("IMOOD TRACKER");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, new DashBoardFragment());
        ft.commit();
        setNotifacation();
        sessionManager = new SessionManager(NavigationActivity.this);
        getFilePaths();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setnavigationHeader();
        IndicatorServiceHelper.startService(NavigationActivity.this);
    }

    private void setBottomNavigation() {


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment2 = null;

                switch (item.getItemId()) {

                    case R.id.nav_dashboard:
                        fragment2 = new DashBoardFragment();
                        break;
                    case R.id.nav_mood:
                        fragment2 = new MoodCalendarFragment();
                        break;
                    case R.id.nav_social_interaction:
                        fragment2 = new SocialIntegrationFragment();
                        break;

                }
                if (fragment2 != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment2);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }

    private boolean isServiceRunning(Class<IndicatorService> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getFilePaths() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths----->: " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
        }
        cursor.close();


    }

    private void setnavigationHeader() {
        View header = navigationView.getHeaderView(0);
        txtName = header.findViewById(R.id.txtName);
        txtEmail = header.findViewById(R.id.txtEmail);
        //    imgUser = header.findViewById(R.id.imageView);
        txtName.setText(Constant.mUserName.toUpperCase());
        txtEmail.setText(Constant.mUserEmail);
      /*  Glide.with(getApplicationContext()).load(user.get(session.KEY_IMAGE))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgUser);*/
    }

    private void setNotifacation() {
        Intent myIntent = new Intent(NavigationActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NavigationActivity.this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        int mMinute = new Time(System.currentTimeMillis()).getMinutes() + 1;
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int SECOND = Calendar.getInstance().get(Calendar.SECOND);
        firingCal.set(Calendar.HOUR, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, mMinute); // Particular minute
        firingCal.set(Calendar.SECOND, SECOND); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        if (id == R.id.nav_manage) {
            //fragment = new DashBoardFragment();
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        } else if (id == R.id.nav_calendar) {
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_mood);
          //  fragment = new MoodCalendarFragment();
        } else if (id == R.id.nav_calllogs) {
         //   fragment = new SocialIntegrationFragment();
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_social_interaction);
        } else if (id == R.id.nav_googlefit) {
            fragment = new GoogleFitDataFragment();
        } else if (id == R.id.nav_resetpassword) {
            fragment = new ResetPasswordFragment();
        } else if (id == R.id.nav_graph) {
            fragment = new LineChartFragment();
        } else if (id == R.id.nav_logout) {
            sessionManager.logoutUser();
            finish();
        } else if (id == R.id.nav_getCurrentLocation) {
            fragment = new GetCurrentLocationFragment();
        } else if (id == R.id.nav_editprofile) {
            fragment = new EditProfilFragment();
        } else if (id == R.id.nav_work_location) {
            fragment = new WorkLocationFragment();
        } else if (id == R.id.nav_social) {
            fragment = new SocialGraphViewPagerFragment();
        } else if (id == R.id.nav_step) {
            fragment = new StepGraphViewPagerFragment();
        } else if (id == R.id.nav_mood_social_call) {
            fragment = new MoodandSocial_CallFragment();
        } else if (id == R.id.nav_mood_social) {
            fragment = new MoodAndSocialGraphFragment();
        } else if (id == R.id.nav_mood_phone_lock) {
            fragment = new MoodandPhoneLockFragment();
        } else if (id == R.id.nav_mood_pics) {
            fragment = new MoodAndNewPicsFragment();
        } else if (id == R.id.nav_mood_Calls) {
            fragment = new MoodsAndCallsFragment();
        } else if (id == R.id.nav_mood_contacts) {
            fragment = new MoodandNewContactFragment();
        } else if (id == R.id.nav_mood_calories) {
            fragment = new MoodandCaloriesFragment();
        } else if (id == R.id.MoodSetting) {
            fragment = new MoodSettingsFragment();
        } else if (id == R.id.nav_mood_work_home_time) {
            fragment = new MoodandHome_Work_TimeFragment();
        } else if (id == R.id.nav_home_work) {
            fragment = new WorkAndHomeGraphFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void RemainningCallAPI() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Call<RemainingCallModel> callModelCall = Constant.apiService.remainingCall(Constant.mUserId, formattedDate);
        callModelCall.enqueue(new Callback<RemainingCallModel>() {
            @Override
            public void onResponse(Call<RemainingCallModel> call, Response<RemainingCallModel> response) {
                try {
                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        getCallDetails(response.body().getResult().get(i));
                        Log.e(TAG, "onResponse: ------>>>>>---->>> " + response.body().getResult().get(i));

                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ------>>>>>---->>>" + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<RemainingCallModel> call, Throwable t) {
                Log.e(TAG, "onFailure:------>>>>>---->>> " + t.getMessage());
            }
        });
    }

    private void getCallDetails(String s) {
        Log.e(TAG, "Display Api remaining date" + s);
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null,
                null, null);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        calllogsListModels.clear();
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {

            String Name = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            StringTokenizer tk = new StringTokenizer(String.valueOf(callDayTime));
            String day = tk.nextToken();
            String Month = tk.nextToken();
            String Date = tk.nextToken();
            String Time = tk.nextToken();
            String TIMEZONE = tk.nextToken();
            String Year = tk.nextToken();

            if (Month.equals("Jan")) {
                mMonthId = "01";
            } else if (Month.equals("Feb")) {
                mMonthId = "02";
            } else if (Month.equals("Mar")) {
                mMonthId = "03";
            } else if (Month.equals("Apr")) {
                mMonthId = "04";
            } else if (Month.equals("May")) {
                mMonthId = "05";
            } else if (Month.equals("Jun")) {
                mMonthId = "06";
            } else if (Month.equals("Jul")) {
                mMonthId = "07";
            } else if (Month.equals("Aug")) {
                mMonthId = "08";
            } else if (Month.equals("Sep")) {
                mMonthId = "09";
            } else if (Month.equals("Oct")) {
                mMonthId = "10";
            } else if (Month.equals("Nov")) {
                mMonthId = "11";
            } else if (Month.equals("Dec")) {
                mMonthId = "12";
            }

            String mNewDate = Year + "-" + mMonthId + "-" + Date;
            String mDate = Year + "-" + mMonthId + "-" + Date;


            if (Name != null && !Name.isEmpty() && !Name.equals("null")) {
                mName = Name;
            } else {
                mName = "Unknown";
            }
            if (mNewDate.equals(s)) {
                calllogsListModel = new CalllogsListModel(mName, phNumber, callType, mDate, Time, callDuration);
                calllogsListModels.add(calllogsListModel);

            }

        }


        JSONArray contaArray = new JSONArray();
        Log.e(TAG, "getCallDetails:----->>>>>>----<<<<<<< " + calllogsListModels.size() + "------>>>>" + s);
        for (int i = 0; i < calllogsListModels.size(); i++) {
            try {
                singObj = new JSONObject();
                singObj.put("name", calllogsListModels.get(i).getName());
                singObj.put("contactno", calllogsListModels.get(i).getPhone_number());
                singObj.put("type", calllogsListModels.get(i).getCall_type());
                singObj.put("duration", calllogsListModels.get(i).getCall_duration());
                singObj.put("time", calllogsListModels.get(i).getCall_time());
                singObj.put("date", calllogsListModels.get(i).getCall_date());
                contaArray.put(singObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        org.json.JSONObject object = singObj;
        JsonParser jsonParser = new JsonParser();
//        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

        Log.e(TAG, "getCallDetails: " + array.toString());


        apicall(array.toString(), s);
        Log.e(TAG, "getCallDetails------>>>>>------->>>>>>> " + array.toString());

    }

    public void createNotification() {


    /*    Bitmap bitmap = getBitmapFromURL("https://www.gstatic.com/webp/gallery3/1.png");
        NotificationCompat.BigPictureStyle notifystyle = new NotificationCompat.BigPictureStyle();
        notifystyle.bigPicture(bitmap);*/
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.gcm_custom_layout);
        //  contentView.setImageViewBitmap(R.id.happy, bitmap);
        //contentView.setTextViewText(R.id.title, "Testing");

        contentView.setOnClickPendingIntent(R.id.tital,
                getPendingSelfIntent(NavigationActivity.this, MyOnClick));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product1";
            // The user-visible name of the channel.
            CharSequence name = "Product1";
            String description = "Notifications regarding our products";
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MAX);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);
            Intent intent1 = new Intent(NavigationActivity.this, NavigationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product1")
                    .setSmallIcon(R.mipmap.ic_launcher) //your app icon
                    .setChannelId("id_product1")
                    .setContentIntent(pendingIntent)
                    .setContent(contentView);
            notificationManager.notify(1, notificationBuilder.build());

        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Log.e(TAG, "donenenenenenenennenenenenenen11");
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "donenenenenenenennenenenenenen22");
        if (MyOnClick.equals(intent.getAction())) {
            Log.e(TAG, "donenenenenenenennenenenenenen33");
        }
    }

    private void apicall(String toString, String s) {
        Constant.setSession(getApplicationContext());
        Log.e(TAG, "mUserId: " + Constant.mUserId);

        Call<CallInsertModel> call = Constant.apiService.insertCallData(toString, Constant.mUserId, s);
        call.enqueue(new Callback<CallInsertModel>() {
                         @Override
                         public void onResponse(Call<CallInsertModel> call, Response<CallInsertModel> response) {
                             // Log.e(TAG, "onResponse:----<<<<<<----->>>>>> " + response.body().getDate());
                             //  Toast.makeText(NavigationActivity.this, "" + response.body().getDate(), Toast.LENGTH_SHORT).show();
                         }

                         @Override
                         public void onFailure(Call<CallInsertModel> call, Throwable t) {
                             Log.e(TAG, "onFailure:-------->>>------>>>>---->>> " + t.getMessage());
                         }
                     }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

        // The JobService that we want to run
        final ComponentName name = new ComponentName(NavigationActivity.this, MyJobService.class);

        // Schedule the job
        final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.e(TAG, "Scheduled job successfully!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
        final long interval = TimeUnit.HOURS.toMillis(hour); // run every hour
        final boolean isPersistent = true; // persist through boot
        final int networkType = JobInfo.NETWORK_TYPE_ANY; // Requires some sort of connectivity

        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }

        return jobInfo;
    }


}
