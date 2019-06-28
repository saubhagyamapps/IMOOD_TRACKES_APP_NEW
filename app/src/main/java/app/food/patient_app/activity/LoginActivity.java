package app.food.patient_app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.LoginModel;
import app.food.patient_app.model.LoginWithFbModel;
import app.food.patient_app.model.LoginWithGmailModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_LoginEmail, edt_LoginPwd;
    Button btn_Login;
    TextView txt_Forgot, txt_SignUp;
    private static final String TAG = "LoginActivity";
    String mLoginEmail, mLoginPwd, mFirebaseId, mDeviceId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SessionManager sessionManager;
    SignInButton SignIn_Google;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    String personName, email;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    String fbName, fbEmail, fbId, fbBirthday, fbLoaction, fbImageurl;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   FacebookSdk.sdkInitialize(getApplicationContext());
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        setContentView(R.layout.activity_login);
        initialize();
        getClick();
    }

    @Override
    public void onBackPressed() {
        openMainScreen();
        super.onBackPressed();
    }

    private void openMainScreen() {
        Intent intent = new Intent(LoginActivity.this, Login_Regi_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
    }

    private void initialize() {
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        LoginManager.getInstance().logOut();
        getFilePaths();
        sessionManager = new SessionManager(LoginActivity.this);
        edt_LoginEmail = findViewById(R.id.edt_login_email);
        edt_LoginPwd = findViewById(R.id.edt_login_pwd);
        btn_Login = findViewById(R.id.btn_login);
        mDeviceId = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mFirebaseId = FirebaseInstanceId.getInstance().getToken();
        txt_Forgot = findViewById(R.id.txt_forgot);
        txt_SignUp = findViewById(R.id.txt_SignUp);
        loginButton = findViewById(R.id.login_fb);

        SignIn_Google = findViewById(R.id.signup_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginActivity.this.getResources().getString(R.string.server_client_id))
                .requestEmail().build();*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email", "user_gender"));
                loginwithFb();
            }
        });
    }

    private void loginwithFb() {
        Constant.progressDialog(LoginActivity.this);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                {
                                    try {

                                        Log.e("object", object.toString());
                                        fbName = object.getString("name");
                                        fbEmail = object.getString("email");
                                        fbId = object.getString("id");

                                        if (object.has("birthday")) {
                                            String birthday = object.getString("birthday");
                                        }
                                        if (object.has("gender")) {
                                            String gender = object.getString("gender");
                                        }

                                        fbImageurl = "https://graph.facebook.com/" + fbId + "/picture?type=large";
                                        Log.e("Picture", fbImageurl);
                                        Log.e(TAG, "Name: " + fbName + ", email: " + fbEmail
                                                + ", DOB ");

                                        LOGINWITHFBAPICALL();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email,gender,birthday,link");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

                Log.d(TAG, "Login attempt cancelled.");
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        accessToken.setCurrentAccessToken(null);
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));

                        loginwithFb();
                    }
                }); //.executeAsync();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");

                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email,user_birthday"));
            }
        });
    }

    public void LOGINWITHFBAPICALL() {
        Call<LoginWithFbModel> login = Constant.apiService.getLoginWithFb(fbId, fbName, fbEmail, fbImageurl, mDeviceId, mFirebaseId);
        login.enqueue(new Callback<LoginWithFbModel>() {
            @Override
            public void onResponse(Call<LoginWithFbModel> call, Response<LoginWithFbModel> response) {
                Log.e(TAG, "onResponse: " + response.body().getResult());

                List<LoginWithFbModel.ResultBean> resultBean = response.body().getResult();
                sessionManager.CreateLoginSession(resultBean.get(0).getId(), fbName, fbEmail, "", "", mFirebaseId, mFirebaseId,fbImageurl);

                SharedPreferences prefs = null;
                prefs = getSharedPreferences("app.food.patient_app", MODE_PRIVATE);
                Constant.progressBar.dismiss();

                if (prefs.getBoolean("firstrun", true)) {
                    Intent intent = new Intent(LoginActivity.this, HomeAddressActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
                    prefs.edit().putBoolean("firstrun", false).apply();
                } else {
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    finish();
                    overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
                }

            }

            @Override
            public void onFailure(Call<LoginWithFbModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }


    private void getClick() {
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
        txt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        txt_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
            }
        });
        SignIn_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
            }
        });
    }

    private void getValue() {
        mLoginEmail = edt_LoginEmail.getText().toString().trim();
        mLoginPwd = edt_LoginPwd.getText().toString().trim();
        getValidate();
    }

    private void getValidate() {
        if (mLoginEmail.equalsIgnoreCase("")) {
            edt_LoginEmail.setError("Required");
            edt_LoginEmail.setFocusable(true);
        } else if (mLoginPwd.equalsIgnoreCase("")) {
            edt_LoginPwd.setError("Required");
            edt_LoginPwd.setFocusable(true);
        } else if (mLoginEmail.matches(emailPattern)) {
            getLoginUser();
        } else {
            edt_LoginEmail.setError("Invalid email address");
            edt_LoginEmail.setFocusable(true);
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

        }
    }

    private void getLoginUser() {
        Constant.setSession(LoginActivity.this);
        Constant.progressDialog(LoginActivity.this);
        Call<LoginModel> loginModelCall = Constant.apiService.getUserLogin(mLoginEmail, mLoginPwd, mFirebaseId, mDeviceId);

        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                Constant.progressBar.dismiss();

                if (response.body().getStatus().equals("0")) {
                    LoginModel.ResultBean resultBean = response.body().getResult().get(0);
                    sessionManager.CreateLoginSession(resultBean.getId(), resultBean.getUsername(), mLoginEmail, resultBean.getMobile(), mLoginPwd, resultBean.getFirebase_id(), resultBean.getDevice_id(),"");
                    Toast.makeText(LoginActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    finish();
                    overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: " + "API call Failed");
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e(TAG, "Status: " + status);
                    }
                });
        signIn();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:---->" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());


            try {
                personName = acct.getDisplayName();
                email = acct.getEmail();
                Log.e(TAG, "Name: " + personName + ", email: " + email);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginWithGmailAPICALL(personName, email);

        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoginWithGmailAPICALL(final String personName, final String email) {
        Constant.setSession(LoginActivity.this);
        Constant.progressDialog(LoginActivity.this);
        Call<LoginWithGmailModel> login = Constant.apiService.loginWithGmail(email, personName, mFirebaseId, mDeviceId);
        login.enqueue(new Callback<LoginWithGmailModel>() {
            @Override
            public void onResponse(Call<LoginWithGmailModel> call, Response<LoginWithGmailModel> response) {
                LoginWithGmailModel.ResultBean resultBean = response.body().getResult().get(0);
                Constant.progressBar.dismiss();
                SharedPreferences prefs = null;
                prefs = getSharedPreferences("app.food.patient_app", MODE_PRIVATE);
                sessionManager.CreateLoginSession(resultBean.getId(), personName, email, "", "", mFirebaseId, mFirebaseId,"");
                if (prefs.getBoolean("firstrun", true)) {
                    Intent intent = new Intent(LoginActivity.this, HomeAddressActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
                    prefs.edit().putBoolean("firstrun", false).apply();
                }else {
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);
                }

            }

            @Override
            public void onFailure(Call<LoginWithGmailModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "login with gamil "+t.getMessage() );
                Toast.makeText(LoginActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void getFilePaths() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
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
        }
        cursor.close();


    }
}