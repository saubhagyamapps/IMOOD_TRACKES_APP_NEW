package app.food.patient_app.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.EditProfileModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfilFragment extends Fragment {
    private static final String TAG = "EditProfilFragment";
    View mView;
    CircleImageView imageView, prof_camera_image;
    EditText edt_profemail, edt_profusername, edt_profmobileno;
    Button btn_update;
    private final static int IMAGE_RESULT = 200;
    String filePath = "null";
    Call<EditProfileModel> editProfileModelCall;
    SessionManager sessionManager;
    String mDeviceId, mFirebaseId;
    Uri picUri;
    MultipartBody.Part body;
    String mImages;
    String mMobileNO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_edit_profil, container, false);
        Constant.setSession(getActivity());
        getActivity().setTitle("");
        initializ();
        return mView;
    }

    private void initializ() {
        sessionManager = new SessionManager(getActivity());
        imageView = mView.findViewById(R.id.acc_profile_image);
        prof_camera_image = mView.findViewById(R.id.prof_camera_image);
        edt_profusername = mView.findViewById(R.id.edt_profusername);
        edt_profemail = mView.findViewById(R.id.edt_profemail);
        edt_profmobileno = mView.findViewById(R.id.edt_profmobileno);
        btn_update = mView.findViewById(R.id.btn_update);
        edt_profusername.setEnabled(false);
        edt_profemail.setEnabled(false);

        setValue();
        updateClick();

    }

    private void setValue() {

        edt_profemail.setText(Constant.mUserEmail);
        edt_profusername.setText(Constant.mUserName);
        edt_profmobileno.setText(Constant.mUserMobile);
        try {
            /*Glide.with(getActivity()).load(Constant.mImagesPath)
                    .thumbnail(0.5f)
                    .into(imageView);*/

            Glide.with(getActivity()).load(Constant.mImagesPath+Constant.mImages)
                    .thumbnail(0.5f)
                    .into(imageView);
        } catch (Exception e) {
        }
    }

    private void updateClick() {
        prof_camera_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_profmobileno.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    Apicall();
                }
            }
        });
    }

    private void Apicall() {
        Constant.progressDialog(getActivity());
        mMobileNO = edt_profmobileno.getText().toString().trim();
        mDeviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mFirebaseId = FirebaseInstanceId.getInstance().getToken();
        //MultipartBody.Part body = null;
        try {
            File file = new File(filePath);
            final RequestBody requestfile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("profile_image", file.getName(), requestfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody u_Id =
                RequestBody.create(MediaType.parse("multipart/form-data"), Constant.mUserId);
        RequestBody u_Username =
                RequestBody.create(MediaType.parse("multipart/form-data"), Constant.mUserName);
        RequestBody u_Email =
                MultipartBody.create(MediaType.parse("multipart/form-data"), Constant.mUserEmail);
        RequestBody u_MobileNo =
                MultipartBody.create(MediaType.parse("multipart/form-data"),edt_profmobileno.getText().toString() );

        if (!filePath.equals("null")) {
            editProfileModelCall = Constant.apiService.
                    editWIthImagesProfile(u_Id, u_Email, u_MobileNo, body, u_Username);

        } else {
            editProfileModelCall = Constant.apiService.editProfile(Constant.mUserId, Constant.mUserEmail, Constant.mUserMobile, Constant.mUserName);
        }
        editProfileModelCall.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(Call<EditProfileModel> call, Response<EditProfileModel> response) {
                Constant.progressBar.dismiss();
                mImages = response.body().getResult().get(0).getImage();
                sessionManager.CreateLoginSession(Constant.mUserId, Constant.mUserName, Constant.mUserEmail, edt_profmobileno.getText().toString(), Constant.mPassword, mFirebaseId, mDeviceId,mImages);
                try {
                    sessionManager.ImagesPath(response.body().getPath());
                } catch (Exception e) {

                }
                Toast.makeText(getActivity(), "Updated Succesfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<EditProfileModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: " +t.getMessage() );
            }
        });
    }


    private Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();

        PackageManager packageManager = getActivity().getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
             //imageView.setImageURI(outputFileUri);
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == IMAGE_RESULT) {

                filePath = getImageFilePath(data);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(selectedImage);

                }
            }

        }
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



}