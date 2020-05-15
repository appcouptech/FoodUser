package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCManageAddressActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;


public class FC_ManagelocationPickerActivty extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = FC_ManagelocationPickerActivty.class.getSimpleName();
    AC_Textview txtcurrentlocation;
    LocationAdapter locationadapter;
    RecyclerView rv_savedaddress;
    Snackbar bar;
    Context context;
    View parentLayout;
    ArrayList<LocationObject> locationObjects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fc_location_picker_activity);
        String apiKey = "AIzaSyBHMt8kGGgqUc11TluyJOgMrqFJfdaPbYQ";

        context= FC_ManagelocationPickerActivty.this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
       // LocationObject = new ArrayList<>();
        if (bundle != null)
        {
            FC_Common.addressid = (String) bundle.get("addressid");
            FC_Common.addresslandmark = (String) bundle.get("addresslandmark");
            FC_Common.addresstype = (String) bundle.get("addresstype");
            FC_Common.addressbuilding = (String) bundle.get("addressbuilding");
            FC_Common.addresslatitude = (String) bundle.get("addresslatitude");
            FC_Common.addresslongitude = (String) bundle.get("addresslongitude");
            FC_Common.change_address = (String) bundle.get("change_address");
            FC_Common.check = (String) bundle.get("check");
            FC_Common.device_id = (String) bundle.get("deviceid");
            FC_Common.token_type = (String) bundle.get("token_type");
            FC_Common.access_token = (String) bundle.get("access_token");

            Log.d("dfgfdgdfg","dfgfdgfd"+FC_Common.addresslatitude);
            Log.d("dfgfdgdfg","dfgfdgfd"+FC_Common.addresslongitude);
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                LatLng latLng = place.getLatLng();
                assert latLng != null;
                FC_Common.addresslatitude = String.valueOf(latLng.latitude);
                FC_Common.addresslongitude = String.valueOf(latLng.longitude);
                FC_Common.change_address="location";

                Intent intent = new Intent(FC_ManagelocationPickerActivty.this, FC_ManageEditAddressActivity.class);
                intent.putExtra("addressid", FC_Common.addressid);
                intent.putExtra("addresslandmark", FC_Common.addresslandmark);
                intent.putExtra("addresstype", FC_Common.addresstype);
                intent.putExtra("addressbuilding", FC_Common.addressbuilding);
                intent.putExtra("addresslatitude", FC_Common.addresslatitude);
                intent.putExtra("addresslongitude", FC_Common.addresslongitude);
                intent.putExtra("deviceid", FC_Common.device_id);
                intent.putExtra("token_type", FC_Common.token_type);
                intent.putExtra("access_token", FC_Common.access_token);
                intent.putExtra("change_address",FC_Common.change_address);
                intent.putExtra("check",FC_Common.check);
                startActivity(intent);
            }
            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        FindviewById();
        LocationRecycler();
        locationlist();
    }

    private void FindviewById() {
        txtcurrentlocation = findViewById(R.id.txtcurrentlocation);
        parentLayout = findViewById(android.R.id.content);
        rv_savedaddress = findViewById(R.id.rv_savedaddress);

        txtcurrentlocation.setText("");
        txtcurrentlocation.setOnClickListener(this);

        LocationObject object=new LocationObject();
        object.setD_images("");
        object.setD_info("");
        locationObjects.add(object);


    }

    private void locationlist() {


        //Utils.playProgressBar(FC_locationPickerActivty.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONLIST,
                response -> {
                    Utils.log(context, "Location:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            //LocationObject.clear();
                            JSONArray dataArray = obj.getJSONArray("data");
                            locationObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                LocationObject playerModel = new LocationObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setUser_id(product.getString("user_id"));
                                    playerModel.setBuilding(product.getString("building"));
                                    playerModel.setLandmark(product.getString("landmark"));
                                    playerModel.setMap_address(product.getString("map_address"));
                                    playerModel.setLatitude(product.getString("latitude"));
                                    playerModel.setLongitude(product.getString("longitude"));
                                    playerModel.setType(product.getString("type"));
                                    playerModel.setIs_default(product.getString("is_default"));
                                    locationObjects.add(playerModel);

                                    if (locationObjects != null) {
                                        locationadapter.visibleContentLayout();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                    locationlist();
                                }
                            }

                        }
                        else {
                            locationlist();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        locationlist();
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                    }
                }, error -> {

                    String error_value = String.valueOf(error);
                    snackBar(error_value);
                    Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
                    Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
                    locationlist();
                }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                // Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FC_ManagelocationPickerActivty.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(request -> {
            // Utils.stopProgressBar();

        });

    }
    private void LocationRecycler() {


        locationadapter = new LocationAdapter( locationObjects);
        rv_savedaddress.setAdapter(locationadapter);
        rv_savedaddress.setLayoutManager(new LinearLayoutManager(FC_ManagelocationPickerActivty.this, LinearLayoutManager.VERTICAL, false));

    }
    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

        boolean visible;
        private final ArrayList<LocationObject> locationObjects;

        LocationAdapter( ArrayList<LocationObject> locationObjects) {
            this.locationObjects = locationObjects;
        }

        @NonNull
        @Override
        public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_saved_location_items, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {


            if (visible){

                holder.txt_type.setText(locationObjects.get(position).getType());
                holder.lt_doorno.setText(getResources().getString(R.string.Doorno)+" "+locationObjects.get(position).getBuilding());
                holder.lt_landmark.setText(getResources().getString(R.string.landmark)+" "+locationObjects.get(position).getLandmark());
                holder.txt_address.setText(locationObjects.get(position).getMap_address());
                if (locationObjects.get(position).getType().equalsIgnoreCase("HOME"))
                {
                    holder.lm_imageload.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.search));
                }
                else if (locationObjects.get(position).getType().equalsIgnoreCase("WORK"))
                {
                    holder. lm_imageload.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.ac_favorite));
                }
                else if (locationObjects.get(position).getType().equalsIgnoreCase("OTHER"))
                {
                    holder. lm_imageload.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.ac_offers));
                }

            }
        }

        @Override
        public int getItemCount() {
            return locationObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        void visibleContentLayout() {
            visible = true;

            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageload ;
            LoaderTextView txt_type ,txt_address,lt_doorno,lt_landmark;
            LinearLayout lin_main;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                lt_doorno = itemView.findViewById(R.id.lt_doorno);
                lt_landmark = itemView.findViewById(R.id.lt_landmark);
                lm_imageload = itemView.findViewById(R.id.lm_imageload);

                txt_type = itemView.findViewById(R.id.txt_type);
                txt_address = itemView.findViewById(R.id.txt_address);
                lin_main = itemView.findViewById(R.id.lin_main);
            }
        }
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.txtcurrentlocation) {
            FC_Common.change_address = "current";
            Intent intent = new Intent(FC_ManagelocationPickerActivty.this, FC_ManageEditAddressActivity.class);
            intent.putExtra("addressid", FC_Common.addressid);
            intent.putExtra("addresslandmark", FC_Common.addresslandmark);
            intent.putExtra("addresstype", FC_Common.addresstype);
            intent.putExtra("addressbuilding", FC_Common.addressbuilding);
            intent.putExtra("addresslatitude", FC_Common.addresslatitude);
            intent.putExtra("addresslongitude", FC_Common.addresslongitude);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("change_address", FC_Common.change_address);
            intent.putExtra("check", FC_Common.check);

            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {

        Log.d("fghfghfgh","fdhfdghf"+FC_Common.change_address);

            Intent intent = new Intent(FC_ManagelocationPickerActivty.this, FC_ManageEditAddressActivity.class);
            intent.putExtra("addressid", FC_Common.addressid);
            intent.putExtra("addresslandmark", FC_Common.addresslandmark);
            intent.putExtra("addresstype", FC_Common.addresstype);
            intent.putExtra("addressbuilding", FC_Common.addressbuilding);
            intent.putExtra("addresslatitude", FC_Common.addresslatitude);
            intent.putExtra("addresslongitude", FC_Common.addresslongitude);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("change_address",FC_Common.change_address);
            intent.putExtra("check",FC_Common.check);
            startActivity(intent);
            finish();

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
