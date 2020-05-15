package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity;

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

import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCPojo.FCFavoritesObject.FavoritesObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.R;

public class FC_FavoritesActivity extends AppCompatActivity {

    private RecyclerView rv_favorites ;
    private FavoritesAdapter favoritesAdapter;
    ArrayList<FavoritesObject> favoritesObjects ;
    ImageView img_backBtn ;
    Context context;
    View parentLayout;
    Snackbar bar;
    AC_Textview txt_emptyview;
    private int counter = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc__favorites);
        context=FC_FavoritesActivity.this;
        FindViewById();

        favoritesAdapter = new FavoritesAdapter(favoritesObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_favorites.setLayoutManager(itemViewLLres);
        rv_favorites.setAdapter(favoritesAdapter);

        img_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        FavouriteList();

       /* handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (counter > 5) {
                    counter = 0;
                    FavouriteList();
                }

                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }

    private void FindViewById() {
        txt_emptyview = findViewById(R.id.txt_emptyview);
        rv_favorites = findViewById(R.id.rv_favorites);
        parentLayout = findViewById(android.R.id.content);
        img_backBtn = findViewById(R.id.img_backBtn);

        txt_emptyview.setText(getResources().getString(R.string.favouritelist));
        favoritesObjects = new ArrayList<>();
        FavoritesObject object = new FavoritesObject();
        object.setD_images("");
        object.setD_info("");
        favoritesObjects.add(object);
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


    //Pastorders Async Task Start//
    private void FavouriteList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_FAVOURITELIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            txt_emptyview.setVisibility(View.GONE);
                            rv_favorites.setVisibility(View.VISIBLE);
                            JSONArray dataArray = obj.getJSONArray("data");
                            favoritesObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                FavoritesObject playerModel = new FavoritesObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setRestaurant_status(product.getString("restaurant_status"));

                                    favoritesObjects.add(playerModel);
                                    if (favoritesObjects != null) {

                                        favoritesAdapter.visibleContentLayout();
                                    }
                                    else {
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_favorites.setVisibility(View.GONE);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    snackBar(String.valueOf(ex));
                                    /*final int counter_pastorder = counter++;
                                    if (counter_pastorder < 5) {
                                        FavouriteList();
                                    }*/
                                }}
                        } else {

                          /*  final int counter_pastorder = counter++;
                            if (counter_pastorder < 5) {
                                FavouriteList();
                            }*/
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_favorites.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                       /* final int counter_pastorder = counter++;
                        if (counter_pastorder < 5) {
                            FavouriteList();
                        }*/
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
           /* final int counter_pastorder = counter++;
            if (counter_pastorder < 5) {
                FavouriteList();
            }*/
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("sdfsdfsdfsdf","sdfsdfsd"+params);
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
    private class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
        private final ArrayList<FavoritesObject> favoritesObjects;
        private boolean visible;

        FavoritesAdapter(ArrayList<FavoritesObject> favoritesObjects) {
            this.favoritesObjects = favoritesObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_favorites, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.lm_imageDish.setImageDrawable(getDrawable(R.drawable.test_item));

                Picasso.get().load(favoritesObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(favoritesObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(favoritesObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(favoritesObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(favoritesObjects.get(position).getCurrency());
                holder.lt_costlimit.setText(favoritesObjects.get(position).getCost_limit());

                holder.txt_rating.setText(favoritesObjects.get(position).getRating());
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
                holder.ll_mainLayout.setOnClickListener(view -> {
                    Log.d("sadf","df");
                    FC_Common.restaurantid=favoritesObjects.get(position).getId();
                    Intent HotelDetailsIntent = new Intent(FC_FavoritesActivity.this, FC_HotelDetailsActivity.class);
                    HotelDetailsIntent.putExtra("hotelid", FC_Common.restaurantid);
                    HotelDetailsIntent.putExtra("recent_search","0");
                    startActivity(HotelDetailsIntent);


                });
            }
        }

        @Override
        public int getItemCount() {
            return favoritesObjects.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LinearLayout ll_mainLayout,ll_rating,ll_content;
            AC_Textview txt_rating;
            LoaderTextView lt_currency,lt_costlimit,lt_restaurantName,lt_cuisine,lt_deliveryEstimation;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                ll_mainLayout = itemView.findViewById(R.id.ll_mainLayout);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                txt_rating = itemView.findViewById(R.id.txt_rating);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
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
