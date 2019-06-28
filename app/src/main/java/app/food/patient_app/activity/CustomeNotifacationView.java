package app.food.patient_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;


public class CustomeNotifacationView extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CustomeNotifacationView";
    LinearLayout layout, layOutRad, layOutGood, layOutMeh, layOutBad, layOutAwful;
    ImageView imageAwful,imageRad,imageGood,imageMeh,imageBad;
    TextView txtDayName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_notifacation_view_acivity);
        Constant.setSession(CustomeNotifacationView.this);
        initialization();

    }

    private void initialization() {
        layout = findViewById(R.id.layout);
        layOutRad = findViewById(R.id.layOutRad);
        layOutGood = findViewById(R.id.layOutGood);
        layOutMeh = findViewById(R.id.layOutMeh);
        layOutBad = findViewById(R.id.layOutBad);
        layOutAwful = findViewById(R.id.layOutAwful);

        imageBad = findViewById(R.id.imageBad);
        imageMeh = findViewById(R.id.imageMeh);
        imageGood = findViewById(R.id.imageGood);
        imageRad = findViewById(R.id.imageRad);
        imageAwful = findViewById(R.id.imageAwful);

        txtDayName = findViewById(R.id.txtDayName);

        layOutRad.setOnClickListener(this);
        layOutGood.setOnClickListener(this);
        layOutMeh.setOnClickListener(this);
        layOutBad.setOnClickListener(this);
        layOutAwful.setOnClickListener(this);

        layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        setCurrentDay();
    }

    private void setCurrentDay() {
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        txtDayName.setText("HOW IS YOUR "+weekday_name.toUpperCase()+"?");

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.layOutRad:
                imageRad.setBackgroundResource(R.drawable.rad_fill);
                selectedMoodAcivity("rad");
                break;
            case R.id.layOutGood:
                imageGood.setBackgroundResource(R.drawable.good_fill);
                selectedMoodAcivity("good");
                break;
            case R.id.layOutMeh:
                imageMeh.setBackgroundResource(R.drawable.meh_fill);
                selectedMoodAcivity("meh");
                break;
            case R.id.layOutBad:
                imageBad.setBackgroundResource(R.drawable.bad_fill);
                selectedMoodAcivity("bad");
                break;
            case R.id.layOutAwful:
                imageAwful.setBackgroundResource(R.drawable.awful_fill);
                selectedMoodAcivity("awful");
                break;
        }
    }

    public void selectedMoodAcivity(String moodKey) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        Log.e(TAG, "selectedMoodAcivity------>time " + currentDateTimeString);
        Intent intent = new Intent(CustomeNotifacationView.this, MoodNextActivity.class);
        intent.putExtra("moodkey", moodKey);
        intent.putExtra("date", Constant.currentDate());
        intent.putExtra("time", currentDateTimeString);
        startActivity(intent);
        finish();

    }
}