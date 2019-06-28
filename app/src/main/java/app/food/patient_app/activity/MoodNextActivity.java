package app.food.patient_app.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import app.food.patient_app.R;
import app.food.patient_app.model.MoodInsertModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoodNextActivity extends AppCompatActivity {
    private static final String TAG = "MoodNextActivity";
    ImageView image_Work, image_Meeting, image_Friends, image_Exercise, image_Music;
    FloatingActionButton fab, closeImage;
    ImageView titalImages, imageRelax, imageWork, imagesFamily, imagesFriends, imagesDate, imagesSport,
            imagesParty, imagesMovies, imagesReading, imagesGaming, imagesShoping, imagesGoodMeal, imagesCleaning;
    TextView txt_Date, txt_Time;
    String localTime;
    Date currentTime;
    DateFormat dateFormat, timeFormat;
    DatePickerDialog datePickerDialog;
    Calendar dateSelected;
    Date startDate;
    int todaydate, thisMonth;
    Calendar mCalendar;
    EditText edt_Notes;
    String mDate, mMood, mTime, mActivity, mNotes;

    String getCurrentDate;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String fdate;
    Bundle bundle = new Bundle();
    LinearLayout imgesRight,imgesBack;
    final Calendar myCalendar = Calendar.getInstance();
    LinearLayout layoutWork, layoutRelax, layoutFamily, layoutFriends, layoutDate, layoutSport,
            layoutParty, layoutMovies, layoutReading, layoutGaming, layoutShoping, layoutGoodMeal, layoutCleaning;
    Boolean mRelaxFlag = true, mWorkFlag = true, mFamilyFlag = true, mFriendsFlag = true, mDateFlag = true, mSportFlag = true,
            mPartyFlag = true, mMoviesFlag = true, mReadingFlag = true, mGamingFlag = true, mShopingFlag = true, mGoodMealFlag = true, mCleaning = true;
    String mAcicvity = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_next);
        Constant.setSession(MoodNextActivity.this);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        mMood = getIntent().getExtras().getString("moodkey");
        mTime = getIntent().getExtras().getString("time");
        mDate = getIntent().getExtras().getString("date");
        setImages();
        initialize();
        // setDate_Time();
        // getDate_Time();
        // getClick();
    }

    private void setImages() {
        titalImages = findViewById(R.id.titalImages);
        if (mMood.equals("rad")) {
            titalImages.setBackgroundResource(R.drawable.rad_fill);
        } else if (mMood.equals("good")) {
            titalImages.setBackgroundResource(R.drawable.good_fill);
        } else if (mMood.equals("meh")) {
            titalImages.setBackgroundResource(R.drawable.meh_fill);
        } else if (mMood.equals("bad")) {
            titalImages.setBackgroundResource(R.drawable.bad_fill);
        } else {
            titalImages.setBackgroundResource(R.drawable.awful_fill);
        }
    }

    private void initialize() {

        imageWork = findViewById(R.id.imageWork);
        imageRelax = findViewById(R.id.imageRelax);
        imagesFamily = findViewById(R.id.imagesFamily);
        imagesFriends = findViewById(R.id.imagesFriends);
        imagesDate = findViewById(R.id.imagesDate);
        imagesSport = findViewById(R.id.imagesSport);
        imagesParty = findViewById(R.id.imagesParty);
        imagesMovies = findViewById(R.id.imagesMovies);
        imagesReading = findViewById(R.id.imagesReading);
        imagesGaming = findViewById(R.id.imagesGaming);
        imagesShoping = findViewById(R.id.imagesShoping);
        imagesGoodMeal = findViewById(R.id.imagesGoodMeal);
        imagesCleaning = findViewById(R.id.imagesCleaning);

        layoutWork = findViewById(R.id.layoutWork);
        layoutRelax = findViewById(R.id.layoutRelax);
        layoutFamily = findViewById(R.id.layoutFamily);
        layoutFriends = findViewById(R.id.layoutFriends);
        layoutDate = findViewById(R.id.layoutDate);
        layoutSport = findViewById(R.id.layoutSport);
        layoutParty = findViewById(R.id.layoutParty);
        layoutMovies = findViewById(R.id.layoutMovies);
        layoutReading = findViewById(R.id.layoutReading);
        layoutGaming = findViewById(R.id.layoutGaming);
        layoutShoping = findViewById(R.id.layoutShoping);
        layoutGoodMeal = findViewById(R.id.layoutGoodMeal);
        layoutCleaning = findViewById(R.id.layoutCleaning);
        edt_Notes = findViewById(R.id.edt_notes);
        imgesRight = findViewById(R.id.imgesRight);
        imgesBack = findViewById(R.id.imgesBack);

        fab = findViewById(R.id.fab_button);
        selectClick();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.progressDialog(MoodNextActivity.this);
                selecteAcivity();

            }
        });
        imgesBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgesRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecteAcivity();
            }
        });
    }

    private void selectClick() {
        imageRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRelaxFlag) {

                    mRelaxFlag = false;
                    layoutRelax.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imageRelax.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_relaxiconw));
                } else {
                    mRelaxFlag = true;
                    layoutRelax.setBackgroundResource(R.drawable.layout_round);
                    imageRelax.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_relaxicon));
                }
            }
        });
        imageWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWorkFlag) {
                    mWorkFlag = false;
                    layoutWork.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imageWork.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_workiconw));
                } else {
                    mWorkFlag = true;
                    layoutWork.setBackgroundResource(R.drawable.layout_round);
                    imageWork.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_workicon));
                }
            }
        });
        imagesFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFamilyFlag) {
                    mFamilyFlag = false;
                    layoutFamily.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesFamily.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_familyiconw));
                } else {
                    mFamilyFlag = true;
                    layoutFamily.setBackgroundResource(R.drawable.layout_round);
                    imagesFamily.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_familyicon));
                }
            }
        });
        imagesFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFriendsFlag) {
                    mFriendsFlag = false;
                    layoutFriends.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesFriends.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_friendsiconw));
                } else {
                    mFriendsFlag = true;
                    layoutFriends.setBackgroundResource(R.drawable.layout_round);
                    imagesFriends.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_friendsicon));
                }
            }
        });
        imagesDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDateFlag) {
                    mDateFlag = false;
                    layoutDate.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesDate.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dateliconw));
                } else {
                    mDateFlag = true;
                    layoutDate.setBackgroundResource(R.drawable.layout_round);
                    imagesDate.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dateicon));
                }
            }
        });
        imagesSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSportFlag) {
                    mSportFlag = false;
                    layoutSport.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesSport.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_sporticonw));
                } else {
                    mSportFlag = true;
                    layoutSport.setBackgroundResource(R.drawable.layout_round);
                    imagesSport.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_sporticon));
                }
            }
        });
        imagesShoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShopingFlag) {
                    mShopingFlag = false;
                    layoutShoping.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesShoping.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopingiconw));
                } else {
                    mShopingFlag = true;
                    layoutShoping.setBackgroundResource(R.drawable.layout_round);
                    imagesShoping.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopingicon));
                }
            }
        });
        imagesParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPartyFlag) {
                    mPartyFlag = false;
                    layoutParty.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesParty.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_partyiconw));
                } else {
                    mPartyFlag = true;
                    layoutParty.setBackgroundResource(R.drawable.layout_round);
                    imagesParty.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_partyticon));
                }
            }
        });
        imagesMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMoviesFlag) {
                    mMoviesFlag = false;
                    layoutMovies.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesMovies.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_moviesiconw));
                } else {
                    mMoviesFlag = true;
                    layoutMovies.setBackgroundResource(R.drawable.layout_round);
                    imagesMovies.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_moviesicon));
                }
            }
        });
        imagesReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mReadingFlag) {
                    mReadingFlag = false;
                    layoutReading.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesReading.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_readingiconw));
                } else {
                    mReadingFlag = true;
                    layoutReading.setBackgroundResource(R.drawable.layout_round);
                    imagesReading.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_readingicon));
                }
            }
        });
        imagesGaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGamingFlag) {
                    mGamingFlag = false;
                    layoutGaming.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesGaming.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_gamingiconw));
                } else {
                    mGamingFlag = true;
                    layoutGaming.setBackgroundResource(R.drawable.layout_round);
                    imagesGaming.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_gamingicon));
                }
            }
        });
        imagesShoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShopingFlag) {
                    mShopingFlag = false;
                    layoutShoping.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesShoping.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopingiconw));
                } else {
                    mShopingFlag = true;
                    layoutShoping.setBackgroundResource(R.drawable.layout_round);
                    imagesShoping.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopingicon));
                }
            }
        });
        imagesGoodMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGoodMealFlag) {
                    mGoodMealFlag = false;
                    layoutGoodMeal.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesGoodMeal.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_goodmealiconw));
                } else {
                    mGoodMealFlag = true;
                    layoutGoodMeal.setBackgroundResource(R.drawable.layout_round);
                    imagesGoodMeal.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_goodmealicon));
                }
            }
        });
        imagesCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCleaning) {
                    mCleaning = false;
                    layoutCleaning.setBackgroundResource(R.drawable.layout_boader_with_color);
                    imagesCleaning.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_cleaningliconw));
                } else {
                    mCleaning = true;
                    layoutCleaning.setBackgroundResource(R.drawable.layout_round);
                    imagesCleaning.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_cleaningicon));
                }
            }
        });
    }


    public void selecteAcivity() {
        if (!mWorkFlag) {
            mAcicvity = mAcicvity + "work,";
        }
        if (!mRelaxFlag) {
            mAcicvity = mAcicvity + "relax,";
        }
        if (!mFamilyFlag) {
            mAcicvity = mAcicvity + "family,";
        }
        if (!mFriendsFlag) {
            mAcicvity = mAcicvity + "friends,";
        }
        if (!mDateFlag) {
            mAcicvity = mAcicvity + "date,";
        }
        if (!mSportFlag) {
            mAcicvity = mAcicvity + "sport,";
        }
        if (!mPartyFlag) {
            mAcicvity = mAcicvity + "party,";
        }
        if (!mMoviesFlag) {
            mAcicvity = mAcicvity + "movies,";
        }
        if (!mReadingFlag) {
            mAcicvity = mAcicvity + "reading,";
        }
        if (!mGamingFlag) {
            mAcicvity = mAcicvity + "gaming,";
        }
        if (!mShopingFlag) {
            mAcicvity = mAcicvity + "shoping,";
        }
        if (!mGoodMealFlag) {
            mAcicvity = mAcicvity + "goodmeal,";
        }
        if (!mCleaning) {
            mAcicvity = mAcicvity + "cleaning,";
        }
        mAcicvity = mAcicvity.substring(0, mAcicvity.length() - 1);
        mAcicvity = mAcicvity.replace("null", "");
        Log.e(TAG, "selecteAcivity: " + mAcicvity);
        InsertMoodData();
        Log.e(TAG, "Date---->"+mDate );
        Log.e(TAG, "Time---->" +mTime);
        Log.e(TAG, "Mood---->" +mMood);
    }

    private void InsertMoodData() {
        mNotes=edt_Notes.getText().toString();
        int mMinute = new Time(System.currentTimeMillis()).getMinutes();
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        String mCurrentTime = String.valueOf(hour) + ":" + String.valueOf(mMinute) + ":" + String.valueOf(second);

        Call<MoodInsertModel> moodInsertModelCall =
                Constant.apiService.SetMoodDetails(Constant.mUserId, mDate, mTime, mMood, mAcicvity, mNotes);

        moodInsertModelCall.enqueue(new Callback<MoodInsertModel>() {
            @Override
            public void onResponse(Call<MoodInsertModel> call, Response<MoodInsertModel> response) {
                Constant.progressBar.dismiss();
                String status = response.body().getStatus();
                if (response.body().getStatus().equals("0")) {
                    Toast.makeText(MoodNextActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MoodNextActivity.this, NavigationActivity.class));

                } else {
                    Toast.makeText(MoodNextActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoodInsertModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }

    private void setDate_Time() {

        String getCurrentDate = DateFormat.getDateInstance().format(new Date());
        currentTime = mCalendar.getTime();
        timeFormat = new SimpleDateFormat("HH:mm:a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        localTime = timeFormat.format(currentTime);
        txt_Date.setText(getCurrentDate);
        txt_Time.setText(localTime);
    }

    private void getDate_Time() {
        datePickerDialog = new DatePickerDialog(MoodNextActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String getCurrentDate = DateFormat.getDateInstance().format(new Date());
                mCalendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                TimeZone tz = dateSelected.getTimeZone();
                startDate = mCalendar.getTime();
                todaydate = mCalendar.get(Calendar.DAY_OF_MONTH);
                fdate = sd.format(startDate);
                // txt_Date.setText(fdate);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
