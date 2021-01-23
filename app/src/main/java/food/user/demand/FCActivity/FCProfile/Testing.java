package food.user.demand.FCActivity.FCProfile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class Testing extends Activity {
    Context context;
    ArrayList<LocationObject> LocationObject = new ArrayList<>();
    //LocationAdapter locationadapter;
    NewArrivalAdapter locationadapter;
    RecyclerView rv_savedaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(Testing.this,getResources().getConfiguration());
        setContentView(R.layout.activity_testing);
        rv_savedaddress=findViewById(R.id.rv_savedaddress);

        locationlist();

    }
    private void locationlist() {
        Utils.playProgressBar(Testing.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.log(context,"Location:"+response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("success").equals("1")) {
                                //LocationObject.clear();
                                JSONArray dataArray = obj.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    LocationObject playerModel = new LocationObject();
                                    JSONObject product = dataArray.getJSONObject(i);
                                    try {


                                        playerModel.setId(product.getString("id"));
//                                    playerModel.setUser_id(product.getString("user_id"));
//                                    playerModel.setLandmark(product.getString("landmark"));
                                        playerModel.setMap_address(product.getString("map_address"));
//                                    playerModel.setLatitude(product.getString("latitude"));
//                                    playerModel.setLongitude(product.getString("longitude"));
                                        playerModel.setType(product.getString("type"));
//                                    playerModel.setIs_default(product.getString("is_default"));
                                        Log.d("fdhfdgfd","43fdgfd"+product.getString("map_address"));
                                        Log.d("fdhfdgfd","ghgfdgfd"+product.getString("type"));
                                        LocationObject.add(playerModel);
                                    }
                                    catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                        Log.d("dfgdfgfd","dfgfdgfd"+ex);
                                    }
                                }
                                LocationRecycler();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // snackBar(String.valueOf(e));
                            Utils.log(context,"dfgdfgdfg"+"dfgfd"+e);
                            Log.d("dfgfdgfdg","dfgfdgdf"+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        String error_value=String.valueOf(error);
                       // snackBar(error_value);
                        Utils.log(context,"dfgdfgdfg"+"dfgfd"+error);
                        Log.d("dfgfdgfdg","d324dffgfdgdf"+error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Utils.log(context,"token_type:"+ FC_Common.token_type);
                Utils.log(context,"access_token:"+FC_Common.access_token);
                // Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type_dup+" "+FC_Common.access_token_dup);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Testing.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                Utils.stopProgressBar();
            }
        });

    }

    private void LocationRecycler() {


        locationadapter = new NewArrivalAdapter( LocationObject);
        rv_savedaddress.setAdapter(locationadapter);
        rv_savedaddress.setLayoutManager(new LinearLayoutManager(Testing.this, LinearLayoutManager.HORIZONTAL, false));

    }
    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

        boolean visible;

        private final ArrayList<LocationObject> LocationObject;

        LocationAdapter( ArrayList<LocationObject> LocationObject) {

            this.LocationObject = LocationObject;
        }

        @NonNull
        @Override
        public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_test, parent, false);
            LocationAdapter.ViewHolder viewHolder = new LocationAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {

            try {


                LocationObject items = LocationObject.get(position);
                Utils.log(context,"items:"+items);

                Log.d("hfdhfdhfd","fghfghfg"+items.getType());


                holder.txt_type.setText(items.getType());
                holder.txt_address.setText(items.getMap_address());

        /*    if (items==null)
            {
                *//*Image View *//*
                holder.lm_imageload.setVisibility(View.VISIBLE);
                holder.lm_image.setVisibility(View.GONE);

                *//*Text View *//*
                holder.txt_type_load.setVisibility(View.VISIBLE);
                holder.txt_type.setVisibility(View.GONE);
                holder.txt_address_load.setVisibility(View.VISIBLE);
                holder.txt_address.setVisibility(View.GONE);
            }
            else
            {
                holder.lm_imageload.setVisibility(View.GONE);
                holder.lm_image.setVisibility(View.VISIBLE);

                *//*Text View *//*
                holder.txt_type_load.setVisibility(View.GONE);
                holder.txt_type.setVisibility(View.VISIBLE);
                holder.txt_address_load.setVisibility(View.GONE);
                holder.txt_address.setVisibility(View.VISIBLE);

                holder.txt_type.setText(items.getType());
                holder.txt_address.setText(items.getMap_address());
                if (items.getType().equalsIgnoreCase("HOME"))
                {
                    holder.lm_image.setImageDrawable(Objects.requireNonNull(FC_locationPickerActivty.this).
                            getDrawable(R.drawable.search));
                }
                else if (items.getType().equalsIgnoreCase("WORK"))
                {
                    holder.lm_image.setImageDrawable(Objects.requireNonNull(FC_locationPickerActivty.this).
                            getDrawable(R.drawable.ac_favorite));
                }
               else if (items.getType().equalsIgnoreCase("OTHER"))
                {
                    holder.lm_image.setImageDrawable(Objects.requireNonNull(FC_locationPickerActivty.this).
                            getDrawable(R.drawable.ac_offers));
                }

            }*/
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Log.d("dfhdfhfdhgfd","dfgfd"+ex);
            }
        }

        @Override
        public int getItemCount() {
            return LocationObject.size();
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
       TextView txt_address,txt_type;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_address=itemView.findViewById(R.id.txt_address);
                txt_type = itemView.findViewById(R.id.txt_type);
            }
        }
    }

    private class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.ViewHolder> {


        private final ArrayList<LocationObject> newArrivalsFragmentObjects;
        int minteger = 0;

        public NewArrivalAdapter( ArrayList<LocationObject> newArrivalsFragmentObjects) {
            this.newArrivalsFragmentObjects = newArrivalsFragmentObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.adapter_test, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            try {




                    holder.txt_type.setText("" + newArrivalsFragmentObjects.get(position).getType());




            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Dfhgdfgdf", "dfgdf" + ex);
            }

        }

        @Override
        public int getItemCount() {
            return newArrivalsFragmentObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_address,txt_type;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_address=itemView.findViewById(R.id.txt_address);
                txt_type = itemView.findViewById(R.id.txt_type);
            }
        }
    }





}