package app.food.patient_app.activity;

import android.content.Context;
import android.widget.Toast;

import app.food.patient_app.model.LockCountModel;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockAPICallClass {
    Context context;

    public LockAPICallClass(Context context) {
        this.context = context;
        LockCountApiCAll();
    }

    public void LockCountApiCAll() {
        Constant.setSession(context);
        Call<LockCountModel> modelCall = Constant.apiService.getLockCount(Constant.mUserId, Constant.currentDate());
        modelCall.enqueue(new Callback<LockCountModel>() {
            @Override
            public void onResponse(Call<LockCountModel> call, Response<LockCountModel> response) {
                Toast.makeText(context, "lock update", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LockCountModel> call, Throwable t) {
                Toast.makeText(context, "lock update"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
