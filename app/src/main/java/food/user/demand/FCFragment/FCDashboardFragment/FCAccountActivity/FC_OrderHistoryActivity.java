package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCPojo.FCHotelDetailsActivityObject.ItemViewOrderObject;
import food.user.demand.FCPojo.FCOrderHistoryObject.OrderHistoryObject;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_OrderHistoryActivity extends AppCompatActivity {

    Snackbar bar;
    private RecyclerView rv_orderHistory ;
    private OrderHistoryAdapter orderHistoryAdapter ;
    ArrayList<OrderHistoryObject> orderHistoryObjects ;
    ImageView img_backBtn ;
    AC_Textview txt_emptyview;
    View parentLayout;
    Context context;
    private List<ItemViewOrderObject> Listvalues = new ArrayList<>();
    private ItemViewOrderObject ListDatas;
    private DialogPlus itemdialog;
    private ListView  lst_itemview;
    private ItemOrderAdapter itemOrderAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_OrderHistoryActivity.this,getResources().getConfiguration());
        setContentView(R.layout.fragment_fc__order_history);
        context=FC_OrderHistoryActivity.this;
        FindViewById();

        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());


        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_orderHistory.setLayoutManager(itemViewLLres);
        rv_orderHistory.setAdapter(orderHistoryAdapter);

        img_backBtn.setOnClickListener(view -> onBackPressed());

        PastOrderList();

      /*  handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (counter > 5) {
                    counter = 0;
                    PastOrderList();
                }

                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }

    private void FindViewById() {
        rv_orderHistory =findViewById(R.id.rv_orderHistory);
        img_backBtn = findViewById(R.id.img_backBtn);
        txt_emptyview = findViewById(R.id.txt_emptyview);
        parentLayout = findViewById(android.R.id.content);
        orderHistoryObjects = new ArrayList<>();
        OrderHistoryObject historyObject = new OrderHistoryObject();
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderHistoryObjects.add(historyObject);
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderHistoryObjects.add(historyObject);
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderHistoryObjects.add(historyObject);
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


    private void PastOrderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_HISTORY,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("dfgdfgfdgdf","fdgdfgv"+obj);
                        if (obj.optString("success").equals("1")) {
                            txt_emptyview.setVisibility(View.GONE);
                            rv_orderHistory.setVisibility(View.VISIBLE);
                            JSONArray dataArray = obj.getJSONArray("data");
                            orderHistoryObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                OrderHistoryObject playerModel = new OrderHistoryObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setItem(product.getString("item"));
                                    playerModel.setTotal(product.getString("total"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setCuisine_id(product.getString("cuisine_id"));
                                    playerModel.setCreated_at(product.getString("created_at"));
                                    playerModel.setStatus(product.getString("status"));

                                    orderHistoryObjects.add(playerModel);
                                    if (orderHistoryObjects != null) {

                                        orderHistoryAdapter.visibleContentLayout();
                                    }
                                    else {
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_orderHistory.setVisibility(View.GONE);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    snackBar(String.valueOf(ex));
                                    /*final int counter_pastorder = counter++;
                                    if (counter_pastorder < 5) {
                                        PastOrderList();
                                    } */
                                }}
                        } else {

                            /*final int counter_pastorder = counter++;
                            if (counter_pastorder < 5) {
                                PastOrderList();
                            }*/
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_orderHistory.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller" + e);
                       /* final int counter_pastorder = counter++;
                        if (counter_pastorder < 5) {
                            PastOrderList();
                        }*/
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("Hotsellernew" + error_value);
           /*  final int counter_pastorder = counter++;
           if (counter_pastorder < 5) {
                PastOrderList();
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

    private class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

        private final ArrayList<OrderHistoryObject> orderHistoryObjects;
        boolean visible ;

        OrderHistoryAdapter(ArrayList<OrderHistoryObject> orderHistoryObjects) {
            this.orderHistoryObjects = orderHistoryObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_past_order_items, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible){

                holder.txt_reOrder.setVisibility(View.GONE);
                holder.txt_rating.setVisibility(View.GONE);
                holder.lt_status.setVisibility(View.VISIBLE);
                holder.img_detailItem.setVisibility(View.VISIBLE);
                Log.d("gfdgdfg","dfgdfgdfg"+orderHistoryObjects.get(position).getRestaurant_name());
                Picasso.get().load(orderHistoryObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lc_img);
                holder.lt_status.setText(orderHistoryObjects.get(position).getStatus());
                holder.lt_restaurantName.setText(orderHistoryObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(orderHistoryObjects.get(position).getCuisine_id());
                holder.lt_itemName.setText(orderHistoryObjects.get(position).getItem());
                holder.lt_currency.setText(orderHistoryObjects.get(position).getCurrency());
                holder.lt_itemTotal.setText(orderHistoryObjects.get(position).getTotal());
                holder.lt_datetime.setText(orderHistoryObjects.get(position).getCreated_at());
                holder.img_detailItem.setOnClickListener(v -> {
                    FC_Common.order_id=orderHistoryObjects.get(position).getId();
                    ItemDialog();
                });



                FC_Common.date_time = orderHistoryObjects.get(position).getCreated_at();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date oneWayTripDate = input.parse(FC_Common.date_time);
                    assert oneWayTripDate != null;
                    String date = output.format(oneWayTripDate);
                    holder.lt_datetime.setText(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return orderHistoryObjects.size();
        }

        void visibleContentLayout() {
            visible = true;
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
            LoaderCircluarImageView lc_img;
            AC_Textview txt_reOrder,txt_rating;
            ImageView img_detailItem;
            LoaderTextView lt_status,lt_restaurantName,lt_cuisine,lt_itemName,lt_currency,lt_itemTotal,lt_datetime;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                lt_status = itemView.findViewById(R.id.lt_status);
                img_detailItem = itemView.findViewById(R.id.img_detailItem);
                lc_img = itemView.findViewById(R.id.lc_img);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_itemName = itemView.findViewById(R.id.lt_itemName);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_itemTotal = itemView.findViewById(R.id.lt_itemTotal);
                lt_datetime = itemView.findViewById(R.id.lt_datetime);
                txt_reOrder = itemView.findViewById(R.id.txt_reOrder);
                txt_rating = itemView.findViewById(R.id.txt_rating);
                txt_rating.setVisibility(View.GONE);
                txt_reOrder.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     void ItemDialog() {
        itemdialog = DialogPlus.newDialog(Objects.requireNonNull(context))
                .setContentHolder(new ViewHolder(R.layout.dialogitemview))
                .setCancelable(true)
                .setGravity( Gravity.CENTER)
                .setOnItemClickListener((dialogPlus, item, view, position) -> {
                }).setExpanded(false).create();
        itemdialog.show();
        ImageView imgcancel = (ImageView) itemdialog.findViewById(R.id.imgcancel);

        assert imgcancel != null;
        imgcancel.setOnClickListener(v -> itemdialog.dismiss());
        lst_itemview = (ListView) itemdialog.findViewById(R.id.lst_itemview);

        ItemDialogAsync();
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ItemDialogAsync() {
        Utils.playProgressBar(FC_OrderHistoryActivity.this);
        StringRequest stringRequest = new StringRequest( Request.Method.POST, FC_URL.URL_ITEMDETAIL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray arr = jsonObject.getJSONArray("data");
                        Log.d("sdgsdfsd","dfsd"+FC_URL.URL_ITEMDETAIL);
                        Listvalues.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject product = arr.getJSONObject(i);
                            ListDatas = new ItemViewOrderObject();
                            ListDatas.setCuisine_name(product.getString("product_name"));
                            ListDatas.setQty(product.getString("qty"));
                            Listvalues.add(ListDatas);
                        }
                        if (Listvalues != null && lst_itemview != null) {

                            Utils.stopProgressBar();
                            itemOrderAdapters = new ItemOrderAdapter(context, Listvalues);
                            lst_itemview.setAdapter(itemOrderAdapters);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("dgsdfsd","dfsdf"+e);
                        Log.d("dgsdfsd","dfsdf"+FC_URL.URL_ITEMDETAIL);
                    } }, error -> Utils.toast(context,"" + R.string.reach)){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FC_Common.order_id);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
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

    public static class ItemOrderAdapter extends ArrayAdapter<ItemViewOrderObject> {
        private List<ItemViewOrderObject> _list;
        @SuppressLint("StaticFieldLeak")

        ItemOrderAdapter(Context context, List<ItemViewOrderObject> lst) {
            super(context, 0, lst);
            _list = lst;
        }
        @NonNull
        @Override
        public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("ViewHolder") final View view = inflater.inflate( R.layout.layout_itemview, parent, false);
            try {
                ItemViewOrderObject vidItem = _list.get(position);
                LoaderTextView lt_itemName =view.findViewById(R.id.lt_itemName);
                LoaderTextView lt_qty =view.findViewById(R.id.lt_qty);

                lt_itemName.setText(vidItem.getCuisine_name());
                lt_qty.setText(vidItem.getQty());
            } catch (Exception pre) {
                pre.printStackTrace();
            }
            return view;
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
