package food.user.demand.FCActivity.FCNewUserAddressSet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Main2Activity;
import food.user.demand.FCActivity.FCDashboard.FC_DashboardActivity;
import food.user.demand.FCActivity.FCProfile.Testing;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
import food.user.demand.Testing_New.RegisterTesting;

public class FC_NewUserAddressActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    InputMethodManager inputMgr;
    LinearLayout ll_mainLayout, lin_other, lin_work, lin_home;
    AC_Edittext edt_home, edt_landmark;
    AC_Textview txt_usecurrentlocation, txt_changeBtn, txt_saveAddress, txt_locationStreetField, txt_locationMainField;
    View parentLayout;
    Snackbar bar;
    // map progress

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient googleApiClient;
    //GoogleApi googleApiClient;
    private GoogleSignInClient mSignInClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    double latitude; // latitude
    double longitude;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    Context context;
    private ImageView img_back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_intro_address_activity);
        context = FC_NewUserAddressActivity.this;
        CheckPermission();

        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FindviewById();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            FC_Common.username = (String) bundle.get("username");
            FC_Common.usermobile = (String) bundle.get("usermobile");
            FC_Common.useremail = (String) bundle.get("useremail");
            FC_Common.usergender = (String) bundle.get("usergender");
            FC_Common.device_id = (String) bundle.get("deviceid");
            FC_Common.token_type = (String) bundle.get("token_type");
            FC_Common.access_token = (String) bundle.get("access_token");
            FC_Common.latitude = (String) bundle.get("latitude");
            FC_Common.longitude = (String) bundle.get("longitude");
            FC_Common.gpsenabled = (String) bundle.get("gpsenabled");
            FC_Common.change_address = (String) bundle.get("change_address");

            if (FC_Common.change_address.equalsIgnoreCase("ManageAddress")) {
                FC_Common.longitude = (String) bundle.get("longitude");
                FC_Common.change_address = (String) bundle.get("change_address");
            }
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.username);
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.usermobile);
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.useremail);
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.usergender);
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.device_id);
            Log.d("fgjhfghjfg", "fdhfdhfd" + FC_Common.token_type);
            Log.d("gpsenabled", "gpsenabled" + FC_Common.gpsenabled);

        }

        if (txt_locationMainField.getText().toString().trim().equalsIgnoreCase("") &&
                txt_locationStreetField.getText().toString().trim().equalsIgnoreCase("") &&
                edt_home.getText().toString().trim().equalsIgnoreCase("")) {
            txt_saveAddress.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
        } else {
            txt_saveAddress.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_green));
        }


        edt_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_home.getText().toString().trim().equalsIgnoreCase("") ||
                        txt_locationStreetField.getText().toString().trim().equalsIgnoreCase("") ||
                        txt_locationMainField.getText().toString().trim().equalsIgnoreCase("")) {
                    txt_saveAddress.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                } else {
                    txt_saveAddress.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_green));
                }

            }
        });

    }

    private void CheckPermission() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_intro_address);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) FC_NewUserAddressActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_NewUserAddressActivity.this)) {
           // Toast.makeText(FC_NewUserAddressActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(FC_NewUserAddressActivity.this)) {
           // Toast.makeText(FC_NewUserAddressActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_NewUserAddressActivity.this)) {
            Log.e("TAG", "Gps already enabled");
           // Toast.makeText(FC_NewUserAddressActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
          //  Toast.makeText(FC_NewUserAddressActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(FC_NewUserAddressActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {


                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       /* locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);*/

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(FC_NewUserAddressActivity.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    private void FindviewById() {
        txt_changeBtn = findViewById(R.id.txt_changeBtn);
        txt_saveAddress = findViewById(R.id.txt_saveAddress);
        txt_locationMainField = findViewById(R.id.txt_locationMainField);
        txt_locationStreetField = findViewById(R.id.txt_locationStreetField);
        ll_mainLayout = findViewById(R.id.ll_mainLayout);
        lin_other = findViewById(R.id.lin_other);
        txt_usecurrentlocation = findViewById(R.id.txt_usecurrentlocation);
        lin_work = findViewById(R.id.lin_work);
        lin_home = findViewById(R.id.lin_home);
        edt_home = findViewById(R.id.edt_home);
        edt_landmark = findViewById(R.id.edt_landmark);
        parentLayout = findViewById(android.R.id.content);
        img_back = findViewById(R.id.img_back);

        txt_changeBtn.setOnClickListener(this);
        txt_saveAddress.setOnClickListener(this);
        lin_home.setOnClickListener(this);
        lin_work.setOnClickListener(this);
        lin_other.setOnClickListener(this);
        txt_usecurrentlocation.setOnClickListener(this);
        img_back.setOnClickListener(this);

        String token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("sdgsdgsd", "dfdf" + token);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.txt_changeBtn:

                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                Intent intent = new Intent(FC_NewUserAddressActivity.this, FC_NewUserLocationPickerActivty.class);
                intent.putExtra("username", FC_Common.username);
                intent.putExtra("usermobile", FC_Common.usermobile);
                intent.putExtra("useremail", FC_Common.useremail);
                intent.putExtra("usergender", FC_Common.usergender);
                intent.putExtra("deviceid", FC_Common.device_id);
                intent.putExtra("token_type", FC_Common.token_type);
                intent.putExtra("access_token", FC_Common.access_token);
                intent.putExtra("latitude", FC_Common.latitude);
                intent.putExtra("longitude", FC_Common.longitude);
                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                intent.putExtra("change_address", FC_Common.change_address);
                startActivity(intent);


                break;

            case R.id.txt_saveAddress:

                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);

                // Register();
                if (edt_home.getText().toString().trim().equalsIgnoreCase("")) {
                    String value = "Flat Number Required";
                    snackBar(value);
                } else if (txt_locationMainField.getText().toString().trim().equalsIgnoreCase("")) {
                    String value = "Location Required click Current location";
                    snackBar(value);
                } else if (txt_locationStreetField.getText().toString().trim().equalsIgnoreCase("")) {
                    String value = "Location Required click Current location";
                    snackBar(value);
                } else {
                    FC_Common.homeaddress = edt_home.getText().toString().trim();
                    FC_Common.Locality = txt_locationMainField.getText().toString().trim();
                    FC_Common.Address = txt_locationStreetField.getText().toString().trim();
                    FC_Common.landmark = edt_landmark.getText().toString().trim();

                   /* Intent intent1 = new Intent(FC_NewUserAddressActivity.this, RegisterTesting.class);
                    startActivity(intent1);*/
                    Utils.playProgressBar(FC_NewUserAddressActivity.this);
                    Register();

                    Log.d("sdgsdfsdf", "gsdfsdfs");

                }


                break;
            case R.id.lin_home:
                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                lin_home.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                lin_work.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                lin_other.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                FC_Common.addresstype = "HOME";
                break;
            case R.id.lin_work:
                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                lin_home.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                lin_work.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                lin_other.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                FC_Common.addresstype = "WORK";
                break;
            case R.id.lin_other:
                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                lin_home.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                lin_work.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                lin_other.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                FC_Common.addresstype = "OTHER";
                onMapReady(mGoogleMap);
                break;
            case R.id.txt_usecurrentlocation:
                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                FC_Common.change_address = "NewUser";
                onMapReady(mGoogleMap);
                break;

            case R.id.img_back :
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
      /*  mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();

            Log.d("cghfghfgh", "fghfg" + FC_Common.change_address);
            Log.d("cghfghfgh", "232dffghfg" + locationList);
            if (FC_Common.change_address.equalsIgnoreCase("NewUser")) {
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mLastLocation = location;
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }

                    //Place current location marker
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                    FC_Common.latitude = String.valueOf(latitude);
                    FC_Common.longitude = String.valueOf(longitude);
                    Log.d("dgsdgsdfg", "latitude : " + latitude);
                    Log.d("dgsdgsdfg", "latitude : " + FC_Common.latitude);
                    Log.d("dgsdgsdfg", "longitude : " + longitude);
                    Log.d("dgsdgsdfg", "longitude : " + FC_Common.longitude);
                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                    String cityName = null;
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;

                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        if (addresses.size() > 0)
                            System.out.println(addresses.get(0).getLocality());

                        String adminArea = addresses.get(0).getAdminArea();
                        String featureName = addresses.get(0).getFeatureName();
                        String subAdminArea = addresses.get(0).getSubAdminArea();
                        String subLocality = addresses.get(0).getSubLocality();
                        String subThoroughfare = addresses.get(0).getSubThoroughfare();
                        String Address = addresses.get(0).getAddressLine(0);

                        Log.d(" gmap", " adminArea : " + adminArea);
                        Log.d(" gmap", " featureName : " + featureName);
                        Log.d(" gmap", " subAdminArea : " + subAdminArea);
                        Log.d(" gmap", " subLocality : " + subLocality);
                        Log.d(" gmap", " subThoroughfare : " + subThoroughfare);
                        Log.d(" gmap", " Address : " + Address);

                        if (subLocality == null) {
                            txt_locationMainField.setText("" + subAdminArea);
                        } else {
                            txt_locationMainField.setText("" + subLocality);
                        }

                        txt_locationStreetField.setText("" + Address);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Location location = locationList.get(locationList.size() - 1);
                //Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                //mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                double latitude = Double.parseDouble(FC_Common.latitude);
                double longitude = Double.parseDouble(FC_Common.longitude);

                //Place current location marker
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
                // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                FC_Common.latitude = String.valueOf(latitude);
                FC_Common.longitude = String.valueOf(longitude);
                Log.d("dgsdgsdfg", "latitude : " + latitude);
                Log.d("dgsdgsdfg", "latitude : " + FC_Common.latitude);
                Log.d("dgsdgsdfg", "longitude : " + longitude);
                Log.d("dgsdgsdfg", "longitude : " + FC_Common.longitude);
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;

                try {
                    addresses = gcd.getFromLocation(latitude,
                            longitude, 1);
                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());

                    String adminArea = addresses.get(0).getAdminArea();
                    String featureName = addresses.get(0).getFeatureName();
                    String subAdminArea = addresses.get(0).getSubAdminArea();
                    String subLocality = addresses.get(0).getSubLocality();
                    String subThoroughfare = addresses.get(0).getSubThoroughfare();
                    String Address = addresses.get(0).getAddressLine(0);

                    Log.d(" gmap", " adminArea : " + adminArea);
                    Log.d(" gmap", " featureName : " + featureName);
                    Log.d(" gmap", " subAdminArea : " + subAdminArea);
                    Log.d(" gmap", " subLocality : " + subLocality);
                    Log.d(" gmap", " subThoroughfare : " + subThoroughfare);
                    Log.d(" gmap", " Address : " + Address);

                    if (subLocality == null) {
                        txt_locationMainField.setText("" + subAdminArea);
                    } else {
                        txt_locationMainField.setText("" + subLocality);
                    }

                    txt_locationStreetField.setText("" + Address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(FC_NewUserAddressActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ADDUSER,
                ServerResponse -> {

                    Log.d("xfghdfhdfhfdhdf", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.message = jsonResponse1.getString("message");
                        /*FC_Common.message = jsonResponse1.getString("message");
                        FC_Common.token_type = jsonResponse1.getString("token_type");
                        FC_Common.access_token= jsonResponse1.getString("access_token");*/
                        if (FC_Common.status.equalsIgnoreCase("1")) {


                            snackBar(FC_Common.message);
                            GetUserDetails();

                            Log.d("fgjfghfgh","fghjfgjhfg");
                           /* Intent intent= new Intent(FC_NewUserAddressActivity.this, RegisterTesting.class);
                            startActivity(intent);*/

                        } else {

                            snackBar(FC_Common.message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(String.valueOf(e));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String value = volleyError.toString();
                snackBar(value);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", FC_Common.addresstype);
                params.put("map_address", FC_Common.Address);
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("landmark", FC_Common.landmark);
                params.put("building", FC_Common.homeaddress);
                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_NewUserAddressActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.d("ServerResponse", "" + ServerResponse);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            //FC_Common.status = jsonResponse1.getString("success");
                            FC_Common.id = jsonResponse1.getString("id");
                            FC_Common.name = jsonResponse1.getString("name");
                            FC_Common.email = jsonResponse1.getString("email");
                            FC_Common.mobile = jsonResponse1.getString("mobile");
                            FC_Common.email_verified_at = jsonResponse1.getString("email_verified_at");
                            FC_Common.dial_code = jsonResponse1.getString("dial_code");
                            FC_Common.location_name = jsonResponse1.getString("location_name");
                            FC_Common.location_type = jsonResponse1.getString("location_type");
                            FC_Common.latitude = jsonResponse1.getString("latitude");
                            FC_Common.longitude = jsonResponse1.getString("longitude");
                            FC_Common.status = jsonResponse1.getString("status");
                            FC_Common.is_guest = jsonResponse1.getString("is_guest");
                            FC_Common.picture = jsonResponse1.getString("picture");
                            FC_Common.device_token = jsonResponse1.getString("device_token");
                            FC_Common.device_id = jsonResponse1.getString("device_id");
                            FC_Common.device_type = jsonResponse1.getString("device_type");
                            FC_Common.login_by = jsonResponse1.getString("login_by");
                            FC_Common.social_unique_id = jsonResponse1.getString("social_unique_id");
                            FC_Common.cust_id = jsonResponse1.getString("cust_id");
                            FC_Common.wallet_balance = jsonResponse1.getString("wallet_balance");
                            FC_Common.rating = jsonResponse1.getString("rating");
                            FC_Common.userotp = jsonResponse1.getString("otp");
                            FC_Common.created_at = jsonResponse1.getString("created_at");
                            FC_Common.updated_at = jsonResponse1.getString("updated_at");
                            FC_Common.token_type = jsonResponse1.getString("token_type");
                            FC_Common.access_token = jsonResponse1.getString("access_token");
                            FC_Common.gender = jsonResponse1.getString("gender");
                            FC_Common.filter_price_min = jsonResponse1.getString("filter_price_min");
                            FC_Common.filter_price_max = jsonResponse1.getString("filter_price_max");
                            FC_Common.is_default = jsonResponse1.getString("is_default");
                            Log.d("fdhdfhfd", "dfgfd" + FC_Common.id);

                            if (FC_Common.status.equalsIgnoreCase("1")) {
                                String value = "dgsdgsdgsd";

                                snackBar(value);

                                FC_User user = new FC_User(jsonResponse1.getString("id"),
                                        jsonResponse1.getString("name"),
                                        jsonResponse1.getString("email"),
                                        jsonResponse1.getString("mobile"),
                                        jsonResponse1.getString("email_verified_at"),
                                        jsonResponse1.getString("dial_code"),
                                        jsonResponse1.getString("location_name"),
                                        jsonResponse1.getString("location_type"),
                                        jsonResponse1.getString("latitude"),
                                        jsonResponse1.getString("longitude"),
                                        jsonResponse1.getString("status"),
                                        jsonResponse1.getString("is_guest"),
                                        jsonResponse1.getString("picture"),
                                        jsonResponse1.getString("device_token"),
                                        jsonResponse1.getString("device_id"),
                                        jsonResponse1.getString("device_type"),
                                        jsonResponse1.getString("login_by"),
                                        jsonResponse1.getString("social_unique_id"),
                                        jsonResponse1.getString("cust_id"),
                                        jsonResponse1.getString("wallet_balance"),
                                        jsonResponse1.getString("rating"),
                                        jsonResponse1.getString("otp"),
                                        jsonResponse1.getString("created_at"),
                                        jsonResponse1.getString("updated_at"),
                                        jsonResponse1.getString("token_type"),
                                        jsonResponse1.getString("access_token"),
                                        jsonResponse1.getString("gender"),
                                        jsonResponse1.getString("filter_price_min"),
                                        jsonResponse1.getString("filter_price_max"),
                                        jsonResponse1.getString("is_default")
                                );

                                FC_SharedPrefManager.getInstance(FC_NewUserAddressActivity.this).userLogin(user);
                                SharedPreferences sharedPreferences = FC_NewUserAddressActivity.this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //editor.putBoolean("FirstLogin", true);
                                editor.commit();
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_NewUserAddressActivity.this);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                                editor1.putString(Utils.id, FC_Common.id);
                                editor1.putString(Utils.name, FC_Common.name);
                                editor1.putString(Utils.email, FC_Common.email);
                                editor1.putString(Utils.mobile, FC_Common.mobile);
                                editor1.putString(Utils.email_verified_at, FC_Common.email_verified_at);
                                editor1.putString(Utils.dial_code, FC_Common.dial_code);
                                editor1.putString(Utils.location_name, FC_Common.location_name);
                                editor1.putString(Utils.location_type, FC_Common.location_type);
                                editor1.putString(Utils.latitude, FC_Common.latitude);
                                editor1.putString(Utils.longitude, FC_Common.longitude);
                                editor1.putString(Utils.status, FC_Common.status);
                                editor1.putString(Utils.is_guest, FC_Common.is_guest);
                                editor1.putString(Utils.picture, FC_Common.picture);
                                editor1.putString(Utils.device_token, FC_Common.device_token);
                                editor1.putString(Utils.device_id, FC_Common.device_id);
                                editor1.putString(Utils.device_type, FC_Common.device_type);
                                editor1.putString(Utils.login_by, FC_Common.login_by);
                                editor1.putString(Utils.social_unique_id, FC_Common.social_unique_id);
                                editor1.putString(Utils.cust_id, FC_Common.cust_id);
                                editor1.putString(Utils.wallet_balance, FC_Common.wallet_balance);
                                editor1.putString(Utils.rating, FC_Common.rating);
                                editor1.putString(Utils.userotp, FC_Common.userotp);
                                editor1.putString(Utils.created_at, FC_Common.created_at);
                                editor1.putString(Utils.updated_at, FC_Common.updated_at);
                                editor1.putString(Utils.token_type, FC_Common.token_type);
                                editor1.putString(Utils.access_token, FC_Common.access_token);
                                editor1.putString(Utils.gender, FC_Common.gender);
                                editor1.putString(Utils.filter_price_min, FC_Common.filter_price_min);
                                editor1.putString(Utils.filter_price_max, FC_Common.filter_price_max);
                                editor1.putString(Utils.is_default, FC_Common.is_default);
                                editor1.apply();

                                Utils.stopProgressBar();
                               /* Intent intent= new Intent(FC_NewUserAddressActivity.this, RegisterTesting.class);
                                startActivity(intent);*/
                                Intent intent = new Intent(FC_NewUserAddressActivity.this, FC_DashboardActivity.class);
                                intent.putExtra("location_name", FC_Common.location_name);
                                intent.putExtra("location_type", FC_Common.location_type);
                                intent.putExtra("latitude", FC_Common.latitude);
                                intent.putExtra("longitude", FC_Common.longitude);
                                intent.putExtra("gpsenabled", "false");
                                startActivity(intent);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("fdhbdfghdf", "dfhdf" + e);
                            snackBar(String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String value = volleyError.toString();
                snackBar(value);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FC_Common.token_type + " " + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_NewUserAddressActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(parentLayout, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bar.dismiss();
                                }
                            });
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }

    @Override
    public void onBackPressed() {

        if (FC_Common.change_address.equalsIgnoreCase("NewUser")) {
           /* Intent intent = new Intent(FC_IntroAddressActivity.this, FC_IntroProfileActivity.class);
            intent.putExtra("username", FC_Common.username);
            intent.putExtra("usermobile", FC_Common.usermobile);
            intent.putExtra("useremail", FC_Common.useremail);
            intent.putExtra("usergender", FC_Common.usergender);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("latitude", FC_Common.latitude);
            intent.putExtra("longitude", FC_Common.longitude);
            intent.putExtra("change_address", FC_Common.change_address);
            startActivity(intent);*/
        } else if (FC_Common.change_address.equalsIgnoreCase("splash")) {

        }

    }

}
