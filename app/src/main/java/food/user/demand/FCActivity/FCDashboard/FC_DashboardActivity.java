package food.user.demand.FCActivity.FCDashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCFragment.FCDashboardFragment.FC_AccountFragment;
import food.user.demand.FCFragment.FCDashboardFragment.FC_CartFragment;
import food.user.demand.FCFragment.FCDashboardFragment.FC_ExploreFragment;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FC_HomeFragment;
import food.user.demand.FCSplash.FC_Splash;
import food.user.demand.FCUtils.FC_Logout;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
@SuppressLint("Registered")
public class FC_DashboardActivity extends AppCompatActivity implements View.OnClickListener , OnMapReadyCallback ,FC_AccountFragment.LogoutAdmin{
    LinearLayout ll_home ,ll_explore ,ll_cart ,ll_account ;
    ImageView img_home ,img_explore ,img_cart ,img_account ;
    AC_Textview txt_home ,txt_explore ,txt_cart ,txt_account ;

    Fragment homeFragment = new FC_HomeFragment();
    FC_User user;
    Context context;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    //GoogleApi googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    double latitude; // latitude
    double longitude;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    private int backStack,logOut;

    public static FragmentManager fragmentManager;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_dasboard);

        Log.d("fghfghfdghfg","dash");
        fragmentManager = getSupportFragmentManager();
        FindviewById();

        // Default Home Fragment Loading
        user = FC_SharedPrefManager.getInstance(FC_DashboardActivity.this).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.email_verified_at = String.valueOf(user.getemail_verified_at());
        FC_Common.dial_code = String.valueOf(user.getdial_code());
        FC_Common.location_name = String.valueOf(user.getlocation_name());
        FC_Common.location_type = String.valueOf(user.getlocation_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.status = String.valueOf(user.getstatus());
        FC_Common.is_guest = String.valueOf(user.getis_guest());
        FC_Common.picture = String.valueOf(user.getpicture());
        FC_Common.device_token = String.valueOf(user.getdevice_token());
        FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.device_type = String.valueOf(user.getdevice_type());
        FC_Common.login_by = String.valueOf(user.getlogin_by());
        FC_Common.social_unique_id = String.valueOf(user.getsocial_unique_id());
        FC_Common.cust_id = String.valueOf(user.getcust_id());
        FC_Common.wallet_balance = String.valueOf(user.getwallet_balance());
        FC_Common.rating = String.valueOf(user.getrating());
        FC_Common.userotp = String.valueOf(user.getotp());
        FC_Common.created_at = String.valueOf(user.getcreated_at());
        FC_Common.updated_at = String.valueOf(user.getupdated_at());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.gender = String.valueOf(user.getgender());

        Log.d("dfgdfgfdg","12dfgfdgd"+FC_Common.location_name);
        Log.d("dfgdfgfdg","21dfgfdgd"+FC_Common.latitude);
        Log.d("dfgdfgfdg","2323dfgfdgd"+FC_Common.longitude);
        Log.d("dfgdfgfdg","44dfgfdgd"+FC_Common.name);
        Log.d("device_id","device_id"+FC_Common.device_id);
        DefaultFragmnet();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            FC_Common.location_name = (String) bundle.get("location_name");
            FC_Common.location_type = (String) bundle.get("location_type");
            FC_Common.latitude = (String) bundle.get("latitude");
            FC_Common.longitude = (String) bundle.get("longitude");
            FC_Common.gpsenabled = (String) bundle.get("gpsenabled");
            Log.d("cvbxcbxcbxc","dfgfdgdf"+FC_Common.location_name);
            Log.d("cvbxcbxcbxc","dfgfdgdf"+FC_Common.location_type);
            Log.d("cvbxcbxcbxc","dfgfdgdf"+FC_Common.latitude);
            Log.d("cvbxcbxcbxc","dfgfdgdf"+FC_Common.longitude);
            Log.d("gpsenabled","dfgfdgdf"+FC_Common.gpsenabled);
        }
        if (FC_Common.gpsenabled.equalsIgnoreCase("false"))
        {
            GetUserDetailsDefault();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FC_DashboardActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.custom_device_location_off_alert, null);
            dialogBuilder.setView(dialogView);

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.START;
            wmlp.x = 30;   //x position
            wmlp.y = 30;   //y position

            AC_Textview txt_noThanks = dialogView.findViewById(R.id.txt_noThanks);
            AC_Textview txt_enableLocation = dialogView.findViewById(R.id.txt_enableLocation);
            txt_noThanks.setOnClickListener(view1 -> alertDialog.dismiss());
            txt_enableLocation.setOnClickListener(v -> {
                CheckPermission();
                alertDialog.dismiss();
            });
            alertDialog.show();


        }
        else {
            GetUserDetailsDefault();
            CheckPermission();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FC_DashboardActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.custom_location_set_alert, null);
            dialogBuilder.setView(dialogView);

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.START;
            wmlp.x = 30;   //x position
            wmlp.y = 30;   //y position

            AC_Textview txt_got_it = dialogView.findViewById(R.id.txt_got_it);
            AC_Textview txt_currentLocation = dialogView.findViewById(R.id.txt_currentLocation);
            txt_currentLocation.setText("Welcome To "+ FC_Common.location_type);
            txt_got_it.setOnClickListener(view1 -> alertDialog.dismiss());

            alertDialog.show();

        }
    }
    private void CheckPermission() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_intro_address);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) FC_DashboardActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_DashboardActivity.this)) {


        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(FC_DashboardActivity.this)) {
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_DashboardActivity.this)) {
            Log.e("TAG", "Gps already enabled");
            FC_Common.gpsenabled="true";
            enableLoc();

        } else {
            Log.e("TAG", "Gps already enabled");
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                        resolvable.startResolutionForResult(FC_DashboardActivity.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }
    private void FindviewById() {

        ll_home = findViewById(R.id.ll_home);
        ll_explore = findViewById(R.id.ll_explore);
        ll_cart = findViewById(R.id.ll_cart);
        ll_account = findViewById(R.id.ll_account);

        img_home = findViewById(R.id.img_home);
        img_explore = findViewById(R.id.img_explore);
        img_cart = findViewById(R.id.img_cart);
        img_account = findViewById(R.id.img_account);

        txt_home = findViewById(R.id.txt_home);
        txt_home.setText(R.string.d_home);
        txt_explore = findViewById(R.id.txt_explore);
        txt_explore.setText(R.string.d_explore);
        txt_cart = findViewById(R.id.txt_cart);
        txt_cart.setText(R.string.d_cart);
        txt_account = findViewById(R.id.txt_account);
        txt_account.setText(R.string.d_account);

        ll_home.setOnClickListener(this);
        ll_explore.setOnClickListener(this);
        ll_cart.setOnClickListener(this);
        ll_account.setOnClickListener(this);

    }


    private void DefaultFragmnet() {

        img_home.setBackgroundResource(R.drawable.home_s);
        img_explore.setBackgroundResource(R.drawable.explore);
        img_cart.setBackgroundResource(R.drawable.cart);
        img_account.setBackgroundResource(R.drawable.account);

        txt_home.setTextColor(getResources().getColor(R.color.txt_site_red_color));
        txt_explore.setTextColor(getResources().getColor(R.color.colorgrey));
        txt_cart.setTextColor(getResources().getColor(R.color.colorgrey));
        txt_account.setTextColor(getResources().getColor(R.color.colorgrey));

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction =
//                fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fl_fcDashboardFragment, homeFragment);
//        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ll_home :

                backStack = 0;

                img_home.setBackgroundResource(R.drawable.home_s);
                img_explore.setBackgroundResource(R.drawable.explore);
                img_cart.setBackgroundResource(R.drawable.cart);
                img_account.setBackgroundResource(R.drawable.account);

                txt_home.setTextColor(getResources().getColor(R.color.txt_site_red_color));
                txt_explore.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_cart.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_account.setTextColor(getResources().getColor(R.color.colorgrey));

                Fragment homeFragment = new FC_HomeFragment();
                FragmentTransaction fragmentTransactionHome = fragmentManager.beginTransaction();
                // fragmentTransactionHome.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                fragmentTransactionHome.replace(R.id.fl_fcDashboardFragment, homeFragment);
                fragmentTransactionHome.commit();

                break;

            case R.id.ll_explore :

                backStack = 1;

                img_home.setBackgroundResource(R.drawable.home);
                img_explore.setBackgroundResource(R.drawable.explore_s);
                img_cart.setBackgroundResource(R.drawable.cart);
                img_account.setBackgroundResource(R.drawable.account);

                txt_home.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_explore.setTextColor(getResources().getColor(R.color.txt_site_red_color));
                txt_cart.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_account.setTextColor(getResources().getColor(R.color.colorgrey));

                Fragment exploreFragment = new FC_ExploreFragment();
                FragmentTransaction fragmentTransactionExplore = fragmentManager.beginTransaction();
                // fragmentTransactionExplore.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                fragmentTransactionExplore.replace(R.id.fl_fcDashboardFragment, exploreFragment);
                fragmentTransactionExplore.commit();

                break;

            case R.id.ll_cart :

                backStack = 1;

                img_home.setBackgroundResource(R.drawable.home);
                img_explore.setBackgroundResource(R.drawable.explore);
                img_cart.setBackgroundResource(R.drawable.cart_s);
                img_account.setBackgroundResource(R.drawable.account);
                txt_home.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_explore.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_cart.setTextColor(getResources().getColor(R.color.txt_site_red_color));
                txt_account.setTextColor(getResources().getColor(R.color.colorgrey));

                Fragment cartFragment = new FC_CartFragment();
                FragmentTransaction fragmentTransactionCart = fragmentManager.beginTransaction();
               // fragmentTransactionCart.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                fragmentTransactionCart.replace(R.id.fl_fcDashboardFragment, cartFragment);
                fragmentTransactionCart.commit();

                break;

            case R.id.ll_account :

                backStack = 1;

                img_home.setBackgroundResource(R.drawable.home);
                img_explore.setBackgroundResource(R.drawable.explore);
                img_cart.setBackgroundResource(R.drawable.cart);
                img_account.setBackgroundResource(R.drawable.account_s);

                txt_home.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_explore.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_cart.setTextColor(getResources().getColor(R.color.colorgrey));
                txt_account.setTextColor(getResources().getColor(R.color.txt_site_red_color));

                Fragment accountFragment = new FC_AccountFragment();
                FragmentTransaction fragmentTransactionAccount = fragmentManager.beginTransaction();
                // fragmentTransactionAccount.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                fragmentTransactionAccount.replace(R.id.fl_fcDashboardFragment, accountFragment);
                fragmentTransactionAccount.commit();

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
       /* mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
               // mGoogleMap.setMyLocationEnabled(true);

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
           // mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();


                Utils.log(context,"fsdfsdfsd"+"cgwrer23rvdgsdfsdfsd"+FC_Common.gpsenabled);
                Log.d("dfghfdgfdhgdf","cgwrer23rvdgsdfsdfsd");
                if (FC_Common.gpsenabled.equalsIgnoreCase("true")) {
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

                        int height = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
                        int width = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
                        Bitmap b =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_location_marker));
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                      //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                        FC_Common.latitude = String.valueOf(latitude);
                        FC_Common.longitude = String.valueOf(longitude);
                        Log.d("dgsdgsdfg", "latitude : " + latitude);
                        Log.d("dgsdgsdfg", "latitude : " + FC_Common.latitude);
                        Log.d("dgsdgsdfg", "longitude : " + longitude);
                        Log.d("dgsdgsdfg", "longitude : " + FC_Common.longitude);
                        //move map camera
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

                        String cityName = null;
                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            if (addresses.size() > 0)
                                System.out.println(addresses.get(0).getLocality());

                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();
                            String adminArea = addresses.get(0).getAdminArea();
                            String featureName = addresses.get(0).getFeatureName();
                            String subAdminArea = addresses.get(0).getSubAdminArea();
                            String subLocality = addresses.get(0).getSubLocality();
                            String subThoroughfare = addresses.get(0).getSubThoroughfare();
                            FC_Common.address_set = addresses.get(0).getAddressLine(0);

                            FC_Common.latitude_set = String.valueOf(addresses.get(0).getLatitude());
                            FC_Common.longitude_set = String.valueOf(addresses.get(0).getLongitude());
                            Log.d(" gmap", " adminArea : " + adminArea);
                            Log.d(" gmap", " featureName : " + featureName);
                            Log.d(" gmap", " subAdminArea : " + subAdminArea);
                            Log.d(" gmap", " subLocality : " + subLocality);
                            Log.d(" gmap", " subThoroughfare : " + subThoroughfare);
                            Log.d(" gmap", " Address : " + FC_Common.address_set);
                            Log.d(" gmap", " latitude : " + FC_Common.latitude);
                            Log.d(" gmap", " longitude : " + FC_Common.longitude);
                            if (subLocality == null) {
                                FC_Common.location_type_set = addresses.get(0).getSubAdminArea();
                            } else {
                                FC_Common.location_type_set = addresses.get(0).getSubLocality();
                            }
                        /*SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_DashboardActivity.this);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                        editor1.putString(Utils.location_name, FC_Common.address);
                        editor1.putString(Utils.latitude, FC_Common.address);
                        editor1.putString(Utils.longitude, FC_Common.longitude);
                        editor1.apply();

                        user = FC_SharedPrefManager.getInstance(FC_DashboardActivity.this).getUser();
                        //FC_Common.id = String.valueOf(user.getid());
                        FC_Common.location_name = String.valueOf(user.getlocation_name());
                        FC_Common.latitude = String.valueOf(user.getlatitude());
                        FC_Common.longitude = String.valueOf(user.getlongitude());
                        Utils.log(context,"zxgxcgdxg"+FC_Common.address);
                        Utils.log(context,"zxgxcgdxg"+FC_Common.latitude);
                        Utils.log(context,"zxgxcgdxg"+FC_Common.longitude);*/

                            GetUserDetails();
                            // txt_locationStreetField.setText("" + Address);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    };

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(FC_DashboardActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
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
                   // mGoogleMap.setMyLocationEnabled(true);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                //Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void GetUserDetailsDefault() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.d("ServerResponse", "gbsgbsdfg" + ServerResponse);
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
//                            FC_Common.location_name = FC_Common.address_set;
//                            FC_Common.location_type = FC_Common.location_type_set;
//                            FC_Common.latitude = FC_Common.latitude_set;
//                            FC_Common.longitude = FC_Common.longitude_set;
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
                            Log.d("fdhdfhfd","dfgfd"+FC_Common.latitude);

                            if (FC_Common.status.equalsIgnoreCase("1"))
                            {

                                String value="dgsdgsdgsd";

                                // snackBar(value);

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

                                FC_SharedPrefManager.getInstance(FC_DashboardActivity.this).userLogin(user);
                                SharedPreferences sharedPreferences = FC_DashboardActivity.this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //editor.putBoolean("FirstLogin", true);
                                editor.commit();
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_DashboardActivity.this);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                                editor1.putString(Utils.id, FC_Common.id);
                                editor1.putString(Utils.name, FC_Common.name);
                                editor1.putString(Utils.email, FC_Common.email);
                                editor1.putString(Utils.mobile, FC_Common.mobile);
                                editor1.putString(Utils.email_verified_at, FC_Common.email_verified_at);
                                editor1.putString(Utils.dial_code, FC_Common.dial_code);
//                                editor1.putString(Utils.location_name, FC_Common.address_set);
//                                editor1.putString(Utils.location_type, FC_Common.location_type_set);
//                                editor1.putString(Utils.latitude, FC_Common.latitude_set);
//                                editor1.putString(Utils.longitude, FC_Common.longitude_set);
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


                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction =
                                        fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fl_fcDashboardFragment, homeFragment);
                                fragmentTransaction.commit();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("fdhbdfghdf", "dfhdf" + e);
                            //snackBar(String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String value = volleyError.toString();
                //snackBar(value);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_DashboardActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.d("ServerResponse", "chedk" + ServerResponse);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            //FC_Common.status = jsonResponse1.getString("success");
                            FC_Common.id = jsonResponse1.getString("id");
                            FC_Common.name = jsonResponse1.getString("name");
                            FC_Common.email = jsonResponse1.getString("email");
                            FC_Common.mobile = jsonResponse1.getString("mobile");
                            FC_Common.email_verified_at = jsonResponse1.getString("email_verified_at");
                            FC_Common.dial_code = jsonResponse1.getString("dial_code");
//                            FC_Common.location_name = jsonResponse1.getString("location_name");
//                            FC_Common.location_type = jsonResponse1.getString("location_type");
//                            FC_Common.latitude = jsonResponse1.getString("latitude");
//                            FC_Common.longitude = jsonResponse1.getString("longitude");
                            FC_Common.location_name = FC_Common.address_set;
                            FC_Common.location_type = FC_Common.location_type_set;
                            FC_Common.latitude = FC_Common.latitude_set;
                            FC_Common.longitude = FC_Common.longitude_set;
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
                            FC_Common.is_default = "0";
                            Log.d("fdhdfhfd","dfgfd"+FC_Common.is_default);

                            if (FC_Common.status.equalsIgnoreCase("1"))
                            {

                                String value="dgsdgsdgsd";

                               // snackBar(value);

                                FC_User user = new FC_User(jsonResponse1.getString("id"),
                                        jsonResponse1.getString("name"),
                                        jsonResponse1.getString("email"),
                                        jsonResponse1.getString("mobile"),
                                        jsonResponse1.getString("email_verified_at"),
                                        jsonResponse1.getString("dial_code"),
                                        FC_Common.address_set,
                                        FC_Common.location_type_set,
                                        FC_Common.latitude_set,
                                        FC_Common.longitude_set,
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
                                        FC_Common.is_default
                                );

                                FC_SharedPrefManager.getInstance(FC_DashboardActivity.this).userLogin(user);
                                SharedPreferences sharedPreferences = FC_DashboardActivity.this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //editor.putBoolean("FirstLogin", true);
                                editor.commit();
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_DashboardActivity.this);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                                editor1.putString(Utils.id, FC_Common.id);
                                editor1.putString(Utils.name, FC_Common.name);
                                editor1.putString(Utils.email, FC_Common.email);
                                editor1.putString(Utils.mobile, FC_Common.mobile);
                                editor1.putString(Utils.email_verified_at, FC_Common.email_verified_at);
                                editor1.putString(Utils.dial_code, FC_Common.dial_code);
                                editor1.putString(Utils.location_name, FC_Common.address_set);
                                editor1.putString(Utils.location_type, FC_Common.location_type_set);
                                editor1.putString(Utils.latitude, FC_Common.latitude_set);
                                editor1.putString(Utils.longitude, FC_Common.longitude_set);
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


//                                Intent intent = new Intent(FC_DashboardActivity.this, FC_DashboardActivity.class);
//                                startActivity(intent);
//                                finish();

                                try {
                                    Fragment homeFragment = new FC_HomeFragment();
                                    FragmentTransaction fragmentTransactionHome = fragmentManager.beginTransaction();
                                    fragmentTransactionHome.replace(R.id.fl_fcDashboardFragment, homeFragment);
                                    fragmentTransactionHome.commit();
                                }
                                catch (Exception ex){
                                    ex.printStackTrace();
                                }



                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("fdhbdfghdf", "dfhdf" + e);
                            //snackBar(String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String value = volleyError.toString();
                //snackBar(value);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_DashboardActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @Override
    public void onBackPressed() {

        if (backStack == 1) {
            Log.d("dfhdfgfd","dgfsdfsd");
            loadFragment();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            backStack = 0;
        } else {
            Log.d("dfhdfgfd","checkdgfsdfsd");
           // super.onBackPressed();
            androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_exit, null);
            TextView txt_message = dialogView.findViewById(R.id.txt_alertMessage);
            TextView txt_no =  dialogView.findViewById(R.id.txt_no);
            TextView txt_yes = dialogView.findViewById(R.id.txt_yes);
            dialogBuilder.setView(dialogView);

            final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            txt_message.setText("Are you sure want to \n exit from application");

            txt_no.setOnClickListener(v -> alertDialog.dismiss());

            txt_yes.setOnClickListener(v -> {

                finishAffinity();
                System.exit(0);
            });
            alertDialog.show();
        }

    }

    private void loadFragment() {

        img_home.setBackgroundResource(R.drawable.home_s);
        img_explore.setBackgroundResource(R.drawable.explore);
        img_cart.setBackgroundResource(R.drawable.cart);
        img_account.setBackgroundResource(R.drawable.account);

        txt_home.setTextColor(getResources().getColor(R.color.txt_site_red_color));
        txt_explore.setTextColor(getResources().getColor(R.color.colorgrey));
        txt_cart.setTextColor(getResources().getColor(R.color.colorgrey));
        txt_account.setTextColor(getResources().getColor(R.color.colorgrey));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fcDashboardFragment, homeFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onLogOut(int logout) {

        logOut = logout;
        FC_SharedPrefManager.deleteAllSharePrefs();// Deleting all shared preference data
        SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        boolean loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
        FC_Logout preferenceManager = new FC_Logout(getApplicationContext());
        preferenceManager.setFirstTimeLaunch(true);

        Intent intent = new Intent(FC_DashboardActivity.this, FC_Splash.class);
        startActivity(intent);
        finish();

    }
}
