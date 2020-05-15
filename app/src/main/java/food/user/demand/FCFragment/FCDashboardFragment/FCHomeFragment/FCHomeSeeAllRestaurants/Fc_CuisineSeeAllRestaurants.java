package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragment.FCHomeSeeAllRestaurants;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCPojo.FCHomeFragmentObject.AllRestaurantObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCUtils.StatefulRecyclerView.StatefulRecyclerView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class Fc_CuisineSeeAllRestaurants extends AppCompatActivity {
    InputMethodManager inputMgr;
    View parentLayout;
    Snackbar bar;
    Handler handler;
    private int allresataurantcounter = 0;
    StatefulRecyclerView rv_allRestaurants;
    Context context;
    private AllRestaurantAdapter allRestaurantAdapter;
    private ArrayList<AllRestaurantObject> allRestaurantObjects;
    ImageView img_backBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_home_see_all_restaurants);
        context= Fc_CuisineSeeAllRestaurants.this;
        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FindviewById();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Utils.log(context,"FC_Common.latitude :"+FC_Common.latitude);
        Utils.log(context,"FC_Common.longitude :"+FC_Common.longitude);
        Utils.log(context,"FC_Common.access_token :"+FC_Common.access_token);
        if (bundle != null)
        {
            FC_Common.CuisineItemId = (String) bundle.get("cuisines");
        }
        //RecyclerView Adapter//
        AllRestaurantRecycler();
        //RecyclerView List//
        AllRestaurantList();

        /*handler = new Handler();
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

        img_backBtn.setOnClickListener(view -> {

            onBackPressed();
        });

        //allRestaurantAdapter  Set//
        allRestaurantAdapter = new AllRestaurantAdapter(Fc_CuisineSeeAllRestaurants.this, allRestaurantObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(Fc_CuisineSeeAllRestaurants.this, LinearLayoutManager.VERTICAL, false);
        rv_allRestaurants.setLayoutManager(itemViewLLres);
        rv_allRestaurants.setAdapter(allRestaurantAdapter);
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

        allRestaurantObjects = new ArrayList<>();
        AllRestaurantObject object = new AllRestaurantObject();
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);
        object.setD_images("");
        object.setD_images("");
        allRestaurantObjects.add(object);
    }

    //AllRestaurant Async Task Start//
    private void AllRestaurantList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_CUISNELISTREST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.log(context, "URL_CUISNELISTREST" + response);
                        Utils.log(context, "URL_CUISNELISTREST" + FC_URL.URL_CUISNELISTREST);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("success").equals("1")) {
                                //LocationObject.clear();
                                JSONArray dataArray = obj.getJSONArray("data");
                                allRestaurantObjects.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    AllRestaurantObject playerModel = new AllRestaurantObject();
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
                                        allRestaurantObjects.add(playerModel);

                                        if (allRestaurantObjects != null) {
                                            allRestaurantAdapter.visibleContentLayout();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                        /*final int counter_AllResataurant = allresataurantcounter++;
                                        Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                                        if (counter_AllResataurant<5) {
                                            AllRestaurantList();
                                        }*/

                                    }
                                }

                            }
                            else {
                                /*final int counter_AllResataurant = allresataurantcounter++;
                                Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                                if (counter_AllResataurant<5) {
                                    AllRestaurantList();
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            snackBar("Hotseller"+String.valueOf(e));
                          /*  final int counter_AllResataurant = allresataurantcounter++;
                            Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                            if (counter_AllResataurant<5) {
                                AllRestaurantList();
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
               /* final int counter_AllResataurant = allresataurantcounter++;
                Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                if (counter_AllResataurant<5) {
                    AllRestaurantList();
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("cuisine_id", FC_Common.CuisineItemId);
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Fc_CuisineSeeAllRestaurants.this).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Utils.log(context,"dgsdgsdfgsd"+ex);
        }

    }
    private void AllRestaurantRecycler() {
        allRestaurantAdapter = new AllRestaurantAdapter(Fc_CuisineSeeAllRestaurants.this, allRestaurantObjects);
        rv_allRestaurants.setAdapter(allRestaurantAdapter);
        rv_allRestaurants.setLayoutManager(new LinearLayoutManager(Fc_CuisineSeeAllRestaurants.this, LinearLayoutManager.VERTICAL, false));

    }
    private class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.ViewHolder> {
        private final FragmentActivity activity;
        private final ArrayList<AllRestaurantObject> allRestaurantObjects;
        boolean visible;

        AllRestaurantAdapter(FragmentActivity activity, ArrayList<AllRestaurantObject> allRestaurantObjects) {
            this.activity = activity;
            this.allRestaurantObjects = allRestaurantObjects;
        }

        @NonNull
        @Override
        public AllRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_all_restaurant, parent, false);
            AllRestaurantAdapter.ViewHolder viewHolder = new AllRestaurantAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull AllRestaurantAdapter.ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                // holder.lm_imageDish.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.test_item));

                FC_Common.Allrestaurant_status=allRestaurantObjects.get(position).getRestaurant_status();
                //Picasso.get().load(allRestaurantObjects.get(position).getRestaurant_logo()).into(holder.lm_imageDish);
                Picasso.get().load(allRestaurantObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(allRestaurantObjects.get(position).getRestaurant_name());
                holder.txt_cuisines.setText(allRestaurantObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(allRestaurantObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(allRestaurantObjects.get(position).getCurrency());
                holder.txt_ratings.setText(allRestaurantObjects.get(position).getRating());
                holder.lt_costlimit.setText(allRestaurantObjects.get(position).getCost_limit()+" For "+allRestaurantObjects.get(position).getPerson_limit());
                Double doublestax =Double.parseDouble(holder.txt_ratings.getText().toString());
                holder.txt_ratings.setText(String.format("%.1f", doublestax));
                if (FC_Common.Allrestaurant_status.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                }
                else if (FC_Common.Allrestaurant_status.equalsIgnoreCase("CLOSED")){
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }

                holder.ll_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FC_Common.restaurantid=allRestaurantObjects.get(position).getId();
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
            return allRestaurantObjects.size();
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
            LoaderImageView lm_imageDish;
            LoaderTextView lt_restaurantName,lt_deliveryEstimation,lt_currency,lt_costlimit;
            AC_Textview txt_cuisines,txt_ratings,txt_timeOut;
            LinearLayout ll_content,ll_main;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                ll_main = itemView.findViewById(R.id.ll_main);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                txt_cuisines = itemView.findViewById(R.id.txt_cuisines);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                ll_content = itemView.findViewById(R.id.ll_content);


            }
        }
    }
    //AllRestaurant Async Task End//
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
