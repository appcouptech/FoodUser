package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCActivity.FCCartActivity.FC_CartActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FCSearchFragment.FC_SearchHotelDetailsActivity;
import food.user.demand.FCPojo.FCHotelDetailsActivityObject.MenuListObject;
import food.user.demand.FCUtils.BottomDailog.BottomDialogFragmentAddonProducts;
import food.user.demand.FCUtils.LikeButton.LikeButton;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;
import food.user.demand.Testing_New.Call;
import food.user.demand.Testing_New.Description;
import food.user.demand.Testing_New.Header;
import food.user.demand.Testing_New.Recommended;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class FC_HotelDetailsActivity extends AppCompatActivity implements View.OnClickListener, BottomDialogFragmentAddonProducts.AddonProducts {
    private RecyclerView rv_recommended, rv_menuItem;
    AC_Textview txt_vegNonveg,txt_ok,txt_cancel,txt_header,txt_restaurantNameHeader, txt_restaurantTime;
    private List<Object> menulist;
    SwitchCompat sw_vegNonveg;
    BottomSheetDialog paymentdialog;
    LikeButton vector_android_button;
    LinearLayout ll_coupon,ll_mainLayout,reveal_items,ll_rating,ll_clearcart,ll_payment,ll_viewCartLayoutBtn, ll_menuMainLayout, ll_viewCart, ll_contentMainLayout, ll_menu, ll_menuList, ll_toolbarTextLayout, ll_hotelDetails, ll_hotelTiming, ll_restaurantInfo, ll_vegNonVegType;
    String check = "false";
    LoaderTextView lt_hotelDetails, lt_hotelTiming, lt_restaurantInfo, lt_cartCurrency, lt_totalPrice;
    private NestedScrollView nsv_hotelsDetails;
    private MultiViewTypeAdapter multiViewTypeAdapter;
    public static FragmentManager fragmentManagerHotelDetailsActiviy;
    AC_Textview txt_totalQty, txt_costLimit, txt_personLimit,txt_couponcode, txt_day, txt_timing, txt_phone, txt_restaurantName, txt_cuisines, txt_rating, txt_currency, txt_deliveryEstimation, txt_addAddress;
    Context context;
    private Rect scrollBounds = new Rect();
    private View view_scroll;
    View parentLayout;
    Snackbar bar;
    FC_User user;
    int addcheck = 0;
    boolean hidden = true;
    ImageView img_VegNonVeg;
    private ArrayList<MenuListObject> menuListObjects = new ArrayList<>();
    private ArrayList<String> scrollValues = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_HotelDetailsActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_fc__hotel_details);
        context = FC_HotelDetailsActivity.this;
        fragmentManagerHotelDetailsActiviy = getSupportFragmentManager();
        FindViewById();
        user = FC_SharedPrefManager.getInstance(context).getUser();
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FC_Common.restaurantid = (String) bundle.get("hotelid");
            FC_Common.recent_search = (String) bundle.get("recent_search");

            Log.d("xgsdgsdgsd", "dfgdfgfd" + FC_Common.recent_search);
            Log.d("xgsdgsdgsd", "dfgdfgfd" + FC_Common.restaurantid);
        }


        rv_recommended = findViewById(R.id.rv_recommended);
        // Set Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_recommended.setLayoutManager(linearLayoutManager);
        // Limiting the size
        rv_recommended.setHasFixedSize(true);

        /*new Handler().postDelayed(() -> {
            Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
            ll_menu.startAnimation(animSlideUp1);
            ll_menu.setVisibility(View.VISIBLE);

        }, 1000);*/

        rv_recommended.setOnTouchListener((v, event) -> {
            ll_menuList.setVisibility(View.GONE);
            return false;
        });

        ll_mainLayout.setOnTouchListener((v, event) -> {
            ll_menuList.setVisibility(View.GONE);
            return false;
        });
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
        // Initialize list items
        init();

    }

    private void init() {

       scrollValues.add("500");

        Log.d("sdfgsdvxzvxzcv", "dfgdfgfd" + FC_Common.restaurantid);
        menulist = new ArrayList<>();
        // Initiating Adapter
        multiViewTypeAdapter = new MultiViewTypeAdapter(context);
        rv_recommended.setAdapter(multiViewTypeAdapter);
        // Adding some demo data(Call &amp; Description Objects).
        // You can get them from your data server
        menulist.add(new Call("John", "9:30 AM"));
        menulist.add(new Call("Rob", "9:40 AM"));
        menulist.add(new Call("Rob", "9:40 AM"));
        menulist.add(new Call("Mia", "10:40 AM"));
        menulist.add(new Call("Scott", "10:45 AM"));
        // Set items to adapter
        multiViewTypeAdapter.menuFeed(menulist);
        multiViewTypeAdapter.notifyDataSetChanged();
        AllRestaurantList();


    }

    private void FindViewById() {

        vector_android_button = findViewById(R.id.vector_android_button);
        txt_vegNonveg = findViewById(R.id.txt_vegNonveg);
        ll_mainLayout = findViewById(R.id.ll_mainLayout);
        txt_totalQty = findViewById(R.id.txt_totalQty);
        lt_cartCurrency = findViewById(R.id.lt_cartCurrency);
        lt_totalPrice = findViewById(R.id.lt_totalPrice);
        ll_viewCart = findViewById(R.id.ll_viewCart);
        ll_contentMainLayout = findViewById(R.id.ll_contentMainLayout);
        ll_toolbarTextLayout = findViewById(R.id.ll_toolbarTextLayout);
        rv_menuItem = findViewById(R.id.rv_menuItem);
        lt_restaurantInfo = findViewById(R.id.lt_restaurantInfo);
        ll_restaurantInfo = findViewById(R.id.ll_restaurantInfo);
        lt_hotelDetails = findViewById(R.id.lt_hotelDetails);
        ll_vegNonVegType = findViewById(R.id.ll_vegNonVegType);
        ll_hotelDetails = findViewById(R.id.ll_hotelDetails);
        lt_hotelTiming = findViewById(R.id.lt_hotelTiming);
        ll_hotelTiming = findViewById(R.id.ll_hotelTiming);
        //reveal_items = findViewById(R.id.reveal_items);
       // reveal_items.setVisibility(View.INVISIBLE);
        parentLayout = findViewById(android.R.id.content);
        lt_hotelDetails.setVisibility(View.VISIBLE);
        ll_hotelDetails.setVisibility(View.GONE);
        lt_hotelTiming.setVisibility(View.VISIBLE);
        ll_hotelTiming.setVisibility(View.GONE);
        lt_restaurantInfo.setVisibility(View.VISIBLE);
        ll_restaurantInfo.setVisibility(View.GONE);

        txt_restaurantNameHeader = findViewById(R.id.txt_restaurantNameHeader);
        txt_restaurantTime = findViewById(R.id.txt_restaurantTime);
        ll_menuList = findViewById(R.id.ll_menuList);
        rv_recommended = findViewById(R.id.rv_recommended);
        ll_menu = findViewById(R.id.ll_menu);
        txt_restaurantName = findViewById(R.id.txt_restaurantName);
        txt_phone = findViewById(R.id.txt_phone);
       // FrameLayout fl_menuLayout = findViewById(R.id.fl_menuLayout);
        txt_timing = findViewById(R.id.txt_timing);
        txt_day = findViewById(R.id.txt_day);
        txt_couponcode = findViewById(R.id.txt_couponcode);
        ll_coupon = findViewById(R.id.ll_coupon);
        txt_personLimit = findViewById(R.id.txt_personLimit);
        txt_costLimit = findViewById(R.id.txt_costLimit);
        txt_cuisines = findViewById(R.id.txt_cuisines);
        txt_rating = findViewById(R.id.txt_rating);
        ll_rating = findViewById(R.id.ll_rating);
        ll_menuList.setVisibility(View.INVISIBLE);
        txt_currency = findViewById(R.id.txt_currency);
        txt_deliveryEstimation = findViewById(R.id.txt_deliveryEstimation);
        txt_addAddress = findViewById(R.id.txt_addAddress);
        nsv_hotelsDetails = findViewById(R.id.nsv_hotelsDetails);
        view_scroll = findViewById(R.id.view_scroll);
        ll_menu.setVisibility(View.GONE);
        ll_menu.setOnClickListener(this);


        ll_menuMainLayout = findViewById(R.id.ll_menuMainLayout);
        sw_vegNonveg = findViewById(R.id.sw_vegNonveg);
        ll_menuMainLayout = findViewById(R.id.ll_menuMainLayout);
        ll_viewCartLayoutBtn = findViewById(R.id.ll_viewCartLayoutBtn);
        ImageView img_backBtn = findViewById(R.id.img_backBtn);
        ImageView img_search = findViewById(R.id.img_search);
        img_VegNonVeg = findViewById(R.id.img_VegNonVeg);


        img_backBtn.setOnClickListener(this);
        sw_vegNonveg.setOnCheckedChangeListener(onCheckedChanged());
        ll_menu.setOnClickListener(this);
        ll_viewCartLayoutBtn.setOnClickListener(this);
        img_search.setOnClickListener(this);
        Log.d("fhfdhfdgfd","cxbxcvbxcv"+FC_Common.favourite);
        vector_android_button.setOnClickListener(v -> {
            Log.d("fhfdhfdgfd","dfgfdgfdg"+FC_Common.favourite);
            if (FC_Common.favourite.equalsIgnoreCase("0"))
            {
                String favouritecode="1";
                String url=FC_URL.URL_FAVOURITEADD;
                Favourite(favouritecode,url);
            }
            else {
                String favouritecode="2";
                String url=FC_URL.URL_FAVOURITEDELETE;
                Favourite(favouritecode,url);
            }
        });
        /*vector_android_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Log.d("fhfdhfdgfd","dfgfdgfdg"+FC_Common.favourite);
                String favouritecode="1";
                String url=FC_URL.URL_FAVOURITEADD;
                Favourite(favouritecode,url);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Log.d("fhfdhfdgfd","24334dfgfdgfdg"+FC_Common.favourite);
                String favouritecode="2";
                String url=FC_URL.URL_FAVOURITEDELETE;
                Favourite(favouritecode,url);

            }
        });*/

        /*callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.MAIN_TITLE, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));
        callSMSList.add(new Model(Model.SUB_TITLE, ""));
        callSMSList.add(new Model(Model.DESCRIPTION, ""));*/
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void AllRestaurantList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTLIST,
                response -> {
            try {
                        JSONObject jsonObject = new JSONObject(response);
                Log.d("dfgfdgdfg","dfgdfgd"+jsonObject);
                        FC_Common.hotelid = jsonObject.getString("id");
                        FC_Common.restaurant_name = jsonObject.getString("restaurant_name");
                        FC_Common.restaurant_cuisine = jsonObject.getString("cuisines");
                        FC_Common.dish_id = jsonObject.getString("dish_id");
                        FC_Common.restaurant_logo = jsonObject.getString("restaurant_logo");
                        FC_Common.restaurant_phone = jsonObject.getString("restaurant_phone");
                        FC_Common.rating = jsonObject.getString("rating");
                        Double doublestax = Double.parseDouble(FC_Common.rating);
                        FC_Common.delivery_estimation = jsonObject.getString("delivery_estimation");
                        FC_Common.cost_limit = jsonObject.getString("cost_limit");
                        FC_Common.restaurant_address = jsonObject.getString("restaurant_address");
                        FC_Common.currency = jsonObject.getString("currency");
                        FC_Common.restaurant_status = jsonObject.getString("restaurant_status");
                        FC_Common.person_limit = jsonObject.getString("person_limit");
                        FC_Common.opens = jsonObject.getString("opens");
                        FC_Common.total_quantity = jsonObject.getString("total_quantity");
                        FC_Common.total_price = jsonObject.getString("total_price");
                        FC_Common.favourite = jsonObject.getString("favourite");
                        FC_Common.discount_text = jsonObject.getString("discount_text");
                        FC_Common.minimum_order = jsonObject.getInt("minimum_order");
                        FC_Common.maximum_order = jsonObject.getInt("maximum_order");
                        txt_totalQty.setText(FC_Common.total_quantity + " Items");
                        lt_cartCurrency.setText(FC_Common.currency);
                        lt_totalPrice.setText(FC_Common.total_price);
                        txt_couponcode.setText(FC_Common.discount_text);
                        Log.d("dfgfdgdfg","dfgdfgd"+FC_Common.minimum_order);
                        Log.d("dfgfdgdfg","dfgdfgd"+FC_Common.maximum_order);
                        Log.d("dfgfdgdfg","dfgdfgd"+jsonObject);
                        if (FC_Common.favourite.equalsIgnoreCase("1"))
                        {
                            vector_android_button.setVisibility(View.VISIBLE);
                            vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                            vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                        }
                        if (!FC_Common.favourite.equalsIgnoreCase("1"))
                        {
                            vector_android_button.setVisibility(View.VISIBLE);
                            vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites));
                            vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites));
                        }


                        if (FC_Common.dish_id.equalsIgnoreCase("1"))
                        {
                            ll_vegNonVegType.setVisibility(View.GONE);
                            img_VegNonVeg.setBackgroundResource(R.drawable.veg);
                        } else {
                            ll_vegNonVegType.setVisibility(View.VISIBLE);
                            img_VegNonVeg.setBackgroundResource(R.drawable.non_veg);
                        }

                        if (FC_Common.typeNonVeg.equalsIgnoreCase("1"))
                        {
                            img_VegNonVeg.setBackgroundResource(R.drawable.veg);
                        }
                        else {
                            img_VegNonVeg.setBackgroundResource(R.drawable.non_veg);
                        }

                        if (FC_Common.discount_text.equalsIgnoreCase("0"))
                        {
                                ll_coupon.setVisibility(View.GONE);
                        }
                        else {
                            ll_coupon.setVisibility(View.VISIBLE);
                        }

                        lt_hotelDetails.setVisibility(View.GONE);
                        ll_hotelDetails.setVisibility(View.VISIBLE);
                        lt_hotelTiming.setVisibility(View.GONE);
                        ll_hotelTiming.setVisibility(View.VISIBLE);
                        lt_restaurantInfo.setVisibility(View.GONE);
                        ll_restaurantInfo.setVisibility(View.VISIBLE);
                        txt_restaurantName.setText(FC_Common.restaurant_name);
                        txt_phone.setText(FC_Common.restaurant_phone);
                        txt_cuisines.setText(FC_Common.restaurant_cuisine);
                        txt_rating.setText(String.format("%.1f", doublestax));
                        //txt_rating.setText("5.0");
                Double double_test=Double.parseDouble(txt_rating.getText().toString());
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
                        JSONArray jsonArray = jsonObject.getJSONArray("category");
                        JSONArray dataArray = jsonObject.getJSONArray("recommended");
                        Log.d("dfhdfhgdfhg", "sdgsdfgDfhgfdgdf" + jsonArray);
                        menulist.clear();
                        for (int x = 0; x < dataArray.length(); x++) {
                            //JSONObject object = jsonArray.getJSONObject(x);
                            JSONObject product = dataArray.getJSONObject(x);


                            menulist.add(new Recommended(product.getString("id"),
                                    product.getString("product_name"),
                                    product.getString("product_description"),
                                    product.getString("category_id"),
                                    product.getString("cuisine_id"),
                                    product.getString("dish_id"),
                                    product.getString("photo"),
                                    product.getString("price"),
                                    product.getString("orignal_price"),
                                    product.getString("discount"),
                                    product.getString("discount_type"),
                                    product.getString("availability"),
                                    product.getString("price_status"),
                                    product.getString("addon_status"),
                                    product.getString("next_avail_time1"),
                                    product.getString("next_avail_time2"),
                                    product.getString("next_avail_time3"),
                                    product.getString("next_available"),
                                    product.getString("quantity")));
                            multiViewTypeAdapter.menuFeed(menulist);
                            multiViewTypeAdapter.notifyDataSetChanged();

                            check = "true";
                            multiViewTypeAdapter.visibleContentLayout(check);


                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONArray dataArray3 = object.getJSONArray("products");
                            Log.d("xcvbxcbxcb", "dataArray3" + dataArray3);
                            String name = object.getString("category_name");
                            Log.d("dfhdfhgdfhg", "object" + name);
                            Log.d("dfhdfhgdfhg", "dfgfd" + name.length());
                            // if (name.length() > 1) {
                            Log.d("fdgdgdfg", "xvxcbcdfgdfgfd");
                            menulist.add(new Header(object.getString("category_name")));
                            multiViewTypeAdapter.menuFeed(menulist);
                            multiViewTypeAdapter.notifyDataSetChanged();

//                                }
//                                else {
                            Log.d("fdgdgdfg", "dataArray3" + dataArray3);
                            Log.d("fdgdgdfg", "ddfghet43tcvfgdfgfd" + dataArray3.length());
                            for (int j = 0; j < dataArray3.length(); j++) {
                                JSONObject product2 = dataArray3.getJSONObject(j);
                                menulist.add(new Description(product2.getString("id"),
                                        product2.getString("product_name"),
                                        product2.getString("product_description"),
                                        product2.getString("category_id"),
                                        product2.getString("cuisine_id"),
                                        product2.getString("dish_id"),
                                        product2.getString("photo"),
                                        product2.getString("price"),
                                        product2.getString("orignal_price"),
                                        product2.getString("discount"),
                                        product2.getString("discount_type"),
                                        product2.getString("availability"),
                                        product2.getString("price_status"),
                                        product2.getString("addon_status"),
//                                            product2.getString("ingredients"),
//                                            product2.getString("preparations"),
                                        product2.getString("next_avail_time1"),
                                        product2.getString("next_avail_time2"),
                                        product2.getString("next_avail_time3"),
                                        product2.getString("next_available"),
                                        product2.getString("quantity")));
                                multiViewTypeAdapter.menuFeed(menulist);
                                Log.d("fdgdgdfg", "dg436vcbrtydfgdfgfd");
                                multiViewTypeAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("asfasfasfsaf", "34543cvbvcb" + e);
                        //snackBar("Please Try Again");
                      /*  final int counter_menutype = MenuTypecounter++;
                        Utils.log(context, "countervalue" + "A:" + counter_menutype);
                        if (counter_menutype < 5) {
                            AllRestaurantList();
                        }*/
                    }

                }, error -> {
            //displaying the error in toast if occurrs
            String error_value = String.valueOf(error);
            //snackBar("Please Try Again");
           /* final int counter_menutype = MenuTypecounter++;
            Utils.log(context, "countervalue" + "A:" + counter_menutype);
            if (counter_menutype < 5) {
                AllRestaurantList();
            }*/
            Log.d("cvbxcbxcbvxc", "fgr546t4fgbddfgfdg" + error_value);


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", FC_Common.restaurantid);
                params.put("term", "");
                params.put("recent_search", FC_Common.recent_search);
                params.put("vn_type", FC_Common.typeNonVeg);
                Log.d("getParams", "" + params);
                Log.d("getParams", "" + FC_URL.URL_PRODUCTLIST);
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addProducts(int products) {
        addcheck = products;
        if (addcheck == 1) {
            //init();
            AllRestaurantList();
            Log.d("fdhdfhgfd","dfgsdfgsd"+addcheck);
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

    public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CALL = 1, TYPE_DESCRIPTION = 2, TYPE_Header = 3, TYPE_RECOMMENDED = 4;
        private List<Object> menufeed;
        boolean visible;
        String check_value = "false";
        // Context is not used here but may be required to
        // perform complex operations or call methods from outside
        private Context context;

        // Constructor
        MultiViewTypeAdapter(Context context) {
            this.context = context;

        }

        void menuFeed(List<Object> menufeed) {
            this.menufeed = menufeed;
            //notifyDataSetChanged();
        }

        // We need to override this as we need to differentiate
        // which type viewHolder to be attached
        // This is being called from onBindViewHolder() method
        @Override
        public int getItemViewType(int position) {
            if (menufeed.get(position) instanceof Call) {
                return TYPE_CALL;
            } else if (menufeed.get(position) instanceof Description) {
                return TYPE_DESCRIPTION;
            } else if (menufeed.get(position) instanceof Header) {
                return TYPE_Header;
            } else if (menufeed.get(position) instanceof Recommended) {
                return TYPE_RECOMMENDED;
            }

            return -1;
        }

        // Invoked by layout manager to replace the contents of the views
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            int viewType = holder.getItemViewType();


            switch (viewType) {
                case TYPE_CALL:
                    ((MultiViewTypeAdapter.LoaderView) holder).showCallDetails();
                    break;
                case TYPE_DESCRIPTION:
                    Description Description = (Description) menufeed.get(position);
                    ((MultiViewTypeAdapter.DescriptionView) holder).showDescription(Description);
                    break;
                case TYPE_Header:
                    Header header = (Header) menufeed.get(position);
                    ((MultiViewTypeAdapter.HeaderView) holder).showHeader(header);
                    rv_recommended.post(() -> {
                        /*  float y = rv_recommended.getY() + rv_recommended.getChildAt(position).getY();*/
                        float y = rv_recommended.getChildAt(position).getY();
                        scrollValues.add(String.valueOf((int) y));
                    });
                    break;
                case TYPE_RECOMMENDED:
                    Recommended recommend = (Recommended) menufeed.get(position);
                    Log.d("fdhsfdhsfhsfd", "dfhfdgdf" + visible);
                    ((MultiViewTypeAdapter.RecommendedView) holder).showRecommended(recommend);
                    break;


            }
        }

        @Override
        public int getItemCount() {
            return menufeed.size();
        }

        /*void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }*/
        void visibleContentLayout(String check) {
            Log.d("dsfsdgdsgf", "gsdfsdf" + check);

            if (check.equalsIgnoreCase("false")) {
                // ll_content.setVisibility(View.GONE);
                visible = false;
                check_value = "false";
                notifyDataSetChanged();
            } else {
                //ll_content.setVisibility(View.VISIBLE);
                check_value = "true";
                visible = true;
                notifyDataSetChanged();
            }

        }

        // Invoked by layout manager to create new views
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            // Attach layout for single cell
            int layout;
            RecyclerView.ViewHolder viewHolder;
            // Identify viewType returned by getItemViewType(...)
            // and return ViewHolder Accordingly
            switch (viewType) {
                case TYPE_CALL:
                    layout = R.layout.layout_recommended_items;
                    View callsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder = new MultiViewTypeAdapter.LoaderView(callsView);
                    break;
                case TYPE_DESCRIPTION:
                    layout = R.layout.layout_description_items;
                    View smsView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder = new MultiViewTypeAdapter.DescriptionView(smsView);
                    break;
                case TYPE_Header:
                    layout = R.layout.layout_sub_title_items;
                    View header = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder = new MultiViewTypeAdapter.HeaderView(header);

                    break;
                case TYPE_RECOMMENDED:
                    layout = R.layout.layout_recommended_items;
                    View recommended = LayoutInflater
                            .from(parent.getContext())
                            .inflate(layout, parent, false);
                    viewHolder = new MultiViewTypeAdapter.RecommendedView(recommended);
                    break;
                default:
                    viewHolder = null;
                    break;
            }
            assert viewHolder != null;
            return viewHolder;
        }

        // First ViewHolder of object type Call
        // Reference to the views for each call items to display desired information
        class LoaderView extends RecyclerView.ViewHolder {
            LinearLayout ll_loader;
            LoaderTextView lt_loaderView;
            ProgressBar pb_cart;

            LoaderView(View itemView) {
                super(itemView);
                // Initiate view
                lt_loaderView = itemView.findViewById(R.id.lt_loaderView);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                pb_cart = itemView.findViewById(R.id.pb_cart);
            }

            void showCallDetails() {
                // Attach values for each item
                lt_loaderView.setVisibility(View.VISIBLE);
                ll_loader.setVisibility(View.VISIBLE);

            }

        }

        // Second ViewHolder of object type Description
        // Reference to the views for each call items to display desired information
        class DescriptionView extends RecyclerView.ViewHolder {
            LinearLayout ll_nextAvailable, ll_content, ll_addBtn, ll_loader, ll_addAndMinus;
            ImageView img_VegNonVegType, img_plus, img_minus;
            LoaderTextView lt_loaderView, lt_nextAvailable;
            AC_Textview txt_restaurantName, txt_currency, txt_price, txt_preparation, txt_quantity;
            ProgressBar pb_cart;

            DescriptionView(View itemView) {
                super(itemView);
                // Initiate view
                img_VegNonVegType = itemView.findViewById(R.id.img_VegNonVegType);
                img_plus = itemView.findViewById(R.id.img_plus);
                img_minus = itemView.findViewById(R.id.img_minus);
                txt_restaurantName = itemView.findViewById(R.id.txt_restaurantName);
                txt_currency = itemView.findViewById(R.id.txt_currency);
                txt_price = itemView.findViewById(R.id.txt_price);
                txt_preparation = itemView.findViewById(R.id.txt_preparation);
                ll_content = itemView.findViewById(R.id.ll_content);
                lt_loaderView = itemView.findViewById(R.id.lt_loaderView);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                pb_cart = itemView.findViewById(R.id.pb_cart);
                ll_addBtn = itemView.findViewById(R.id.ll_addBtn);
                ll_addAndMinus = itemView.findViewById(R.id.ll_addAndMinus);
                ll_nextAvailable = itemView.findViewById(R.id.ll_nextAvailable);
                lt_nextAvailable = itemView.findViewById(R.id.lt_nextAvailable);
                txt_quantity = itemView.findViewById(R.id.txt_quantity);


            }

            void showDescription(Description Description) {
                // Attach values for each item
                txt_restaurantName.setText(Description.getProduct_name());
                 txt_currency.setText(FC_Common.currency);
                txt_price.setText(Description.getPrice());
                txt_preparation.setText(Description.getProduct_description());
                txt_quantity.setText(Description.getquantity());
                FC_Common.availabilty_Description = (Description.getAvailability());
                ll_content.setVisibility(View.VISIBLE);
                Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
                ll_menu.startAnimation(animSlideUp1);
                ll_menu.setVisibility(View.VISIBLE);
                lt_loaderView.setVisibility(View.GONE);
                ll_loader.setVisibility(View.GONE);

                if (FC_Common.total_quantity.equalsIgnoreCase("0")) {
                    ll_viewCart.setVisibility(View.GONE);
                } else {
                    ll_viewCart.setVisibility(View.VISIBLE);
                    Animation animSlideUp11 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp11);

                }
                //ll_viewCart.setVisibility(View.VISIBLE);
                pb_cart.setVisibility(View.GONE);

                if (FC_Common.availabilty_Description.equalsIgnoreCase("0")) {
                    ll_nextAvailable.setVisibility(View.VISIBLE);
                    ll_addAndMinus.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.GONE);
                    //lt_nextAvailable.setText(R.string.nextavailable+""+recommened.getNext_avail_time1());
                    lt_nextAvailable.setText(Description.getnext_available());
                } else {
                    ll_nextAvailable.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.VISIBLE);
                    if (Description.getquantity().equalsIgnoreCase("0")) {
                        ll_addBtn.setVisibility(View.VISIBLE);
                        ll_addAndMinus.setVisibility(View.GONE);
                    } else {
                        ll_addBtn.setVisibility(View.GONE);
                        ll_addAndMinus.setVisibility(View.VISIBLE);
                    }
                }
                if (Description.getDish_id().equalsIgnoreCase("1")) {
                    img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                } else {
                    img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                ll_addBtn.setOnClickListener(view -> {
                    if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN")) {
                        FC_Common.hotelpricing = Description.getPrice_status();
                        FC_Common.addonpricing = Description.getAddon_status();
                        FC_Common.productID = Description.getid();
                        FC_Common.quantity = Description.getquantity();
                        FC_Common.price = Integer.parseInt(Description.getPrice());
                        FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        if (FC_Common.hotelpricing.equalsIgnoreCase("1") ||
                                FC_Common.addonpricing.equalsIgnoreCase("1")) {
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + FC_Common.hotelpricing);
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + FC_Common.addonpricing);

                            Bundle bundle = new Bundle();
                            bundle.putString("hotelpricing", FC_Common.hotelpricing);
                            bundle.putString("addonpricing", FC_Common.addonpricing);
                            bundle.putString("hotelid", FC_Common.restaurantid);
                            bundle.putString("productID", FC_Common.productID);
                            bundle.putString("quantity", FC_Common.quantity);

                            BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                            //finish();
                        } else {
                            pb_cart.setVisibility(View.VISIBLE);
                            //FC_Common.quantity="1";
                            int count = Integer.parseInt(Description.getquantity());

                            count = count + 1;
                            FC_Common.hotelpricing = Description.getPrice_status();
                            FC_Common.addonpricing = Description.getAddon_status();
                            FC_Common.productID = Description.getid();
                            FC_Common.quantity = String.valueOf(count);
                              /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                            Log.d("fhdfgdfg","max");
                            if(FC_Common.maximum_order>FC_Common.priceTotal)
                            {
                                Log.d("fhdfgdfg","fdgdgdmin");
                                snackBar("item to be adding please wait");
                                UpdateMenu();
                            }
                            else {
                                snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                            }
                            // }
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + "fail");
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + "fail");
                        }


                    } else {
                        ll_addBtn.setVisibility(View.GONE);
                        snackBar(FC_Common.restaurant_name + " Currently UnAvailable");
                    }

                });

                img_plus.setOnClickListener(view -> {

                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(Description.getquantity());

                    count = count + 1;
                    FC_Common.hotelpricing = Description.getPrice_status();
                    FC_Common.addonpricing = Description.getAddon_status();
                    FC_Common.productID = Description.getid();
                    FC_Common.quantity = String.valueOf(count);
                    FC_Common.price = Integer.parseInt(Description.getPrice());
                    FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                    Utils.log(context, "sdfsdfsdfsdfs" + "count : " + count);
                    Utils.log(context, "sdfsdfsdfsdfs" + "quantity : " + FC_Common.quantity);
                    /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                    Log.d("fhdfgdfg","max");
                    if(FC_Common.maximum_order>FC_Common.priceTotal)
                    {
                        Log.d("fhdfgdfg","fdgdgdmin");
                        UpdateMenu();
                    }
                    else {
                        snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                    }
                    // }


                });
                img_minus.setOnClickListener(v -> {
                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(Description.getquantity());
                    count = count - 1;
                    FC_Common.hotelpricing = Description.getPrice_status();
                    FC_Common.addonpricing = Description.getAddon_status();
                    FC_Common.productID = Description.getid();
                    FC_Common.quantity = String.valueOf(count);
                    UpdateMenu();
                    Utils.log(context, "sdfsdfsdfg" + "count : " + count);
                });


            }


        }

        class HeaderView extends RecyclerView.ViewHolder {

            AC_Textview txt_header;

            HeaderView(View itemView) {
                super(itemView);
                txt_header = itemView.findViewById(R.id.txt_header);

            }

            void showHeader(Header header) {
                // Attach values for each item
                //String categoryname   =header.getCategoryname();
                txt_header.setText(header.getCategoryname());
                txt_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("dfhdfgdf","Dfghsdfsd");

                    }
                });

            }
        }


        class RecommendedView extends RecyclerView.ViewHolder {
            LinearLayout ll_loader, ll_content, ll_addBtn, ll_dishimage, ll_tick, ll_close, ll_addAndMinus, ll_nextAvailable;
            ImageView img_tick, img_close, img_VegNonVegType, img_plus, img_timeOut, img_minus;
            CircleImageView imgcr_dishImage;
            LoaderTextView lt_currency,lt_restaurantname, lt_cuisine, lt_loaderView, lt_nextAvailable;
            LoaderTextView lt_dishPrice;
            ProgressBar pb_cart;
            AC_Textview txt_quantity;

            RecommendedView(View itemView) {
                super(itemView);
                lt_currency = itemView.findViewById(R.id.lt_currency);
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

            @SuppressLint("SetTextI18n")
            void showRecommended(Recommended recommened) {
                // Attach values for each item
                //String categoryname   =recommened.getCategoryname();
                Log.d("check_value", "dfgffdgfd" + check_value);
                lt_loaderView.setVisibility(View.GONE);
                ll_loader.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
                ll_menu.startAnimation(animSlideUp1);
                ll_menu.setVisibility(View.VISIBLE);

                lt_currency.setText(FC_Common.currency);
                lt_restaurantname.setText(recommened.getProduct_name());
                lt_cuisine.setText(recommened.getCuisine_id());
                lt_dishPrice.setText(recommened.getPrice());
                txt_quantity.setText(recommened.getquantity());
                FC_Common.availabilty = recommened.getAvailability();
                Picasso.get().load(recommened.getPhoto()).into(imgcr_dishImage);

                if (!recommened.getquantity().equalsIgnoreCase("0"))
                {
                    img_close.setVisibility(View.VISIBLE);
                    Animation animSlideUp11 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp11);
                }
                else {
                    img_close.setVisibility(View.GONE);
                }
                if (FC_Common.total_quantity.equalsIgnoreCase("0")) {
                    ll_viewCart.setVisibility(View.GONE);
                } else {
                    ll_viewCart.setVisibility(View.VISIBLE);
                    Animation animSlideUp12 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    ll_viewCart.startAnimation(animSlideUp12);

                }
                //ll_viewCart.setVisibility(View.VISIBLE);
                pb_cart.setVisibility(View.GONE);
                Log.d("fdghdfgdfg", "fdgdfgdf" + recommened.getAvailability());
                if (FC_Common.availabilty.equalsIgnoreCase("0")) {
                    ll_nextAvailable.setVisibility(View.VISIBLE);
                    img_timeOut.setVisibility(View.VISIBLE);
                    ll_addAndMinus.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.GONE);
                    //lt_nextAvailable.setText(R.string.nextavailable+""+recommened.getNext_avail_time1());
                    lt_nextAvailable.setText(recommened.getnext_available());
                } else {
                    ll_nextAvailable.setVisibility(View.GONE);
                    img_timeOut.setVisibility(View.GONE);
                    ll_addBtn.setVisibility(View.VISIBLE);
                    if (recommened.getquantity().equalsIgnoreCase("0")) {
                        ll_addBtn.setVisibility(View.VISIBLE);
                        ll_addAndMinus.setVisibility(View.GONE);
                    } else {
                        ll_addBtn.setVisibility(View.GONE);
                        ll_addAndMinus.setVisibility(View.VISIBLE);
                    }
                }

                /**/
                if (recommened.getDish_id().equalsIgnoreCase("1")) {
                    img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                } else {
                    img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                ll_addBtn.setOnClickListener(view -> {
                    if (FC_Common.restaurant_status.equalsIgnoreCase("OPEN")) {
                        FC_Common.hotelpricing = recommened.getPrice_status();
                        FC_Common.addonpricing = recommened.getAddon_status();
                        FC_Common.productID = recommened.getid();
                        FC_Common.quantity = recommened.getquantity();
                        FC_Common.price = Integer.parseInt(recommened.getPrice());
                        FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.productID);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.total_price);
                        Log.d("dfhdfghdfgd", "dfgdfgdfg" + FC_Common.price);
                        Log.d("fhdfgdfg", "priceTotal" + FC_Common.priceTotal);

                        if (FC_Common.hotelpricing.equalsIgnoreCase("1") ||
                                FC_Common.addonpricing.equalsIgnoreCase("1")) {
                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + FC_Common.hotelpricing);
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + FC_Common.addonpricing);

                            Bundle bundle = new Bundle();
                            bundle.putString("hotelpricing", FC_Common.hotelpricing);
                            bundle.putString("addonpricing", FC_Common.addonpricing);
                            bundle.putString("hotelid", FC_Common.restaurantid);
                            bundle.putString("productID", FC_Common.productID);
                            bundle.putString("quantity", FC_Common.quantity);

                            Log.d("fdhgdfgfd","343fghfgdgfd"+FC_Common.quantity);

                            BottomDialogFragmentAddonProducts addPhotoBottomDialogFragment = BottomDialogFragmentAddonProducts.newInstance();
                            addPhotoBottomDialogFragment.setArguments(bundle);
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                            //finish();
                        } else {
                            pb_cart.setVisibility(View.VISIBLE);
                            //FC_Common.quantity="1";
                            int count = Integer.parseInt(recommened.getquantity());

                            count = count + 1;
                            FC_Common.hotelpricing = recommened.getPrice_status();
                            FC_Common.addonpricing = recommened.getAddon_status();
                            FC_Common.productID = recommened.getid();
                            FC_Common.quantity = String.valueOf(count);
                           /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                                Log.d("fhdfgdfg","max");
                                if(FC_Common.maximum_order>FC_Common.priceTotal)
                                {
                                    Log.d("fhdfgdfg","fdgdgdmin");
                                    snackBar("item to be adding please wait");
                                    UpdateMenu();
                                }
                                else {
                                    snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                                }
                           // }


                            Utils.log(context, "sdfsdfsdfs" + " hotelpricing : " + "fail");
                            Utils.log(context, "sdfsdfsdfs" + " addonpricing : " + "fail");
                        }


                    } else {
                        ll_addBtn.setVisibility(View.GONE);
                        snackBar(FC_Common.restaurant_name + " Currently UnAvailable");
                    }

                });

                img_plus.setOnClickListener(view -> {

                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(recommened.getquantity());

                    count = count + 1;
                    FC_Common.hotelpricing = recommened.getPrice_status();
                    FC_Common.addonpricing = recommened.getAddon_status();
                    FC_Common.productID = recommened.getid();
                    FC_Common.quantity = String.valueOf(count);
                    FC_Common.price = Integer.parseInt(recommened.getPrice());
                    FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);
                    Utils.log(context, "sdfsdfsdfsdfs" + "count : " + count);
                    Utils.log(context, "fhdfgdfg" + "priceTotal : " + FC_Common.priceTotal);
                    Utils.log(context, "fhdfgdfg" + "maximum_order : " + FC_Common.maximum_order);
                    /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                    Log.d("fhdfgdfg","max");
                    if(FC_Common.maximum_order>FC_Common.priceTotal)
                    {
                        Log.d("fhdfgdfg","fdgdgdmin");
                        UpdateMenu();
                    }
                    else {
                        snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                    }
                    // }

                });
                img_minus.setOnClickListener(v -> {
                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(recommened.getquantity());
                    count = count - 1;
                    FC_Common.hotelpricing = recommened.getPrice_status();
                    FC_Common.addonpricing = recommened.getAddon_status();
                    FC_Common.productID = recommened.getid();
                    FC_Common.quantity = String.valueOf(count);
                    UpdateMenu();
                    Utils.log(context, "sdfsdfsdfg" + "count : " + count);
                });
                img_tick.setOnClickListener(v -> {
                    pb_cart.setVisibility(View.VISIBLE);
                    int count = Integer.parseInt(recommened.getquantity());
                    count = count + 1;
                    FC_Common.hotelpricing = recommened.getPrice_status();
                    FC_Common.addonpricing = recommened.getAddon_status();
                    FC_Common.productID = recommened.getid();
                    FC_Common.quantity = String.valueOf(count);
                    FC_Common.price = Integer.parseInt(recommened.getPrice());
                    FC_Common.priceTotal=FC_Common.price+Integer.parseInt(FC_Common.total_price);

                    /* if(FC_Common.minimum_order<FC_Common.priceTotal)
                            {*/
                    Log.d("fhdfgdfg","max");
                    if(FC_Common.maximum_order>FC_Common.priceTotal)
                    {
                        snackBar("item to be adding please wait");
                        Log.d("fhdfgdfg","fdgdgdmin");
                        UpdateMenu();
                    }
                    else {
                        snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                    }
                    // }
                    Utils.log(context, "sdfsdfsdfg" + "count : " + count);
                });
                img_close.setOnClickListener(v -> {
                    pb_cart.setVisibility(View.VISIBLE);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    alertDialog.setTitle("Delete item");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are sure You Want Remove From this item in cart");

                    // On pressing the Settings button.
                    alertDialog.setPositiveButton("Delete", (dialog, which) -> {
                        pb_cart.setVisibility(View.VISIBLE);
                        FC_Common.hotelpricing = recommened.getPrice_status();
                        FC_Common.addonpricing = recommened.getAddon_status();
                        FC_Common.productID = recommened.getid();
                        FC_Common.quantity = "0";
                        UpdateMenu();
                    });

                    // On pressing the cancel button
                    alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                    // Showing Alert Message
                    alertDialog.show();

                });

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
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", FC_Common.productID);
                params.put("partner_id", FC_Common.restaurantid);
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
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
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
    private void Favourite(String favouritecode,String url) {
        Utils.playProgressBar(FC_HotelDetailsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            Utils.stopProgressBar();

                            if (favouritecode.equalsIgnoreCase("1"))
                            {
                                Utils.stopProgressBar();
                                AllRestaurantList();
                                snackBar(FC_Common.message);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                            }
                            else {
                                Utils.stopProgressBar();
                                AllRestaurantList();
                                snackBar(FC_Common.message);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites));
                            }

                        } else {
                            Utils.stopProgressBar();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
                    Utils.stopProgressBar();
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant_id", FC_Common.restaurantid);
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


    private void MenuListAsync() {
        // scrollValues.clear();
        Utils.playProgressBar(FC_HotelDetailsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTSHORT,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            menuListObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                MenuListObject playerModel = new MenuListObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setCategory_id(product.getString("category_id"));
                                    playerModel.setCategory_name(product.getString("category_name"));
                                    playerModel.setCount(product.getString("count"));
                                    playerModel.setScrollPostion("500");

                                    menuListObjects.add(playerModel);

                                    if (menuListObjects != null) {
                                        Utils.stopProgressBar();
                                        menuAdapter();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Utils.stopProgressBar();
                                    Log.d("ex", "ex :" + ex);
                                }
                            }
                        } else {
                            Utils.stopProgressBar();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        snackBar("Hotseller" + e);
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            Utils.stopProgressBar();
            snackBar("Hotsellernew" + error_value);

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", FC_Common.restaurantid);
                Utils.log(context, "params: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void menuAdapter() {


        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuListObjects);
        LinearLayoutManager menu = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_menuItem.setLayoutManager(menu);
        rv_menuItem.setAdapter(menuItemAdapter);

        ll_contentMainLayout.setBackgroundColor(getResources().getColor(R.color.txt_lite_gray_color));
    }

    private class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

        private final ArrayList<MenuListObject> menuListObjects;

        MenuItemAdapter(ArrayList<MenuListObject> menuListObjects) {
            this.menuListObjects = menuListObjects;
        }

        @NonNull
        @Override
        public MenuItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_menu_items, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuItemAdapter.ViewHolder holder, int position) {



            Animation animSlideUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
            holder.ll_main.startAnimation(animSlideUp1);
            holder.txt_name.setText(menuListObjects.get(position).getCategory_name());
            holder.txt_size.setText(menuListObjects.get(position).getCount());

            menuListObjects.get(position).setScrollPostion(scrollValues.get(position));


            holder.itemView.setOnClickListener(view -> {

                ll_menuList.setVisibility(View.GONE);
                Animation animSlideUp12 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_menu);
                ll_menu.startAnimation(animSlideUp12);
                ll_menu.setVisibility(View.VISIBLE);
                ll_contentMainLayout.setBackgroundColor(getResources().getColor(R.color.white));
              //  fl_menuLayout.setClickable(false);

                //orderdialog.dismiss();
                Log.d("size", "menuListObjects" + menuListObjects.get(position).getScrollPostion());
                nsv_hotelsDetails.smoothScrollTo(0, Integer.parseInt(menuListObjects.get(position).getScrollPostion()));


            });

        }

        @Override
        public int getItemCount() {
            return menuListObjects.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            AC_Textview txt_name, txt_size;
            LinearLayout ll_main;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                ll_main = itemView.findViewById(R.id.ll_main);
                txt_name = itemView.findViewById(R.id.txt_name);
                txt_size = itemView.findViewById(R.id.txt_size);


            }
        }
    }


    @Override
    protected void onResume() {

        Log.d("fghdfhdfgdf","dfgdfgdfgdfgfd");
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_backBtn:

                onBackPressed();

                break;

            case R.id.ll_menu:

                ll_menuList.setVisibility(View.VISIBLE);
              //  fl_menuLayout.setClickable(true);

              /*  @SuppressLint("InflateParams")
                View view1 = getLayoutInflater().inflate(R.layout.dialogmenuview, null);
                FindViewByIdBottomDialog1(view1);
                MenuListAsync();
                orderdialog = new BottomSheetDialog(context);
                orderdialog.setContentView(view1);
                orderdialog.show();*/

                MenuListAsync();


                break;

            case R.id.ll_viewCartLayoutBtn:
                FC_Common.priceTotal=Integer.parseInt(FC_Common.total_price);
                if(FC_Common.maximum_order>FC_Common.priceTotal)
                {
                    Intent cartIntent = new Intent(context, FC_CartActivity.class);
                    startActivity(cartIntent);
                }
                else {
                    snackBar(getResources().getString(R.string.max_ord)+"  "+FC_Common.currency+" "+FC_Common.maximum_order);
                }


                break;

            case R.id.img_search:


               /* Bundle bundle = new Bundle();
                bundle.putString("restaurant_name",FC_Common.restaurant_name);
                bundle.putString("delivery_estimation",FC_Common.delivery_estimation);
                Fragment SearchFragment = new FC_SearchHotelDetailsFragment();
                FragmentTransaction fragmentTransactionSearch = fragmentManagerHotelDetailsActiviy.beginTransaction();
                fragmentTransactionSearch.replace(R.id.fl_searchLayout, SearchFragment);
                SearchFragment.setArguments(bundle);
                fragmentTransactionSearch.addToBackStack(null);
                fragmentTransactionSearch.commit();*/
                Intent restaurant = new Intent(context, FC_SearchHotelDetailsActivity.class);
                restaurant.putExtra("restaurant_name",FC_Common.restaurant_name);
                restaurant.putExtra("delivery_estimation",FC_Common.delivery_estimation);
                FC_Common.searchlist="";
                startActivity(restaurant);

                break;
           /* case R.id.vector_android_button:
                favorites_on
                break;*/
        }

    }

    @Override
    public void onDestroy() {

        // handler.removeCallbacks(handler);
        super.onDestroy();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void ItemDialog() {
        DialogPlus itemdialog = DialogPlus.newDialog(Objects.requireNonNull(context))
                .setContentHolder(new ViewHolder(R.layout.dialogmenuview))
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnItemClickListener((dialogPlus, item, view, position) -> {
                }).setExpanded(false).create();
        itemdialog.show();

        rv_menuItem = (RecyclerView) itemdialog.findViewById(R.id.rv_menuItem);

        MenuListAsync();
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
    private void FindViewByIdBottomDialog1(View view) {

        rv_menuItem = view.findViewById(R.id.rv_menuItem);
    }


    private CompoundButton.OnCheckedChangeListener onCheckedChanged() {
        return (buttonView, isChecked) -> {
            switch (buttonView.getId()) {
                case R.id.sw_vegNonveg:
                    setButtonState(isChecked);
                    break;
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

    @SuppressLint("ObsoleteSdkInt")
    private void makeEffect(final LinearLayout layout, int cx, int cy) {

        int radius = Math.max(layout.getWidth(), layout.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                layout.setVisibility(View.VISIBLE);
                animator.start();
                invalidateOptionsMenu();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        layout.setVisibility(View.INVISIBLE);
                        invalidateOptionsMenu();
                        hidden = true;
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();
            }
        } else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
                layout.setVisibility(View.VISIBLE);
                anim.start();
                invalidateOptionsMenu();
                hidden = false;

            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;
                        invalidateOptionsMenu();
                    }
                });
                anim.start();
            }
        }
    }

}