package app.food.patient_app.receiver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.food.patient_app.R;
import app.food.patient_app.data.AppItem;
import app.food.patient_app.data.DataManager;
import app.food.patient_app.lockscreen.LockService;
import app.food.patient_app.lockscreen.SharedPrefererenceCounter;
import app.food.patient_app.model.ChangeWorkLocationModel;
import app.food.patient_app.model.HomeLocationStoreModel;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.InsertSocialTimeModel;
import app.food.patient_app.model.LocationChgangeModel;
import app.food.patient_app.model.SocialusageListModel;
import app.food.patient_app.util.AppUtil;
import app.food.patient_app.util.Constant;
import app.food.patient_app.util.MyContentObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.ALARM_SERVICE;


public class AlarmReceiver extends BroadcastReceiver{
    private long mTotal;
    String mMessages;
    String mTital;

    SocialusageListModel socialusageListModel;
    private List<SocialusageListModel> array_list;
    JSONObject singObj;
    SharedPrefererenceCounter counter;
    SharedPreferences sharedPreferences;
    private static final String TAG = "AlarmReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        getFilePaths(context);
        storeSocialTime(context);
        context.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyContentObserver(new Handler(), context));
        lockORunlock(context);
        requestLocationUpdates(context);
        System.gc();
    }

    private void callWorkLocationAPI(Context context, String address, String lat, String log) {
        System.gc();
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);
        Call<ChangeWorkLocationModel> call=Constant.apiService.StoreWorkLocarion(Constant.mUserId,address,lat,log,mCurrentDate);
        call.enqueue(new Callback<ChangeWorkLocationModel>() {
            @Override
            public void onResponse(Call<ChangeWorkLocationModel> call, Response<ChangeWorkLocationModel> response) {
              //  Log.e(TAG, "onResponse:---work location " );
            }

            @Override
            public void onFailure(Call<ChangeWorkLocationModel> call, Throwable t) {
              //  Log.e(TAG, "onFailure: :---work location "+t.getMessage() );
            }
        });
    }

    private void lockORunlock(Context context) {
        System.gc();
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            //it is locked
          //  Toast.makeText(context, "locked", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "locked:------ >");
        } else {
            //it is not locked
            //Toast.makeText(context, "Unlocked", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Unlocked:------ >");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void storeSocialTime(Context context) {
        System.gc();
        array_list = new ArrayList<>();

        DataManager manager = DataManager.getInstance();
        List<AppItem> items = manager.getApps(context, 0, 0);
        for (AppItem item : items) {
            if (item.mUsageTime <= 0) continue;
            mTotal += item.mUsageTime;
        }
        Log.e(TAG, "Total Time:==> " + AppUtil.formatMilliSeconds(mTotal));
        for (AppItem item : items) {
            Log.e(TAG, "App name: " + item.mName);
            mTital = item.mName;
            mMessages = AppUtil.formatMilliSeconds(item.mUsageTime);
            Log.e(TAG, "usages Time " + mMessages);

            socialusageListModel = new SocialusageListModel(item.mName, item.mUsageTime);
            array_list.add(socialusageListModel);
        }

        JSONArray contaArray = new JSONArray();
        for (int i = 0; i < array_list.size(); i++) {
            try {
                singObj = new JSONObject();
                singObj.put("name", array_list.get(i).getName());
                singObj.put("usage", array_list.get(i).getUsage());
                contaArray.put(singObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        Log.e(TAG, "Array_LIst " + array.toString());
        SocialUsageCall(array, context);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Registering contact observer

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        int mMinute = new Time(System.currentTimeMillis()).getMinutes() + 1;
        //   int mMinute = new Time(System.currentTimeMillis()).getMinutes();
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int mSecond = Calendar.getInstance().get(Calendar.SECOND);
        Log.e(TAG, "Time---> : " + hour);
        firingCal.set(Calendar.HOUR, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, mMinute); // Particular minute
        firingCal.set(Calendar.SECOND, mSecond); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent1);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent1);
        }

      /*  int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Product";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(mTital)
                    .setContentText(mMessages)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            mNotificationManager.notify(notifyID, notification);
        }*/
    }


    private void SocialUsageCall(JsonArray array, Context context) {
        System.gc();
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);

        Call<InsertSocialTimeModel> stringCall = Constant.apiService.InsertSocialData(Constant.mUserId, mCurrentDate, array.toString());
        stringCall.enqueue(new Callback<InsertSocialTimeModel>() {
            @Override
            public void onResponse(Call<InsertSocialTimeModel> call, Response<InsertSocialTimeModel> response) {
                Log.e(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<InsertSocialTimeModel> call, Throwable t) {
                Log.d("response", "Getting response from server : " + t);
            }
        });

    }

    private void StoreHomeAddress(Context context,String address, String LATITUDE, String LONGITUDE) {
        System.gc();
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);
        Call<HomeLocationStoreModel> storeModelCall=Constant.apiService.storeHomeLocation(Constant.mUserId,mCurrentDate,address,LATITUDE,LONGITUDE);
        storeModelCall.enqueue(new Callback<HomeLocationStoreModel>() {
            @Override
            public void onResponse(Call<HomeLocationStoreModel> call, Response<HomeLocationStoreModel> response) {

            }

            @Override
            public void onFailure(Call<HomeLocationStoreModel> call, Throwable t) {

            }
        });
    }

    public void getFilePaths(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int count = cursor.getCount();

      /*  String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths:-----> " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }*/
        APICALLIMAGESCOUNT(context, count);

        cursor.close();


    }

    private void APICALLIMAGESCOUNT(final Context context, int count) {
        System.gc();
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);
        Call<ImageCountModel> countModelCall = Constant.apiService.getImageCount(Constant.mUserId, mCurrentDate, String.valueOf(count));
        countModelCall.enqueue(new Callback<ImageCountModel>() {
            @Override
            public void onResponse(Call<ImageCountModel> call, Response<ImageCountModel> response) {
                try {
                    if (response.body().getStatus().equals("0")) {
                    //    Toast.makeText(context, response.body().getResult().get(0).getCount(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<ImageCountModel> call, Throwable t) {

            }
        });
    }

    private void requestLocationUpdates(final Context context) {
        System.gc();
        LocationRequest request = new LocationRequest();
        request.setInterval(3000);
        request.setFastestInterval(3000);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                       // Log.e(TAG, "location update " + location.getLongitude());
                      //  Log.e(TAG, "long update " + location.getLongitude());


                        getAddress(context, location.getLatitude(), location.getLongitude());

                    }
                }
            }, null);
        }
    }


    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

             /*   Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);*/

                LocationChangeAPICALL(context, address.replace(state+postalCode+","+country,""), LATITUDE, LONGITUDE);
                StoreHomeAddress(context,address, String.valueOf(LATITUDE),String.valueOf(LONGITUDE));
                callWorkLocationAPI(context,address, String.valueOf(LATITUDE),String.valueOf(LONGITUDE));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void LocationChangeAPICALL(final Context context, String address, final double LATITUDE, double LONGITUDE) {
        System.gc();
        Constant.setSession(context);
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Call<LocationChgangeModel> modelCall = Constant.apiService.locationChange(Constant.mUserId, mCurrentDate,
                address, String.valueOf(LATITUDE), String.valueOf(LONGITUDE));
        modelCall.enqueue(new Callback<LocationChgangeModel>() {
            @Override
            public void onResponse(Call<LocationChgangeModel> call, Response<LocationChgangeModel> response) {
                Log.e(TAG, "onResponse: " );
              //  Toast.makeText(context, String.valueOf(LATITUDE), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LocationChgangeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " );
            }
        });
    }

}
