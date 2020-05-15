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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import food.user.demand.FCPojo.FCManageAddress.ManageAddressObject;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_ManageAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_address ;
    private AddressAdapter addressAdapter ;
    View parentLayout;
    Snackbar bar;
    private ImageView img_backBtn ;
    Context context;
    AC_Textview txt_addAddressToProcess;
    ArrayList<ManageAddressObject> manageAddressObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc__manage_address);
        context=FC_ManageAddressActivity.this;

        FindViewById();

        addressAdapter = new AddressAdapter(manageAddressObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_address.setLayoutManager(itemViewLLres);
        rv_address.setAdapter(addressAdapter);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //CartAdapter data notification
                addressAdapter.visibleContentLayout();

            }
        },3000);*/

        img_backBtn.setOnClickListener(view -> onBackPressed());

        ManageAddressRecycler();
        ManageAddressList();

    }

    private void FindViewById() {
        rv_address = findViewById(R.id.rv_address);
        img_backBtn = findViewById(R.id.img_backBtn);
        txt_addAddressToProcess = findViewById(R.id.txt_addAddressToProcess);
        parentLayout = findViewById(android.R.id.content);

        txt_addAddressToProcess.setOnClickListener(this);
        manageAddressObjects = new ArrayList<>();
        ManageAddressObject addressObject = new ManageAddressObject();
        addressObject.setD_images("");
        addressObject.setD_info("");
        manageAddressObjects.add(addressObject);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }





    private void ManageAddressList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONLIST,
                response -> {
                    Utils.log(context, "Location:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            //ManageAddressObject.clear();
                            JSONArray dataArray = obj.getJSONArray("data");
                            manageAddressObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                ManageAddressObject playerModel = new ManageAddressObject();
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
                                    manageAddressObjects.add(playerModel);

                                    if (manageAddressObjects != null) {
                                        addressAdapter.visibleContentLayout();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                    ManageAddressList();
                                }
                            }

                        }
                        else {
                            ManageAddressList();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        ManageAddressList();
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                    }
                }, error -> {

                    String error_value = String.valueOf(error);
                    snackBar(error_value);
                    Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
                    Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
                    ManageAddressList();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FC_ManageAddressActivity.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
        requestQueue.addRequestFinishedListener(request -> {
            // Utils.stopProgressBar();

        });

    }
    private void ManageAddressRecycler() {
        addressAdapter = new AddressAdapter(manageAddressObjects);
        rv_address.setAdapter(addressAdapter);
        rv_address.setLayoutManager(new LinearLayoutManager(FC_ManageAddressActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_addAddressToProcess) {
            FC_Common.change_address = "current";
            Intent intent = new Intent(FC_ManageAddressActivity.this, FC_ManageEditAddressActivity.class);
            intent.putExtra("addressid", "");
            intent.putExtra("addresslandmark", "");
            intent.putExtra("addresstype", "");
            intent.putExtra("addressbuilding", "");
            intent.putExtra("addresslatitude", "");
            intent.putExtra("addresslongitude", "");
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("change_address", FC_Common.change_address);
            intent.putExtra("check", "2");
            startActivity(intent);
            finish();
        }
    }

    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        private final ArrayList<ManageAddressObject> manageAddressObjects;
        boolean visible ;

        AddressAdapter(ArrayList<ManageAddressObject> manageAddressObjects) {
            this.manageAddressObjects = manageAddressObjects ;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_manage_address_items, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {

                holder.txt_type.setText(manageAddressObjects.get(position).getType());
                holder.txt_landmark.setText(getResources().getString(R.string.landmark)+" "+manageAddressObjects.get(position).getLandmark());
                holder.txt_doorno.setText(getResources().getString(R.string.Doorno)+" "+manageAddressObjects.get(position).getBuilding());
                holder.txt_address.setText(manageAddressObjects.get(position).getMap_address());

                holder.lm_load.setVisibility(View.GONE);
                holder.lm_image.setImageDrawable(getDrawable(R.drawable.home_location));

                if (manageAddressObjects.get(position).getType().equalsIgnoreCase("HOME"))
                {
                    holder.lm_image.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.home_address));
                }
                else if (manageAddressObjects.get(position).getType().equalsIgnoreCase("WORK"))
                {
                    holder. lm_image.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.work_address));
                }
                else if (manageAddressObjects.get(position).getType().equalsIgnoreCase("OTHER"))
                {
                    holder. lm_image.setImageDrawable(Objects.requireNonNull(context).
                            getDrawable(R.drawable.other_address));
                }
                holder.txt_deleteAddress.setOnClickListener(v -> {

                    ManageAddressObject item = manageAddressObjects.get(position);
                    FC_Common.addressid=item.getId();

                    if (item.getIs_default().equalsIgnoreCase("1")){
                        snackBar(getResources().getString(R.string.default_address));
                    }
                    else {
                        DeleteAddress(FC_Common.addressid);
                    }


                });
                holder.txt_editAddress.setOnClickListener(v -> {

                    ManageAddressObject item = manageAddressObjects.get(position);
                    FC_Common.addressid=item.getId();
                    FC_Common.addresslandmark=item.getLandmark();
                    FC_Common.addressbuilding=item.getBuilding();
                    FC_Common.addresslongitude=item.getLongitude();
                    FC_Common.addresslatitude=item.getLatitude();
                    FC_Common.addresstype=item.getType();
                    Intent intent = new Intent(FC_ManageAddressActivity.this, FC_ManageEditAddressActivity.class);
                    intent.putExtra("addressid", FC_Common.addressid);
                    intent.putExtra("addresslandmark", FC_Common.addresslandmark);
                    intent.putExtra("addresstype", FC_Common.addresstype);
                    intent.putExtra("addressbuilding", FC_Common.addressbuilding);
                    intent.putExtra("addresslatitude", FC_Common.addresslatitude);
                    intent.putExtra("addresslongitude", FC_Common.addresslongitude);
                    intent.putExtra("deviceid", FC_Common.device_id);
                    intent.putExtra("token_type", FC_Common.token_type);
                    intent.putExtra("access_token", FC_Common.access_token);
                    intent.putExtra("change_address", "ManageAddress");
                    intent.putExtra("check", "1");
                    startActivity(intent);
                    finish();
                });
            }
        }

        @Override
        public int getItemCount() {
            return manageAddressObjects.size();
        }

        void visibleContentLayout() {

            visible = true ;
            notifyDataSetChanged();
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
            LoaderImageView lm_image ;
            LoaderImageView lm_load ;
            AC_Textview txt_type,txt_address,txt_editAddress,txt_deleteAddress,txt_doorno,txt_landmark;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_doorno =itemView.findViewById(R.id.txt_doorno);
                txt_landmark =itemView.findViewById(R.id.txt_landmark);
                lm_image =itemView.findViewById(R.id.lm_image);
                lm_load =itemView.findViewById(R.id.lm_load);
                txt_type =itemView.findViewById(R.id.txt_type);
                txt_address =itemView.findViewById(R.id.txt_address);
                txt_editAddress =itemView.findViewById(R.id.txt_editAddress);
                txt_deleteAddress =itemView.findViewById(R.id.txt_deleteAddress);
            }
        }
    }

    private void DeleteAddress(String addressid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_LOCATIONDELETE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {
                            ManageAddressList();
                            snackBar(FC_Common.message);
                        }
                        else
                        {
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", addressid);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FC_ManageAddressActivity.this).getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

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

   @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
