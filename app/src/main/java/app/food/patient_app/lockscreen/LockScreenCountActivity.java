package app.food.patient_app.lockscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import app.food.patient_app.R;

public class LockScreenCountActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView textCounter, textOnCounter, textOffCounter;
    SharedPrefererenceCounter counter;
    SharedPreferences sharedPreferences;
    Button button;
    private static final String TAG = "LockScreenCountActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locscreen_count);

        startService(new Intent(this, LockService.class));
        counter = SharedPrefererenceCounter.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        textCounter = (TextView) findViewById(R.id.cout_textview);
        textOffCounter = (TextView) findViewById(R.id.text_off_counter);
        textOnCounter = (TextView) findViewById(R.id.text_on_counter);
        timecounter();
        try {
            startCountDown();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void startCountDown() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String inputString = "23:50:30.500";


        Date date = sdf.parse("1970-01-01 " + inputString);
        System.out.println("in milliseconds: " + date.getTime());
        Log.e(TAG, "startCountDown: "+date.getTime() );
        final TextView days = (TextView)findViewById(R.id.days);
        final TextView hours = (TextView)findViewById(R.id.hours);
        final TextView mins = (TextView)findViewById(R.id.minutes);
        final TextView seconds = (TextView)findViewById(R.id.seconds);


        new CountDownTimer(date.getTime(), 1000){

            @Override

            public void onTick(long millisUntilFinished) {

                days.setText(TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+"");

                hours.setText((TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)))+"");

                mins.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+"");

                seconds.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+"");
            }

            @Override

            public void onFinish() {
                days.setText("Count down completed");
                hours.setText("");
                mins.setText("");
                seconds.setText("");
            }
        }.start();
    }

    private void timecounter() {

        Calendar start_calendar = Calendar.getInstance();
        Calendar end_calendar = Calendar.getInstance();

        long start_millis = start_calendar.getTimeInMillis(); //get the start time in milliseconds
        long end_millis = end_calendar.getTimeInMillis(); //get the end time in milliseconds
        long total_millis = (end_millis - start_millis); //total time in milliseconds
        end_calendar.set(2020, 10, 6); // 10 = November, month start at 0 = January

        //1000 = 1 second interval
        CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                textCounter.setText(days + ":" + hours + ":" + minutes + ":" + seconds); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @Override
            public void onFinish() {
                textCounter.setText("Finish!");
            }
        };
        cdt.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.e("TAG", "onSharedPrefereneChanged");
        if (s.equals("value")) {
            textCounter.setText(counter.getSharedPreferences());
        } else if (s.equals("value_of")) {
            textOffCounter.setText(counter.getOffCounter());
        } else if (s.equals("value_on")) {
            textOnCounter.setText(counter.getOnCounter());
        }
    }
}
