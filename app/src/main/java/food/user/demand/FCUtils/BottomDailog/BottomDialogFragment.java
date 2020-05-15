package food.user.demand.FCUtils.BottomDailog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import food.user.demand.FCActivity.FCCartActivity.FC_CartAddressActivity;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private LocationAdapter locationadapter;
    private ArrayList<LocationObject> locationObjects = new ArrayList<>();
    private RecyclerView rv_search;
    private Addresschange addresschange;
    private int changetype = 0;
    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // get the views and attach the listener

        return inflater.inflate(R.layout.layout_address_bottom_sheet, container,
                false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        if (getActivity() instanceof Addresschange)
            addresschange = (Addresschange) getActivity();
        FindViewById(view);
        LocationRecycler();
        locationlist();
    }

    private void FindViewById(View view) {


        rv_search = view.findViewById(R.id.rv_search);

        LinearLayout ll_addNewAddress = view.findViewById(R.id.ll_addNewAddress);

        ll_addNewAddress.setOnClickListener(view1 -> {

            Intent addAddressToProcess = new Intent(getActivity(), FC_CartAddressActivity.class);
            startActivity(addAddressToProcess);

        });

        LocationObject object = new LocationObject();
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
                                    playerModel.setLandmark(product.getString("landmark"));
                                    playerModel.setBuilding(product.getString("building"));
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

                        } else {
                            locationlist();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        locationlist();
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                    }
                }, error -> {

                    Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
                    Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
                    locationlist();
                }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Utils.log(context, "token_type:" + FC_Common.token_type);
                Utils.log(context, "access_token:" + FC_Common.access_token);
                // Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(request -> {
            // Utils.stopProgressBar();

        });

    }

    private void LocationRecycler() {


        locationadapter = new LocationAdapter( locationObjects);
        rv_search.setAdapter(locationadapter);
        rv_search.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

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


            if (visible) {

                holder.txt_type.setText(locationObjects.get(position).getType());
                holder.txt_address.setText(locationObjects.get(position).getMap_address());
                holder.lt_doorno.setText(getResources().getString(R.string.Doorno)+" "+locationObjects.get(position).getBuilding());
                holder.lt_landmark.setText(getResources().getString(R.string.landmark)+" "+locationObjects.get(position).getLandmark());
                if (locationObjects.get(position).getType().equalsIgnoreCase("HOME")) {
                    holder.lm_imageload.setImageDrawable(Objects.requireNonNull(context).getDrawable(R.drawable.home_address));
                } else if (locationObjects.get(position).getType().equalsIgnoreCase("WORK")) {
                    holder.lm_imageload.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.work_address));
                } else if (locationObjects.get(position).getType().equalsIgnoreCase("OTHER")) {
                    holder.lm_imageload.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.other_address));
                }

                holder.lin_main.setOnClickListener(v -> {
                    LocationObject item = locationObjects.get(position);
                    FC_Common.default_address = item.getId();

                    UpdateDefaultAddress(FC_Common.default_address);

                    Utils.log(context, "dfsdfdsfs" + FC_Common.default_address);

                });
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
            LoaderImageView lm_imageload;
            LoaderTextView txt_type, txt_address,lt_doorno,lt_landmark;
            LinearLayout lin_main;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                lm_imageload = itemView.findViewById(R.id.lm_imageload);
                lt_doorno = itemView.findViewById(R.id.lt_doorno);
                lt_landmark = itemView.findViewById(R.id.lt_landmark);
                txt_type = itemView.findViewById(R.id.txt_type);
                txt_address = itemView.findViewById(R.id.txt_address);
                lin_main = itemView.findViewById(R.id.lin_main);
            }
        }
    }


    private void UpdateDefaultAddress(String default_address) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONDEFAULT,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            GetUserDetails();
                           // snackBar(FC_Common.message);
                        } else {
                            Utils.toast(context,FC_Common.message);
                           // snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                    }
                },
                error -> {

                    //snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", default_address);
                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }


    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("ServerResponse", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.id = jsonResponse1.getString("id");
                        FC_Common.name = jsonResponse1.getString("name");
                        FC_Common.email = jsonResponse1.getString("email");
                        FC_Common.mobile = jsonResponse1.getString("mobile");
                        FC_Common.email_verified_at = jsonResponse1.getString("email_verified_at");
                        FC_Common.dial_code = jsonResponse1.getString("dial_code");
                        FC_Common.location_name = jsonResponse1.getString("location_name");
                        FC_Common.location_type = jsonResponse1.getString("location_type");
                        FC_Common.latitude = jsonResponse1.getString("latitude");
                        FC_Common.longitude = jsonResponse1.getString("longitude");
                        FC_Common.status = jsonResponse1.getString("status");
                        FC_Common.is_guest = jsonResponse1.getString("is_guest");
                        FC_Common.picture = jsonResponse1.getString("picture");
                        FC_Common.device_token = jsonResponse1.getString("device_token");
                        FC_Common.device_id = jsonResponse1.getString("device_id");
                        FC_Common.device_type = jsonResponse1.getString("device_type");
                        FC_Common.login_by = jsonResponse1.getString("login_by");
                        FC_Common.social_unique_id = jsonResponse1.getString("social_unique_id");
                        FC_Common.cust_id = jsonResponse1.getString("cust_id");
                        FC_Common.wallet_balance = jsonResponse1.getString("wallet_balance");
                        FC_Common.rating = jsonResponse1.getString("rating");
                        FC_Common.userotp = jsonResponse1.getString("otp");
                        FC_Common.created_at = jsonResponse1.getString("created_at");
                        FC_Common.updated_at = jsonResponse1.getString("updated_at");
                        FC_Common.token_type = jsonResponse1.getString("token_type");
                        FC_Common.access_token = jsonResponse1.getString("access_token");
                        FC_Common.gender = jsonResponse1.getString("gender");
                        FC_Common.filter_price_min = jsonResponse1.getString("filter_price_min");
                        FC_Common.filter_price_max = jsonResponse1.getString("filter_price_max");
                        FC_Common.is_default = jsonResponse1.getString("is_default");
                        Log.d("fdhdfhfd", "dfgfd" + FC_Common.id);

                        if (FC_Common.status.equalsIgnoreCase("1")) {
                           /* String value="dgsdgsdgsd";

                            snackBar(value);*/

                            FC_User user = new FC_User(jsonResponse1.getString("id"),
                                    jsonResponse1.getString("name"),
                                    jsonResponse1.getString("email"),
                                    jsonResponse1.getString("mobile"),
                                    jsonResponse1.getString("email_verified_at"),
                                    jsonResponse1.getString("dial_code"),
                                    jsonResponse1.getString("location_name"),
                                    jsonResponse1.getString("location_type"),
                                    jsonResponse1.getString("latitude"),
                                    jsonResponse1.getString("longitude"),
                                    jsonResponse1.getString("status"),
                                    jsonResponse1.getString("is_guest"),
                                    jsonResponse1.getString("picture"),
                                    jsonResponse1.getString("device_token"),
                                    jsonResponse1.getString("device_id"),
                                    jsonResponse1.getString("device_type"),
                                    jsonResponse1.getString("login_by"),
                                    jsonResponse1.getString("social_unique_id"),
                                    jsonResponse1.getString("cust_id"),
                                    jsonResponse1.getString("wallet_balance"),
                                    jsonResponse1.getString("rating"),
                                    jsonResponse1.getString("otp"),
                                    jsonResponse1.getString("created_at"),
                                    jsonResponse1.getString("updated_at"),
                                    jsonResponse1.getString("token_type"),
                                    jsonResponse1.getString("access_token"),
                                    jsonResponse1.getString("gender"),
                                    jsonResponse1.getString("filter_price_min"),
                                    jsonResponse1.getString("filter_price_max"),
                                    jsonResponse1.getString("is_default")
                            );

                            FC_SharedPrefManager.getInstance(context).userLogin(user);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //editor.putBoolean("FirstLogin", true);
                            editor.apply();
                            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                            editor1.putString(Utils.id, FC_Common.id);
                            editor1.putString(Utils.name, FC_Common.name);
                            editor1.putString(Utils.email, FC_Common.email);
                            editor1.putString(Utils.mobile, FC_Common.mobile);
                            editor1.putString(Utils.email_verified_at, FC_Common.email_verified_at);
                            editor1.putString(Utils.dial_code, FC_Common.dial_code);
                            editor1.putString(Utils.location_name, FC_Common.location_name);
                            editor1.putString(Utils.location_type, FC_Common.location_type);
                            editor1.putString(Utils.latitude, FC_Common.latitude);
                            editor1.putString(Utils.longitude, FC_Common.longitude);
                            editor1.putString(Utils.status, FC_Common.status);
                            editor1.putString(Utils.is_guest, FC_Common.is_guest);
                            editor1.putString(Utils.picture, FC_Common.picture);
                            editor1.putString(Utils.device_token, FC_Common.device_token);
                            editor1.putString(Utils.device_id, FC_Common.device_id);
                            editor1.putString(Utils.device_type, FC_Common.device_type);
                            editor1.putString(Utils.login_by, FC_Common.login_by);
                            editor1.putString(Utils.social_unique_id, FC_Common.social_unique_id);
                            editor1.putString(Utils.cust_id, FC_Common.cust_id);
                            editor1.putString(Utils.wallet_balance, FC_Common.wallet_balance);
                            editor1.putString(Utils.rating, FC_Common.rating);
                            editor1.putString(Utils.userotp, FC_Common.userotp);
                            editor1.putString(Utils.created_at, FC_Common.created_at);
                            editor1.putString(Utils.updated_at, FC_Common.updated_at);
                            editor1.putString(Utils.token_type, FC_Common.token_type);
                            editor1.putString(Utils.access_token, FC_Common.access_token);
                            editor1.putString(Utils.gender, FC_Common.gender);
                            editor1.putString(Utils.filter_price_min, FC_Common.filter_price_min);
                            editor1.putString(Utils.filter_price_max, FC_Common.filter_price_max);
                            editor1.putString(Utils.is_default, FC_Common.is_default);
                            editor1.apply();
                            changetype = 1;
                            if (addresschange != null)
                                addresschange.CheckType(changetype);
                            //onB
                            dismiss();


                            /*Intent intent = new Intent(context, FC_CartActivity.class);
                            startActivity(intent);
                            finish();*/

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                       // snackBar(String.valueOf(e));
                    }
                }, volleyError -> {

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FC_Common.token_type + " " + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface Addresschange {
        void CheckType(int products);
    }
}