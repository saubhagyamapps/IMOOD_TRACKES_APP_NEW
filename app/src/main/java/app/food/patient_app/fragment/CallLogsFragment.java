package app.food.patient_app.fragment;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wickerlabs.logmanager.LogObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.adapter.AppUsageMonthlyAdaptar;
import app.food.patient_app.model.AllCallsDataModel;
import app.food.patient_app.model.CalllogsListModel;
import app.food.patient_app.model.GetSMSCountModel;
import app.food.patient_app.model.GetSocialUsageListModel;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.LockCountShowModel;
import app.food.patient_app.model.NewAddedCountactCountModel;
import app.food.patient_app.model.SocialItegrationModel;
import app.food.patient_app.receiver.MessageListener;
import app.food.patient_app.receiver.MessageReceiver;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallLogsFragment extends Fragment implements MessageListener {
    View mView;
    private static final int SOME_VALUE_TO_START_APP_ONE = 0;
    String[] selection;
    String calltype = null;
    TextView textView = null;
    int number, type, duration;
    String phNumber, callType, callDuration;
    Date callDate, date;
    Date callDayTime;
    String mName;
    String result;
    JsonObject jsonObject;
    private static final int READ_LOGS = 725;
    private ListView logList;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
    LogObject logObject;
    ArrayList contactInfoArray;
    ArrayList ContactName;
    private static final String TAG = "CallLogsFragment";
    CalllogsListModel calllogsListModel;
    ArrayList<CalllogsListModel> calllogsListModels = new ArrayList<>();
    JSONObject singObj;
    String mMonthId = "";
    TextView txt_Date, txt_Total_Calls, txt_Total_Duration, txt_Total_MissedCall;
    Date todayDate;
    String mCurrentDate;
    RecyclerView recyclerView_apps;
    AppUsageMonthlyAdaptar appUsageMonthlyAdaptar;
    TextView txtToday_Add_contact, txtToday_Add_images, txt_current_date, txt_Sms_Count, txt_lockScreenCount,txtTodayDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_call_logs, container, false);
        getActivity().setTitle("");
        return mView;
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        initializ();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializ() {
        Constant.setSession(getActivity());
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        ContactName = new ArrayList();
        contactInfoArray = new ArrayList<>();
        txtToday_Add_contact = mView.findViewById(R.id.txtToday_Add_contact);
        txt_lockScreenCount = mView.findViewById(R.id.txt_lockScreenCount);
        txtToday_Add_images = mView.findViewById(R.id.txtToday_Add_images);

        txt_Total_Calls = mView.findViewById(R.id.txt_totalcalls);
        txt_Total_Duration = mView.findViewById(R.id.txt_callsduration);
        txt_Total_MissedCall = mView.findViewById(R.id.txt_totalmissedcall);
        txt_Sms_Count = (TextView) mView.findViewById(R.id.txt_sms_count);
        recyclerView_apps = mView.findViewById(R.id.app_recycle);
        txtTodayDate = mView.findViewById(R.id.txtTodayDate);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_apps.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_apps.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getActivity().getTheme()));
        MessageReceiver.bindListener(this);
        getCallsData();
        getMonthlyUsage();
        getNewCountactList();
        getFilePaths(getActivity());
        GetSMSCount();
        setLockScreenCount();
        txtTodayDate.setText(Constant.currentDate());
        getTotalData();
    }

    private void getTotalData() {
        Call<SocialItegrationModel> modelCall=Constant.apiService.getAllSocialData(Constant.mUserId,Constant.currentDate(),"1");
        modelCall.enqueue(new Callback<SocialItegrationModel>() {
            @Override
            public void onResponse(Call<SocialItegrationModel> call, Response<SocialItegrationModel> response) {

                String Duration_call = "";
                float sum = response.body().getTotal_callduration_count();
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
                        Duration_call = minutes + " min " + formatter.format(seconds) + " secs";
                    }
                } else if (sum >= 3600) {

                    result = String.valueOf(sum / 3600);
                    String decimal = result.substring(0, result.lastIndexOf("."));
                    String point = "0" + result.substring(result.lastIndexOf("."));

                    int hours = Integer.parseInt(decimal);
                    float minutes = Float.parseFloat(point) * 60;

                    DecimalFormat formatter = new DecimalFormat("#");
                    Duration_call = hours + " hrs " + formatter.format(minutes) + " min";

                }
                txt_Total_Calls.setText("Dial calls : " + response.body().getTotal_call_count());
                txt_Total_Duration.setText("Call duration : " + Duration_call);
                txt_Total_MissedCall.setText("Missed calls : " + response.body().getMissed_call_count());
                txt_lockScreenCount.setText("Lock Screen : "+String.valueOf(response.body().getScreenlockout_count()));
                txtToday_Add_images.setText("New Images : "+String.valueOf(response.body().getImage_count()));
                txtToday_Add_contact.setText("New Contact : "+String.valueOf(response.body().getCountuserdata_count()));
                txt_Sms_Count.setText("New SMS : "+String.valueOf(response.body().getTotal_sms_count()));
            }

            @Override
            public void onFailure(Call<SocialItegrationModel> call, Throwable t) {

            }
        });

    }

    private void setLockScreenCount() {

        Call<LockCountShowModel> modelCall = Constant.apiService.showLockCount(Constant.mUserId, Constant.currentDate());
        modelCall.enqueue(new Callback<LockCountShowModel>() {
            @Override
            public void onResponse(Call<LockCountShowModel> call, Response<LockCountShowModel> response) {
                Log.e(TAG, "setLockScreenCount: " + Constant.mLockCounter);
             /*   if (response.body().getStatus() == 0) {
                    txt_lockScreenCount.setText("Lock Screen Count "+response.body().getResult());
                } else {
                    txt_lockScreenCount.setText("Lock Screen Count 0");

                }*/
            }

            @Override
            public void onFailure(Call<LockCountShowModel> call, Throwable t) {

            }
        });
    }

    public void getFilePaths(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths:-----> " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
          //  Log.i("PATH", arrPath[i]);
        }
        APICALLIMAGESCOUNT(count);

        cursor.close();


    }

    private void APICALLIMAGESCOUNT(int count) {
        //txt_current_date.setText(mCurrentDate);
        Call<ImageCountModel> countModelCall = Constant.apiService.getImageCount(Constant.mUserId, mCurrentDate, String.valueOf(count));
        countModelCall.enqueue(new Callback<ImageCountModel>() {
            @Override
            public void onResponse(Call<ImageCountModel> call, Response<ImageCountModel> response) {
             /*   try {
                    if (response.body().getStatus().equals("0")) {
                        txtToday_Add_images.setText("Today " + response.body().getResult().get(0).getCount() + " new images add");
                    }
                } catch (Exception e) {

                }*/

            }

            @Override
            public void onFailure(Call<ImageCountModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ->APICALLIMAGESCOUNT" + t.getMessage());
            }
        });
    }

    private void getNewCountactList() {

        Call<NewAddedCountactCountModel> addedCountactCountModelCall = Constant.apiService.getNewAddedCountactCount(Constant.mUserId, mCurrentDate);
        addedCountactCountModelCall.enqueue(new Callback<NewAddedCountactCountModel>() {
            @Override
            public void onResponse(Call<NewAddedCountactCountModel> call, Response<NewAddedCountactCountModel> response) {
           //     txtToday_Add_contact.setText("Today " + response.body().getCount() + " new Contact add");
            }

            @Override
            public void onFailure(Call<NewAddedCountactCountModel> call, Throwable t) {

            }
        });
    }

    private void getCallsData() {
        Call<AllCallsDataModel> allCallsDataModelCall = Constant.apiService.getAllCallData(Constant.mUserId, mCurrentDate);
        allCallsDataModelCall.enqueue(new Callback<AllCallsDataModel>() {
            @Override
            public void onResponse(Call<AllCallsDataModel> call, Response<AllCallsDataModel> response) {

              /*  String Duration_call = "";
                float sum = Integer.parseInt(response.body().getTotal_callduration());
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
                        Duration_call = minutes + " min " + formatter.format(seconds) + " secs";
                    }
                } else if (sum >= 3600) {

                    result = String.valueOf(sum / 3600);
                    String decimal = result.substring(0, result.lastIndexOf("."));
                    String point = "0" + result.substring(result.lastIndexOf("."));

                    int hours = Integer.parseInt(decimal);
                    float minutes = Float.parseFloat(point) * 60;

                    DecimalFormat formatter = new DecimalFormat("#");
                    Duration_call = hours + " hrs " + formatter.format(minutes) + " min";

                }
                txt_Date.setText(mCurrentDate);
                txt_Total_Calls.setText("Total number of calls  " + response.body().getTotal_call());
                txt_Total_Duration.setText("Total calls duration  " + Duration_call);
                txt_Total_MissedCall.setText("Total missed calls  " + response.body().getTotal_missedcall());
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onFailure(Call<AllCallsDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    private void getMonthlyUsage() {

        Call<GetSocialUsageListModel> socialusageListModelCall = Constant.apiService.CallSocialList(Constant.mUserId, Constant.currentDate(),"1");
        socialusageListModelCall.enqueue(new Callback<GetSocialUsageListModel>() {
            @Override
            public void onResponse(Call<GetSocialUsageListModel> call, Response<GetSocialUsageListModel> response) {
                if (response.body().getStatus().equals("0")) {
                    List<GetSocialUsageListModel.ResultBean> resultBeans = response.body().getResult();
                    appUsageMonthlyAdaptar = new AppUsageMonthlyAdaptar(getActivity(), resultBeans);
                    recyclerView_apps.setHasFixedSize(true);
                    appUsageMonthlyAdaptar.notifyDataSetChanged();
                    recyclerView_apps.setAdapter(appUsageMonthlyAdaptar);
                }
            }

            @Override
            public void onFailure(Call<GetSocialUsageListModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void messageReceived(String message) {
        Toast.makeText(getActivity(), "New Message Received: " + message, Toast.LENGTH_SHORT).show();

    }

    private void GetSMSCount() {
        Call<GetSMSCountModel> getSMSCountModelCall = Constant.apiService.getSMSCount(Constant.mUserId, mCurrentDate);
        getSMSCountModelCall.enqueue(new Callback<GetSMSCountModel>() {
            @Override
            public void onResponse(Call<GetSMSCountModel> call, Response<GetSMSCountModel> response) {
/*
                if (response.body().getStatus().equals("0")) {
                    txt_Sms_Count.setText("Today's SMS Count   " + response.body().getCount());
                } else {
                    Log.e(TAG, "onResponse: " + "cannot fetch count");
                }*/
            }

            @Override
            public void onFailure(Call<GetSMSCountModel> call, Throwable t) {

            }
        });
    }
}