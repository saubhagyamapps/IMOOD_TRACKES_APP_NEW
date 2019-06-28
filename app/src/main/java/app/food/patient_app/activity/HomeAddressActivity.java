package app.food.patient_app.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.food.patient_app.R;
import app.food.patient_app.adapter.PlaceArrayAdapter;
import app.food.patient_app.model.HomeLocationStoreModel;
import app.food.patient_app.model.InsertWorkLocationModel;
import app.food.patient_app.model.StoreCurrentHomeAddressModel;
import app.food.patient_app.util.Constant;
import app.food.patient_app.util.TrackerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAddressActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "WorkLocationFragment";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    int FLAG = 1;
    Button imageViewLocation;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private AutoCompleteTextView mAutocompleteTextViewTo_workLocation, mAutocompleteTextViewFrom;
    private double mStartlatitude, mStartlongitude;

    private RelativeLayout locationView;
    private Button btnEditAddress, btnSave;
    private RecyclerView recycleviewLocation;
    private TextView txtAddress, txtAddressHome, txtTime, txtDate;
    static float dist;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbackto
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("Location", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            if (FLAG == 1) {
                mStartlatitude = place.getLatLng().latitude;
                mStartlongitude = place.getLatLng().longitude;
            }
            Log.i("name", place.getName().toString());
            Log.i("coordinates", place.getLatLng().toString());
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListenerTo
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("Location", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackto);
            Log.i("Location", "Fetching details for ID: " + item.placeId);
        }
    };

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        FLAG = 1;
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

              //  Log.d(TAG, "getAddress:  address" + address);
              //  Log.d(TAG, "getAddress:  city" + city);
              //  Log.d(TAG, "getAddress:  state" + state);
               // Log.d(TAG, "getAddress:  postalCode" + postalCode);
              //  Log.d(TAG, "getAddress:  knownName" + knownName);
                mAutocompleteTextViewTo_workLocation.setText(address);

              //  Log.e(TAG, " only city address----->" + address.replace(", " + state, "").replace(postalCode + ",", "").replace(country, ""));
                StoreCurrentHomeAddress(address, String.valueOf(LATITUDE), String.valueOf(LONGITUDE));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void StoreCurrentHomeAddress(String address, String lat, String log) {
        Call<StoreCurrentHomeAddressModel> modelCall = Constant.apiService.storeLocation(Constant.mUserId, address, lat, log);
        modelCall.enqueue(new Callback<StoreCurrentHomeAddressModel>() {
            @Override
            public void onResponse(Call<StoreCurrentHomeAddressModel> call, Response<StoreCurrentHomeAddressModel> response) {
                Constant.progressBar.dismiss();
                Intent intent=new Intent(HomeAddressActivity.this,WorkAddressActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter);

            }

            @Override
            public void onFailure(Call<StoreCurrentHomeAddressModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhome_address);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        Constant.setSession(HomeAddressActivity.this);
      //  startService(new Intent(HomeAddressActivity.this, TrackerService.class));

        initialization();
        requestLocationUpdates();

    }

    private void initialization() {

        btnEditAddress = findViewById(R.id.btnEditAddress);
        recycleviewLocation = findViewById(R.id.recycleviewLocation);
        txtAddressHome = findViewById(R.id.txtAddressHome);

        locationView = findViewById(R.id.locationView);
        mLayoutManager = new LinearLayoutManager(HomeAddressActivity.this, LinearLayoutManager.VERTICAL, false);
        // recycleviewLocation.setLayoutManager(mLayoutManager);
        btnSave = findViewById(R.id.btnSave);
        mGoogleApiClient = new GoogleApiClient.Builder(HomeAddressActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(HomeAddressActivity.this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextViewTo_workLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewTO_worklocation);

        imageViewLocation = findViewById(R.id.imgLocation);
        mAutocompleteTextViewTo_workLocation.setThreshold(0);
        mAutocompleteTextViewTo_workLocation.setOnItemClickListener(mAutocompleteClickListenerTo);
        mPlaceArrayAdapter = new PlaceArrayAdapter(HomeAddressActivity.this, R.layout.list_view, BOUNDS_MOUNTAIN_VIEW, null, R.drawable.ic_location);
        mAutocompleteTextViewTo_workLocation.setAdapter(mPlaceArrayAdapter);




    }


    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(HomeAddressActivity.this);
        mGoogleApiClient.disconnect();
    }



    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(100);
        request.setFastestInterval(100);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(HomeAddressActivity.this);
        int permission = ContextCompat.checkSelfPermission(HomeAddressActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                     //   Log.e(TAG, "location update " + location.getLongitude());
                      //  Log.e(TAG, "long update " + location.getLongitude());
                        imageViewLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Constant.progressDialog(HomeAddressActivity.this);
                                FLAG = 0;
                                mStartlatitude = location.getLatitude();
                                mStartlongitude = location.getLongitude();
                                getAddress(HomeAddressActivity.this, mStartlatitude, mStartlongitude);

                            }
                        });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.progressDialog(HomeAddressActivity.this);
                                getAddress(HomeAddressActivity.this, mStartlatitude, mStartlongitude);
                            }
                        });
                    }
                }
            }, null);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

        Log.i("Location", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {

        mPlaceArrayAdapter.setGoogleApiClient(null);

        Log.e("Location", "Google Places API connection suspended.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Location", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(HomeAddressActivity.this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
