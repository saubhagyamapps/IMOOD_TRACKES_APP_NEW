package app.food.patient_app.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import app.food.patient_app.GlideApp;
import app.food.patient_app.R;
import app.food.patient_app.data.AppItem;
import app.food.patient_app.data.DataManager;
import app.food.patient_app.db.DbIgnoreExecutor;
import app.food.patient_app.model.CalllogsListModel;
import app.food.patient_app.model.InsertSocialTimeModel;
import app.food.patient_app.model.SocialItegrationModel;
import app.food.patient_app.model.SocialusageListModel;
import app.food.patient_app.service.AlarmService;
import app.food.patient_app.util.AppUtil;
import app.food.patient_app.util.Constant;
import app.food.patient_app.util.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Fragment {


    private LinearLayout mSort;
    private Switch mSwitch;
    private TextView mSwitchText;
    private RecyclerView mList;
    private MyAdapter mAdapter;
    private AlertDialog mDialog;
    private SwipeRefreshLayout mSwipe;
    private TextView mSortName, txtTotalTime;
    private long mTotal;
    private int mDay;
    private PackageManager mPackageManager;
    View mView;
    String NAME;
    Long mUSAGE;
    JSONObject singObj;
    Date todayDate;
    String mCurrentDate;
    private static final String TAG = "MainActivity";
    SocialusageListModel socialusageListModel;
    private List<SocialusageListModel> array_list;
    String mMessages;
    String result;
    TextView txtCallTime,txtImages,txtLock,txtMsg,app_time,txtCont;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.activity_main, container, false);
        getActivity().setTitle("");
        mPackageManager = getActivity().getPackageManager();
        mSort = mView.findViewById(R.id.sort_group);
        mSortName = mView.findViewById(R.id.sort_name);
        mSwitch = mView.findViewById(R.id.enable_switch);
        mSwitchText = mView.findViewById(R.id.enable_text);
        txtLock = mView.findViewById(R.id.txtLock);
        txtCallTime = mView.findViewById(R.id.txtCallTime);
        txtImages = mView.findViewById(R.id.txtImages);
        txtMsg = mView.findViewById(R.id.txtMsg);
        txtCont = mView.findViewById(R.id.txtCont);
        app_time = mView.findViewById(R.id.app_time);
        txtTotalTime = mView.findViewById(R.id.txtTotalTime);
        array_list = new ArrayList<>();
        mAdapter = new MyAdapter();
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        DataManager manager = DataManager.getInstance();
        List<AppItem> items = manager.getApps(getActivity(), 0, 0);
        for (AppItem item : items) {
            if (item.mUsageTime <= 0) continue;
            mTotal += item.mUsageTime;

        }
        Log.e(TAG, "Total Time:==> " + AppUtil.formatMilliSeconds(mTotal));
        for (AppItem item : items) {
            Log.e(TAG, "App Name: " + item.mName);
            mMessages =  AppUtil.formatMilliSeconds(item.mUsageTime);
            Log.e(TAG, "App Usage Time "+mMessages);

                socialusageListModel = new SocialusageListModel(item.mName, item.mUsageTime);
                array_list.add(socialusageListModel);
            }


            Log.e(TAG, "onCreateView: "+array_list.toString());



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
        Log.e(TAG, "Array_LIst "+array.toString() );
        SocialUsageCall(array);


        mList = mView.findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), DividerItemDecoration.VERTICAL);
     //   dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getActivity().getTheme()));
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(mAdapter);


      //  initLayout();
        //initEvents();
        initSpinner();
        initSort();
        //dataFromAdapter();
        if (DataManager.getInstance().hasPermission(getActivity())) {
            process();
            getActivity().startService(new Intent(getActivity(), AlarmService.class));
        }

        getTotalData();
        return mView;
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
                        Duration_call = minutes + " m " + formatter.format(seconds) + " s";
                    }
                } else if (sum >= 3600) {

                    result = String.valueOf(sum / 3600);
                    String decimal = result.substring(0, result.lastIndexOf("."));
                    String point = "0" + result.substring(result.lastIndexOf("."));

                    int hours = Integer.parseInt(decimal);
                    float minutes = Float.parseFloat(point) * 60;

                    DecimalFormat formatter = new DecimalFormat("#");
                    Duration_call = hours + " h " + formatter.format(minutes) + " m";

                }

                txtCallTime.setText(Duration_call);
                app_time.setText(String.valueOf(response.body().getTotal_call_count())+" Times");
                txtImages.setText(String.valueOf(response.body().getImage_count()));
                txtLock.setText(String.valueOf(response.body().getScreenlockout_count()));
                txtMsg.setText(String.valueOf(response.body().getTotal_sms_count()));
                txtCont.setText(String.valueOf(response.body().getCountuserdata_count()));

            }

            @Override
            public void onFailure(Call<SocialItegrationModel> call, Throwable t) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initLayout() {
      /*  mSwipe = mView.findViewById(R.id.swipe_refresh);
        if (DataManager.getInstance().hasPermission(getActivity())) {
            mSwitchText.setText(R.string.enable_apps_monitoring);
            mSwitch.setVisibility(View.GONE);
            mSort.setVisibility(View.GONE);
            mSwipe.setEnabled(true);
        } else {
            mSwitchText.setText(R.string.enable_apps_monitor);
            mSwitch.setVisibility(View.VISIBLE);
            mSort.setVisibility(View.GONE);
            mSwitch.setChecked(false);
            mSwipe.setEnabled(false);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initSort() {
        if (DataManager.getInstance().hasPermission(getActivity())) {
            mSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triggerSort();
                }
            });
        }
    }

    private void triggerSort() {
        mDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sort)
                .setSingleChoiceItems(R.array.sort, PreferenceManager.getInstance().getInt(PreferenceManager.PREF_LIST_SORT), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceManager.getInstance().putInt(PreferenceManager.PREF_LIST_SORT, i);
                        process();
                        mDialog.dismiss();
                    }
                })
                .create();
        mDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initSpinner() {
        if (DataManager.getInstance().hasPermission(getActivity())) {
            Spinner spinner = mView.findViewById(R.id.spinner);
            spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.duration, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mDay != i) {


                        int[] values = getResources().getIntArray(R.array.duration_int);
                        mDay = values[i];
                        process();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initEvents() {

       /* mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                process();
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();

        if (!DataManager.getInstance().hasPermission(getActivity())) {
            mSwitch.setChecked(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    private void process() {
        if (DataManager.getInstance().hasPermission(getActivity())) {
            mList.setVisibility(View.INVISIBLE);
            int sortInt = PreferenceManager.getInstance().getInt(PreferenceManager.PREF_LIST_SORT);
            mSortName.setText(getSortName(sortInt));

            new MyAsyncTask().execute(sortInt, mDay);
        }
    }

    private String getSortName(int sortInt) {
        return getResources().getStringArray(R.array.sort)[sortInt];
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AppItem info = mAdapter.getItemInfoByPosition(item.getOrder());
        switch (item.getItemId()) {
            case R.id.ignore:
                DbIgnoreExecutor.getInstance().insertItem(info);
                process();
                Toast.makeText(getActivity(), R.string.ignore_success, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.open:
                startActivity(mPackageManager.getLaunchIntentForPackage(info.mPackageName));
                return true;
            case R.id.more:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + info.mPackageName));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), 1);
                return true;
            case R.id.sort:
                triggerSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(">>>>>>>>", "result code " + requestCode + " " + resultCode);
        if (resultCode > 0) process();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDialog != null) mDialog.dismiss();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<AppItem> mData;

        MyAdapter() {
            super();
            mData = new ArrayList<>();
        }

        public MyAdapter(List<AppItem> mData) {
            mData = new ArrayList<>();
        }

        void updateData(List<AppItem> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public  AppItem getItemInfoByPosition(int position) {
            if (mData.size() > position) {

                return mData.get(position);
            }
            return null;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

                AppItem item = getItemInfoByPosition(position);
                holder.mName.setText(item.mName);
                NAME = item.mName;
                holder.mUsage.setText(AppUtil.formatMilliSeconds(item.mUsageTime));


                //   socialusageListModel = new AppItem(NAME,item.mUsageTime);
                holder.mTime.setText(String.valueOf(item.mCount) + " Times");
                if (mTotal > 0) {
                    holder.mProgress.setProgress((int) (item.mUsageTime * 100 / mTotal));
                } else {
                    holder.mProgress.setProgress(0);
                }

                GlideApp.with(getActivity())
                        .load(AppUtil.getPackageIcon(getActivity(), item.mPackageName))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mIcon);
                holder.setOnClickListener(item);
            }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {

            private TextView mName;
            private TextView mUsage;
            private TextView mTime;
            private ImageView mIcon;
            private ProgressBar mProgress;

            MyViewHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.app_name);
                mUsage = itemView.findViewById(R.id.app_usage);
                mTime = itemView.findViewById(R.id.app_time);
                mIcon = itemView.findViewById(R.id.app_image);
                mProgress = itemView.findViewById(R.id.progressBar);
                //  itemView.setOnCreateContextMenuListener(this);
            }

            @SuppressLint("RestrictedApi")
            void setOnClickListener(final AppItem item) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }


        }
    }


    private void SocialUsageCall(JsonArray array) {

        Constant.setSession(getActivity());

        Call<InsertSocialTimeModel> stringCall = Constant.apiService.InsertSocialData(Constant.mUserId,mCurrentDate,array.toString());
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

    @SuppressLint("StaticFieldLeak")
    class MyAsyncTask extends AsyncTask<Integer, Void, List<AppItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipe.setRefreshing(true);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected List<AppItem> doInBackground(Integer... integers) {
            return DataManager.getInstance().getApps(getActivity(), integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mList.setVisibility(View.VISIBLE);
            mTotal = 0;
            for (AppItem item : appItems) {
                if (item.mUsageTime <= 0) continue;
                mTotal += item.mUsageTime;
                item.mCanOpen = mPackageManager.getLaunchIntentForPackage(item.mPackageName) != null;
            }
            mSwitchText.setText("");
            txtTotalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(mTotal)));
            //mSwipe.setRefreshing(false);
            mAdapter.updateData(appItems);
        }
    }

    private void dataFromAdapter()
    {

        for (int i =0; i<=mAdapter.getItemCount(); i++)
        {

                NAME = mAdapter.getItemInfoByPosition(i).mName;
                mUSAGE = mAdapter.getItemInfoByPosition(i).mUsageTime;
        }

        JSONArray contaArray = new JSONArray();

        try {
            singObj = new JSONObject();
            singObj.put("name", NAME);
            singObj.put("usage", mUSAGE);
            contaArray.put(singObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        Log.e(TAG, "onBindViewHolder: "+array );
        //   SocialUsageCall(array);
    }


}
