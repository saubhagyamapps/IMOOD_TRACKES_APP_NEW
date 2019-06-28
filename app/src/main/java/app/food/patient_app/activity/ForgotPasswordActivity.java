package app.food.patient_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.food.patient_app.R;
import app.food.patient_app.model.ForgotPasswordModel;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    EditText edt_ForgotPwd_Email;
    Button btn_Send_Reset_Link;
    String mForgotEmail;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialize();
        getClick();
    }

    private void initialize() {
        edt_ForgotPwd_Email = findViewById(R.id.edt_forgotpwd_email);
        btn_Send_Reset_Link = findViewById(R.id.btn_send_email);
    }

    private void getClick() {
        btn_Send_Reset_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
    }

    private void getValue() {
        mForgotEmail = edt_ForgotPwd_Email.getText().toString().trim();
        getValidate();
    }

    private void getValidate() {
        if (mForgotEmail.equalsIgnoreCase("")) {
            edt_ForgotPwd_Email.setError("Required");
            edt_ForgotPwd_Email.setFocusable(true);
        } else if (mForgotEmail.matches(emailPattern)) {
            SendResetLink();
        } else {
            edt_ForgotPwd_Email.setError("Invalid email address");
            edt_ForgotPwd_Email.setFocusable(true);
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

        }
    }

    private void SendResetLink() {
        Constant.progressDialog(ForgotPasswordActivity.this);
        Call<ForgotPasswordModel> forgotPasswordModelCall = Constant.apiService.getResetLink(mForgotEmail);

        forgotPasswordModelCall.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {

                Constant.progressBar.dismiss();
                if (response.body().getStatus().equals("0")) {
                    Toast.makeText(ForgotPasswordActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: " + "API Call Failed");
            }
        });
    }

    @Override
    public void onBackPressed() {
        openMainScreen();
        super.onBackPressed();
    }

    private void openMainScreen() {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
    }
}
