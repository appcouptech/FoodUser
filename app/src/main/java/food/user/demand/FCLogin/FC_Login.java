package food.user.demand.FCLogin;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import food.user.demand.FCActivity.FCDashboard.FC_DashboardActivity;
import food.user.demand.FCActivity.FCNewUserAddressSet.FC_NewUserIntroActivity;
import food.user.demand.FCSplash.FC_Splash;
import food.user.demand.FCUtils.CountryPicker.CountryCodePicker;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.NetworkChangeReceiver;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_Login extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {

    public static final int RC_HINT = 1000;
    EditText txtotp,txt_countrywithnumber;
    AC_Textview txt_number;
    CredentialsClient mCredentialsClient;
    LinearLayout ll_noInternetConnection;
    View parentLayout;
    CountryCodePicker countrypicker;
    NetworkChangeReceiver networkChangeReceiver;
    Snackbar bar;
    TextView txtsignin,counttime,resendotp,txtotpverify;
    private LinearLayout ll_login ,ll_recentOtp ;
    ProgressBar progressbar;
    LinearLayout lin_country;
    IntentFilter intentFilter;
    String selected_country_code;
    InputMethodManager inputMgr;
    LinearLayout ll_loginMain ,ll_field;
    FC_User user;
    View view_otp ;
    Context context;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.fc_login);
        context=FC_Login.this;
        user = FC_SharedPrefManager.getInstance(FC_Login.this).getUser();
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
       // FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        lin_country = findViewById(R.id.lin_country);
        txtotp = findViewById(R.id.txtotp);
        txt_number = findViewById(R.id.txt_number);
        txtotpverify = findViewById(R.id.txtotpverify);
        view_otp= findViewById(R.id.view_otp);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    FC_Common.devicetoken = task.getResult().getToken();
                });

        @SuppressLint("HardwareIds")
        String token= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("sdgsdgsd","dfdf"+token);
        FC_Common.device_id = Settings.Secure.getString(FC_Login.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        txtsignin = findViewById(R.id.txtsignin);
        ll_recentOtp = findViewById(R.id.ll_recentOtp);
        ll_login = findViewById(R.id.ll_login);
        ll_loginMain = findViewById(R.id.ll_loginMain);
        ll_field = findViewById(R.id.ll_field);
        counttime=findViewById(R.id.counttime);
        txt_countrywithnumber=findViewById(R.id.txt_countrywithnumber);
        resendotp=findViewById(R.id.resendotp);
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ll_noInternetConnection = findViewById(R.id.ll_noInternetConnection);
        countrypicker = findViewById(R.id.countrypicker);
        selected_country_code = countrypicker.getDefaultCountryCode();
        Log.d("fghdgdfgdfg","dgsfgsdfs"+selected_country_code);

        countrypicker.setOnCountryChangeListener(() -> {
            //Alert.showMessage(RegistrationActivity.this, countrypicker.getSelectedCountryCodeWithPlus());
            selected_country_code= countrypicker.getSelectedCountryCode();
            Log.d("dfgdfgdfg","dfgdf"+selected_country_code);
        });
        progressbar = findViewById(R.id.progressbar);

       Log.d("dfgdfgfd","fgdfgfdg");
        boolean isConnected = NetworkChangeReceiver.isConnected(this);
        isOnline(isConnected);

        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();

        if (bundle != null)
        {
            FC_Common.gpsenabled = (String) bundle.get("gpsenabled");
        }

        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        parentLayout = findViewById(android.R.id.content);
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();
        mCredentialsClient = Credentials.getClient(this);
        PendingIntent intent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
        //requestStoragePermission();
        txt_number.setOnClickListener(v -> {
            txt_number.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);
            //resendotp.setVisibility(View.GONE);
            txtotp.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            lin_country.setVisibility(View.VISIBLE);
            countrypicker.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.VISIBLE);
            //txtsignin.setVisibility(View.VISIBLE);
        });
        ll_login.setOnClickListener(v -> {
            inputMgr.hideSoftInputFromWindow(ll_login.getWindowToken(), 0);
           // lin_country.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);
            //String test = countrypicker.getDefaultCountryCode();
            //Log.d("fghdgdfgdfg","dgsfgsdfs"+test);
            Log.d("vxdfdfsd", "24343passss");
            if (txt_number.getVisibility() == View.VISIBLE)
            {
                Log.d("vxdfdfsd", "passss");

                if (txt_number.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";

                    snackBar(value);
                }
                else
                {
                    FC_Common.mobilenumber=txt_number.getText().toString();
                    loginExecute();
                }

                //maxID = 0;
            }
            else if (txt_countrywithnumber.getVisibility()== View.VISIBLE) {
                Log.d("vxdfdfsd", "fail");
                if (txt_countrywithnumber.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    snackBar(value);
                }
                else
                {

                    FC_Common.mobilenumber="+"+selected_country_code+" "+txt_countrywithnumber.getText().toString();
                    loginExecute();
                }
            }
            else {
                Log.d("vxdfdfsd", "dfghdghfail");
            }


        });

        ll_loginMain.setOnClickListener(view -> {
            inputMgr.hideSoftInputFromWindow(ll_loginMain.getWindowToken(), 0);
        });

        ll_field.setOnClickListener(view -> {
            inputMgr.hideSoftInputFromWindow(ll_field.getWindowToken(), 0);
        });

        ll_recentOtp.setOnClickListener(v -> {
            txtotp.setText("");
            txtotp.setHint(getResources().getString(R.string.otp));
            inputMgr.hideSoftInputFromWindow(ll_recentOtp.getWindowToken(), 0);
           // lin_country.setVisibility(View.GONE);
            counttime.setVisibility(View.GONE);
            ll_recentOtp.setVisibility(View.GONE);

            Log.d("vxdfdfsd", "24343passss");
            if (txt_number.getVisibility() == View.VISIBLE)
            {
                Log.d("vxdfdfsd", "passss");

                if (txt_number.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    ll_recentOtp.setVisibility(View.VISIBLE);
                    snackBar(value);
                }
                else
                {
                    FC_Common.mobilenumber=txt_number.getText().toString();
                    loginExecute();
                }

                //maxID = 0;
            }
            else if (txt_countrywithnumber.getVisibility()== View.VISIBLE) {
                Log.d("vxdfdfsd", "fail");
                if (txt_countrywithnumber.getText().toString().equalsIgnoreCase("")) {
                    counttime.setVisibility(View.GONE);
                    String value="Phone Number cannot Be Empty";
                    ll_recentOtp.setVisibility(View.VISIBLE);
                    snackBar(value);
                }
                else
                {
                    FC_Common.mobilenumber="+"+selected_country_code+" "+txt_countrywithnumber.getText().toString();
                    loginExecute();


                }
            }
            else {
                Log.d("vxdfdfsd", "dfghdghfail");
            }

        });
        txtotp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("dgddsfd","gsdgsdf"+FC_Common.otplength);
                if(txtotp.getText().toString().length()>=FC_Common.otplength)     //size as per your requirement
                {
                    inputMgr.hideSoftInputFromWindow(txtotp.getWindowToken(), 0);
                    FC_Common.otpcheck = txtotp.getText().toString().trim();
                    if (FC_Common.otpcheck.equalsIgnoreCase(FC_Common.otp))
                    {
                        inputMgr.hideSoftInputFromWindow(txtotp.getWindowToken(), 0);
                        Log.d("fgghfghfghfg","dgdfgdfgfdg"+FC_Common.user_type);
                        Log.d("fgghfghfghfg","dgdfgdfgfdg"+FC_Common.mobilenumber);


                      if (FC_Common.user_type.equalsIgnoreCase("new_user")) {
                          Intent intent=new Intent(FC_Login.this, FC_NewUserIntroActivity.class);
                         // Intent intent=new Intent(FC_Login.this, FC_IntroProfileActivity.class);
                          Log.d("dfgfdgfdg","dfgfdgfd");
                          intent.putExtra("mobilenumber", FC_Common.mobilenumber);
                          intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                          startActivity(intent);
                          //Log.d("dfgfdgfdg","dfgfdgfd");

                      }
                      else
                      {
                          loginCheck();
                      }



                    }
                    else
                    {
                        String value = "OTP Mismatches or Wrong OTp";
                        snackBar(value);
                        Log.d("fgghfghfghfg","32423421dgdfgdfgfdg");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void loginExecute() {
        Utils.playProgressBar(FC_Login.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOGINOTP,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.message = jsonResponse1.getString("message");
                        FC_Common.user_type = jsonResponse1.getString("user_type");
                        FC_Common.otp = jsonResponse1.getString("otp");
                        FC_Common.otplength=FC_Common.otp.length();
                        if (FC_Common.status.equalsIgnoreCase("1")) {


                            Utils.stopProgressBar();
                            counttime.setVisibility(View.VISIBLE);
                            txtotp.setVisibility(View.VISIBLE);
                            view_otp.setVisibility(View.VISIBLE);
                            ll_login.setVisibility(View.GONE);
                           // txtotpverify.setVisibility(View.VISIBLE);
                            if (txt_number.getVisibility() == View.VISIBLE)
                            {
                                lin_country.setVisibility(View.GONE);
                                txt_number.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lin_country.setVisibility(View.VISIBLE);
                                txt_number.setVisibility(View.GONE);
                            }

                            new CountDownTimer(10000, 1000) {

                                @SuppressLint("SetTextI18n")
                                public void onTick(long millisUntilFinished) {
                                    counttime.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                                    if (txt_number.getVisibility() == View.VISIBLE)
                                    {
                                        lin_country.setVisibility(View.GONE);
                                        txt_number.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        lin_country.setVisibility(View.VISIBLE);
                                        txt_number.setVisibility(View.GONE);
                                    }
                                }

                                public void onFinish() {

                                    //txtotpverify.setVisibility(View.GONE);
                                    counttime.setVisibility(View.GONE);
                                    ll_login.setVisibility(View.GONE);
                                    ll_recentOtp.setVisibility(View.VISIBLE);
                                    txtotp.setVisibility(View.INVISIBLE);
                                    view_otp.setVisibility(View.INVISIBLE);
                                    if (txt_number.getVisibility() == View.VISIBLE)
                                    {
                                        lin_country.setVisibility(View.GONE);
                                        txt_number.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        lin_country.setVisibility(View.VISIBLE);
                                        txt_number.setVisibility(View.GONE);
                                    }

                                }
                            }.start();
                        } else {
                            Utils.stopProgressBar();
                            progressbar.setVisibility(View.GONE);
                            snackBar(FC_Common.message);
                            counttime.setVisibility(View.GONE);
                            if (txt_number.getVisibility() == View.VISIBLE)
                            {
                                lin_country.setVisibility(View.GONE);
                                txt_number.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                lin_country.setVisibility(View.VISIBLE);
                                txt_number.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        snackBar(getResources().getString(R.string.reach));
                        counttime.setVisibility(View.GONE);
                        if (txt_number.getVisibility() == View.VISIBLE)
                        {
                            lin_country.setVisibility(View.GONE);
                            txt_number.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            lin_country.setVisibility(View.VISIBLE);
                            txt_number.setVisibility(View.GONE);
                        }
                    }
                }, volleyError -> {
            Utils.stopProgressBar();
            snackBar(getResources().getString(R.string.reach));
                    counttime.setVisibility(View.GONE);
                    if (txt_number.getVisibility() == View.VISIBLE)
                    {
                        lin_country.setVisibility(View.GONE);
                        txt_number.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lin_country.setVisibility(View.VISIBLE);
                        txt_number.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("fgjfghfghfg","dfhgfhfghf"+FC_Common.mobilenumber);
                params.put("mobile", FC_Common.mobilenumber);
                params.put("device_token", FC_Common.devicetoken);
                params.put("device_id", FC_Common.device_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_Login.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void loginCheck() {
        Utils.playProgressBar(FC_Login.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOGIN,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashlogin" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.token_type = jsonResponse1.getString("token_type");
                        FC_Common.access_token = jsonResponse1.getString("access_token");

                        if (FC_Common.status.equalsIgnoreCase("1")) {
                           // Utils.stopProgressBar();
                            GetUserDetails();

                        } else {
                            Utils.stopProgressBar();
                            progressbar.setVisibility(View.GONE);
                            snackBar(FC_Common.message);
                        }
                    } catch (JSONException e) {
                        Utils.stopProgressBar();
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                    }
                }, volleyError -> {
            Utils.stopProgressBar();
            snackBar(getResources().getString(R.string.reach));
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("fgjfghfghfg","dfhgfhfghf"+FC_Common.mobilenumber);
                params.put("mobile", FC_Common.mobilenumber);
                params.put("otp", FC_Common.otp);
                params.put("device_token", FC_Common.devicetoken);
                params.put("device_id", FC_Common.device_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_Login.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT) {
            if (data != null) {
                //Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                Log.d("fdgdfgdfg","sdgsd"+cred);
                if (cred != null) {
                    final String unformattedPhone = cred.getId();
                    String formattedPhone;
                    String code = Locale.getDefault().getCountry();
                    Log.d("fdgdfgdfg","fdgdfgd : "+code+"-"+unformattedPhone);
                    formattedPhone = PhoneNumberUtils.formatNumber(unformattedPhone,Locale.getDefault().getCountry());
                   // formattedPhone = PhoneNumberUtils.formatNumber(unformattedPhone);
                    txt_number.setText("" +formattedPhone);

                    Log.d("dghdfhdfh","13223fdhdfghfd");
                    txt_number.setVisibility(View.VISIBLE);
                    countrypicker.setVisibility(View.GONE);
                    lin_country.setVisibility(View.GONE);

                }
                else
                {
                    Log.d("dghdfhdfh","fdhdfghfd");
                    txt_number.setVisibility(View.GONE);
                    countrypicker.setVisibility(View.VISIBLE);
                    lin_country.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void isOnline(boolean isConnected) {

        if (!isConnected) {

            Log.d("dffhdfhfd","dfgdfgdfg");
            ll_noInternetConnection.setVisibility(View.VISIBLE);
            Utils.noInternetConnection(ll_noInternetConnection, FC_Login.this);
          //  progressbar.setVisibility(View.VISIBLE);
        } else {
            Log.d("dffhdfhfd","vnvcn");
            Utils.removeInternetConnection(ll_noInternetConnection);
            ll_noInternetConnection.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
        }
    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(parentLayout, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
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

        Utils.showExitDialog(FC_Login.this, "Are you sure want to \n exit from application");
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
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline(isConnected);
    }
    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                ServerResponse -> {

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
                        Log.d("fdhdfhfd","dfgfd"+FC_Common.id);

                        if (FC_Common.status.equalsIgnoreCase("1"))
                        {
                           /* String value="dgsdgsdgsd";

                            snackBar(value);*/

                            Utils.stopProgressBar();
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

                            FC_SharedPrefManager.getInstance(FC_Login.this).userLogin(user);
                            SharedPreferences sharedPreferences = FC_Login.this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FirstLogin", true);
                            editor.apply();
                            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_Login.this);
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
                            txtotp.setText("");
                            txtotp.setHint(getResources().getString(R.string.otp));
                            if (FC_Common.user_type.equalsIgnoreCase("new_user"))
                            {

                                Intent intent=new Intent(FC_Login.this, FC_NewUserIntroActivity.class);
                                Log.d("gpsenabled","dfgfdgfd"+FC_Common.gpsenabled);
                                intent.putExtra("mobilenumber", FC_Common.mobilenumber);
                                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                startActivity(intent);
                            }
                            else if(FC_Common.user_type.equalsIgnoreCase("existing_user"))
                            {
                                Intent intent=new Intent(FC_Login.this, FC_DashboardActivity.class);
                                intent.putExtra("location_name", FC_Common.location_name);
                                intent.putExtra("location_type", FC_Common.location_type);
                                intent.putExtra("latitude", FC_Common.latitude);
                                intent.putExtra("longitude", FC_Common.longitude);
                                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                startActivity(intent);
                            }
                        }



                    } catch (JSONException e) {
                        Utils.stopProgressBar();
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                    }
                }, volleyError -> {
            Utils.stopProgressBar();
            snackBar(getResources().getString(R.string.reach));
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_Login.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

}
