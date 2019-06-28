package app.food.patient_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import app.food.patient_app.R;

public class Login_Regi_Activity extends AppCompatActivity {
    Button btn_SignUp, btn_Signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__regi_);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        initialization();
        cliked();
    }

    private void initialization() {
        btn_SignUp = findViewById(R.id.btn_signup);
        btn_Signin = findViewById(R.id.btn_signin);

    }

    private void cliked() {
        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Signin.setBackground(getResources().getDrawable(R.drawable.signup_button));
                btn_SignUp.setBackgroundColor(getResources().getColor(R.color.white));
                btn_Signin.setTextColor(getResources().getColor(R.color.white));
                btn_SignUp.setTextColor(getResources().getColor(R.color.colorPrimary));
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_SignUp.setBackground(getResources().getDrawable(R.drawable.signup_button));
                btn_Signin.setBackgroundColor(getResources().getColor(R.color.white));
                btn_SignUp.setTextColor(getResources().getColor(R.color.white));
                btn_Signin.setTextColor(getResources().getColor(R.color.colorPrimary));
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
            }
        });
    }
}