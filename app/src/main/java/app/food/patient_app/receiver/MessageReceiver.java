package app.food.patient_app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.food.patient_app.model.SendSMSCountModel;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageReceiver extends BroadcastReceiver {
    private static MessageListener mListener;
    private static final String TAG = "MessageReceiver";
    String hms;
    Date todayDate;
    String mCurrentDate;
    static final String ACTION =
            "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Bundle data = intent.getExtras();

        try {

            if (data!=null)
            {

                if (intent.getAction().equals(ACTION)) {
                    Object[] pdus = (Object[]) data.get("pdus");
                    for (int i = 0; i < pdus.length; i++) {

                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        Log.e(TAG, "onReceive: " + smsMessage.getIndexOnIcc());
                        String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                                + "Email From: " + smsMessage.getEmailFrom()
                                + "Emal Body: " + smsMessage.getEmailBody()
                                + "Display message body: " + smsMessage.getDisplayMessageBody()
                                + "Time in millisecond: " + smsMessage.getTimestampMillis()
                                + "Time In HH:MM:SS" + hms
                                + "Message: " + smsMessage.getMessageBody();
                        long millis = smsMessage.getTimestampMillis();
                        hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        Log.e(TAG, "onReceive: " + hms);
                        Log.e(TAG, "Alarmservice---->: " + message);
                        //   Toast.makeText(mContext, "New SMS arrived", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "SMS count updated", Toast.LENGTH_SHORT).show();
                        String sms_sender = smsMessage.getOriginatingAddress();
                        String sms_body = smsMessage.getDisplayMessageBody();
                        String sms_time = String.valueOf(smsMessage.getTimestampMillis());
                        SendSMSCount(sms_sender, context);
                        mListener.messageReceived(message);
                    }
                }

            } else {
                Log.e(TAG, "onReceive: "+"data is null");
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }



    }

    private void SendSMSCount(String sms_sender, Context context) {
        Constant.setSession(context);
        Call<SendSMSCountModel> sendSMSCountModelCall = Constant.apiService.InsertSMS(Constant.mUserId,mCurrentDate,sms_sender);
        sendSMSCountModelCall.enqueue(new Callback<SendSMSCountModel>() {
            @Override
            public void onResponse(Call<SendSMSCountModel> call, Response<SendSMSCountModel> response) {

                if (response.body().getStatus().equals("0"))
                {
                    Log.e(TAG, "insert onResponse--->>>: "+"Success " );
                }
                else {
                }
                Log.e(TAG, "insert onResponse:--->> "+ "Failed" );
            }

            @Override
            public void onFailure(Call<SendSMSCountModel> call, Throwable t) {

                Log.e(TAG, "onFailure: "+"FAILED");
            }
        });
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
