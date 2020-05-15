package food.user.demand.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCActivity.FCProfile.FC_locationPickerActivty;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class TestRecycleView extends AppCompatActivity {

    ArrayList<LocationObject> locationObjects = new ArrayList<>();
    private LocationAdapter locationAdapter;
    private RecyclerView rv_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_recycleview);

        rv_location = findViewById(R.id.rv_location);



        locationAdapter = new LocationAdapter(TestRecycleView.this, locationObjects);
        rv_location.setLayoutManager(new LinearLayoutManager(TestRecycleView.this, LinearLayoutManager.VERTICAL, false));
        rv_location.setAdapter(locationAdapter);

        LocationObject object = new LocationObject();
        object.setD_images("");
        object.setD_info("");
        locationObjects.add(object);
        object.setD_images("");
        object.setD_info("");
        locationObjects.add(object);
        object.setD_images("");
        object.setD_info("");
        locationObjects.add(object);


        locationlist();
    }

    private void locationlist() {


        //Utils.playProgressBar(FC_locationPickerActivty.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.log(TestRecycleView.this, "Location:" + response);
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
                                        playerModel.setLandmark(product.getString("landmark"));
                                        playerModel.setMap_address(product.getString("map_address"));
                                        playerModel.setLatitude(product.getString("latitude"));
                                        playerModel.setLongitude(product.getString("longitude"));
                                        playerModel.setType(product.getString("type"));
                                        playerModel.setIs_default(product.getString("is_default"));
                                        locationObjects.add(playerModel);

                                        if (locationObjects != null) {
                                            locationAdapter.visibleContentLayout();

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
                            Toast.makeText(TestRecycleView.this, " " + e, Toast.LENGTH_SHORT).show();
                            locationlist();
                            Utils.log(TestRecycleView.this, "dfgdfgdfg" + "dfgfd" + e);
                            Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        String error_value = String.valueOf(error);

                        Toast.makeText(TestRecycleView.this, " " + error_value, Toast.LENGTH_SHORT).show();
                        Utils.log(TestRecycleView.this, "dfgdfgdfg" + "dfgfd" + error);
                        Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
                        locationlist();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Utils.log(TestRecycleView.this, "token_type:" + FC_Common.token_type_dup);
                Utils.log(TestRecycleView.this, "access_token:" + FC_Common.access_token_dup);
                // Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type_dup + " " + FC_Common.access_token_dup);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(TestRecycleView.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                // Utils.stopProgressBar();

            }
        });

    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
        private final TestRecycleView activity;
        private final ArrayList<LocationObject> locationObjects;
        boolean visible;

        LocationAdapter(TestRecycleView activity, ArrayList<LocationObject> locationObjects) {
            this.activity = activity;
            this.locationObjects = locationObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_saved_location_items, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible){

               holder. txt_type.setText(locationObjects.get(position).getType());
               holder. txt_address.setText(locationObjects.get(position).getMap_address());
                if (locationObjects.get(position).getType().equalsIgnoreCase("HOME"))
                {
                    holder.lm_imageload.setImageDrawable(Objects.requireNonNull(activity).
                            getDrawable(R.drawable.search));
                }
                else if (locationObjects.get(position).getType().equalsIgnoreCase("WORK"))
                {
                    holder. lm_imageload.setImageDrawable(Objects.requireNonNull(activity).
                            getDrawable(R.drawable.ac_favorite));
                }
                else if (locationObjects.get(position).getType().equalsIgnoreCase("OTHER"))
                {
                    holder. lm_imageload.setImageDrawable(Objects.requireNonNull(activity).
                            getDrawable(R.drawable.ac_offers));
                }
            }
        }

        @Override
        public int getItemCount() {
            return locationObjects.size();
        }

        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LoaderImageView lm_imageload ;
            LoaderTextView txt_type ,txt_address ;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                lm_imageload = itemView.findViewById(R.id.lm_imageload);

                txt_type = itemView.findViewById(R.id.txt_type);
                txt_address = itemView.findViewById(R.id.txt_address);
            }
        }
    }
}
