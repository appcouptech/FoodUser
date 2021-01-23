package food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentViewOnMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
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
import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCHomeFragmentHotelDetailsActivity.FC_HotelDetailsActivity;
import food.user.demand.FCPojo.FCHomeFragmentViewOnMapObject.FC_ViewOnMapObject;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_ViewOnMapActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private RecyclerView rv_hotelDetails;
    private ArrayList<FC_ViewOnMapObject> fc_viewOnMapObjects;
    private ViewHotelsOnMapAdapter viewHotelsOnMapAdapter;
    ImageView img_backBtn;
    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleMap mGoogleMap;
    LatLng latLng;
    LinearLayout ll_searchLayout;
    Marker mCurrLocationMarker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_ViewOnMapActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_fc__view_on_map);

        context=FC_ViewOnMapActivity.this;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_intro_address);
        Objects.requireNonNull(mapFragment).getMapAsync(FC_ViewOnMapActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.token_type = String.valueOf(user.gettoken_type());

        FindViewById();

        viewHotelsOnMapAdapter = new ViewHotelsOnMapAdapter(FC_ViewOnMapActivity.this, fc_viewOnMapObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_hotelDetails.setLayoutManager(itemViewLLres);
        rv_hotelDetails.setAdapter(viewHotelsOnMapAdapter);

        ViewmapofferList();

        img_backBtn.setOnClickListener(view -> onBackPressed());

    }


    private void FindViewById() {
        rv_hotelDetails = findViewById(R.id.rv_hotelDetails);
        img_backBtn = findViewById(R.id.img_backBtn);
        ll_searchLayout = findViewById(R.id.ll_searchLayout);
        ll_searchLayout.setVisibility(View.GONE);
        fc_viewOnMapObjects = new ArrayList<>();
        FC_ViewOnMapObject onMapObject = new FC_ViewOnMapObject();
        onMapObject.setD_images("");
        onMapObject.setD_info("");
        fc_viewOnMapObjects.add(onMapObject);
        onMapObject.setD_images("");
        onMapObject.setD_info("");
        fc_viewOnMapObjects.add(onMapObject);
        onMapObject.setD_images("");
        onMapObject.setD_info("");
        fc_viewOnMapObjects.add(onMapObject);
        onMapObject.setD_images("");
        onMapObject.setD_info("");
        fc_viewOnMapObjects.add(onMapObject);
        onMapObject.setD_images("");
        onMapObject.setD_info("");
        fc_viewOnMapObjects.add(onMapObject);
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

    private void ViewmapofferList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_ALLRESTAURANTMAP,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("dfgfdgfd","dfgdfgfd"+obj);
                        if (obj.optString("success").equals("1")) {
                            //LocationObject.clear();
                            JSONArray dataArray = obj.getJSONArray("data");
                            fc_viewOnMapObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                FC_ViewOnMapObject playerModel = new FC_ViewOnMapObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRestaurant_latitude(product.getString("restaurant_latitude"));
                                    playerModel.setRestaurant_longitude(product.getString("restaurant_longitude"));
                                    playerModel.setDistance(product.getString("distance"));
                                    playerModel.setCuisines(product.getString("cuisines"));
                                    playerModel.setRating(product.getString("rating"));
                                    playerModel.setDelivery_estimation(product.getString("delivery_estimation"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setCost_limit(product.getString("cost_limit"));
                                    playerModel.setPerson_limit(product.getString("person_limit"));
                                    fc_viewOnMapObjects.add(playerModel);
                                    if (fc_viewOnMapObjects != null) {

                                        // lt_greatOffer.setText(R.string.great_offer);
                                     //
                                        viewHotelsOnMapAdapter.visibleContentLayout();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("dfgfdgsfdg","esd"+ex);

                                }
                            }
                            if (fc_viewOnMapObjects != null) {
                                mapView();
                            }
                        } else {
                            //lt_greatOffer.setText(R.string.noitem);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // snackBar(getResources().getString(R.string.reach));

                    }
                }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                Log.d("auth","auth" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("auth","auth" + params);
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

    private void mapView() {

       Log.d("hotel map size" ,"hotel map size : "+fc_viewOnMapObjects.size()) ;

        for (int i = 0 ; i < fc_viewOnMapObjects.size(); i++){

            double lat = Double.parseDouble(fc_viewOnMapObjects.get(i).getRestaurant_latitude());
            double lng = Double.parseDouble(fc_viewOnMapObjects.get(i).getRestaurant_longitude());

            latLng = new LatLng(lat,lng);
            Log.d("zoom", " latLng: " + latLng);

            int height = (int) getResources().getDimension(R.dimen.bitmap_iconSize1);
            int width = (int) getResources().getDimension(R.dimen.bitmap_iconSize1);
               /* BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_driver_marker);
                Bitmap b = bitmapdraw.getBitmap();*/
            Bitmap b =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_hotel_marker));
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(fc_viewOnMapObjects.get(i).getRestaurant_name());
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            CameraPosition googlePlex = CameraPosition.builder()
                    .target(latLng)
                    .zoom(15)
                    .build();

            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

        }
    }

    private void mapViewdummy() {

        Log.d("hotel map size" ,"hotel map size : "+fc_viewOnMapObjects.size()) ;

        //for (int i = 0 ; i < fc_viewOnMapObjects.size(); i++){



        //}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private class ViewHotelsOnMapAdapter extends RecyclerView.Adapter<ViewHotelsOnMapAdapter.ViewHolder> {
        private final FC_ViewOnMapActivity activity;
        private final ArrayList<FC_ViewOnMapObject> viewOnMapObjects;
        boolean visible;
        int row_index;

        ViewHotelsOnMapAdapter(FC_ViewOnMapActivity activity, ArrayList<FC_ViewOnMapObject> viewOnMapObjects) {
            this.activity = activity;
            this.viewOnMapObjects = viewOnMapObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_hotels_details_on_map, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.lc_item.setVisibility(View.GONE);
                holder.img_item.setVisibility(View.VISIBLE);
                Picasso.get().load(viewOnMapObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.img_item);
               /* Picasso.get().load(R.drawable.time)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.img_time);*/

                holder.lt_hotelName.setText(viewOnMapObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(viewOnMapObjects.get(position).getCuisines());
                holder.lt_deliveryEstimation.setText(viewOnMapObjects.get(position).getDelivery_estimation());
                holder.lt_currency.setText(viewOnMapObjects.get(position).getCurrency());
                holder.lt_costlimit.setText(viewOnMapObjects.get(position).getCost_limit() + " For " + viewOnMapObjects.get(position).getPerson_limit());
                //holder.lt_distance.setText(viewOnMapObjects.get(position).getCost_limit() + " KM ");

                holder.lt_distance.setText(viewOnMapObjects.get(position).getDistance());
                Double doublestax1 = Double.parseDouble(holder.lt_distance.getText().toString());
                holder.lt_distance.setText(String.format("%.1f", doublestax1)+ " KM ");


                holder.txt_ratings.setText(viewOnMapObjects.get(position).getRating());
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


                holder.ll_main.setOnLongClickListener(v -> {
                    FC_Common.restaurantid=viewOnMapObjects.get(position).getId();
                    Intent HotelDetailsIntent = new Intent(FC_ViewOnMapActivity.this, FC_HotelDetailsActivity.class);
                    HotelDetailsIntent.putExtra("hotelid", FC_Common.restaurantid);
                    HotelDetailsIntent.putExtra("recent_search","0");
                    startActivity(HotelDetailsIntent);
                    return false;
                });

                holder.ll_main.setOnClickListener(view -> {

                    row_index = position;
                    notifyDataSetChanged();
                   // mapView();
                  /*  FC_Common.restaurantid=viewOnMapObjects.get(position).getId();
                    Intent HotelDetailsIntent = new Intent(FC_ViewOnMapActivity.this, FC_HotelDetailsActivity.class);
                    HotelDetailsIntent.putExtra("hotelid", FC_Common.restaurantid);
                    HotelDetailsIntent.putExtra("recent_search","0");
                    startActivity(HotelDetailsIntent);*/

                    double lat = Double.parseDouble(fc_viewOnMapObjects.get(position).getRestaurant_latitude());
                    double lng = Double.parseDouble(fc_viewOnMapObjects.get(position).getRestaurant_longitude());

                    latLng = new LatLng(lat,lng);
                    Log.d("zoom", " latLng: " + latLng);

                    int height = (int) getResources().getDimension(R.dimen.bitmap_iconSize1);
                    int width = (int) getResources().getDimension(R.dimen.bitmap_iconSize1);
               /* BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_driver_marker);
                Bitmap b = bitmapdraw.getBitmap();*/
                    Bitmap b =  getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_hotel_marker));
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(fc_viewOnMapObjects.get(position).getRestaurant_name()).visible(true);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(fc_viewOnMapObjects.get(position).getRestaurant_name());
                      //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(latLng)
                            .zoom(15)
                            .build();

                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                    IconGenerator iconFactory = new IconGenerator(context);
                    /*Marker marker1 = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
                    marker1.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Marker 1")));*/
                    Marker marker2 = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
                    marker2.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.
                            makeIcon(fc_viewOnMapObjects.get(position).getRestaurant_name())));


                });

                if (row_index == position) {
                    holder.ll_mainLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_hot_sellers_outline_red));
                } else {
                    holder.ll_mainLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_hotel_details_on_map));
                }
            }
        }

        @Override
        public int getItemCount() {
            return viewOnMapObjects.size();
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

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_item;
            LoaderCircluarImageView lc_item;
            LinearLayout ll_mainLayout,ll_rating,ll_main;
            AC_Textview txt_ratings;
            LoaderTextView lt_hotelName ,lt_distance,lt_cuisine,lt_deliveryEstimation,lt_currency,lt_costlimit;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                ll_main = itemView.findViewById(R.id.ll_main);
                txt_ratings = itemView.findViewById(R.id.txt_ratings);
                lt_distance = itemView.findViewById(R.id.lt_distance);
                ll_rating = itemView.findViewById(R.id.ll_rating);
                lc_item = itemView.findViewById(R.id.lc_item);
                img_item = itemView.findViewById(R.id.img_item);
                ll_mainLayout = itemView.findViewById(R.id.ll_mainLayout);
                lt_hotelName = itemView.findViewById(R.id.lt_hotelName);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_deliveryEstimation = itemView.findViewById(R.id.lt_deliveryEstimation);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
            }
        }
    }
    public void makersMaker(GoogleMap googleMap){

    }
}
