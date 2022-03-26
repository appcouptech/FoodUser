package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentGreatOffersActivity.FCGreatOfferForSetOFHotelsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCPojo.FCSetOfHotelsOfferObjects.SetOfHotelsOfferObjects;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_SetOfHotelsOfferActivity extends AppCompatActivity {

    private NestedScrollView nsv_hotels;
    private StatefulRecyclerView rv_setOfHotels;
    Snackbar bar;
    private LinearLayout ll_toolbarLayout;
    private LinearLayout ll_mainLayout;
    private LinearLayout ll_filter;
    private SetOfHotelsOfferAdapter setOfHotelsOfferAdapter;
    private ArrayList<SetOfHotelsOfferObjects> hotelsOfferObjects ;
    private String TAG = "scroll : ";
    private Rect scrollBounds = new Rect();
    private ImageView img_backBtn ,img_back,img_banner ;
    private LoaderTextView lt_restaurantCount;
    private View parentLayout;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_SetOfHotelsOfferActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_fc__set_of_hotels_offer);
context=FC_SetOfHotelsOfferActivity.this;


        FindViewById();
        nsv_hotels.getHitRect(scrollBounds);

        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FC_Common.bannerid = (String) bundle.get("bannerid");
            FC_Common.bannerdevicesize = (String) bundle.get("sizes");

            Log.d("fhdfgdfg", "dfgdfgfd" + FC_Common.bannerid);
        }

        nsv_hotels.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (ll_filter != null) {
                if (ll_filter.getLocalVisibleRect(scrollBounds)) {
                    layoutChange2();
                } else {
                    Log.i(TAG, "No");
                    layoutChange();
                }
            }

        });

        img_backBtn.setOnClickListener(v -> onBackPressed());

        img_back.setOnClickListener(v -> onBackPressed());


   /*     ll_filterBtn.setOnClickListener(view -> {

            bottomSheetValue = 1;
            BottomDialogForFilter filtervegBottomDialogFragment = BottomDialogForFilter.newInstance();
            filtervegBottomDialogFragment.onclick(FC_SetOfHotelsOfferActivity.this,bottomSheetValue);
            filtervegBottomDialogFragment.show(getSupportFragmentManager(),
                    "dialog_fragment");

        });*/

      /*  ll_toolBarFilterBtn.setOnClickListener(view -> {

            bottomSheetValue = 1;
            BottomDialogForFilter filtervegBottomDialogFragment = BottomDialogForFilter.newInstance();
            filtervegBottomDialogFragment.onclick(FC_SetOfHotelsOfferActivity.this,bottomSheetValue);
            filtervegBottomDialogFragment.show(getSupportFragmentManager(),
                    "dialog_fragment");
        });*/
        SetOfhotelsRecycler();
        SetofHotelsList();

        setOfHotelsOfferAdapter = new SetOfHotelsOfferAdapter(hotelsOfferObjects);
        LinearLayoutManager menu = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_setOfHotels.setLayoutManager(menu);
        rv_setOfHotels.setAdapter(setOfHotelsOfferAdapter);

     /*   handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something

                if (counter > 5) {
                    counter = 0;
                    SetofHotelsList();
                }
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/
    }

    private void layoutChange2() {
        ll_toolbarLayout.setVisibility(View.GONE);
        ll_mainLayout = new LinearLayout(this, null, 0, R.style.FCAppTheme);
    }

    private void layoutChange() {
        ll_toolbarLayout.setVisibility(View.VISIBLE);
        ll_mainLayout = new LinearLayout(this, null, 0, R.style.FCAppSetOfHotelsToolbar);
    }

    private void FindViewById() {
        parentLayout = findViewById(android.R.id.content);
        nsv_hotels = findViewById(R.id.nsv_hotels);
        img_banner = findViewById(R.id.img_banner);
        lt_restaurantCount = findViewById(R.id.lt_restaurantCount);
        rv_setOfHotels = findViewById(R.id.rv_setOfHotels);
        img_backBtn = findViewById(R.id.img_backBtn);
        img_back = findViewById(R.id.img_back);

        ll_mainLayout = findViewById(R.id.ll_mainLayout);
        ll_filter = findViewById(R.id.ll_filter);
        ll_toolbarLayout = findViewById(R.id.ll_toolbarLayout);
        hotelsOfferObjects = new ArrayList<>();
        SetOfHotelsOfferObjects offerObjects = new SetOfHotelsOfferObjects();
        offerObjects.setD_images("");
        offerObjects.setD_info("");
        hotelsOfferObjects.add(offerObjects);
        offerObjects.setD_images("");
        offerObjects.setD_info("");
        hotelsOfferObjects.add(offerObjects);

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    //AllRestaurant Async Task Start//
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void SetofHotelsList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_BANNERRESTAURANT,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            FC_Common.bannerRestaurantcount = obj.getString("restaurant_count");
                            FC_Common.bannerRestaurantImage= obj.getString("banner_img");
                            Picasso.get().load(FC_Common.bannerRestaurantImage)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(img_banner);

                            lt_restaurantCount.setText(FC_Common.bannerRestaurantcount+" - "+getResources().
                                    getString(R.string.restaurants));
                            hotelsOfferObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                SetOfHotelsOfferObjects playerModel = new SetOfHotelsOfferObjects();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setPerson_limit(product.getString("person_limit"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setRestaurant_latitude(product.getString("restaurant_latitude"));
                                    playerModel.setRestaurant_longitude(product.getString("restaurant_longitude"));
                                    playerModel.setDish_id(product.getString("dish_id"));
                                    playerModel.setDistance(product.getString("distance"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setRestaurant_status(product.getString("restaurant_status"));
                                    hotelsOfferObjects.add(playerModel);
                                    if (hotelsOfferObjects != null) {

                                        setOfHotelsOfferAdapter.visibleContentLayout();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                   /* final int counter_Resataurant = counter++;
                                    if (counter_Resataurant < 5) {
                                        SetofHotelsList();
                                    } */
                                }}
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                       /* final int counter_Resataurant = counter++;
                        if (counter_Resataurant < 5) {
                            SetofHotelsList();
                        }*/
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
           /* final int counter_Resataurant = counter++;
            if (counter_Resataurant < 5) {
                SetofHotelsList();
            }*/
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("banner_id", FC_Common.bannerid);
                params.put("banner_sizes", FC_Common.bannerdevicesize);
                Utils.log(context,"params: "+params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("dfgfdgdfgdf","dfgdfgdf"+params);
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

    private void SetOfhotelsRecycler() {
        setOfHotelsOfferAdapter = new SetOfHotelsOfferAdapter(hotelsOfferObjects);
        rv_setOfHotels.setAdapter(setOfHotelsOfferAdapter);
        rv_setOfHotels.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }
    private class SetOfHotelsOfferAdapter extends RecyclerView.Adapter<SetOfHotelsOfferAdapter.ViewHolder>{
        private final ArrayList<SetOfHotelsOfferObjects> hotelsOfferObjects;
        boolean visible;

        SetOfHotelsOfferAdapter( ArrayList<SetOfHotelsOfferObjects> hotelsOfferObjects) {
            this.hotelsOfferObjects = hotelsOfferObjects ;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_set_of_hotels_items, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.lm_imageDish.setImageDrawable(getDrawable(R.drawable.test_item));
                FC_Common.setofhotels_status=hotelsOfferObjects.get(position).getRestaurant_status();
                Picasso.get().load(hotelsOfferObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_costlimit.setText(hotelsOfferObjects.get(position).getCost_limit());
                holder.lt_hotelName.setText(hotelsOfferObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(hotelsOfferObjects.get(position).getCuisines());
                holder.lt_currency.setText(hotelsOfferObjects.get(position).getCurrency());
                holder.lt_deliveryEstimation.setText(hotelsOfferObjects.get(position).getDelivery_estimation());

                holder.txt_rating.setText(hotelsOfferObjects.get(position).getRating());
                Double doublestax = Double.parseDouble(holder.txt_rating.getText().toString());
                holder.txt_rating.setText(String.format("%.1f", doublestax));
                Double double_test=Double.parseDouble(holder.txt_rating.getText().toString());
                Double double_one=1.0;
                Double double_two=2.0;
                Double double_three=3.0;
                Double double_four=4.0;
                Double double_five=5.0;

                if (double_test<=double_one){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate1));
                }
                else if (double_test<=double_two){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate2));
                    Log.d("fgujfghfg","2132132dfgdfg"+doublestax);
                }
                else if (double_test<=double_three){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate3));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_four){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate4));
                    Log.d("fgujfghfg","213dfgdfg"+doublestax);
                }
                else if (double_test<=double_five){
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_rate5));
                }
                else{
                    holder.ll_rating.setBackgroundColor(getResources().getColor(R.color.txt_normal));
                }

                if (FC_Common.setofhotels_status.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                } else if (FC_Common.setofhotels_status.equalsIgnoreCase("CLOSED")) {
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }
                holder.ll_itemViewFocus.setOnClickListener(view -> {
                    FC_Common.restaurantid=hotelsOfferObjects.get(position).getId();
                    Intent hoteldetails = new Intent(FC_SetOfHotelsOfferActivity.this, FC_HotelDetailsActivity.class);
                    hoteldetails.putExtra("hotelid", FC_Common.restaurantid);
                    hoteldetails.putExtra("recent_search","0");
                    startActivity(hoteldetails);
                });
            }
        }

        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return hotelsOfferObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LinearLayout ll_content,ll_itemViewFocus,ll_rating;
            LoaderTextView lt_hotelName,lt_cuisine,lt_deliveryEstimation,lt_currency,lt_costlimit;
            AC_Textview txt_rating,txt_timeOut;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                ll_rating = itemView.findViewById(R.id.ll_rating);
                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                txt_rating = itemView.findViewById(R.id.txt_rating);
                lt_hotelName = itemView.findViewById(R.id.lt_hotelName);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_itemViewFocus = itemView.findViewById(R.id.ll_itemViewFocus);
            }
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
}
