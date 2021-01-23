package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import food.user.demand.FCPojo.FCRestaurantOfferObject.RestaurantOfferObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FC_RestaurantOfferFragment extends Fragment {

    private RecyclerView rv_restaurantOffers;
    private RestaurantOfferAdapter restaurantOfferAdapter;
    private ArrayList<RestaurantOfferObject> restaurantOfferObjects;

    public FC_RestaurantOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Utils.adjustFontScale(Objects.requireNonNull(getActivity()),getResources().getConfiguration());
        return inflater.inflate(R.layout.fragment_restaurant_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FindViewById(view);

        AllRestaurantList();
        AllRestaurantRecycler();
        restaurantOfferAdapter = new RestaurantOfferAdapter(restaurantOfferObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_restaurantOffers.setLayoutManager(itemViewLLres);
        rv_restaurantOffers.setAdapter(restaurantOfferAdapter);

    }
    private void FindViewById(View view) {
        rv_restaurantOffers = view.findViewById(R.id.rv_restaurantOffers);

        restaurantOfferObjects = new ArrayList<>();
        RestaurantOfferObject offerObject = new RestaurantOfferObject();
        offerObject.setD_images("");
        offerObject.setD_images("");
        restaurantOfferObjects.add(offerObject);
        offerObject.setD_images("");
        offerObject.setD_images("");
        restaurantOfferObjects.add(offerObject);
        offerObject.setD_images("");
        offerObject.setD_images("");
        restaurantOfferObjects.add(offerObject);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    private void AllRestaurantList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ALLRESTAURANTLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            restaurantOfferObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                RestaurantOfferObject playerModel = new RestaurantOfferObject();
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
                                    playerModel.setDiscount_text(product.getString("discount_text"));
                                    restaurantOfferObjects.add(playerModel);
                                    if (restaurantOfferObjects != null) {
                                        restaurantOfferAdapter.visibleContentLayout();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                     }}
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                         }
                }, error -> {
            //String error_value = String.valueOf(error);

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                Utils.log(getContext(),"params: "+params);
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()).getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void AllRestaurantRecycler() {
        restaurantOfferAdapter = new RestaurantOfferAdapter(restaurantOfferObjects);
        rv_restaurantOffers.setAdapter(restaurantOfferAdapter);
        rv_restaurantOffers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private class RestaurantOfferAdapter extends RecyclerView.Adapter<RestaurantOfferAdapter.ViewHolder> {
        private final ArrayList<RestaurantOfferObject> restaurantOfferObjects;
        boolean visible;

        RestaurantOfferAdapter(ArrayList<RestaurantOfferObject> restaurantOfferObjects) {
            this.restaurantOfferObjects = restaurantOfferObjects;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_restaurant_offer_items, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
               // holder.lm_imageDish.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.test_item));

                holder.ll_content.setVisibility(View.VISIBLE);
                FC_Common.restaurant_offerstatus = restaurantOfferObjects.get(position).getRestaurant_status();
                Picasso.get().load(restaurantOfferObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lm_imageDish);
                holder.lt_restaurantName.setText(restaurantOfferObjects.get(position).getRestaurant_name());
                holder.lt_cuisines.setText(restaurantOfferObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(restaurantOfferObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(restaurantOfferObjects.get(position).getCurrency());

                holder.lt_costlimit.setText(restaurantOfferObjects.get(position).getCost_limit() + " For " + restaurantOfferObjects.get(position).getPerson_limit());

                holder.txt_ratings.setText(restaurantOfferObjects.get(position).getRating());
                Double doublestax = Double.parseDouble(holder.txt_ratings.getText().toString());
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


                if (FC_Common.restaurant_offerstatus.equalsIgnoreCase("OPEN")) {
                    holder.txt_timeOut.setVisibility(View.GONE);
                } else if (FC_Common.restaurant_offerstatus.equalsIgnoreCase("CLOSED")) {
                    holder.txt_timeOut.setVisibility(View.VISIBLE);
                }
                if (restaurantOfferObjects.get(position).getDiscount_text().equalsIgnoreCase("0"))
                {
                    holder.ll_coupon.setVisibility(View.GONE);
                }
                else {
                    holder.ll_coupon.setVisibility(View.VISIBLE);
                    holder.txt_couponcode.setText(restaurantOfferObjects.get(position).getDiscount_text());
                }
                holder.ll_main.setOnClickListener(view -> {
                    FC_Common.restaurantid=restaurantOfferObjects.get(position).getId();
                    Intent restaurant = new Intent(getContext(), FC_HotelDetailsActivity.class);
                    restaurant.putExtra("hotelid",FC_Common.restaurantid);
                    restaurant.putExtra("recent_search","0");
                    startActivity(restaurant);
                });
            }

        }

        @Override
        public int getItemCount() {
            return restaurantOfferObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageDish;
            LoaderTextView lt_restaurantName, lt_deliveryEstimation, lt_currency, lt_costlimit,lt_cuisines;
            AC_Textview txt_couponcode, txt_ratings,txt_timeOut;
            LinearLayout ll_coupon,ll_content, ll_main,ll_rating;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                ll_coupon = itemView.findViewById(R.id.ll_coupon);
                txt_couponcode = itemView.findViewById(R.id.txt_couponcode);
                lm_imageDish = itemView.findViewById(R.id.lm_imageDish);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                lt_cuisines = itemView.findViewById(R.id.lt_cuisines);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                txt_timeOut = itemView.findViewById(R.id.txt_timeOut);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_main = itemView.findViewById(R.id.ll_main);
            }
        }
    }
}
