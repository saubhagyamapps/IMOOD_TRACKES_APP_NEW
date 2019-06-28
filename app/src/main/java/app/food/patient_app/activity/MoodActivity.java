package app.food.patient_app.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;

public class MoodActivity extends AppCompatActivity {

    ImageView imgRad, imgMeh, imgBad, imgAwful, imgGood;

    String mDate, mTime, mMood, mActivity, mNotes;
    TextView txt_today_date,txt_today_time;
    final Calendar myCalendar = Calendar.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    FloatingActionButton closeImage, nextImage;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.setSession(MoodActivity.this);
        setContentView(R.layout.activity_mood);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        initialize();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialize() {
        closeImage = findViewById(R.id.close_dialog);
        nextImage = findViewById(R.id.next_dialog);
        imgRad = findViewById(R.id.happy);
        imgMeh = findViewById(R.id.inlove);
        imgBad = findViewById(R.id.smiling);
        imgAwful = findViewById(R.id.smilinghappy);
        imgGood = findViewById(R.id.veryhappy);

        txt_today_date = findViewById(R.id.txt_today_date);
        txt_today_time = findViewById(R.id.txt_today_time);
        txt_today_date.setText(Constant.currentDate());

        Calendar cal = Calendar.getInstance();

        long timeInMillis = System.currentTimeMillis();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "hh:mm a");
        String dateforrow = dateFormat.format(cal1.getTime());

        txt_today_time.setText(dateforrow);
        getClick();
        getDate();
        gettime();

    }

    private void gettime() {
        txt_today_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MoodActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >=13 && selectedHour < 24) {
                                selectedHour -= 12;
                            }
                            else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        if (selectedMinute < 10) {
                            mm_precede = "0";
                        }
                        txt_today_time.setText( + selectedHour + ":" + mm_precede + minute + AM_PM);
                      //  Toast.makeText(MoodActivity.this, "" + selectedHour + ":" + mm_precede + minute + AM_PM, Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }


    private void getDate() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        txt_today_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int year = mcurrentDate.get(Calendar.YEAR);
                final int month = mcurrentDate.get(Calendar.MONTH);
                final int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(MoodActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                      String  mSelctedDay="";
                      String  mMonthdDay="";
                        if (selectedday < 10) {
                            mSelctedDay = "0";
                        }
                        if(selectedmonth<10){
                            mMonthdDay="0";
                        }

                        txt_today_date.setText(new StringBuilder().append(selectedyear).append("-").append(selectedmonth + 1).append("-").append(mSelctedDay+selectedday));
                        int month_k = selectedmonth + 1;

                    }
                }, year, month, day);
                mDatePicker.setTitle("Please select date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txt_today_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void getClick() {

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MoodNextActivity.class);
                intent.putExtra("moodkey", mMood);
                startActivity(intent);
            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoodActivity.this, NavigationActivity.class));
            }
        });
        imgRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood = "rad";
                imgRad.setBackgroundResource(R.drawable.rad_fill);
                imgGood.setBackgroundResource(R.drawable.good);
                imgMeh.setBackgroundResource(R.drawable.meh);
                imgBad.setBackgroundResource(R.drawable.bad);
                imgAwful.setBackgroundResource(R.drawable.awful);

                IntenMethod(mMood, imgRad);
            }
        });
        imgMeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood = "meh";
                imgRad.setBackgroundResource(R.drawable.rad);
                imgGood.setBackgroundResource(R.drawable.good);
                imgMeh.setBackgroundResource(R.drawable.meh_fill);
                imgBad.setBackgroundResource(R.drawable.bad);
                imgAwful.setBackgroundResource(R.drawable.awful);
                IntenMethod(mMood, imgMeh);
            }
        });
        imgBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood = "bad";
                imgRad.setBackgroundResource(R.drawable.rad);
                imgGood.setBackgroundResource(R.drawable.good);
                imgMeh.setBackgroundResource(R.drawable.meh);
                imgBad.setBackgroundResource(R.drawable.bad_fill);
                imgAwful.setBackgroundResource(R.drawable.awful);
                IntenMethod(mMood, imgBad);
            }
        });


        imgAwful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood = "awful";
                imgRad.setBackgroundResource(R.drawable.rad);
                imgGood.setBackgroundResource(R.drawable.good);
                imgMeh.setBackgroundResource(R.drawable.meh);
                imgBad.setBackgroundResource(R.drawable.bad);
                imgAwful.setBackgroundResource(R.drawable.awful_fill);
                IntenMethod(mMood, imgAwful);
            }
        });

        imgGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood = "good";
                imgRad.setBackgroundResource(R.drawable.rad);
                imgGood.setBackgroundResource(R.drawable.good_fill);
                imgMeh.setBackgroundResource(R.drawable.meh);
                imgBad.setBackgroundResource(R.drawable.bad);
                imgAwful.setBackgroundResource(R.drawable.awful);
                IntenMethod(mMood, imgGood);
            }
        });
    }

    public void IntenMethod(String moodKey, ImageView image) {
        image.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
        Toast.makeText(MoodActivity.this, "Mood is " + mMood, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MoodActivity.this, MoodNextActivity.class);
        intent.putExtra("moodkey", moodKey);
        intent.putExtra("date", txt_today_date.getText().toString());
        intent.putExtra("time", txt_today_time.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
    }

}