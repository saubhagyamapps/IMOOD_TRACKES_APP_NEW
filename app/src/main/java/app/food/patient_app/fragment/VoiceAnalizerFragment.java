package app.food.patient_app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.food.patient_app.R;
import app.food.patient_app.speechrecognition.OnSpeechRecognitionListener;
import app.food.patient_app.speechrecognition.OnSpeechRecognitionPermissionListener;
import app.food.patient_app.speechrecognition.SpeechRecognition;

public class VoiceAnalizerFragment extends Fragment implements OnSpeechRecognitionListener, OnSpeechRecognitionPermissionListener {
    View mView;
    private static final String TAG = "VoiceAnalizerFragment";
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_voice_analizer, container, false);
        init();
        return mView;
    }

    private void init() {
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                testing();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 2000);


    }

    private void testing() {
        SpeechRecognition speechRecognition = new SpeechRecognition(getActivity());
        speechRecognition.setSpeechRecognitionPermissionListener(this);
        speechRecognition.setSpeechRecognitionListener(this);
        speechRecognition.startSpeechRecognition();
    }

    @Override
    public void OnSpeechRecognitionStarted() {
        Log.e(TAG, "OnSpeechRecognitionStarted: ");
    }

    @Override
    public void OnSpeechRecognitionStopped() {
        Log.e(TAG, "OnSpeechRecognitionStopped: ");
    }

    @Override
    public void OnSpeechRecognitionFinalResult(String s) {

        Log.e(TAG, "OnSpeechRecognitionCurrentResult: " + s);
    }

    @Override
    public void OnSpeechRecognitionCurrentResult(String s) {
        //this is called multiple times when SpeechRecognition is
        //still listening. It returns each recognized word when the user is still speaking
        Log.e(TAG, "OnSpeechRecognitionCurrentResult: " + s);
    }

    @Override
    public void OnSpeechRecognitionError(int i, String s) {
        Log.e(TAG, "OnSpeechRecognitionError: " + s.length());
        Log.e(TAG, "OnSpeechRecognitionError: " + String.valueOf(i));
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }
}
