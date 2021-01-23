package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCTopPickRestaurants;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentItemActivity.FC_ItemActivity;
import food.user.demand.FCPojo.FCTopPickObject.TopPickObject;
import food.user.demand.FCUtils.BottomDailog.BottomDialogFragmentAddonProducts;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class Fc_TopPickRestaurant extends AppCompatActivity implements  BottomDialogFragmentAddonProducts.AddonProducts {
    InputMethodManager inputMgr;
    View parentLayout;
    Snackbar bar;
    Handler handler;
    SwitchCompat sw_vegNonveg;
    ImageView img_VegNonVeg;
    BottomSheetDialog paymentdialog;
    private int allresataurantcounter = 0;
    StatefulRecyclerView rv_allRestaurants;
    Context context;
    private View view_scroll;
    int addcheck = 0;
    NestedScrollView  nsv_hotelsDetails;
    private Rect scrollBounds = new Rect();
    LoaderTextView lt_hotelDetails, lt_hotelTiming, lt_restaurantInfo, lt_cartCurrency, lt_totalPrice;
    private TopPickAdapter topPickAdapter;
    private ArrayList<TopPickObject> topPickObjects;
    ImageView img_backBtn,img_search ;
    LinearLayout ll_rating,ll_viewCartLayoutBtn,ll_main,ll_toolbarTextLayout,ll_clearcart,ll_payment,ll_viewCart,ll_hotelDetails, ll_hotelTiming, ll_restaurantInfo, ll_vegNonVegType;
    AC_Textview txt_restaurantTime,txt_restaurantNameHeader,txt_vegNonveg,txt_ok,txt_cancel,txt_header, txt_totalQty,txt_costLimit, txt_personLimit, txt_day, txt_timing,
            txt_phone, txt_restaurantName, txt_cuisines_top, txt_rating_top, txt_currency,
            txt_deliveryEstimation, txt_addAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(Fc_TopPickRestaurant.this,getResources().getConfiguration());
        setContentView(R.layout.fc_home_toppick);
        context= Fc_TopPickRestaurant.this;
        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FindviewById();
        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.id = String.valueOf(user.getid());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            FC_Common.toppickid = (String) bundle.get("toppickid");

            Log.d("fhdfgdfg", "dfgdfgfd" + FC_Common.recent_search);
        }
        Utils.log(context,"FC_Common.latitude :"+FC_Common.latitude);
        Utils.log(context,"FC_Common.longitude :"+FC_Common.longitude);
        Utils.log(context,"FC_Common.access_token :"+FC_Common.access_token);
        //RecyclerView Adapter//
        AllRestaurantRecycler();
        //RecyclerView List//
        AllRestaurantList();

       /* handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something

                if (allresataurantcounter>5)
                {
                    allresataurantcounter=0;
                    AllRestaurantList();
                }
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

        nsv_hotelsDetails.getHitRect(scrollBounds);

        nsv_hotelsDetails.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (view_scroll != null) {

                if (view_scroll.getLocalVisibleRect(scrollBounds)) {
                    ll_toolbarTextLayout.setVisibility(View.GONE);
                } else {
                    ll_toolbarTextLayout.setVisibility(View.VISIBLE);
                    txt_restaurantNameHeader.setText(FC_Common.restaurant_name);
                    txt_restaurantTime.setText(FC_Common.delivery_estimation);

                    Log.d("fgjhfghfg","fdghfdgf"+FC_Common.delivery_estimation);
                }
            }
            Log.d("scrollY ", "scrollY : " + scrollY);

        });
        img_backBtn.setOnClickListener(v -> onBackPressed());
        //topPickAdapter  Set//

        topPickAdapter = new TopPickAdapter(topPickObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(Fc_TopPickRestaurant.this, LinearLayoutManager.VERTICAL, false);
        rv_allRestaurants.setLayoutManager(itemViewLLres);
        rv_allRestaurants.setAdapter(topPickAdapter);

        ll_viewCartLayoutBtn.setOnClickListener(v -> {
            Intent cartIntent = new Intent(context, FC_CartActivity.class);
            startActivity(cartIntent);
        });
    }

    @Override
    protected void onResume()
    {
        Log.d("dfgdfgfdgfd","dfgfdgfd");
        AllRestaurantList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void FindviewById() {
        parentLayout = findViewById(android.R.id.content);
        img_VegNonVeg=findViewById(R.id.img_VegNonVeg);
        ll_viewCartLayoutBtn = findViewById(R.id.ll_viewCartLayoutBtn);
        ll_main=findViewById(R.id.ll_main);
        txt_restaurantNameHeader = findViewById(R.id.txt_restaurantNameHeader);
        txt_restaurantTime = findViewById(R.id.txt_restaurantTime);
        ll_toolbarTextLayout=findViewById(R.id.ll_toolbarTextLayout);
        view_scroll = findViewById(R.id.view_scroll);
        txt_vegNonveg=findViewById(R.id.txt_vegNonveg);
        img_search=findViewById(R.id.img_search);
        nsv_hotelsDetails = findViewById(R.id.nsv_hotelsDetails);
        rv_allRestaurants=findViewById(R.id.rv_allRestaurants);
        txt_totalQty=findViewById(R.id.txt_totalQty);
        ll_viewCart=findViewById(R.id.ll_viewCart);
        lt_hotelDetails=findViewById(R.id.lt_hotelDetails);
        lt_hotelTiming=findViewById(R.id.lt_hotelTiming);
        lt_restaurantInfo=findViewById(R.id.lt_restaurantInfo);
        lt_cartCurrency=findViewById(R.id.lt_cartCurrency);
        lt_totalPrice=findViewById(R.id.lt_totalPrice);
        ll_hotelDetails=findViewById(R.id.ll_hotelDetails);
        ll_hotelTiming=findViewById(R.id.ll_hotelTiming);
        ll_restaurantInfo=findViewById(R.id.ll_restaurantInfo);
        ll_vegNonVegType=findViewById(R.id.ll_vegNonVegType);
        txt_costLimit=findViewById(R.id.txt_costLimit);
        txt_personLimit=findViewById(R.id.txt_personLimit);
        txt_day=findViewById(R.id.txt_day);
        txt_timing=findViewById(R.id.txt_timing);
        txt_phone=findViewById(R.id.txt_phone);
        txt_restaurantName=findViewById(R.id.txt_restaurantName);
        txt_cuisines_top=findViewById(R.id.txt_cuisines);
        txt_rating_top=findViewById(R.id.txt_rating);
        ll_rating=findViewById(R.id.ll_rating);
        txt_currency=findViewById(R.id.txt_currency);
        txt_deliveryEstimation=findViewById(R.id.txt_deliveryEstimation);
        txt_addAddress=findViewById(R.id.txt_addAddress);
        sw_vegNonveg = findViewById(R.id.sw_vegNonveg);

        img_search.setVisibility(View.GONE);
        img_backBtn=findViewById(R.id.img_backBtn);

        topPickObjects = new ArrayList<>();
        TopPickObject object = new TopPickObject();
        object.setD_images("");
        object.setD_images("");
        topPickObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        topPickObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        topPickObjects.add(object);

        sw_vegNonveg.setOnCheckedChangeListener(onCheckedChanged());
    }

    //AllRestaurant Async Task Start//
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AllRestaurantList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTDETAIL,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.optString("success").equals("1")) {
                            ll_main.setVisibility(View.GONE);
                        FC_Common.hotelid = jsonObject.getString("id");
                        FC_Common.restaurant_name = jsonObject.getString("restaurant_name");
                        FC_Common.restaurant_logo = jsonObject.getString("restaurant_logo");
                        FC_Common.rating = jsonObject.getString("rating");
                        FC_Common.delivery_estimation = jsonObject.getString("delivery_estimation");
                        FC_Common.cost_limit = jsonObject.getString("cost_limit");
                        FC_Common.restaurant_address = jsonObject.getString("restaurant_address");
                        FC_Common.restaurant_phone = jsonObject.getString("restaurant_phone");
                        FC_Common.dish_id = jsonObject.getString("dish_id");
                        Double doublestax = Double.parseDouble(FC_Common.rating);
                        FC_Common.total_quantity = jsonObject.getString("total_quantity");
                        FC_Common.total_price = jsonObject.getString("total_price");
                        FC_Common.currency = jsonObject.getString("currency");
                        FC_Common.restaurant_cuisine = jsonObject.getString("cuisines");
                        FC_Common.restaurant_status = jsonObject.getString("restaurant_status");
                        FC_Common.person_limit = jsonObject.getString("person_limit");
                        FC_Common.opens = jsonObject.getString("opens");
                        FC_Common.favourite = jsonObject.getString("favourite");
                        txt_totalQty.setText(FC_Common.total_quantity + " Items");
                        lt_cartCurrency.setText(FC_Common.currency);
                        lt_totalPrice.setText(FC_Common.total_price);
                        lt_hotelDetails.setVisibility(View.GONE);
                        ll_hotelDetails.setVisibility(View.VISIBLE);
                        lt_hotelTiming.setVisibility(View.GONE);
                        ll_hotelTiming.setVisibility(View.VISIBLE);
                        lt_restaurantInfo.setVisibility(View.GONE);
                        ll_restaurantInfo.setVisibility(View.VISIBLE);
                        txt_restaurantName.setText(FC_Common.restaurant_name);
                        txt_phone.setText(FC_Common.restaurant_phone);
                        txt_cuisines_top.setText(FC_Common.restaurant_cuisine);
                        txt_rating_top.setText(String.format("%.1f", doublestax));


                            Double double_test=Double.parseDouble(txt_rating_top.getText().toString());
                            Double double_one=1.0;
                            Double double_two=2.0;
                            Double double_three=3.0;
                            Double double_four=4.0;
                            Double double_five=5.0;

                            if (double_test<=double_one){
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate1));
                            }
                            else if (double_test<=double_two){
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate2));
                                Log.d("fgujfghfg","2132132dfgdfg"+doublestax);
                            }
                            else if (double_test<=double_three){
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate3));
                                Log.d("fgujfghfg","213dfgdfg"+doublestax);
                            }
                            else if (double_test<=double_four){
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate4));
                                Log.d("fgujfghfg","213dfgdfg"+doublestax);
                            }
                            else if (double_test<=double_five){
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate5));
                            }
                            else{
                                ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_normal));
                            }

                        txt_deliveryEstimation.setText(FC_Common.delivery_estimation);
                        txt_currency.setText(FC_Common.currency);
                        txt_addAddress.setText(FC_Common.restaurant_address);
                        txt_costLimit.setText(FC_Common.cost_limit);
                        txt_day.setText(FC_Common.restaurant_status);
                        txt_personLimit.setText("For " + FC_Common.person_limit);
                        txt_timing.setText(FC_Common.opens);
                        if (FC_Common.dish_id.equalsIgnoreCase("1")) {
                            ll_vegNonVegType.setVisibility(View.GONE);
                            img_VegNonVeg.setBackgroundResource(R.drawable.veg);
                        } else {
                            ll_vegNonVegType.setVisibility(View.VISIBLE);
                            img_VegNonVeg.setBackgroundResource(R.drawable.non_veg);
                        }


                            //LocationObject.clear();
                            JSONArray dataArray = jsonObject.getJSONArray("products");
                            topPickObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                TopPickObject playerModel = new TopPickObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setProduct_name(product.getString("product_name"));
                                    playerModel.setProduct_description(product.getString("product_description"));
                                    playerModel.setCategory_id(product.getString("category_id"));
                                    playerModel.setCuisine_id(product.getString("cuisine_id"));
                                    playerModel.setDish_id(product.getString("dish_id"));
                                    playerModel.setPhoto(product.getString("photo"));
                                    playerModel.setPrice(product.getString("price"));
                                    playerModel.setOrignal_price(product.getString("orignal_price"));
                                    playerModel.setDiscount(product.getString("discount"));
                                    playerModel.setDiscount_type(product.getString("discount_type"));
                                    playerModel.setAvailability(product.getString("availability"));
                                    playerModel.setPrice_status(product.getString("price_status"));
                                    playerModel.setAddon_status(product.getString("addon_status"));
                                    playerModel.setNext_avail_time1(product.getString("next_avail_time1"));
                                    playerModel.setNext_avail_time2(product.getString("next_avail_time2"));
                                    playerModel.setNext_avail_time3(product.getString("next_avail_time3"));
                                    playerModel.setQuantity(product.getString("quantity"));
                                    playerModel.setNext_available(product.getString("next_available"));
                                    topPickObjects.add(playerModel);

                                    if (topPickObjects != null) {
                                        topPickAdapter.visibleContentLayout();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                   /* final int counter_AllResataurant = allresataurantcounter++;
                                    Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                                    if (counter_AllResataurant<5) {
                                        AllRestaurantList();
                                    }*/

                                }
                            }

                        }
                        else {
                            ll_main.setVisibility(View.VISIBLE);
                            /*final int counter_AllResataurant = allresataurantcounter++;
                            Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                            if (counter_AllResataurant<5) {
                                AllRestaurantList();
                            }*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                       /* final int counter_AllResataurant = allresataurantcounter++;
                        Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                        if (counter_AllResataurant<5) {
                            AllRestaurantList();
                        }*/
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                    }
                }, error -> {
                    //displaying the error in toast if occurrs
                    String error_value = String.valueOf(error);
                    snackBar("Hotsellernew"+error_value);
                    Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
                    Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
                    /*final int counter_AllResataurant = allresataurantcounter++;
                    Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                    if (counter_AllResataurant<5) {
                        AllRestaurantList();
                    }*/
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("id", FC_Common.toppickid);
                params.put("vn_type", FC_Common.typeNonVeg);
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
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Fc_TopPickRestaurant.this).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Utils.log(context,"dgsdgsdfgsd"+ex);
        }

    }
    private void AllRestaurantRecycler() {
        topPickAdapter = new TopPickAdapter(topPickObjects);
        rv_allRestaurants.setAdapter(topPickAdapter);
        rv_allRestaurants.setLayoutManager(new LinearLayoutManager(Fc_TopPickRestaurant.this, LinearLayoutManager.VERTICAL, false));

    }
    private class TopPickAdapter extends RecyclerView.Adapter<TopPickAdapter.ViewHolder> {
        private final ArrayList<TopPickObject> topPickObjects;
        boolean visible;

        TopPickAdapter(ArrayList<TopPickObject> topPickObjects) {
            this.topPickObjects = topPickObjects;
        }

        @NonNull
        @Override
        public TopPickAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_recommended_items, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull TopPickAdapter.ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);


                holder.lt_loaderView.setVisibility(View.GONE);
                holder.ll_loader.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.lt_restaurantname.setText(topPickObjects.get(position).getProduct_name());
                holder.lt_cuisine.setText(topPickObjects.get(position).getCuisine_id());
                holder.lt_dishPrice.setText(topPickObjects.get(position).getPrice());
                holder.txt_quantity.setText(topPickObjects.get(position).getQuantity());
                FC_Common.availabilty = topPickObjects.get(position).getAvailability();
                Picasso.get().load(topPickObjects.get(position).getPhoto()).into(holder.imgcr_dishImage);

                if (!topPickObjects.get(position).getQuantity().equalsIgnoreCase("0"))
                {
                    holder.img_close.setVisibility(View.VISIBLE);
                    Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp1);
                }
                else {
                    holder.img_close.setVisibility(View.GONE);
                }
                if (FC_Common.total_quantity.equalsIgnoreCase("0")) {
                    ll_viewCart.setVisibility(View.GONE);
                } else {
                 ll_viewCart.setVisibility(View.VISIBLE);
                    Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp1);

                }
                holder.pb_cart.setVisibility(View.GONE);
                Log.d("fdghdfgdfg", "fdgdfgdf" + topPickObjects.get(position).getAvailability());
                if (FC_Common.availabilty.equalsIgnoreCase("0")) {
                    holder.ll_nextAvailable.setVisibility(View.VISIBLE);
                    holder.img_timeOut.setVisibility(View.VISIBLE);
                    holder.ll_addAndMinus.setVisibility(View.GONE);
                    holder.ll_addBtn.setVisibility(View.GONE);
                    //lt_nextAvailable.setText(R.string.nextavailable+""+topPickObjects.get(position).getNext_avail_time1());
                    holder.lt_nextAvailable.setText(topPickObjects.get(position).getNext_available());
                } else {
                    holder.ll_nextAvailable.setVisibility(View.GONE);
                    holder.img_timeOut.setVisibility(View.GONE);
                    holder.ll_addBtn.setVisibility(View.VISIBLE);
                    if (topPickObjects.get(position).getQuantity().equalsIgnoreCase("0")) {
                        holder.ll_addBtn.setVisibility(View.VISIBLE);
                        holder.ll_addAndMinus.setVisibility(View.GONE);
                    } else {
                        holder.ll_addBtn.setVisibility(View.GONE);
                        holder.ll_addAndMinus.setVisibility(View.VISIBLE);
                    }
                }

                /**/
                if (topPickObjects.get(position).getDish_id().equalsIgnoreCase("1")) {
                    holder.img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                } else {
                    holder.img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                holder.ll_addBtn.setOnClickListener(view -> {
                    if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN")) {
                        FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                        FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                        FC_Common.productID = topPickObjects.get(position).getId();
                        FC_Common.quantity = topPickObjects.get(position).getQuantity();
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.hotelpricing);
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

                            Log.d("fdhfdghfdg","12dfgdfgfd"+FC_Common.hotelpricing);
                            Log.d("fdhfdghfdg","243dfgdfgfd"+FC_Common.addonpricing);
                            Log.d("fdhfdghfdg","545dfgdfgfd"+FC_Common.toppickid);
                            Log.d("fdhfdghfdg","5464dfgdfgfd"+FC_Common.productID);
                            Log.d("fdhfdghfdg","4534dfgdfgfd"+FC_Common.quantity);
                            BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                            //finish();
                        } else {
                            holder.pb_cart.setVisibility(View.VISIBLE);
                            //FC_Common.quantity="1";
                            int count = Integer.parseInt(topPickObjects.get(position).getQuantity());

                            count = count + 1;
                            FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                            FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                            FC_Common.productID = topPickObjects.get(position).getId();
                            FC_Common.quantity = String.valueOf(count);
                            snackBar("item to be adding please wait");
                            UpdateMenu();
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + "fail");
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + "fail");
                        }


                    } else {
                        holder.ll_addBtn.setVisibility(View.GONE);
                        snackBar(FC_Common.restaurant_name + " Currently UnAvailable");
                    }

                });

                holder.img_plus.setOnClickListener(view -> {

                    holder.pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(topPickObjects.get(position).getQuantity());

                    count = count + 1;
                    FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                    FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                    FC_Common.productID = topPickObjects.get(position).getId();
                    FC_Common.quantity = String.valueOf(count);
                    Utils.log(context, "sdfsdfsdfsdfs" + "count : " + count);
                    Utils.log(context, "sdfsdfsdfsdfs" + "quantity : " + FC_Common.quantity);
                    UpdateMenu();
                });
                holder.img_minus.setOnClickListener(v -> {
                    holder.pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(topPickObjects.get(position).getQuantity());
                    count = count - 1;
                    FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                    FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                    FC_Common.productID = topPickObjects.get(position).getId();
                    FC_Common.quantity = String.valueOf(count);
                    UpdateMenu();
                    Utils.log(context, "sdfsdfsdfg" + "count : " + count);
                });
                holder.img_tick.setOnClickListener(v -> {
                    holder.pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(topPickObjects.get(position).getQuantity());
                    count = count + 1;
                    FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                    FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                    FC_Common.productID = topPickObjects.get(position).getId();
                    FC_Common.quantity = String.valueOf(count);
                    snackBar("item to be adding please wait");
                    UpdateMenu();
                    Utils.log(context, "sdfsdfsdfg" + "count : " + count);
                });
                holder.img_close.setOnClickListener(v -> {
                    holder.pb_cart.setVisibility(View.VISIBLE);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    alertDialog.setTitle("Delete item");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are sure You Want Remove From this item in cart");

                    // On pressing the Settings button.
                    alertDialog.setPositiveButton("Delete", (dialog, which) -> {
                        holder.pb_cart.setVisibility(View.VISIBLE);
                        FC_Common.hotelpricing = topPickObjects.get(position).getPrice_status();
                        FC_Common.addonpricing = topPickObjects.get(position).getAddon_status();
                        FC_Common.productID = topPickObjects.get(position).getId();
                        FC_Common.quantity = "0";
                        UpdateMenu();
                    });

                    // On pressing the cancel button
                    alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                    // Showing Alert Message
                    alertDialog.show();

                });

                holder.ll_content.setOnClickListener(v -> {
                    FC_Common.topdish_id=topPickObjects.get(position).getId();
                    FC_Common.topphoto = topPickObjects.get(position).getPhoto();
                    Intent sharedintent = new Intent(context, FC_ItemActivity.class);
                    sharedintent.putExtra("dishid",FC_Common.topdish_id);
                    sharedintent.putExtra("hotelid",FC_Common.toppickid);
                    sharedintent.putExtra("restaurant_name",FC_Common.restaurant_name);
                    Pair[] pairs;
                    pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(holder.imgcr_dishImage, "transaction_itemImg");
                    ActivityOptions option = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        option = ActivityOptions.makeSceneTransitionAnimation(Fc_TopPickRestaurant.this, pairs);
                    }
                    startActivity(sharedintent, Objects.requireNonNull(option).toBundle());
                });

            }
        }

        @Override
        public int getItemCount() {
            return topPickObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_loader, ll_content, ll_addBtn, ll_dishimage, ll_tick, ll_close, ll_addAndMinus, ll_nextAvailable;
            ImageView img_tick, img_close, img_VegNonVegType, img_plus, img_timeOut, img_minus;
            CircleImageView imgcr_dishImage;
            LoaderTextView lt_restaurantname, lt_cuisine, lt_loaderView, lt_nextAvailable;
            LoaderTextView lt_dishPrice;
            ProgressBar pb_cart;
            AC_Textview txt_quantity;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                lt_nextAvailable = itemView.findViewById(R.id.lt_nextAvailable);
                ll_nextAvailable = itemView.findViewById(R.id.ll_nextAvailable);
                txt_quantity = itemView.findViewById(R.id.txt_quantity);
                img_timeOut = itemView.findViewById(R.id.img_timeOut);
                img_plus = itemView.findViewById(R.id.img_plus);
                img_minus = itemView.findViewById(R.id.img_minus);
                ll_addAndMinus = itemView.findViewById(R.id.ll_addAndMinus);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_tick = itemView.findViewById(R.id.ll_tick);
                ll_close = itemView.findViewById(R.id.ll_close);
                ll_dishimage = itemView.findViewById(R.id.ll_dishimage);
                ll_addBtn = itemView.findViewById(R.id.ll_addBtn);
                lt_loaderView = itemView.findViewById(R.id.lt_loaderView);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                img_tick = itemView.findViewById(R.id.img_tick);
                img_close = itemView.findViewById(R.id.img_close);
                img_VegNonVegType = itemView.findViewById(R.id.img_VegNonVegType);
                imgcr_dishImage = itemView.findViewById(R.id.imgcr_dishImage);
                lt_restaurantname = itemView.findViewById(R.id.lt_restaurantname);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_dishPrice = itemView.findViewById(R.id.lt_dishPrice);
                pb_cart = itemView.findViewById(R.id.pb_cart);


            }
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
                            AllRestaurantList();
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
                        snackBar(getResources().getString(R.string.reach));
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", FC_Common.productID);
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
    //AllRestaurant Async Task End//
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
                            AllRestaurantList();
                        }
                        else {
                            paymentdialog.dismiss();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
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
            AllRestaurantList();
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

    private CompoundButton.OnCheckedChangeListener onCheckedChanged() {
        return (buttonView, isChecked) -> {
            if (buttonView.getId() == R.id.sw_vegNonveg) {
                setButtonState(isChecked);
            }
        };
    }
    private void setButtonState(boolean state) {
        if (state) {
            txt_vegNonveg.setText(R.string.veg_only);
            FC_Common.typeNonVeg="1";
            AllRestaurantList();
            Log.d("fgjhfghfg","ghjghjgh");
        } else {
            txt_vegNonveg.setText(R.string.veg_nonveg);
            FC_Common.typeNonVeg="2";
            AllRestaurantList();
            Log.d("fgjhfghfg","chefjngj");
        }
    }

}
