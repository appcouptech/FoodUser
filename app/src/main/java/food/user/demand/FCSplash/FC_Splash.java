package food.user.demand.FCSplash;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCActivity.FCDashboard.FC_DashboardActivity;
import food.user.demand.FCActivity.FCIntroScreenActivity.FC_IntroScreen;
import food.user.demand.FCActivity.FCProfile.FC_IntroAddressActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCUtils.FC_Logout;
import food.user.demand.FCViews.AC_BoldTextview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.NetworkChangeReceiver;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_Splash extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {
    Boolean loginCheck;
    String versionName="";
    int versionCode;
    TextView nonet;
    ImageView imgnonet;
    RemoteMessage remoteMessage;
    LinearLayout linnonet ,ll_noInternetConnection;
    NetworkChangeReceiver networkChangeReceiver ;
    IntentFilter intentFilter ;
    FC_User user;
    FrameLayout frame_main;
    AC_BoldTextview txt_poweredBy ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_Splash.this,getResources().getConfiguration());
        setContentView(R.layout.fc_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nonet=findViewById(R.id.nonet);
        ll_noInternetConnection =  findViewById(R.id.ll_noInternetConnection);
        linnonet = findViewById(R.id.linnonet);
        frame_main = findViewById(R.id.frame_main);
        imgnonet = findViewById(R.id.imgnonet);
        txt_poweredBy = findViewById(R.id.txt_poweredBy);
        /*Bundle b = getIntent().getExtras();
        assert b != null;
        String someData = b.getString("order_id");
        Log.d("fghdfgfd","dfgfdg"+someData);*/
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        user = FC_SharedPrefManager.getInstance(FC_Splash.this).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.location_name = String.valueOf(user.getlocation_name());
        FC_Common.location_type = String.valueOf(user.getlocation_type());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.gender = String.valueOf(user.getgender());
        FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());


        boolean isConnected = NetworkChangeReceiver.isConnected(this);
        isOnline(isConnected);

        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            Log.d("dfgsdfg","fdgfd"+json);
           // handleDataMessage(json);
        } catch (Exception e) {
            Log.e("dfgsdfg", "zxgbzxvczxc: " + e.getMessage());
           // Log.d("dfgsdfg","fdgfd"+json);
        }


       /*  if (getIntent().getExtras() != null) {
            String someData = getIntent().getExtras().getString("order_id");
            Log.d("fghdfgdg","fdghdfgfd"+someData);
            assert someData != null;

            if (FC_Common.order_id.equalsIgnoreCase("null")){
                Log.d("fghdfgdg","fdghdfgfd"+someData);
            }
            else
            {
                FC_Common.order_id=someData;
                Intent intent = new Intent(FC_Splash.this, FC_OrderPickedUpActivity.class);
                intent.putExtra("order_id",FC_Common.order_id);
                startActivity(intent);
            }

           for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MainActivity: ", "Key: " + key + " Value: " + value);
            }
        }*/

        Log.d("dtgsdgsdgsdf","dfgfdg"+FC_Common.token_type);
        Log.d("dtgsdgsdgsdf","dsafsdffgfdg"+FC_Common.access_token);
        Log.d("dtgsdgsdgsdf","dsafsdffgfdg"+FC_Common.latitude);
        Log.d("dtgsdgsdgsdf","dsafsdffgfdg"+FC_Common.longitude);

        LocationManager lm = (LocationManager)FC_Splash.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps_enabled){
                Log.d("Dfhdfgfdgfdg","dfgfdgfdgdf");
                FC_Common.gpsenabled="false";
            }
            else
            {
                Log.d("Dfhdfgfdgfdg","24435dfgfdgfdgdf");
                FC_Common.gpsenabled="true";
            }
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {}
    }







    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(networkChangeReceiver, intentFilter);
        networkChangeReceiver.setConnectivityReceiverListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline(isConnected);
        Log.d("connection Splash","True");
    }



    private void isOnline(boolean isConnected) {

        if (isConnected){

            frame_main.setVisibility(View.VISIBLE);
            ll_noInternetConnection.setVisibility(View.GONE);
            try {
                String someData = Objects.requireNonNull(getIntent().getExtras()).getString("order_id");
                Log.d("fghdfgdg","fdghdfgfd"+someData.length());
                assert someData != null;
                if (someData.equalsIgnoreCase("")||someData.equalsIgnoreCase("null"))
                {
                    Log.d("fghdfgdg","dgxcv"+someData);
                    AccessCheck();
                }
                else {
                    Log.d("fghdfgdg","fdgxcbxcvczxhdfgfd"+someData);
                    FC_Common.order_id=someData;
                    Intent intent = new Intent(FC_Splash.this, FC_OrderPickedUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("order_id",FC_Common.order_id);
                    startActivity(intent);
                }
            }
            catch (RuntimeException ex)
            {
                ex.printStackTrace();
                AccessCheck();
                Log.d("fdgsdgsd","fdgdf"+ex);
            }


        }
        else {
            Log.d("dfhfdgfdg","fghfgh");
            frame_main.setVisibility(View.GONE);
            ll_noInternetConnection.setVisibility(View.VISIBLE);
            Utils.noInternetConnection(ll_noInternetConnection,FC_Splash.this);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccessCheck() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_TOKENVALIDATE,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");

                        if (FC_Common.status.equalsIgnoreCase("1")){
                            Utils.removeInternetConnection(ll_noInternetConnection);
                            ll_noInternetConnection.setVisibility(View.GONE);

                            Thread timerThread = new Thread() {
                                @SuppressLint("HardwareIds")
                                public void run() {
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    try {

                                        @SuppressLint("HardwareIds")
                                        String token= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                        Log.d("sdgsdgsd","dfdf"+token);
                                        FC_Common.device_id = Settings.Secure.getString(FC_Splash.this.getContentResolver(),
                                                Settings.Secure.ANDROID_ID);


                                        Log.d("xgdfgdfgfd","device_id: "+FC_Common.device_id);

                                        Utils.log(FC_Splash.this,"dghdfgdf"+"sdfsdfgsd");
                                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                        versionName = packageInfo.versionName;
                                        versionCode = packageInfo.versionCode;
                                        SharedPreferences sharedPreferences1 = FC_Splash.this.getSharedPreferences("MyLanguage.txt", Context.MODE_PRIVATE);
                                        Log.d("dfgdfgfd","1231dfgdfgdfg"+FC_Common.token_type);
                                        Log.d("dfgdfgfd","dfgdfgdfg"+FC_Common.access_token);
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                        loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
                                        Utils.log(FC_Splash.this ," loginCheck: " + loginCheck );

                                        if (loginCheck) {

                                            if (FC_Common.token_type.equalsIgnoreCase("")||FC_Common.access_token.equalsIgnoreCase(""))
                                            {
                                                Log.d("sdgsdgsdgsd","grtdfgb");
                                               // Intent intent = new Intent(FC_Splash.this, FC_Login.class);
                                                Intent intent = new Intent(FC_Splash.this, FC_IntroScreen.class);
                                                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                if (FC_Common.latitude.equalsIgnoreCase("")||FC_Common.longitude.equalsIgnoreCase("")||
                                                        FC_Common.latitude.equalsIgnoreCase("null")||FC_Common.longitude.equalsIgnoreCase("null")) {
                                                    Log.d("sdgsdgsdgsd", "46nbv");
                                                    Intent intent = new Intent(FC_Splash.this, FC_IntroAddressActivity.class);
                                                    intent.putExtra("username", FC_Common.name);
                                                    intent.putExtra("usermobile", FC_Common.mobile);
                                                    intent.putExtra("useremail", FC_Common.email);
                                                    intent.putExtra("usergender", FC_Common.gender);
                                                    intent.putExtra("deviceid", FC_Common.device_id);
                                                    intent.putExtra("token_type", FC_Common.token_type);
                                                    intent.putExtra("access_token", FC_Common.access_token);
                                                    intent.putExtra("latitude", FC_Common.latitude);
                                                    intent.putExtra("longitude", FC_Common.longitude);
                                                    intent.putExtra("change_address", "splash");
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Log.d("sdgsdgsdgsd", "46nbv");
                                                    Intent intent = new Intent(FC_Splash.this, FC_DashboardActivity.class);
                                                    intent.putExtra("location_name", FC_Common.location_name);
                                                    intent.putExtra("location_type", FC_Common.location_type);
                                                    intent.putExtra("latitude", FC_Common.latitude);
                                                    intent.putExtra("longitude", FC_Common.longitude);
                                                    intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                                    startActivity(intent);
                                                }
                                            }


                                            Log.d("sdgsdgsdgsd","1243sdfsdfsdfsd");
                                        }
                                        else {
                                           // Intent intent = new Intent(FC_Splash.this, FC_Login.class);
                                            Intent intent = new Intent(FC_Splash.this, FC_IntroScreen.class);
                                            startActivity(intent);
                                            Log.d("sdgsdgsdgsd","sdfsdfsdfsd");
                                        }
                                    } catch (PackageManager.NameNotFoundException e1) {
                                        e1.printStackTrace();
                                        Log.d("dgdssdvsd","sdfsd"+e1);
                                    } }};timerThread.start();
                        }
                        else {
                            FC_SharedPrefManager.deleteAllSharePrefs();// Deleting all shared preference data
                            SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            sharedPreferences.edit().clear().apply();
                            boolean loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
                            FC_Logout preferenceManager = new FC_Logout(getApplicationContext());
                            preferenceManager.setFirstTimeLaunch(true);

                           // Intent intent = new Intent(FC_Splash.this, FC_Login.class);
                            Intent intent = new Intent(FC_Splash.this, FC_IntroScreen.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Log.d("dfgfdgfd","dfgsdfd"+value);
            Utils.toast(FC_Splash.this,"Something Went Wrong");
        /*    Intent intent = new Intent(FC_Splash.this, FC_DashboardActivity.class);
            intent.putExtra("location_name", FC_Common.location_name);
            intent.putExtra("location_type", FC_Common.location_type);
            intent.putExtra("latitude", FC_Common.latitude);
            intent.putExtra("longitude", FC_Common.longitude);
            intent.putExtra("gpsenabled", FC_Common.gpsenabled);
            startActivity(intent);*/

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FC_Common.token_type + " " + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                params.put("X-Requested-With", FC_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FC_Splash.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}
