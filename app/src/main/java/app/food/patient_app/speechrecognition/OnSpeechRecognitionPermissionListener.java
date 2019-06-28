package app.food.patient_app.speechrecognition;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public interface OnSpeechRecognitionPermissionListener {

    void onPermissionGranted();

    void onPermissionDenied();
}
