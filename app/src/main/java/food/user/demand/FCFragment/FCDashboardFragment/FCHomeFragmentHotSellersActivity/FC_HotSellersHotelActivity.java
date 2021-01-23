package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotSellersActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import food.user.demand.FCPojo.FCHotSellersHotelActivityObject.HotSellersHotelObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_HotSellersHotelActivity extends AppCompatActivity {

    ImageView img_backBtn ;
    StatefulRecyclerView rv_allRestaurants;
    InputMethodManager inputMgr;
    View parentLayout;
    Snackbar bar;
    Handler handler;
    private int allhotsellercounter = 0;
    Context context;
    ArrayList<HotSellersHotelObject> hotSellersHotelObjects;
    private HotSellersHotelAdapter hotSellersHotelAdapter ;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_HotSellersHotelActivity.this,getResources().getConfiguration());
        setContentView(R.layout.fc_home_see_all_restaurants);
        context=FC_HotSellersHotelActivity.this;
        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FindviewById();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Utils.log(context,"FC_Common.latitude :"+ FC_Common.latitude);
        Utils.log(context,"FC_Common.longitude :"+FC_Common.longitude);
        Utils.log(context,"FC_Common.access_token :"+FC_Common.access_token);
        if (bundle != null)
        {
        }
        //RecyclerView Adapter//
        AllHotSellerRecycler();
        //RecyclerView List//
        AllHotsellerList();

       /* handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something

                if (allhotsellercounter>5)
                {
                    allhotsellercounter=0;
                    AllHotsellerList();
                }
                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

        img_backBtn.setOnClickListener(view -> {

            onBackPressed();
        });

        //allHotsellerRestaurantAdapter  Set//
        hotSellersHotelAdapter = new HotSellersHotelAdapter(this,hotSellersHotelObjects);
        rv_allRestaurants.setAdapter(hotSellersHotelAdapter);
        rv_allRestaurants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


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

    private void FindviewById() {
        parentLayout = findViewById(android.R.id.content);
        rv_allRestaurants=findViewById(R.id.rv_allRestaurants);

        img_backBtn=findViewById(R.id.img_backBtn);

        hotSellersHotelObjects = new ArrayList<>();
        HotSellersHotelObject object = new HotSellersHotelObject();
        object.setD_images("");
        object.setD_images("");
        hotSellersHotelObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        hotSellersHotelObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        hotSellersHotelObjects.add(object);
    }


    //AllRestaurant Async Task Start//
    private void AllHotsellerList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_HOTSELLERLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.log(context, "URL_HOTSELLERLIST" + response);
                        Utils.log(context, "URL_HOTSELLERLIST" + FC_URL.URL_HOTSELLERLIST);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("success").equals("1")) {
                                //LocationObject.clear();
                                JSONArray dataArray = obj.getJSONArray("data");
                                hotSellersHotelObjects.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    HotSellersHotelObject playerModel = new HotSellersHotelObject();
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
                                        playerModel.setDistance(product.getString("distance"));
                                        playerModel.setCuisines(product.getString("cuisines"));
                                        playerModel.setCurrency(product.getString("currency"));
                                        playerModel.setRestaurant_status(product.getString("restaurant_status"));
                                        hotSellersHotelObjects.add(playerModel);

                                        if (hotSellersHotelObjects != null) {
                                            hotSellersHotelAdapter.visibleContentLayout();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                       /* final int counter_Hotseller = allhotsellercounter++;
                                        Utils.log(context, "countervalue" +"A:"+counter_Hotseller);
                                        if (counter_Hotseller<5) {
                                            AllHotsellerList();
                                        }*/

                                    }
                                }

                            }
                            else {
                                /*final int counter_Hotseller = allhotsellercounter++;
                                Utils.log(context, "countervalue" +"A:"+counter_Hotseller);
                                if (counter_Hotseller<5) {
                                    AllHotsellerList();
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            snackBar("Hotseller"+String.valueOf(e));
                           /* final int counter_Hotseller = allhotsellercounter++;
                            Utils.log(context, "countervalue" +"A:"+counter_Hotseller);
                            if (counter_Hotseller<5) {
                                AllHotsellerList();
                            }*/
                            Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                            Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                String error_value = String.valueOf(error);
                snackBar("Hotsellernew"+error_value);
                Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
                Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
               /* final int counter_Hotseller = allhotsellercounter++;
                Utils.log(context, "countervalue" +"A:"+counter_Hotseller);
                if (counter_Hotseller<5) {
                    AllHotsellerList();
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("type", "all");
                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
    private void AllHotSellerRecycler() {
        hotSellersHotelAdapter = new HotSellersHotelAdapter(this,hotSellersHotelObjects);
        rv_allRestaurants.setAdapter(hotSellersHotelAdapter);
        rv_allRestaurants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }
    private class HotSellersHotelAdapter extends RecyclerView.Adapter<HotSellersHotelAdapter.ViewHolder> {

        private final FC_HotSellersHotelActivity activity;
        private final ArrayList<HotSellersHotelObject> hotSellersHotelObjects;
        boolean visible;

        public HotSellersHotelAdapter(FC_HotSellersHotelActivity activity, ArrayList<HotSellersHotelObject> hotSellersHotelObjects) {
            this.activity = activity ;
            this.hotSellersHotelObjects = hotSellersHotelObjects ;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_hot_seller_all_hotel_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.AllHostseller_status=hotSellersHotelObjects.get(position).getRestaurant_status();
                //Picasso.get().load(hotSellersHotelObjects.get(position).getRestaurant_logo()).into(holder.lm_imageDish);
                Picasso.get().load(hotSellersHotelObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(hotSellersHotelObjects.get(position).getRestaurant_name());
                holder.lt_cuisines.setText(hotSellersHotelObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(hotSellersHotelObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(hotSellersHotelObjects.get(position).getCurrency());
                holder.lt_costlimit.setText(hotSellersHotelObjects.get(position).getCost_limit()+" For "+
                        hotSellersHotelObjects.get(position).getPerson_limit());
                holder.txt_ratings.setText(hotSellersHotelObjects.get(position).getRating());
                Double doublestax =Double.parseDouble(holder.txt_ratings.getText().toString());
                holder.txt_ratings.setText(String.format("%.1f", doublestax));

                Double double_test=Double.parseDouble(holder.txt_ratings.getText().toString());
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

                if (FC_Common.AllHostseller_status.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                }
                else if (FC_Common.AllHostseller_status.equalsIgnoreCase("CLOSED")){
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }

                holder.ll_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FC_Common.restaurantid=hotSellersHotelObjects.get(position).getId();
                        Utils.log(context,"sdfsdfsdfsd"+FC_Common.restaurantid);
                        Intent restaurant = new Intent(activity, FC_HotelDetailsActivity.class);
                        restaurant.putExtra("hotelid",FC_Common.restaurantid);
                        restaurant.putExtra("recent_search","0");
                        startActivity(restaurant);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return hotSellersHotelObjects.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LoaderTextView lt_restaurantName,lt_deliveryEstimation,lt_currency,lt_costlimit,lt_cuisines;
            AC_Textview txt_ratings,txt_timeOut;
            LinearLayout ll_content,ll_main,ll_rating;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                ll_rating = itemView.findViewById(R.id.ll_rating);
                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                ll_main = itemView.findViewById(R.id.ll_main);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                lt_cuisines = itemView.findViewById(R.id.lt_cuisines);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                ll_content = itemView.findViewById(R.id.ll_content);
            }
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
}
