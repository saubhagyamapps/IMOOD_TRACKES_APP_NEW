package app.food.patient_app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import app.food.patient_app.R;
import app.food.patient_app.model.ResetPasswordModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPasswordFragment extends Fragment {
    View mView;
    EditText edt_OldPwd, edt_NewPwd, edt_ConfPwd;
    Button btn_Reset;
    String mOldPwd, mNewPwd, mConfPwd;
    SessionManager sessionManager;
    HashMap<String, String> user;
    private static final String TAG = "ResetPasswordFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        getActivity().setTitle("");
        initialize();
        getClick();
        return mView;
    }

    private void initialize() {
        edt_OldPwd = mView.findViewById(R.id.edt_profOldPass);
        edt_NewPwd = mView.findViewById(R.id.edt_profNewPass);
        edt_ConfPwd = mView.findViewById(R.id.edt_profConfirmNewPass);
        btn_Reset = mView.findViewById(R.id.btn_ResetPassword);
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
    }

    private void getClick()
    {
        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
    }

    private void getValue() {
        mOldPwd = edt_OldPwd.getText().toString().trim();
        mNewPwd = edt_NewPwd.getText().toString().trim();
        mConfPwd = edt_ConfPwd.getText().toString().trim();
        validation();
    }

    private void validation() {
        if (mNewPwd.equals(mConfPwd)) {
                    ResetPasword();
        } else {

            edt_ConfPwd.setError("Confirm password does'n match");
            edt_ConfPwd.setFocusable(true);
            Toast.makeText(getActivity(), "Confirm password does'n match", Toast.LENGTH_SHORT).show();
        }
    }

    private void ResetPasword()
    {
        Constant.progressDialog(getActivity());
        Call<ResetPasswordModel> resetPasswordModelCall = 
                Constant.apiService.getResetPasswrod(user.get(sessionManager.KEY_ID),mOldPwd,mNewPwd);
        
        resetPasswordModelCall.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(Call<ResetPasswordModel> call, Response<ResetPasswordModel> response) {
                
                    Constant.progressBar.dismiss();
                    if (response.body().getStatus().equals("0")) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    edt_OldPwd.setText("");
                    edt_NewPwd.setText("");
                    edt_ConfPwd.setText("");
            }

            @Override
            public void onFailure(Call<ResetPasswordModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: "+ "API Call failed" );
            }
        });
    }

}