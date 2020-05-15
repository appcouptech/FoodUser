package food.user.demand.FCActivity.FCNewUserAddressSet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCLogin.FC_Login;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.PermissionUtils;
import food.user.demand.FCViews.UserDetailsData;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_NewUserIntroActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout ll_mainLayout ;
    InputMethodManager inputMgr;
    TextView txt_saveBtn,edt_mobile ;
    ImageView img_profile;
    CircleImageView img_male,img_female;
    SharedPreferences sharedPreferences;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Boolean loginCheck;
    private Uri selectedImageUri;
    ArrayList<UserDetailsData> UserDetailsData = new ArrayList<>();
    Snackbar bar;
    EditText edt_name,edt_email;
    AC_Edittext edt_referral;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private int selected = 0;
    View parentLayout;
    FC_User user;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_intro_profile);


      /*  if (!PermissionUtils.checkAndRequestPermission(FC_NewUserIntroActivity.this, REQUEST_CODE_ASK_PERMISSIONS, "You need to grant access to Write Storage", permission[0]))
            return;*/

        FC_Common.usergender="";
        user = FC_SharedPrefManager.getInstance(FC_NewUserIntroActivity.this).getUser();
        Log.d("gsdgsdggdfg","dsfs"+FC_Common.device_id);
        FC_Common.device_id = Settings.Secure.getString(FC_NewUserIntroActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("gsdgsdggdfg","cvnfgdsfs"+FC_Common.device_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.app_name);
            String channelName = getString(R.string.app_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_LOW));
            }
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            FC_Common.usermobile = (String) bundle.get("mobilenumber");
            FC_Common.gpsenabled = (String) bundle.get("gpsenabled");

        }

        Log.d("dfhdfgfd","dfgdfg"+FC_Common.usermobile);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                //Log.d(TAG, "Key: " + key + " Value: " + value);
            }

            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.app_name);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.app_name);
                            }
                            // Log.d(TAG, msg);
                            //Toast.makeText(FC_NewUserIntroActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FindviewById();
        //edt_mobile.setEnabled(false);
        edt_mobile.setText(FC_Common.usermobile);
        edt_mobile.setTextColor(getResources().getColor(R.color.black));



        requestStoragePermission();

        if (edt_name.getText().toString().trim().equalsIgnoreCase("")&&
                edt_email.getText().toString().trim().equalsIgnoreCase("")) {
            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
        }
        else {
            txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
        }

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_name.getText().toString().trim().equalsIgnoreCase("")||
                        edt_email.getText().toString().trim().equalsIgnoreCase("")) {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                }
                else if (!edt_email.getText().toString().trim().contains("@") || !edt_email.getText().toString().trim().contains(".com")) {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                    //snackBar(value);
                }
                else {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                }

            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_name.getText().toString().trim().equalsIgnoreCase("")||
                        edt_email.getText().toString().trim().equalsIgnoreCase("")) {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                }

                else if (!edt_email.getText().toString().trim().contains("@") || !edt_email.getText().toString().trim().contains(".com")) {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_change_effect_grey));
                    //snackBar(value);
                }
                else {
                    txt_saveBtn.setBackground(getResources().getDrawable(R.drawable.ripple_button_effect_red));
                }

            }
        });
    }




    private void FindviewById() {
        ll_mainLayout = findViewById(R.id.ll_mainLayout);
        txt_saveBtn = findViewById(R.id.txt_saveBtn);
        img_profile = findViewById(R.id.img_profile);
        img_male = findViewById(R.id.img_male);
        img_female = findViewById(R.id.img_female);
        edt_name = findViewById(R.id.edt_name);
        edt_referral = findViewById(R.id.edt_referral);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_email = findViewById(R.id.edt_email);
        parentLayout = findViewById(android.R.id.content);

        ll_mainLayout.setOnClickListener(this);
        txt_saveBtn.setOnClickListener(this);
        img_female.setOnClickListener(this);
        img_male.setOnClickListener(this);
        img_profile.setOnClickListener(this);

    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_mainLayout:
                inputMgr.hideSoftInputFromWindow(ll_mainLayout.getWindowToken(), 0);
                break;


            case R.id.txt_saveBtn:

                Log.d("sdfsdfsdf","sdfsdf"+FC_Common.usergender);
                FC_Common.updateedtemail=edt_email.getText().toString();

                if (edt_name.getText().toString().trim().equalsIgnoreCase("")) {
                    String value="Name cannot be empty";
                    snackBar(value);
                }
                else  if (edt_mobile.getText().toString().trim().equalsIgnoreCase("")) {
                    String value="Mobile Number cannot be empty";
                    snackBar(value);
                }
                else if (!FC_Common.updateedtemail.contains("@") || !FC_Common.updateedtemail.contains(".com")) {
                    String value="Invalid Email";
                    snackBar(value);
                    //snackBar(value);
                }
                else  if (FC_Common.usergender.equalsIgnoreCase("")) {
                    String value="Choose Gender";
                    snackBar(value);
                }
                else {
                    Log.d("sdfsdfsdf","gdfgdfgdfgdfgfd");
                    FC_Common.username=edt_name.getText().toString().trim();
                    FC_Common.usermobile=edt_mobile.getText().toString().trim();
                    FC_Common.useremail=edt_email.getText().toString().trim();
                    FC_Common.refcode=edt_referral.getText().toString().trim();
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    return;
                                }

                                txt_saveBtn.setEnabled(false);
                                // Get new Instance ID token
                                FC_Common.devicetoken = task.getResult().getToken();
                                Log.d("sdgsdgsdgsd",""+FC_Common.devicetoken);
                                // Log and toast
                                @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
                                String msg = getString(R.string.app_name, FC_Common.devicetoken);


Utils.playProgressBar(FC_NewUserIntroActivity.this);

                              Register();
                               // new savefunc(FC_IntroProfileActivity.this).execute();
                                Log.d("dfgdfgfd","FC_Common.username"+FC_Common.username);
                                Log.d("dfgdfgfd","FC_Common.usermobile"+FC_Common.usermobile);
                                Log.d("dfgdfgfd","FC_Common.devicetype"+FC_Common.devicetype);
                                Log.d("dfgdfgfd","FC_Common.devicetoken"+FC_Common.devicetoken);
                                Log.d("dfgdfgfd","FC_Common.device_id"+FC_Common.device_id);
                                Log.d("dfgdfgfd","FC_Common.useremail"+FC_Common.useremail);
                            });
                }
                break;
            case R.id.img_profile:
                startDialog();
                break;
            case R.id.img_male:
                Log.d("dfhfddfgdf","sdgfsdfsd");
                //GetUserDetails();
                img_male.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                img_female.setBackgroundColor(getResources().getColor(R.color.white));
                FC_Common.usergender="Male";
                break;
            case R.id.img_female:
                img_female.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                img_male.setBackgroundColor(getResources().getColor(R.color.white));
                FC_Common.usergender="Female";
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
                            Log.d("fdhdfhfd","dfgfd"+FC_Common.id);

                            if (FC_Common.status.equalsIgnoreCase("1"))
                            {
                                String value="dgsdgsdgsd";

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

                                FC_SharedPrefManager.getInstance(FC_NewUserIntroActivity.this).userLogin(user);
                                SharedPreferences sharedPreferences = FC_NewUserIntroActivity.this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("FirstLogin", true);
                                editor.commit();
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(FC_NewUserIntroActivity.this);
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
                                Intent intent = new Intent(FC_NewUserIntroActivity.this, FC_NewUserAddressActivity.class);
                                intent.putExtra("username", FC_Common.name);
                                intent.putExtra("usermobile", FC_Common.mobile);
                                intent.putExtra("useremail", FC_Common.email);
                                intent.putExtra("usergender", FC_Common.gender);
                                intent.putExtra("deviceid", FC_Common.device_id);
                                intent.putExtra("token_type", FC_Common.token_type);
                                intent.putExtra("access_token", FC_Common.access_token);
                                intent.putExtra("latitude", FC_Common.latitude);
                                intent.putExtra("longitude", FC_Common.longitude);
                                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                intent.putExtra("change_address", "NewUser");
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
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_NewUserIntroActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }



    private void Register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_REGISTER,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.d("ServerResponse", "" + ServerResponse);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            FC_Common.status = jsonResponse1.getString("success");
                            FC_Common.message = jsonResponse1.getString("message");
                            FC_Common.token_type = jsonResponse1.getString("token_type");
                            FC_Common.access_token= jsonResponse1.getString("access_token");

                            if (FC_Common.status.equalsIgnoreCase("1")) {
                                txt_saveBtn.setEnabled(true);

                                GetUserDetails();


                            } else {
                                txt_saveBtn.setEnabled(true);
                                snackBar(FC_Common.message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            txt_saveBtn.setEnabled(true);
                            Log.d("fdhbdfghdf", "dfhdf" + e);
                            snackBar(String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String value = volleyError.toString();
                txt_saveBtn.setEnabled(true);
                snackBar(value);
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("dfgdfgdfgdf","dfgfdgfd"+String.valueOf(selectedImageUri));
                Log.d("dfgdfgdfgdf","dfgfdgfd"+FC_Common.img_profile);
                params.put("name", FC_Common.username);
                params.put("mobile", FC_Common.usermobile);
                params.put("device_type", FC_Common.devicetype);
                params.put("device_token", FC_Common.devicetoken);
                params.put("device_id", FC_Common.device_id);
                params.put("email", FC_Common.useremail);
                params.put("picture", FC_Common.img_profile);
                //params.put("picture", String.valueOf(selectedImageUri));
                params.put("gender", FC_Common.usergender);
                params.put("referred_by", FC_Common.refcode);
                Log.d("getParams:", "" + params);
                return params;
            }

            /*protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("uploaded_file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }*/

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_NewUserIntroActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void startDialog() {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Objects.requireNonNull(FC_NewUserIntroActivity.this));
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent pictureActionIntent = null;

                pictureActionIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        pictureActionIntent,
                        GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Log.d("dfhdfhdghdf","fdgdfgdfgdfg");
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Log.d("path","path :"+thumbnail);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            String path = MediaStore.Images.Media.insertImage(Objects.requireNonNull(FC_NewUserIntroActivity.this).getContentResolver(), thumbnail, "Title", null);
            Log.d("path","path :"+path);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            Log.d("destination","path :"+destination);
           FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("dfhdfhdghdf","fdgdfgdfg"+e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("dfhdfhdghdf","1231fdgdfgdfg"+e);
            }

            selectedImageUri = Uri.parse(path);
            //selectedImageUri = Uri.fromFile(imagefile);
            Utils.log(FC_NewUserIntroActivity.this, " selectedImageUri:" + selectedImageUri);


            //Log.d("ghmghmjghjmgh","1213fgjnfghjfg"+FC_Common.img_profile);
                img_profile.setBackground(null);
            FC_Common.img_profile = getPath(selectedImageUri);
            //FC_Common.img_profile = path;
            img_profile.setImageBitmap(thumbnail);

            Log.d("Dfgdfgfd","sdfgsdf"+FC_Common.img_profile);

            /*final File mypicturedirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = mypicturedirectory.getPath()+File.separator+"IMG"+timestamp+".jpg";
            File imagefile = new File(filename);
            Uri imageuri = Uri.fromFile(imagefile);
            //selectedImageUri = Uri.parse(path);
            //FC_Common.img_profile = getPath(imageuri);
            Log.d("Dfgdfgfd","sdfgsdf"+imageuri);*/

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {

            selectedImageUri = data.getData();
            if (null != selectedImageUri) {

                img_profile.setBackground(null);
                    FC_Common.img_profile = getPath(selectedImageUri);
                img_profile.setImageURI(selectedImageUri);

                    Log.d("ghmghmjghjmgh","fgjnfghjfg"+FC_Common.img_profile);



                Utils.log(FC_NewUserIntroActivity.this, " selectedImageUri:" + selectedImageUri);
            }
            Utils.log(FC_NewUserIntroActivity.this, " selectedImageUri:" + selectedImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public String getPath(Uri uri) {
        Cursor cursor = FC_NewUserIntroActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = FC_NewUserIntroActivity.this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(FC_NewUserIntroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        //Toast.makeText(FC_NewUserIntroActivity.this, "Granted", Toast.LENGTH_LONG).show();
        if (ActivityCompat.shouldShowRequestPermissionRationale(FC_NewUserIntroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(FC_NewUserIntroActivity.this, "Oops you just denied the Permission!", Toast.LENGTH_LONG).show();
           /* Intent intent =new Intent(FC_NewUserIntroActivity.this, FC_Login.class);
            startActivity(intent);*/
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(FC_NewUserIntroActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(FC_NewUserIntroActivity.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                /*finish();
                startActivity(getIntent());*/
            } else {
                //Displaying another toast if permission is not granted
               // Toast.makeText(FC_NewUserIntroActivity.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(FC_NewUserIntroActivity.this, "Oops you just denied the permission!", Toast.LENGTH_LONG).show();
        }
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

 /*   @Override
    public void onBackPressed() {

       // Utils.showExitDialog(FC_Login.this, "Are you sure want to \n exit from application");
    }*/
}
