package app.food.patient_app.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import app.food.patient_app.R;
import app.food.patient_app.model.RegisterModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_Reg_Name, edt_Reg_Email, edt_Reg_Mobile, edt_Reg_Pwd;
    Button btn_Register;
    String mUserName, mEmail, mMobile, mPwd, mFirebaseId, mDeviceId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = "RegisterActivity";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        initialize();
        getClick();
    }

    private void getClick() {
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
    }

    private void initialize() {
        sessionManager = new SessionManager(RegisterActivity.this);
        mDeviceId = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mFirebaseId = FirebaseInstanceId.getInstance().getToken();
        edt_Reg_Name = findViewById(R.id.edt_reg_username);
        edt_Reg_Email = findViewById(R.id.edt_reg_email);
        edt_Reg_Mobile = findViewById(R.id.edt_reg_mobile);
        edt_Reg_Pwd = findViewById(R.id.edt_reg_pwd);
        btn_Register = findViewById(R.id.btn_register);
    }

    private void getValue() {
        mUserName = edt_Reg_Name.getText().toString().trim();
        mEmail = edt_Reg_Email.getText().toString().trim();
        mMobile = edt_Reg_Mobile.getText().toString().trim();
        mPwd = edt_Reg_Pwd.getText().toString().trim();
        validation();
    }

    private void validation() {
        if (mUserName.equalsIgnoreCase("")) {
            edt_Reg_Name.setError("Required");
            edt_Reg_Name.setFocusable(true);
        } else if (mEmail.equalsIgnoreCase("")) {
            edt_Reg_Email.setError("Required");
            edt_Reg_Email.setFocusable(true);
        } else if (mMobile.equalsIgnoreCase("")) {
            edt_Reg_Mobile.setError("Required");
            edt_Reg_Mobile.setFocusable(true);
        } else if (mPwd.equalsIgnoreCase("")) {
            edt_Reg_Pwd.setError("Required");
            edt_Reg_Pwd.setFocusable(true);
        } else if (mEmail.matches(emailPattern)) {
            getRegister();
        } else {
            edt_Reg_Email.setError("Invalid email address");
            edt_Reg_Email.setFocusable(true);
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

        }
    }

    private void getRegister() {
        Constant.progressDialog(RegisterActivity.this);
        Call<RegisterModel> registerActivityCall = Constant.apiService.getRegisterUser(mUserName, mEmail, mMobile, mPwd, mFirebaseId, mDeviceId);

        registerActivityCall.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {

                if (response.body().getStatus().equals("0")) {

                   sessionManager.CreateLoginSession(response.body().getId(), mUserName, mEmail, mMobile, mPwd, mFirebaseId, mDeviceId,"");
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                Constant.progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: " + "Failed");
            }
        });
    }

    @Override
    public void onBackPressed() {
        openMainScreen();
        super.onBackPressed();
    }

    private void openMainScreen() {
        Intent intent = new Intent(RegisterActivity.this, Login_Regi_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
    }
}