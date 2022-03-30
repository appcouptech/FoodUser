package food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.Activity.HttpConnection;
import food.user.demand.Activity.PathJSONParser;
import food.user.demand.FCActivity.FCDashboard.FC_DashboardActivity;
import food.user.demand.FCPojo.FCHotelDetailsActivityObject.ItemViewOrderObject;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

 public class FC_OrderPickedUpActivity extends AppCompatActivity implements OnMapReadyCallback {
Context context;
    View parentLayout;
    Snackbar bar;
    String userId;
    private BottomSheetDialog ratingdialog;
    AC_Textview txt_rating,txt_driver,txt_status;
    AC_Edittext edt_commentres,edt_commentdriver,edt_cancelcomment;
    private RatingBar ratingBar_restaurant,ratingBar_driver;
    LoaderCircluarImageView lc_imgRestaurant,lc_img,lc_imgdriver,lc_imgcancelRestaurant;
    LoaderTextView lt_cancelrestaurantname,lt_ordercancel,lt_drivername,lt_restaurantName,lt_payment,lt_restaurantname,lt_cuisine,lt_item,lt_currency,lt_totalprice;
    ImageView img_backBtn,img_driver,img_driver_call;
    Marker mCurrLocationMarker , driverMarker ;
    ImageView img_detailItem;
     private int dialogcounter = 0;
    private ListView lst_itemview;
    private List<ItemViewOrderObject> Listvalues = new ArrayList<>();
    private ItemViewOrderObject ListDatas;
    private GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    private DialogPlus itemdialog;
    private AC_Textview lt_orderProcessing, lt_duration ;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    Handler handler;
    double CURRENT_latitude, CURRENT_longitude;
    private GoogleSignInClient mGoogleSignInClient;
    List<Location> locationList = new ArrayList<>();
    final static int REQUEST_LOCATION = 199;
    private int userZoomLevel = 0;
    private float zoom;
    private LatLngBounds bounds;
    LatLng latLng;
    private Polyline polyline ;
    double latitude; // latitude
    double longitude;
    private int counter_res = 0;
    private int counter_driver = 0;
    DatabaseReference dbRef;
    private ItemOrderAdapter itemOrderAdapters;
    private View vw_ordered,vw_orderPickedUP ,vw_preparing ,vw_outForDelivery ,vw_Delivered ;
    private LinearLayout ll_cancel,ll_loader,ll_main,ll_restaurantInfo,ll_driver_layout,ll_del,ll_delLine ;
    private  AC_Textview txt_submitcancel,txt_cancel,txt_cancelsubmit,txt_submit,txt_ratedriver,txt_ordered,txt_orderPickedUP ,txt_preparing ,txt_outForDelivery ,txt_Delivered ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_OrderPickedUpActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_fc__order_picked_up);
        context=FC_OrderPickedUpActivity.this;
        FindViewById();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_intro_address);
        Objects.requireNonNull(mapFragment).getMapAsync(FC_OrderPickedUpActivity.this);

        if (bundle != null) {
            FC_Common.order_id = (String) bundle.get("order_id");
            Log.d("fhdfgdfg", "dfgdfgfd" + FC_Common.recent_search);
        }
        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
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

        Log.d("fhdfgdfg", "check");

        img_backBtn.setOnClickListener(view -> onBackPressed());

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
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


        this.setFinishOnTouchOutside(true);

        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) FC_OrderPickedUpActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_OrderPickedUpActivity.this)) {
          //  Toast.makeText(FC_OrderPickedUpActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(FC_OrderPickedUpActivity.this)) {
           // Toast.makeText(FC_OrderPickedUpActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(FC_OrderPickedUpActivity.this)) {
            Log.e("TAG", "Gps already enabled");
           // Toast.makeText(FC_OrderPickedUpActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
           // Toast.makeText(FC_OrderPickedUpActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
       /* if (FC_Common.endU_status.equalsIgnoreCase("CANCELLED"))
        {
            Log.e("TAG", "Gps already enabled");
        }
        else
        {
            OrderConfirm();

        }*/

        handler = new Handler();
        int delay = 15000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                OrderConfirm();

                Log.d("dfgdfgdf","dgsdf");
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // dbRef = database.getReference("/dev/drivers");
        dbRef = database.getReference("/foodcoup/dev/drivers"+FC_Common.orderdriver_id);
        ValueEventListener changeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String driverid = (String) messageSnapshot.child("driverid").getValue();
                    String device_id = (String) messageSnapshot.child("device_id").getValue();
                    String latitude = (String) messageSnapshot.child("latitude").getValue();
                    String longitude = (String) messageSnapshot.child("longitude").getValue();
                    Log.d("dfghdfgfdg","driverid"+driverid);
                    Log.d("dfghdfgfdg","device_id"+device_id);
                    Log.d("dfghdfgfdg","latitude"+latitude);
                    Log.d("dfghdfgfdg","longitude"+longitude);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("dgsdfsdf", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        dbRef.addValueEventListener(changeListener);

        lt_ordercancel.setOnClickListener(v -> {
            View view1 = getLayoutInflater().inflate(R.layout.layout_cancel_order, null);
            FindViewByIdBottomDialogRating1(view1);
            ratingdialog = new BottomSheetDialog(context);
            ratingdialog.setContentView(view1);
            ratingdialog.show();
        });

        txt_submitcancel.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewById() {
        parentLayout = findViewById(android.R.id.content);
        lc_img = findViewById(R.id.lc_img);
        ll_loader = findViewById(R.id.ll_loader);
        ll_main = findViewById(R.id.ll_main);
        txt_status = findViewById(R.id.txt_status);
        ll_driver_layout = findViewById(R.id.ll_driver_layout);
        img_driver = findViewById(R.id.img_driver);
        txt_driver = findViewById(R.id.txt_driver);
        img_driver_call = findViewById(R.id.img_driver_call);
        lt_restaurantname = findViewById(R.id.lt_restaurantname);
        txt_ratedriver = findViewById(R.id.txt_ratedriver);
        lt_totalprice = findViewById(R.id.lt_totalprice);
        txt_rating = findViewById(R.id.txt_rating);
        ll_driver_layout.setVisibility(View.GONE);

        lt_orderProcessing = findViewById(R.id.lt_orderProcessing);
        lt_ordercancel = findViewById(R.id.lt_ordercancel);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        lt_orderProcessing.startAnimation(anim);

        txt_submitcancel = findViewById(R.id.txt_submitcancel);
        txt_cancel = findViewById(R.id.txt_cancel);
        ll_cancel = findViewById(R.id.ll_cancel);
        lt_duration = findViewById(R.id.lt_duration);
        lt_currency = findViewById(R.id.lt_currency);
        lt_item = findViewById(R.id.lt_item);
        img_detailItem = findViewById(R.id.img_detailItem);
        ll_del = findViewById(R.id.ll_del);
        ll_delLine = findViewById(R.id.ll_delLine);
        lt_cuisine = findViewById(R.id.lt_cuisine);
        img_backBtn = findViewById(R.id.img_backBtn);

        txt_orderPickedUP = findViewById(R.id.txt_orderPickedUP);
        txt_ordered = findViewById(R.id.txt_ordered);
        lt_payment = findViewById(R.id.lt_payment);
        txt_preparing = findViewById(R.id.txt_preparing);
        txt_outForDelivery = findViewById(R.id.txt_outForDelivery);
        txt_Delivered = findViewById(R.id.txt_Delivered);



        vw_ordered = findViewById(R.id.vw_ordered );
        vw_orderPickedUP = findViewById(R.id.vw_orderPickedUP );
        vw_preparing = findViewById(R.id.vw_preparing );
        vw_outForDelivery = findViewById(R.id.vw_outForDelivery );
        vw_Delivered = findViewById(R.id.vw_Delivered );

        txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
        txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_gray_color));
        txt_preparing.setTextColor(getResources().getColor(R.color.txt_gray_color));
        txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_gray_color));
        txt_Delivered.setTextColor(getResources().getColor(R.color.txt_gray_color));

        vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
        vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
        vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
        vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
        vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));

        ll_loader.setVisibility(View.VISIBLE);
        ll_main.setVisibility(View.GONE);

        txt_ratedriver.setOnClickListener(v -> {
            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.layout_rating, null);
            FindViewByIdBottomDialogRating(view1);
            ratingdialog = new BottomSheetDialog(context);
            ratingdialog.setContentView(view1);
            ratingdialog.show();
        });
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

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void OrderConfirm() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ORDERDETAIL,
                response -> {
                    Log.d("xcgfdcbbxc", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");

                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            ll_loader.setVisibility(View.GONE);
                            ll_main.setVisibility(View.VISIBLE);
                            FC_Common.ordered_picked=0;
                            FC_Common.preparing=0;
                            FC_Common.outfordelivery=0;
                            FC_Common.delivered=0;
                            FC_Common.orderrestaurant_name = obj.getString("restaurant_name");
                            FC_Common.ordercuisine_id= obj.getString("cuisine_id");
                            FC_Common.ordername= obj.getString("name");
                            FC_Common.orderdriverrating= obj.getString("rating");
                            FC_Common.orderdriver= obj.getString("avatar");
                            FC_Common.orderdriver_id= obj.getString("driver_id");
                            FC_Common.orderrestaurant= obj.getString("restaurant_logo");
                            FC_Common.ordermobile= obj.getString("mobile");
                            FC_Common.orderid= obj.getString("id");
                            FC_Common.ordercurrency= obj.getString("currency");
                            FC_Common.ordertotal= obj.getString("total");
                            FC_Common.orderitem= obj.getString("item");
                            FC_Common.ordered_picked = obj.getInt("order_picked");
                            FC_Common.preparing = obj.getInt("preparing");
                            FC_Common.outfordelivery = obj.getInt("outfordelivery");
                            FC_Common.delivered = obj.getInt("delivered");
                            FC_Common.paymentmode = obj.getString("payment_mode");
                            FC_Common.d_location_lat = obj.getDouble("location_lat");
                            FC_Common.d_location_lng = obj.getDouble("location_lng");
                            FC_Common.endU_location_lat = obj.getDouble("latitude");
                            FC_Common.endU_location_lng = obj.getDouble("longitude");
                            FC_Common.endU_status = obj.getString("status");
                            /*FC_Common.d_location_lat = Double.parseDouble(obj.getString("location_lat"));
                            FC_Common.d_location_lng = Double.parseDouble(obj.getString("location_lng"));
                            FC_Common.endU_location_lat = Double.parseDouble(obj.getString("latitude"));
                            FC_Common.endU_location_lng = Double.parseDouble(obj.getString("longitude"));*/
                            Log.d("dfgsdfsdf","sdfsdf"+dialogcounter);
                            Log.d("dfgdgsdf","preparing"+FC_Common.endU_status);
                            Log.d("dfgdgsdf","outfordelivery"+FC_Common.outfordelivery);
                            Log.d("dfgdgsdf","ordered_picked"+FC_Common.ordered_picked);
                            if (FC_Common.endU_status.equalsIgnoreCase("CANCELLED"))
                            {

                                FC_Common.cancel_reason = obj.getString("cancel_reason");
                                txt_cancel.setText(FC_Common.cancel_reason);
                                ll_cancel.setVisibility(View.VISIBLE);
                                ll_main.setVisibility(View.GONE);
                               /* Utils.toast(context,"Your order is cancelled");
                                Intent intent = new Intent(FC_OrderPickedUpActivity.this, FC_DashboardActivity.class);
                                intent.putExtra("location_name", FC_Common.location_name);
                                intent.putExtra("location_type", FC_Common.location_type);
                                intent.putExtra("latitude", FC_Common.latitude);
                                intent.putExtra("longitude", FC_Common.longitude);
                                intent.putExtra("gpsenabled", FC_Common.gpsenabled);
                                startActivity(intent);
                                finish();*/
                                FC_Common.order_id="";
                                Log.d("dfgdfgdf","dgfgd"+FC_Common.order_id);
                            }
                            if (FC_Common.endU_status.equalsIgnoreCase("ORDERED")){

                                ll_cancel.setVisibility(View.GONE);
                                ll_main.setVisibility(View.VISIBLE);

                                ll_delLine.setVisibility(View.VISIBLE);
                                ll_del.setVisibility(View.VISIBLE);
                                lt_orderProcessing.setVisibility(View.GONE);
                                lt_ordercancel.setVisibility(View.VISIBLE);
                                ll_driver_layout.setVisibility(View.GONE);

                                txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_preparing.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_Delivered.setTextColor(getResources().getColor(R.color.txt_gray_color));

                                vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));

                                Picasso.get().load(FC_Common.orderrestaurant)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(lc_img);

                                lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                                lt_payment.setText(getResources().getString(R.string.payment)+" : "+FC_Common.paymentmode);
                                lt_cuisine.setText(FC_Common.ordercuisine_id);
                                lt_item.setText(FC_Common.orderitem);
                                lt_currency.setText(FC_Common.ordercurrency);
                                lt_totalprice.setText(FC_Common.ordertotal);
                                //txt_rating.setText(getResources().getString(R.string.rating)+" : "+FC_Common.orderdriverrating);
                                txt_rating.setText(FC_Common.orderdriverrating);
                                Double doublestax = Double.parseDouble(txt_rating.getText().toString());
                                txt_rating.setText(String.format("%.1f", doublestax));
                                img_detailItem.setVisibility(View.VISIBLE);
                                img_detailItem.setOnClickListener(v -> {
                                    ItemDialog();
                                });
                            }
                            //if (FC_Common.ordered_picked == 1){
                           else if (FC_Common.endU_status.equalsIgnoreCase("RECEIVED")||
                                    FC_Common.endU_status.equalsIgnoreCase("ACCEPTED")){

                                ll_cancel.setVisibility(View.GONE);
                                ll_main.setVisibility(View.VISIBLE);
                                Log.d("dfghdfgdfgfd","dfgdfgdfg");
                                ll_delLine.setVisibility(View.VISIBLE);
                                ll_del.setVisibility(View.VISIBLE);
                                lt_orderProcessing.setVisibility(View.VISIBLE);
                                lt_ordercancel.setVisibility(View.GONE);
                                ll_driver_layout.setVisibility(View.GONE);

                                txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_preparing.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_Delivered.setTextColor(getResources().getColor(R.color.txt_gray_color));

                                vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));

                                Picasso.get().load(FC_Common.orderrestaurant)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(lc_img);

                                lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                                lt_payment.setText(getResources().getString(R.string.payment)+" : "+FC_Common.paymentmode);
                                lt_cuisine.setText(FC_Common.ordercuisine_id);
                                lt_item.setText(FC_Common.orderitem);
                                lt_currency.setText(FC_Common.ordercurrency);
                                lt_totalprice.setText(FC_Common.ordertotal);
                                //txt_rating.setText(getResources().getString(R.string.rating)+" : "+FC_Common.orderdriverrating);
                                txt_rating.setText(FC_Common.orderdriverrating);
                                Double doublestax = Double.parseDouble(txt_rating.getText().toString());
                                txt_rating.setText(String.format("%.1f", doublestax));
                                img_detailItem.setVisibility(View.VISIBLE);
                                img_detailItem.setOnClickListener(v -> {
                                    ItemDialog();
                                });
                            }

                           // if (FC_Common.preparing == 1){
                           else if (FC_Common.endU_status.equalsIgnoreCase("ARRIVED")){

                                ll_cancel.setVisibility(View.GONE);
                                ll_main.setVisibility(View.VISIBLE);

                                Log.d("dfghdfgdfgfd","xgdsdxfvdzf");
                                lt_orderProcessing.setVisibility(View.VISIBLE);
                                lt_ordercancel.setVisibility(View.GONE);
                                txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_preparing.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_gray_color));
                                txt_Delivered.setTextColor(getResources().getColor(R.color.txt_gray_color));

                                vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));
                                vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));

                                Picasso.get().load(FC_Common.orderrestaurant)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(lc_img);

                                lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                                lt_payment.setText(getResources().getString(R.string.payment)+" : "+FC_Common.paymentmode);
                                lt_cuisine.setText(FC_Common.ordercuisine_id);
                                lt_item.setText(FC_Common.orderitem);
                                lt_currency.setText(FC_Common.ordercurrency);
                                lt_totalprice.setText(FC_Common.ordertotal);
                                //txt_rating.setText(getResources().getString(R.string.rating)+" : "+FC_Common.orderdriverrating);
                                txt_rating.setText(FC_Common.orderdriverrating);
                                Double doublestax = Double.parseDouble(txt_rating.getText().toString());
                                txt_rating.setText(String.format("%.1f", doublestax));
                                img_detailItem.setVisibility(View.VISIBLE);
                                img_detailItem.setOnClickListener(v -> {
                                    ItemDialog();
                                });

                                ll_driver_layout.setVisibility(View.VISIBLE);
//                                if (counter_driver==0)
//                                {
                                    Picasso.get().load(FC_Common.orderdriver)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into(img_driver);
                                    //counter_driver++;
                               // }


                                // txt_driver.setText(FC_Common.ordermobile+" - "+FC_Common.ordername);
                                txt_driver.setText(FC_Common.ordername);

                                img_driver_call.setOnClickListener(v -> {
                                   /* if (Build.VERSION.SDK_INT < 23) {
                                        phoneCall();
                                    } else {
                                        if (ActivityCompat.checkSelfPermission(FC_OrderPickedUpActivity.this,
                                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                            phoneCall();
                                        } else {
                                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                                            ActivityCompat.requestPermissions(FC_OrderPickedUpActivity.this, PERMISSIONS_STORAGE, 9);
                                        }
                                    }*/
                                    Intent inSOS = new Intent(Intent.ACTION_DIAL);
                                    inSOS.setData(Uri.parse("tel:" + FC_Common.ordermobile));
                                    startActivity(inSOS);
                                });
                            }

                            //if (FC_Common.outfordelivery == 1){
                           else if (FC_Common.endU_status.equalsIgnoreCase("PICKEDUP")||
                                    FC_Common.endU_status.equalsIgnoreCase("REACHED")){
                                Log.d("dfghdfgdfgfd","zsrew43re");
                                ll_cancel.setVisibility(View.GONE);
                                ll_main.setVisibility(View.VISIBLE);

                                lt_orderProcessing.setVisibility(View.VISIBLE);
                                lt_ordercancel.setVisibility(View.GONE);
                                txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_preparing.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_Delivered.setTextColor(getResources().getColor(R.color.txt_gray_color));

                                vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_gray_color));

                                Picasso.get().load(FC_Common.orderrestaurant)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(lc_img);

                                lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                                lt_payment.setText(getResources().getString(R.string.payment)+" : "+FC_Common.paymentmode);
                                lt_cuisine.setText(FC_Common.ordercuisine_id);
                                lt_item.setText(FC_Common.orderitem);
                                lt_currency.setText(FC_Common.ordercurrency);
                                lt_totalprice.setText(FC_Common.ordertotal);
                                //txt_rating.setText(getResources().getString(R.string.rating)+" : "+FC_Common.orderdriverrating);
                                txt_rating.setText(FC_Common.orderdriverrating);
                                Double doublestax = Double.parseDouble(txt_rating.getText().toString());
                                txt_rating.setText(String.format("%.1f", doublestax));
                                img_detailItem.setVisibility(View.VISIBLE);
                                img_detailItem.setOnClickListener(v -> {
                                    ItemDialog();
                                });

                                ll_driver_layout.setVisibility(View.VISIBLE);
//                                if (counter_driver==0)
//                                {
                                    Picasso.get().load(FC_Common.orderdriver)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into(img_driver);
//                                    counter_driver++;
//                                }

                               // txt_driver.setText(FC_Common.ordermobile+" - "+FC_Common.ordername);
                                txt_driver.setText(FC_Common.ordername);

                                img_driver_call.setOnClickListener(v -> {
                                    /*if (Build.VERSION.SDK_INT < 23) {
                                        phoneCall();
                                    } else {
                                        if (ActivityCompat.checkSelfPermission(FC_OrderPickedUpActivity.this,
                                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                            phoneCall();
                                        } else {
                                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                                            ActivityCompat.requestPermissions(FC_OrderPickedUpActivity.this, PERMISSIONS_STORAGE, 9);
                                        }
                                    }*/

                                    Intent inSOS = new Intent(Intent.ACTION_DIAL);
                                    inSOS.setData(Uri.parse("tel:" + FC_Common.ordermobile));
                                    startActivity(inSOS);

                                });

                                FucedLocation();
                            }

                           // if (  FC_Common.delivered == 1){
//                                else if (FC_Common.endU_status.equalsIgnoreCase("COMPLETED")||
//                                    FC_Common.endU_status.equalsIgnoreCase("REACHED")){
                            else if (FC_Common.endU_status.equalsIgnoreCase("COMPLETED")){

                                ll_cancel.setVisibility(View.GONE);
                                ll_main.setVisibility(View.VISIBLE);

                                dialogcounter = dialogcounter + 1;
                                    Log.d("dfgsdfsdf","sdfsdf"+dialogcounter);
                                if (dialogcounter > 1) {
                                    Log.d("dfgsdfsdf","sfsdf"+dialogcounter);
                                    dialogcounter=1;
                                }
                                else {
                                    try {
                                        Log.d("dfgsdfsdf","sdfasds"+dialogcounter);
                                        @SuppressLint("InflateParams")
                                        View view1 = getLayoutInflater().inflate(R.layout.layout_rating, null);
                                        FindViewByIdBottomDialogRating(view1);
                                        ratingdialog = new BottomSheetDialog(context);
                                        ratingdialog.setContentView(view1);
                                        ratingdialog.show();
                                    }
                                    catch(RuntimeException ex){
                                        ex.printStackTrace();
                                    }

                                }

                                lt_orderProcessing.setVisibility(View.VISIBLE);
                                lt_ordercancel.setVisibility(View.GONE);
                                Log.d("dfghdfgdfgfd","fs454fvdfcsd");
                                txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_orderPickedUP.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_preparing.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_outForDelivery.setTextColor(getResources().getColor(R.color.txt_site_green_color));
                                txt_Delivered.setTextColor(getResources().getColor(R.color.txt_site_green_color));

                                vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_orderPickedUP.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_preparing.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_outForDelivery.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
                                vw_Delivered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));


                                Picasso.get().load(FC_Common.orderrestaurant)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(lc_img);

                                lt_restaurantname.setText(FC_Common.orderrestaurant_name);
                                lt_payment.setText(getResources().getString(R.string.payment)+" : "+FC_Common.paymentmode);
                                lt_cuisine.setText(FC_Common.ordercuisine_id);
                                lt_item.setText(FC_Common.orderitem);
                                lt_currency.setText(FC_Common.ordercurrency);
                                lt_totalprice.setText(FC_Common.ordertotal);
                                //txt_rating.setText(getResources().getString(R.string.rating)+" : "+FC_Common.orderdriverrating);
                                txt_rating.setText(FC_Common.orderdriverrating);
                                Double doublestax = Double.parseDouble(txt_rating.getText().toString());
                                txt_rating.setText(String.format("%.1f", doublestax));
                                img_detailItem.setVisibility(View.VISIBLE);
                                img_detailItem.setOnClickListener(v -> {
                                    ItemDialog();
                                });
                            }
//                            vw_ordered.setBackgroundColor(getResources().getColor(R.color.txt_site_green_color));
//                            txt_ordered.setTextColor(getResources().getColor(R.color.txt_site_green_color));







                        }
                        else
                        {
                            snackBar(FC_Common.message);
                            Log.d("dfghdghfgfdb", "fdhfdh" + FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("dfghdghfgfdb", "fdhfdh" + FC_Common.ordered_picked);
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    //snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                FC_Common.ordered_picked=0;
                FC_Common.preparing=0;
                FC_Common.outfordelivery=0;
                FC_Common.delivered=0;
                Log.d("order_id: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                Log.d("getParams: ", "" + params);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void FucedLocation() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted\
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            //mGoogleMap.setMyLocationEnabled(true);
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

    @Override
    public void onBackPressed() {

        Log.d("fghfdhfdg","fdgfdgdf");
        Intent intent = new Intent(context, FC_DashboardActivity.class);
        intent.putExtra("location_name", FC_Common.location_name);
        intent.putExtra("location_type", FC_Common.location_type);
        intent.putExtra("latitude", FC_Common.latitude);
        intent.putExtra("longitude", FC_Common.longitude);
        intent.putExtra("gpsenabled", FC_Common.gpsenabled);
        startActivity(intent);
        finish();
        FC_Common.order_id="";
        //super.onBackPressed();
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

    /*private void phoneCall() {

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+FC_Common.ordermobile));
            context.startActivity(callIntent);
        } else {
            snackBar("You don't assign permission.");
        }
    }*/

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
                        resolvable.startResolutionForResult(FC_OrderPickedUpActivity.this,
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
        mLocationRequest.setInterval(24000); // 1200000 two minute interval
        mLocationRequest.setFastestInterval(24000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        OrderConfirm();

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
               // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
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

              //  latLng = new LatLng(latitude, longitude); // current latlng but we are using the enduser latlng form server
                latLng = new LatLng(FC_Common.endU_location_lat, FC_Common.endU_location_lng);
               // latLng = new LatLng(FC_Common.driver_confirmlatitude, FC_Common.driver_confirmlongitude);

                Log.d("zoom", " latLng: " + latLng);

                int height = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
                int width = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
               /* BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_driver_marker);
                Bitmap b = bitmapdraw.getBitmap();*/
                Bitmap b =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_customer_marker));
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Location");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(latLng)
                        .zoom(17)
                        .build();

                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                if (bounds != null) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                }

                driverlocation();

                OrderConfirm();
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

    private void driverlocation() {

        if (driverMarker != null){
            driverMarker.remove();
        }

        int height1 = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
        int width1 = (int) getResources().getDimension(R.dimen.bitmap_iconSize);
     /*   BitmapDrawable bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.to_location);
        Bitmap b1 = bitmapdraw1.getBitmap();*/
        Bitmap b1 =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_driver_marker));
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, width1, height1, false);

        LatLng point = new LatLng(FC_Common.d_location_lat , FC_Common.d_location_lng);
        //LatLng point = new LatLng(FC_Common.driver_confirmlatitude, FC_Common.driver_confirmlongitude);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(point);
        markerOptions1.title("Deliver Location");
        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1));
        driverMarker = mGoogleMap.addMarker(markerOptions1);

      /*  CameraPosition googlePlex = CameraPosition.builder()
                .target(latLng)
                .build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        if (bounds != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        }*/

        animateMarker(latLng,false);

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

    private String getMapsApiDirectionsUrl() {

        // Origin of route
        String str_origin = "origin=" + FC_Common.endU_location_lat + "," + FC_Common.endU_location_lng;

        // Destination of route
        String str_dest = "destination=" + FC_Common.d_location_lat + "," + FC_Common.d_location_lng;


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
                                ActivityCompat.requestPermissions(FC_OrderPickedUpActivity.this,
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
            String distance = "";
            String duration = "";

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {


                path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {

                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

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
                Log.d("map" , " Distance : " + distance + " , duration : " + duration ) ;
                lt_duration.setVisibility(View.VISIBLE);
                lt_duration.setText(""+ duration);
            } else {
                Toast.makeText(FC_OrderPickedUpActivity.this, "Manage API Account", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ItemDialog() {
        itemdialog = DialogPlus.newDialog(Objects.requireNonNull(context))
                .setContentHolder(new ViewHolder(R.layout.dialogitemview))
                .setCancelable(true)
                .setGravity( Gravity.CENTER)
                .setOnItemClickListener((dialogPlus, item, view, position) -> {
                }).setExpanded(false).create();
        itemdialog.show();
        ImageView imgcancel = (ImageView) itemdialog.findViewById(R.id.imgcancel);

        assert imgcancel != null;
        imgcancel.setOnClickListener(v -> itemdialog.dismiss());
        lst_itemview = (ListView) itemdialog.findViewById(R.id.lst_itemview);

        ItemDialogAsync();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ItemDialogAsync() {
        Utils.playProgressBar(FC_OrderPickedUpActivity.this);
        StringRequest stringRequest = new StringRequest( Request.Method.POST, FC_URL.URL_ITEMDETAIL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray arr = jsonObject.getJSONArray("data");
                        Log.d("sdgsdfsd","dfsd"+FC_URL.URL_ITEMDETAIL);
                        Listvalues.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject product = arr.getJSONObject(i);
                            ListDatas = new ItemViewOrderObject();
                            ListDatas.setCuisine_name(product.getString("product_name"));
                            ListDatas.setQty(product.getString("qty"));
                            Listvalues.add(ListDatas);
                        }
                        if (Listvalues != null && lst_itemview != null) {

                            Utils.stopProgressBar();
                            itemOrderAdapters = new ItemOrderAdapter(context, Listvalues);
                            lst_itemview.setAdapter(itemOrderAdapters);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("dgsdfsd","dfsdf"+e);
                        Log.d("dgsdfsd","dfsdf"+FC_URL.URL_ITEMDETAIL);
                    } }, error -> Utils.toast(context,"" + R.string.reach)){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:12" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        // request queue
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Utils.log(context,"dgsdgsdfgsd"+ex);
        }
    }

    public static class ItemOrderAdapter extends ArrayAdapter<ItemViewOrderObject> {
        private List<ItemViewOrderObject> _list;
        @SuppressLint("StaticFieldLeak")
        LinearLayout lin_click_event;
        private ArrayList<ItemViewOrderObject> arraylist;
        ItemOrderAdapter(Context context, List<ItemViewOrderObject> lst) {
            super(context, 0, lst);
            _list = lst;
            this.arraylist = new ArrayList<>();
            this.arraylist.addAll(_list);
        }
        @NonNull
        @Override
        public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") final View view = inflater.inflate( R.layout.layout_itemview, parent, false);
            try {
                ItemViewOrderObject vidItem = _list.get(position);
                LoaderTextView lt_itemName =view.findViewById(R.id.lt_itemName);
                LoaderTextView lt_qty =view.findViewById(R.id.lt_qty);

                lt_itemName.setText(vidItem.getCuisine_name());
                lt_qty.setText(vidItem.getQty());
            } catch (Exception pre) {
                pre.printStackTrace();
            }
            return view;
        }
    }

    private void FindViewByIdBottomDialogRating(View view) {

        lt_restaurantName=view.findViewById(R.id.lt_restaurantName);
        lc_imgRestaurant=view.findViewById(R.id.lc_imgRestaurant);
        ll_restaurantInfo=view.findViewById(R.id.ll_restaurantInfo);
        lc_imgdriver=view.findViewById(R.id.lc_imgdriver);
        lt_restaurantname=view.findViewById(R.id.lt_restaurantname);
        lt_drivername=view.findViewById(R.id.lt_drivername);
        lt_cuisine=view.findViewById(R.id.lt_cuisine);
        ratingBar_restaurant=view.findViewById(R.id.ratingBar_restaurant);
        ratingBar_driver=view.findViewById(R.id.ratingBar_driver);
        txt_submit=view.findViewById(R.id.txt_submit);
        edt_commentres=view.findViewById(R.id.edt_commentres);
        edt_commentdriver = view.findViewById(R.id.edt_commentdriver);
        lt_restaurantName.setVisibility(View.VISIBLE);
        ll_restaurantInfo.setVisibility(View.VISIBLE);
        edt_commentres.setVisibility(View.VISIBLE);
        lt_drivername.setText(FC_Common.ordername);
        txt_submit.setVisibility(View.VISIBLE);

        Picasso.get().load(FC_Common.orderrestaurant)
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .into(lc_imgRestaurant);

        Picasso.get().load(FC_Common.orderdriver)
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .into(lc_imgdriver);
        txt_submit.setOnClickListener(v -> {

            Log.d("hvcnbvcnbvc","cgcc"+FC_Common.driver_rating);

            if (FC_Common.restaurant_rating.equalsIgnoreCase(""))
            {
                Log.d("hvcnbvcnbvc","dg4wcgcc"+FC_Common.restaurant_rating);
                snackBar(getResources().getString(R.string.rate_res));
            }
            if (FC_Common.driver_rating.equalsIgnoreCase(""))
            {
                Log.d("hvcnbvcnbvc","xcghdfcgcc"+FC_Common.driver_rating);
                snackBar(getResources().getString(R.string.rate_driver));
            }
            if (FC_Common.restaurant_rating.equalsIgnoreCase(".0")||
                    FC_Common.driver_rating.equalsIgnoreCase(".0")||
                    FC_Common.restaurant_rating.equalsIgnoreCase("0.0")||
                    FC_Common.driver_rating.equalsIgnoreCase("0.0")){
                Log.d("fghfghfghfg","fail");
                snackBar(getResources().getString(R.string.rating_res_driver));
            }
            else {
                txt_submit.setEnabled(false);
                FC_Common.restaurant_comments=edt_commentres.getText().toString().trim();
                FC_Common.driver_comments=edt_commentdriver.getText().toString().trim();
                FC_Common.restaurant_rating=String.valueOf(ratingBar_restaurant.getRating());
                FC_Common.driver_rating=String.valueOf(ratingBar_driver.getRating());
                Log.d("fghfghfghfg","fghfghfgh"+FC_Common.driver_rating);
                Updaterating();
            }
        });
    }
     private void FindViewByIdBottomDialogRating1(View view) {

         lt_cancelrestaurantname=view.findViewById(R.id.lt_cancelrestaurantname);
         lc_imgcancelRestaurant=view.findViewById(R.id.lc_imgcancelRestaurant);
         edt_cancelcomment=view.findViewById(R.id.edt_cancelcomment);
         txt_cancelsubmit=view.findViewById(R.id.txt_cancelsubmit);

         Picasso.get().load(FC_Common.orderrestaurant)
                 .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                 .into(lc_imgcancelRestaurant);
         lt_cancelrestaurantname.setText(FC_Common.orderrestaurant_name);
         txt_cancelsubmit.setVisibility(View.VISIBLE);
         txt_cancelsubmit.setOnClickListener(v -> {

             FC_Common.cancel_order_note=edt_cancelcomment.getText().toString();
                 Cancelorder();

         });
     }
    private void Updaterating() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_RATE,
                response -> {
                    Log.d("cvnvncvb", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            txt_submit.setEnabled(true);
                            Utils.toast(context,FC_Common.message);
                            onBackPressed();
                            //snackBar(FC_Common.message);
                            ratingdialog.dismiss();
                        } else {
                            txt_submit.setEnabled(true);
                            Utils.toast(context,FC_Common.message);
                            onBackPressed();
                            //snackBar(FC_Common.message);
                            ratingdialog.dismiss();
                        }

                    } catch (JSONException e) {
                        txt_submit.setEnabled(true);
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                    }
                },
                error -> {
                    txt_submit.setEnabled(true);

                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                params.put("driver_rating", FC_Common.driver_rating);
                params.put("driver_comment", FC_Common.driver_comments);
                params.put("restaurant_rating", FC_Common.restaurant_rating);
                params.put("restaurant_comment", FC_Common.restaurant_comments);
                //  params.put("order_id", "+");

                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
     private void Cancelorder() {

         StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_CANCEL,
                 response -> {
                     Log.d("cvnvncvb", ">>" + response);
                     try {
                         JSONObject obj = new JSONObject(response);
                         FC_Common.success = obj.getString("success");
                         FC_Common.message = obj.getString("message");
                         Log.d("ghfghfghf", "fhfgdhfd" + obj);
                         if (FC_Common.success.equalsIgnoreCase("1")) {

                             onBackPressed();
                             Utils.toast(context,FC_Common.message);
                             //snackBar(FC_Common.message);
                             ratingdialog.dismiss();
                         } else {
                             Utils.toast(context,FC_Common.message);
                             //snackBar(FC_Common.message);
                             ratingdialog.dismiss();
                         }

                     } catch (JSONException e) {
                         txt_submit.setEnabled(true);
                         e.printStackTrace();
                         snackBar(getResources().getString(R.string.reach));
                         Log.d("dfghdghfgfdb", "fdhfdh" + e);
                         // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);
                     }
                 },
                 error -> {
                     txt_submit.setEnabled(true);

                     snackBar(getResources().getString(R.string.reach));
                     Log.d("dfhfdghfgh", "hfdhdf" + error);
                 }) {
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<>();
                 params.put("order_id", FC_Common.order_id);
                 params.put("cancel_reason", FC_Common.cancel_order_note);
                 //  params.put("order_id", "+");

                 Log.d("getParams: ", "" + params);
                 return params;
             }

             @Override
             public Map<String, String> getHeaders() {
                 Map<String, String> params = new HashMap<>();
                 Log.d("getParams: ", "" + params);
                 params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                 return params;
             }
         };

         // request queue
         RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
         requestQueue.add(stringRequest);

     }
}
