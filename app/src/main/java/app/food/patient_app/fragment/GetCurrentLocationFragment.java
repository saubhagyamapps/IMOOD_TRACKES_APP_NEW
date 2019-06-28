package app.food.patient_app.fragment;

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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import app.food.patient_app.adapter.LocationAddressTimeAdepter;
import app.food.patient_app.adapter.PlaceArrayAdapter;
import app.food.patient_app.model.AddressTimeModel;
import app.food.patient_app.model.GetHomeLocationModel;
import app.food.patient_app.model.HomeLocationStoreModel;
import app.food.patient_app.model.StoreCurrentHomeAddressModel;
import app.food.patient_app.util.Constant;
import app.food.patient_app.util.TrackerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetCurrentLocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    int FLAG = 1;
    Button imageViewLocation;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    View mView;
    private AutoCompleteTextView mAutocompleteTextViewTo, mAutocompleteTextViewFrom;
    private double mStartlatitude, mStartlongitude;

    RelativeLayout locationView;
    Button btnEditAddress, btnSave;
    RecyclerView recycleviewLocation;
    TextView txtAddress, txtAddressHome, txtTime, txtDate;
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

               // Log.d(TAG, "getAddress:  address" + address);
             //   Log.d(TAG, "getAddress:  city" + city);
             //   Log.d(TAG, "getAddress:  state" + state);
              //  Log.d(TAG, "getAddress:  postalCode" + postalCode);
              //  Log.d(TAG, "getAddress:  knownName" + knownName);
                mAutocompleteTextViewTo.setText(address);
            //    Log.e(TAG, " only city address----->" + address.replace(", " + state, "").replace(postalCode + ",", "").replace(country, ""));
                StoreCurrentHomeAddress(address, String.valueOf(LATITUDE), String.valueOf(LONGITUDE));
                StoreHomeAddress(address, String.valueOf(LATITUDE), String.valueOf(LONGITUDE));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void StoreHomeAddress(String address, String LATITUDE, String LONGITUDE) {
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(getActivity());
        Call<HomeLocationStoreModel> storeModelCall = Constant.apiService.storeHomeLocation(Constant.mUserId, mCurrentDate, address, LATITUDE, LONGITUDE);
        storeModelCall.enqueue(new Callback<HomeLocationStoreModel>() {
            @Override
            public void onResponse(Call<HomeLocationStoreModel> call, Response<HomeLocationStoreModel> response) {

            }

            @Override
            public void onFailure(Call<HomeLocationStoreModel> call, Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_get_current_location, container, false);
        Constant.setSession(getActivity());
        getHoemLocationAPICALL();
     //   getActivity().startService(new Intent(getActivity(), TrackerService.class));
        initialization();
        requestLocationUpdates();
    //    distance(23.077629f, 72.505712f, 23.077777f, 72.504929f);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("");
    }

    public static float distance(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        dist = (float) (earthRadius * c);
        Log.e(TAG, "distance--------> " + dist);
        return dist;
    }

    private void getHoemLocationAPICALL() {
        txtAddress = mView.findViewById(R.id.txtAddress);
        locationView = mView.findViewById(R.id.locationView);
        Call<GetHomeLocationModel> modelCall = Constant.apiService.getHomeLocation(Constant.mUserId);
        modelCall.enqueue(new Callback<GetHomeLocationModel>() {
            @Override
            public void onResponse(Call<GetHomeLocationModel> call, Response<GetHomeLocationModel> response) {
                if (response.body().getStatus().equals("0")) {
                    locationView.setVisibility(View.GONE);
                    txtAddress.setVisibility(View.VISIBLE);
                    Constant.mHomeLat = response.body().getResult().get(0).getLatitude();
                    Constant.mHomeLong = response.body().getResult().get(0).getLongitude();
                    txtAddress.setText(response.body().getResult().get(0).getAddress());
                } else {
                    locationView.setVisibility(View.VISIBLE);
                    txtAddress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<GetHomeLocationModel> call, Throwable t) {

            }
        });
    }

    private void initialization() {

        btnEditAddress = mView.findViewById(R.id.btnEditAddress);
        recycleviewLocation = mView.findViewById(R.id.recycleviewLocation);
        txtAddressHome = mView.findViewById(R.id.txtAddressHome);
        txtTime = mView.findViewById(R.id.txtTime);
        txtDate = mView.findViewById(R.id.txtDate);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleviewLocation.setLayoutManager(mLayoutManager);
        btnSave = mView.findViewById(R.id.btnSave);
        locationView.setVisibility(View.GONE);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextViewTo = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextViewTO);

        imageViewLocation = mView.findViewById(R.id.imgLocation);
        mAutocompleteTextViewTo.setThreshold(0);
        mAutocompleteTextViewTo.setOnItemClickListener(mAutocompleteClickListenerTo);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), R.layout.list_view, BOUNDS_MOUNTAIN_VIEW, null, R.drawable.ic_location);
        mAutocompleteTextViewTo.setAdapter(mPlaceArrayAdapter);
        txtDate.setText(Constant.currentDate());
        editButtonClick();
        AddressAndTimeListAPICALL();

    }

    private void AddressAndTimeListAPICALL() {
        Constant.progressDialog(getActivity());
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);

        Call<AddressTimeModel> timeModelCall = Constant.apiService.getAddressTime(Constant.mUserId, mCurrentDate);
        timeModelCall.enqueue(new Callback<AddressTimeModel>() {
            @Override
            public void onResponse(Call<AddressTimeModel> call, Response<AddressTimeModel> response) {
                try {
                    Log.e("Size of getdata", response.body().getResult().size() + "");
                    mAdapter = new LocationAddressTimeAdepter(response.body().getResult(), getActivity());
                    recycleviewLocation.setHasFixedSize(true);
                    mAdapter.notifyDataSetChanged();
                    recycleviewLocation.setAdapter(mAdapter);
                    txtAddressHome.setText(response.body().getHome_result().get(0).getAddress());
                    txtTime.setText(response.body().getHome_result().get(0).getTime_difference());
                    Constant.progressBar.dismiss();

                }catch (Exception e){
                    Constant.progressBar.dismiss();
                }

            }

            @Override
            public void onFailure(Call<AddressTimeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                Constant.progressBar.dismiss();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void editButtonClick() {
        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtAddress.getRight() - txtAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        locationView.setVisibility(View.VISIBLE);
                        txtAddress.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });
        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void StoreCurrentHomeAddress(String address, String lat, String log) {
        txtAddress.setText(address);
        Call<StoreCurrentHomeAddressModel> modelCall = Constant.apiService.storeLocation(Constant.mUserId, address, lat, log);
        modelCall.enqueue(new Callback<StoreCurrentHomeAddressModel>() {
            @Override
            public void onResponse(Call<StoreCurrentHomeAddressModel> call, Response<StoreCurrentHomeAddressModel> response) {
                locationView.setVisibility(View.GONE);
                Constant.progressBar.dismiss();
                txtAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<StoreCurrentHomeAddressModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(100);
        request.setFastestInterval(100);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getActivity());
        int permission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                    //    Log.e(TAG, "location update " + location.getLongitude());
                     //   Log.e(TAG, "long update " + location.getLongitude());
                        imageViewLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Constant.progressDialog(getActivity());
                                FLAG = 0;
                                mStartlatitude = location.getLatitude();
                                mStartlongitude = location.getLongitude();
                                getAddress(getActivity(), mStartlatitude, mStartlongitude);
                                Constant.progressBar.dismiss();
                            }
                        });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getAddress(getActivity(), mStartlatitude, mStartlongitude);
                                locationView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }, null);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

        Log.i("Location", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Location", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);

        Log.e("Location", "Google Places API connection suspended.");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


}