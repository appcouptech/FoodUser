package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentItemActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCUtils.BottomDailog.BottomDialogFragmentAddonProducts;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_ItemActivity extends AppCompatActivity implements View.OnClickListener,BottomDialogFragmentAddonProducts.AddonProducts{

    LinearLayout ll_desription, ll_viewCart ,ll_viewCartLayout,ll_clearcart,ll_payment;
    LinearLayout ll_plus ,ll_minus,ll_addAndMinus,ll_nextAvailable,ll_addBtn ;
    ImageView img_back,img_plus;
    Context context;
    View parentLayout;
    AC_Textview txt_quantity,txt_ok,txt_cancel,txt_header,txt_totalQty;
    BottomSheetDialog paymentdialog;
    Snackbar bar;
    ImageView img_VegNonVegType;
    LoaderTextView lt_restaurantName,lt_dishName,lt_nextAvailable;
    AC_Textview txt_specialInstructions;
    LoaderTextView lt_currency,lt_cuisine,lt_dishPrice;
    CircleImageView shared_img;
    ProgressBar pb_cart;
    int addcheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        Utils.adjustFontScale(FC_ItemActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_item);

        context=FC_ItemActivity.this;
        FindViewById();


        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.id = String.valueOf(user.getid());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            FC_Common.topdish_id = (String) bundle.get("dishid");
            FC_Common.toppickid= (String) bundle.get("hotelid");
            FC_Common.restaurant_name= (String) bundle.get("restaurant_name");

            Log.d("fhdfgdfg", "dfgdfgfd" + FC_Common.recent_search);
        }
        Itemview();
    }

    private void FindViewById() {
        parentLayout = findViewById(android.R.id.content);
        img_plus = findViewById(R.id.img_plus);
        ll_plus = findViewById(R.id.ll_plus);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_totalQty = findViewById(R.id.txt_totalQty);
        shared_img = findViewById(R.id.shared_img);
        Picasso.get().load(FC_Common.topphoto).into(shared_img);
        ll_nextAvailable = findViewById(R.id.ll_nextAvailable);
        lt_nextAvailable = findViewById(R.id.lt_nextAvailable);
        ll_addBtn = findViewById(R.id.ll_addBtn);
        lt_dishPrice = findViewById(R.id.lt_dishPrice);
        ll_addAndMinus = findViewById(R.id.ll_addAndMinus);
        pb_cart = findViewById(R.id.pb_cart);
        txt_specialInstructions = findViewById(R.id.txt_specialInstructions);
        lt_cuisine = findViewById(R.id.lt_cuisine);
        img_VegNonVegType = findViewById(R.id.img_VegNonVegType);
        lt_restaurantName = findViewById(R.id.lt_restaurantName);
        lt_currency = findViewById(R.id.lt_currency);
        lt_dishName = findViewById(R.id.lt_dishName);
        ll_desription = findViewById(R.id.ll_desription);
        ll_viewCart = findViewById(R.id.ll_viewCart);
        ll_viewCartLayout = findViewById(R.id.ll_viewCartLayout);

        img_back = findViewById(R.id.img_back);
        ll_plus = findViewById(R.id.ll_plus);
        ll_minus = findViewById(R.id.ll_minus);


        img_back.setOnClickListener(this);
        ll_plus.setOnClickListener(this);
        ll_minus.setOnClickListener(this);
        ll_addBtn.setOnClickListener(this);
        ll_viewCartLayout.setOnClickListener(this);

    }

    private void Itemview() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCT,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        //FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);

                        if (FC_Common.success.equalsIgnoreCase("1")) {

                            ll_desription.setVisibility(View.VISIBLE);
                            FC_Common.topdish_id=obj.getString("id");
                            FC_Common.topproductname=obj.getString("product_name");
                            FC_Common.topdishtype=obj.getString("dish_id");
                            FC_Common.toprestaurant_cuisine=obj.getString("cuisine_id");
                            FC_Common.topcurrency=obj.getString("currency");
                            FC_Common.topprice=obj.getString("price");
                            FC_Common.topnextavailable=obj.getString("next_available");
                            FC_Common.quantity=obj.getString("quantity");
                            FC_Common.topprice_status=obj.getString("price_status");
                            FC_Common.topaddon_status=obj.getString("addon_status");
                        //    FC_Common.topphoto=obj.getString("photo");
                            FC_Common.topavailability=obj.getString("availability");
                            FC_Common.toprestaurantstatus=obj.getString("restaurant_status");
                            FC_Common.topproduct_description=obj.getString("product_description");
                         //   Picasso.get().load(FC_Common.topphoto).into(shared_img);
                            lt_restaurantName.setText(FC_Common.restaurant_name);
                            lt_dishName.setText(FC_Common.topproductname);
                            lt_currency.setText(FC_Common.topcurrency);
                            lt_cuisine.setText(FC_Common.toprestaurant_cuisine);
                            lt_dishPrice.setText(FC_Common.topprice);
                            txt_quantity.setText(FC_Common.quantity);
                            txt_totalQty.setText(FC_Common.quantity);
                            txt_specialInstructions.setText(FC_Common.topproduct_description);
                            if (FC_Common.topdishtype.equalsIgnoreCase("1"))
                            {
                                img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                            }
                            else {
                                img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                            }
                            if (FC_Common.quantity.equalsIgnoreCase("0")) {
                                ll_viewCart.setVisibility(View.GONE);
                            } else {
                                ll_viewCart.setVisibility(View.VISIBLE);
                                Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                                ll_viewCart.startAnimation(animSlideUp1);

                            }
                            pb_cart.setVisibility(View.GONE);

                            if (FC_Common.topavailability.equalsIgnoreCase("0")) {
                                ll_nextAvailable.setVisibility(View.VISIBLE);
                                ll_addAndMinus.setVisibility(View.GONE);
                                ll_addBtn.setVisibility(View.GONE);
                                //lt_nextAvailable.setText(R.string.nextavailable+""+recommened.getNext_avail_time1());
                                lt_nextAvailable.setText(FC_Common.topnextavailable);
                            } else {
                                ll_nextAvailable.setVisibility(View.GONE);
                                ll_addBtn.setVisibility(View.VISIBLE);
                                if (FC_Common.quantity.equalsIgnoreCase("0")) {
                                    ll_addBtn.setVisibility(View.VISIBLE);
                                    ll_addAndMinus.setVisibility(View.GONE);
                                } else {
                                    ll_addBtn.setVisibility(View.GONE);
                                    ll_addAndMinus.setVisibility(View.VISIBLE);
                                }
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));

                    }
                },
                error -> {

                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("product_id", FC_Common.topdish_id);
                params.put("id", FC_Common.toppickid);
                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:12" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back :
                onBackPressed();
                break;

            case R.id.ll_viewCartLayout:

                Intent cartActivityIntent = new Intent(FC_ItemActivity.this, FC_CartActivity.class);
                startActivity(cartActivityIntent);

                break;
            case R.id.ll_addBtn:

                    if (FC_Common.toprestaurantstatus.equalsIgnoreCase("OPEN")) {
                        FC_Common.hotelpricing = FC_Common.topprice_status;
                        FC_Common.addonpricing = FC_Common.topaddon_status;
                        FC_Common.productID = FC_Common.topdish_id;
                        FC_Common.quantity = FC_Common.quantity;
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        if (FC_Common.hotelpricing.equalsIgnoreCase("1") ||
                                FC_Common.addonpricing.equalsIgnoreCase("1")) {
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + FC_Common.hotelpricing);
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + FC_Common.addonpricing);

                            Bundle bundle = new Bundle();
                            bundle.putString("hotelpricing", FC_Common.hotelpricing);
                            bundle.putString("addonpricing", FC_Common.addonpricing);
                            bundle.putString("hotelid", FC_Common.toppickid);
                            bundle.putString("productID", FC_Common.productID);
                            bundle.putString("quantity", FC_Common.quantity);

                            BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                            //finish();
                        } else {
                            pb_cart.setVisibility(View.VISIBLE);
                            //FC_Common.quantity="1";
                            int count = Integer.parseInt(FC_Common.quantity);

                            count = count + 1;
                            FC_Common.hotelpricing = FC_Common.topprice_status;
                            FC_Common.addonpricing = FC_Common.topaddon_status;
                            FC_Common.productID = FC_Common.topdish_id;
                            FC_Common.quantity = String.valueOf(count);
                            snackBar("item to be adding please wait");
                            UpdateMenu();
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + "fail");
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + "fail");
                        }


                    }
                    else {
                        ll_addBtn.setVisibility(View.GONE);
                        snackBar(FC_Common.restaurant_name + " Currently UnAvailable");
                    }

                break;

            case R.id.ll_plus:
                pb_cart.setVisibility(View.VISIBLE);
                int count = Integer.parseInt(FC_Common.quantity);

                count = count + 1;
                FC_Common.hotelpricing = FC_Common.topprice_status;
                FC_Common.addonpricing = FC_Common.topaddon_status;
                FC_Common.productID = FC_Common.topdish_id;
                FC_Common.quantity = String.valueOf(count);
                Utils.log(context, "sdfsdfsdfsdfs" + "count : " + count);
                Utils.log(context, "sdfsdfsdfsdfs" + "quantity : " + FC_Common.quantity);
                UpdateMenu();
                break;
            case R.id.ll_minus :
                pb_cart.setVisibility(View.VISIBLE);
                int minuscount = Integer.parseInt(FC_Common.quantity);
                minuscount = minuscount - 1;
                FC_Common.hotelpricing = FC_Common.topprice_status;
                FC_Common.addonpricing = FC_Common.topaddon_status;
                FC_Common.productID = FC_Common.topdish_id;
                FC_Common.quantity = String.valueOf(minuscount);
                UpdateMenu();
                Utils.log(context, "sdfsdfsdfg" + "count : " + minuscount);
                break;
        }

    }


    private void UpdateMenu() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTADDONUPDATECART,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {

                            Itemview();
                        } else {

                            snackBar(FC_Common.message);
                            @SuppressLint("InflateParams")
                            View view1 = getLayoutInflater().inflate(R.layout.bottom_payment_gateway, null);
                            FindViewByIdBottomDialog(view1);
                            paymentdialog = new BottomSheetDialog(context);
                            paymentdialog.setContentView(view1);
                            paymentdialog.show();
                            txt_cancel.setOnClickListener(v -> paymentdialog.dismiss());
                            txt_ok.setOnClickListener(v -> ClearCart());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {

                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", FC_Common.topdish_id);
                params.put("partner_id", FC_Common.toppickid);
                params.put("quantity", FC_Common.quantity);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ClearCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_PRODUCTCARTCLEAR,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            paymentdialog.dismiss();
                            Itemview();
                        }
                        else {
                            paymentdialog.dismiss();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {

                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

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

    private void FindViewByIdBottomDialog(View view) {

        txt_ok = view.findViewById(R.id.txt_ok);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_header = view.findViewById(R.id.txt_header);
        ll_payment = view.findViewById(R.id.ll_payment);
        ll_clearcart = view.findViewById(R.id.ll_clearcart);
        txt_header.setText(R.string.f_clearcart);
        ll_payment.setVisibility(View.GONE);
        ll_clearcart.setVisibility(View.VISIBLE);

    }

    @Override
    public void addProducts(int products) {
        addcheck = products;
        if (addcheck == 1) {
            //init();
            Itemview();
        }
        else if (addcheck == 2){
            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.bottom_payment_gateway, null);
            FindViewByIdBottomDialog(view1);
            paymentdialog = new BottomSheetDialog(context);
            paymentdialog.setContentView(view1);
            paymentdialog.show();
            txt_cancel.setOnClickListener(v -> paymentdialog.dismiss());
            txt_ok.setOnClickListener(v -> ClearCart());
        }
        Log.d("zxvbzxvzxcxzc", "dfgdfg" );
    }
}
