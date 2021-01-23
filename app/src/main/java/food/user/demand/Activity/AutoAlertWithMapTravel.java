package food.user.demand.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;


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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class AutoAlertWithMapTravel extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    Location location; // location
    double latitude; // latitude
    double longitude;
    Marker mCurrLocationMarker , destinationMarker ;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    ArrayList points = new ArrayList();
    private Handler handler;
    private Runnable runnable;

    double CURRENT_latitude, CURRENT_longitude, CLICK_latitude = 11.081558656564054, CLICK_longitude = 76.98960117995739;
    private GoogleSignInClient mGoogleSignInClient;
    List<Location> locationList = new ArrayList<>();
    private int userZoomLevel = 0;
    private float zoom;
    private LatLngBounds bounds;
    LatLng latLng;
    private Polyline polyline ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(AutoAlertWithMapTravel.this,getResources().getConfiguration());
        setContentView(R.layout.activity_auto_alert_with_map_travel);

        handler = new Handler();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(AutoAlertWithMapTravel.this);


        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) AutoAlertWithMapTravel.this.getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(AutoAlertWithMapTravel.this)) {
           // Toast.makeText(AutoAlertWithMapTravel.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(AutoAlertWithMapTravel.this)) {
          //  Toast.makeText(AutoAlertWithMapTravel.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(AutoAlertWithMapTravel.this)) {
            Log.e("TAG", "Gps already enabled");
         //   Toast.makeText(AutoAlertWithMapTravel.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
          //  Toast.makeText(AutoAlertWithMapTravel.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null) {
            return false;
        }
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

  /*      if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(AutoAlertWithMapTravel.this)
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

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
        }*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      /*  locationRequest.setInterval(30 * 1000);
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
                        resolvable.startResolutionForResult(AutoAlertWithMapTravel.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);

        mLocationRequest = new LocationRequest();
       /* mLocationRequest.setInterval(12000); // 1200000 two minute interval
        mLocationRequest.setFastestInterval(12000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                // mGoogleMap.setMyLocationEnabled(true);

              /*  runnable = new Runnable() {
                    public void run() {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                        handler.postDelayed(this, 16000); //now every 16 seconds its run the volley request continuously when handler stop runnable
                    }
                };
                handler.post(runnable);*/

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            //mGoogleMap.setMyLocationEnabled(true);
        /*    runnable = new Runnable() {
                public void run() {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                    handler.postDelayed(this, 16000); //now every 16 seconds its run the volley request continuously when handler stop runnable
                }
            };
            handler.post(runnable);*/
        }

     /*   mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
               *//* allPoints.add(point);
                map.clear();*//*

                // mGoogleMap.addMarker(new MarkerOptions().position(point));

                int height = 80;
                int width = 80;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.to_location);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                markerOptions.title("Deliver Location");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                Log.d("location ", "point " + "latitude : " + point.latitude + " , longitude :" + point.longitude);
                CLICK_latitude = point.latitude;
                CLICK_longitude = point.longitude;

                String url = getMapsApiDirectionsUrl();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
            }
        });*/


    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d("location ", "locationResult :" + locationResult);
            locationList.clear();
            locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.d("location ", "Location: " + location.getLatitude() + " " + location.getLongitude());
               // Toast.makeText(getApplicationContext(), "" + "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                    Log.d("zoomLevel", "marker remove");
                }

                //Place current location marker
                latitude = location.getLatitude();
                longitude = location.getLongitude();

               /* CURRENT_latitude = latitude;
                CURRENT_longitude = longitude;*/

                latLng = new LatLng(latitude, longitude);
                Log.d("zoom", " latLng: " + latLng);

                int height = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
                int width = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
               /* BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_driver_marker);
                Bitmap b = bitmapdraw.getBitmap();*/
                Bitmap b =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_driver_marker));
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Location");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                //move map camera
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(latLng)
                        .zoom(17)
                        .build();

                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                if (bounds != null) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                }
                animateMarker(latLng,false);
               /* if (userZoomLevel == 0) {
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    //move map camera
                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(latLng)
                            .zoom(17)
                            .build();

                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                    // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                } else {
                    //move map camera
                    Log.d("zoomLevel", " zoom else: " + zoom);
                    // mGoogleMap.setOnMapLoadedCallback(() -> mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0)));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                    // animateMarker(latLng,true);

                    // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
                }

*/

               DestinationMarkerPlace();

                //  mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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

    private void DestinationMarkerPlace() {

        if (destinationMarker != null){
            destinationMarker.remove();
        }

        int height1 = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
        int width1 = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
     /*   BitmapDrawable bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.to_location);
        Bitmap b1 = bitmapdraw1.getBitmap();*/
        Bitmap b1 =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_customer_marker));
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, width1, height1, false);

        LatLng point = new LatLng(CLICK_latitude, CLICK_longitude);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(point);
        markerOptions1.title("Deliver Location");
        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1));
        destinationMarker = mGoogleMap.addMarker(markerOptions1);

        if (polyline != null){
            polyline.remove();
        }

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        mGoogleMap.setOnCameraIdleListener(() -> {
            zoom = mGoogleMap.getCameraPosition().zoom;
            bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
            Log.d("zoomLevel", " zoom bounds: " + bounds);
            Log.d("zoomLevel", " zoom float: " + zoom);

            //use zoomLevel value..
            userZoomLevel = Math.round(zoom);

        });
    }

    private String getMapsApiDirectionsUrl() {

        // Origin of route
        String str_origin = "origin=" + latitude + "," + longitude;

        // Destination of route
        String str_dest = "destination=" + CLICK_latitude + "," + CLICK_longitude;


        // Sensor enabled
        String sensor = "sensor=true";
        String mode = "mode=driving";

        //Key
        String key = "key=" + getResources().getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.d("location", "gdgdfgs" + url);

        return url;
    }

    public void animateMarker(final LatLng toPosition,final boolean hideMarke) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(mCurrLocationMarker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                mCurrLocationMarker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarke) {
                        mCurrLocationMarker.setVisible(false);
                    } else {
                        mCurrLocationMarker.setVisible(true);
                    }
                }
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
                                ActivityCompat.requestPermissions(AutoAlertWithMapTravel.this,
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
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        //  mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            routes.clear();

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            points.clear();
            PolylineOptions polyLineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = new ArrayList<>();
            path.clear();

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {


                path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLACK);
            }

            if (polyLineOptions != null) {
               polyline = mGoogleMap.addPolyline(polyLineOptions);
            } else {
                Toast.makeText(AutoAlertWithMapTravel.this, "Manage API Account", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

