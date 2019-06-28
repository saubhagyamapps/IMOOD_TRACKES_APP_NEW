package app.food.patient_app.fragment;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import app.food.patient_app.R;
import app.food.patient_app.activity.MoodActivity;
import app.food.patient_app.model.DashBoardAverageModel;
import app.food.patient_app.model.InsertStepCountModel;
import app.food.patient_app.model.PercentageModel;
import app.food.patient_app.model.StepCountPerModel;
import app.food.patient_app.ui.MainActivity;
import app.food.patient_app.util.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static java.text.DateFormat.getTimeInstance;

public class DashBoardFragment extends Fragment implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "DashBoardFragment";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    View mView;
    TextView fitTime, workTime, homeTime, phoneUse, socialTime, callTime;
    CircularProgressBar progressBarCall, progressBarSocial, progressFitWorking, progressBarWorking, progressHomeCount, progressBarMood, progressPhoneCall;
    TextView txtCurrentDate, txtHomeCount;
    Handler handler;
    TextView txtWalkingTime, txtSocialTime, txtWorkingTime, txtCallTime, txtWorkingMood;
    String mTotalStep;
    Runnable runnable;
    String result;
    FloatingActionButton floatingMood;
    CircleImageView imageCallIcon, imageSocialIcon, imageWorkIcon, imagesHomeIcon, imageFitnessIcon, imagePhoneIcon;
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    String mMoveMinutes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.new_fragment_dash_board, container, false);
        getActivity().setTitle("");
        Constant.setSession(getActivity());
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Initialize();
        progressBarClick();
        return mView;

    }

    private void progressBarClick() {
        imagesHomeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GetCurrentLocationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        imageFitnessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new StepGraphViewPagerFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        imagePhoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.navigation.setSelectedItemId(R.id.nav_social_interaction);
                /*Fragment fragment = new SocialIntegrationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        imageCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.navigation.setSelectedItemId(R.id.nav_social_interaction);
             /*   Fragment fragment = new SocialIntegrationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        imageSocialIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.navigation.setSelectedItemId(R.id.nav_social_interaction);
          /*      Fragment fragment = new SocialIntegrationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        imageWorkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WorkLocationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        progressBarMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LineChartFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mApiClient.connect();
    }

    private void UsesAPICALL() {
        Call<PercentageModel> modelCall = Constant.apiService.getPercentage(Constant.mUserId, Constant.currentDate());
        modelCall.enqueue(new Callback<PercentageModel>() {
            @Override
            public void onResponse(Call<PercentageModel> call, Response<PercentageModel> response) {
                //SetProgressBars(response.body().getSocialmedia(), response.body().getWork(), response.body().getCallduration());

                socialTime.setText("" + Constant.secondTominORHR(response.body().getSocialmediatime() * 60));
                workTime.setText("" + Constant.secondTominORHR(response.body().getWorktime() * 60));
                callTime.setText("" + Constant.secondTominORHR(response.body().getCalldurationtime()));
                homeTime.setText("" + Constant.secondTominORHR(response.body().getHometime() * 60));
                txtWorkingMood.setText(response.body().getMood().toUpperCase());
                if (response.body().getMood().equals("rad")) {
                    progressBarMood.setBackgroundResource(R.drawable.rad_fill);
                } else if (response.body().getMood().equals("good")) {
                    progressBarMood.setBackgroundResource(R.drawable.good_fill);
                } else if (response.body().getMood().equals("meh")) {
                    progressBarMood.setBackgroundResource(R.drawable.meh_fill);
                } else if (response.body().getMood().equals("bad")) {
                    progressBarMood.setBackgroundResource(R.drawable.bad_fill);
                } else if (response.body().getMood().equals("awful")) {
                    progressBarMood.setBackgroundResource(R.drawable.awful_fill);
                }

                Log.e(TAG, "onResponse:+UsesAPICALL ");
            }

            @Override
            public void onFailure(Call<PercentageModel> call, Throwable t) {
                Log.e(TAG, "onFailure:+UsesAPICALL " + t.getMessage());
            }
        });
    }

    public void countStepCall() {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getActivity()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(getActivity()),
                    fitnessOptions);
        } else {


            handler = new Handler();

            runnable = new Runnable() {
                public void run() {
                    subscribe();
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }


    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        try {
            Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                    .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i(TAG, "Successfully subscribed!");

                                        readData();
                                    } else {
                                        Log.w(TAG, "There was a problem subscribing.", task.getException());
                                    }
                                }
                            });
        } catch (Exception e) {

        }

    }

    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    private void readData() {
        try {
            Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {

                                    Log.e(TAG, "onSuccess:  " + FitnessActivities.class.getFields().toString());


                                    DateFormat dateFormat = getTimeInstance();

                                    for (DataPoint dp : dataSet.getDataPoints()) {

                                        Log.i(TAG, "\tType: " + dp.getDataType().getName());
                                        Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                        Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                        for (Field field : dp.getDataType().getFields()) {
                                            Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                                        }
                                    }

                                    final long total =
                                            dataSet.isEmpty()
                                                    ? 0
                                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                    Log.i(TAG, "Total steps: " + total);

                                    mTotalStep = String.valueOf(total);


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        } catch (Exception e) {

        }
        try {
            Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                    .readDailyTotal(DataType.TYPE_MOVE_MINUTES)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {

                                    Log.e(TAG, "onSuccess:  " + FitnessActivities.class.getFields().toString());


                                    DateFormat dateFormat = getTimeInstance();

                                    for (DataPoint dp : dataSet.getDataPoints()) {

                                        Log.i(TAG, "\tType: " + dp.getDataType().getName());
                                        Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                        Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                        for (Field field : dp.getDataType().getFields()) {
                                            Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                                             mMoveMinutes= String.valueOf(dp.getValue(field));
                                        }

                                    }




                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            handler.removeCallbacks(runnable);
        } catch (Exception e) {

        }

    }

    private void Initialize() {

        progressBarCall = mView.findViewById(R.id.progressBarCall);
        progressPhoneCall = mView.findViewById(R.id.progressPhoneCall);
        progressBarSocial = mView.findViewById(R.id.progressBarSocial);
        progressFitWorking = mView.findViewById(R.id.progressFitWorking);
        progressBarWorking = mView.findViewById(R.id.progressBarWorking);
        progressHomeCount = mView.findViewById(R.id.progressHomeCount);
        progressBarMood = mView.findViewById(R.id.progressBarMood);
        txtCurrentDate = mView.findViewById(R.id.txtCurrentDate);
        floatingMood = mView.findViewById(R.id.floatingMood);
        txtHomeCount = mView.findViewById(R.id.txtHomeCount);
        imageCallIcon = mView.findViewById(R.id.imageCallIcon);
        imageSocialIcon = mView.findViewById(R.id.imageSocialIcon);
        imageWorkIcon = mView.findViewById(R.id.imageWorkIcon);
        imagesHomeIcon = mView.findViewById(R.id.imagesHomeIcon);
        imageFitnessIcon = mView.findViewById(R.id.imageFitnessIcon);
        imagePhoneIcon = mView.findViewById(R.id.imagePhoneIcon);
        fitTime = mView.findViewById(R.id.fitTime);
        workTime = mView.findViewById(R.id.workTime);
        homeTime = mView.findViewById(R.id.homeTime);
        phoneUse = mView.findViewById(R.id.phoneUse);
        socialTime = mView.findViewById(R.id.socialTime);
        callTime = mView.findViewById(R.id.callTime);
        txtCurrentDate.setText("Today");
        // txtWalkingTime = mView.findViewById(R.id.txtWalkingTime);
        txtSocialTime = mView.findViewById(R.id.txtSocialTime);
        txtWorkingTime = mView.findViewById(R.id.txtWorkingTime);
        txtCallTime = mView.findViewById(R.id.txtCallTime);
        txtWorkingMood = mView.findViewById(R.id.txtWorkingMood);
        UsesAPICALL();
        getDashBoardData();
        countStepCall();
        // StepCountAPICALL();
        openMoodFragment();

    }

    private void openMoodFragment() {
        floatingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoodActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDashBoardData() {
        Call<DashBoardAverageModel> modelCall = Constant.apiService.getDashBoardData(Constant.mUserId, Constant.currentDate());
        modelCall.enqueue(new Callback<DashBoardAverageModel>() {
            @Override
            public void onResponse(Call<DashBoardAverageModel> call, Response<DashBoardAverageModel> response) {
                txtSocialTime.setText("(" + response.body().getSocial_media_usage() + "%)");
                txtWorkingTime.setText("(" + response.body().getWork() + "%)");
                txtCallTime.setText("(" + response.body().getCallduration() + "%)");
                txtHomeCount.setText("(" + response.body().getHome() + "%)");

                progressBarCall.setProgress(response.body().getCallduration());
                progressBarSocial.setProgress(response.body().getSocial_media_usage());
                progressPhoneCall.setProgress(45);
                progressHomeCount.setProgress(response.body().getHome());
                progressBarWorking.setProgress(response.body().getWork());
                progressFitWorking.setProgress(60);


            }

            @Override
            public void onFailure(Call<DashBoardAverageModel> call, Throwable t) {

            }
        });
    }


    private void StepCountAPICALL() {
        Call<StepCountPerModel> count = Constant.apiService.getStepPer(Constant.mUserId, Constant.currentDate());
        count.enqueue(new Callback<StepCountPerModel>() {
            @Override
            public void onResponse(Call<StepCountPerModel> call, Response<StepCountPerModel> response) {
                if (response.body().getStatus() == 0) {
                    progressBarWorking.setProgress(response.body().getPercentage());
                }

            }

            @Override
            public void onFailure(Call<StepCountPerModel> call, Throwable t) {

            }
        });
    }

    public void SetProgressBars(long Social, long worktime, long callTime) {
        progressBarCall.setProgress(callTime);
        progressBarWorking.setProgress(worktime);
        progressBarSocial.setProgress(Social);
        Log.e(TAG, "SetProgressBars: ");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        DataSourcesRequest dataSourceRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if (DataType.TYPE_STEP_COUNT_CUMULATIVE.equals(dataSource.getDataType())) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest)
                .setResultCallback(dataSourcesResultCallback);
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {

        Log.e(TAG, "registerFitnessDataListener: " + dataType);
        SensorRequest request = new SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e("GoogleFit", "SensorApi successfully added");
                        }
                    }
                });

    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onDataPoint(DataPoint dataPoint) {
        Log.e(TAG, "onDataPoint:--->>>--->> " + dataPoint);
       /* for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
              //      Toast.makeText(getActivity(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();
                    //txtStepCount.setText(value + " Steps");

                }
            });
        }*/
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                insertStepApiCall(mTotalStep);
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(r, 4000);
    }

    private void insertStepApiCall(String value) {
        Call<InsertStepCountModel> modelCall = Constant.apiService.insertStep(Constant.mUserId, Constant.currentDate(), value,mMoveMinutes);
        modelCall.enqueue(new Callback<InsertStepCountModel>() {
            @Override
            public void onResponse(Call<InsertStepCountModel> call, Response<InsertStepCountModel> response) {

            }

            @Override
            public void onFailure(Call<InsertStepCountModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        Fitness.SensorsApi.remove(mApiClient, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("GoogleFit", "RESULT_CANCELED");
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    }


}
