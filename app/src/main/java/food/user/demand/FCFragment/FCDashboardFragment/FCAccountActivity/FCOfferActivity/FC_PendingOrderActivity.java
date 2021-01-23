package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCPojo.FCHomeFragmentObject.OrderObject;
import food.user.demand.FCPojo.FCHotelDetailsActivityObject.ItemViewOrderObject;
import food.user.demand.FCUtils.Loader.LoaderCircluarImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_PendingOrderActivity extends AppCompatActivity {

    Snackbar bar;
    private RecyclerView rv_orderHistory ;
    ImageView img_backBtn ;
    AC_Textview txt_emptyview,txt_toolbarName;
    View parentLayout;
    ArrayList<OrderObject> orderObjects ;
    private OrderAdapter orderAdapters;
    Context context;
    private List<ItemViewOrderObject> Listvalues = new ArrayList<>();
    private ItemViewOrderObject ListDatas;
    private DialogPlus itemdialog;
    private ListView lst_itemview;
    private ItemOrderAdapter itemOrderAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(FC_PendingOrderActivity.this,getResources().getConfiguration());
        setContentView(R.layout.fragment_fc__order_history);
        context= FC_PendingOrderActivity.this;
        FindViewById();

        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());


        orderAdapters = new OrderAdapter(orderObjects );
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_orderHistory.setLayoutManager(itemViewLLres);
        rv_orderHistory.setAdapter(orderAdapters);

        img_backBtn.setOnClickListener(view -> onBackPressed());

        OrderRecycler();
        Orderlist();

       /* handler = new Handler();
        int delay = 25000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (counter > 5) {
                    counter = 0;
                    Orderlist();
                }

                // ItemViewList();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }

    private void FindViewById() {
        rv_orderHistory =findViewById(R.id.rv_orderHistory);
        img_backBtn = findViewById(R.id.img_backBtn);
        txt_toolbarName = findViewById(R.id.txt_toolbarName);
        txt_emptyview = findViewById(R.id.txt_emptyview);
        parentLayout = findViewById(android.R.id.content);

        txt_toolbarName.setText(getResources().getString(R.string.pendingorders));
        orderObjects = new ArrayList<>();
        OrderObject historyObject = new OrderObject();
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderObjects.add(historyObject);
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderObjects.add(historyObject);
        historyObject.setD_images("");
        historyObject.setD_info("");
        orderObjects.add(historyObject);
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



    private void Orderlist() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_ORDERLIST,
                response -> {
                    Utils.log(context, "Location:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            //LocationObject.clear();
                            txt_emptyview.setVisibility(View.GONE);
                            rv_orderHistory.setVisibility(View.VISIBLE);
                            JSONArray dataArray = obj.getJSONArray("data");
                            orderObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                OrderObject playerModel = new OrderObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setRestaurant_name(product.getString("restaurant_name"));
                                    playerModel.setStatus(product.getString("status"));
                                    playerModel.setRestaurant_logo(product.getString("restaurant_logo"));
                                    playerModel.setRestaurant_address(product.getString("restaurant_address"));
                                    playerModel.setCuisine_id(product.getString("cuisine_id"));
                                    playerModel.setItem(product.getString("item"));
                                    playerModel.setCurrency(product.getString("currency"));
                                    playerModel.setTotal(product.getString("total"));
                                    Log.d("dfhdfgfdg","dfgdfgfd"+product.getString("restaurant_name"));
                                    orderObjects.add(playerModel);

                                    if (orderObjects != null) {
                                        orderAdapters.visibleContentLayout();

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("cvncvbcvbvc", "dfgfdgfd" + ex);
                                }
                            }

                        } else {
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_orderHistory.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("cvncvbcvbvc", "dfgfdgdf" + e);
                        txt_emptyview.setVisibility(View.VISIBLE);
                        rv_orderHistory.setVisibility(View.GONE);
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar(error_value);
            txt_emptyview.setVisibility(View.VISIBLE);
            rv_orderHistory.setVisibility(View.GONE);
            Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
            Log.d("cvncvbcvbvc", "d324dffgfdgdf" + error);
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

    private void OrderRecycler() {


        orderAdapters = new OrderAdapter( orderObjects);
        rv_orderHistory.setAdapter(orderAdapters);
        rv_orderHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

        boolean visible;

        private final ArrayList<OrderObject> orderObjects;
        OrderAdapter( ArrayList<OrderObject> orderObjects) {
            this.orderObjects = orderObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_pendingorder_list, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            if (visible) {

                Picasso.get().load(orderObjects.get(position).getRestaurant_logo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lc_img);

                holder.img_detailItem.setVisibility(View.VISIBLE);
                Log.d("dfgfdgdfg","dfgdfg"+orderObjects.get(position).getRestaurant_name());
                holder.lt_restaurantname.setText(orderObjects.get(position).getRestaurant_name());
                holder.lt_cuisine.setText(orderObjects.get(position).getCuisine_id());
                holder.lt_item.setText(orderObjects.get(position).getItem());
                holder.lt_currency.setText(orderObjects.get(position).getCurrency());
                holder.lt_totalprice.setText(orderObjects.get(position).getTotal());
                holder.lt_cuisine.setText(orderObjects.get(position).getStatus());
                if (orderObjects.get(position).getStatus().equalsIgnoreCase("ORDERED")) {
                    holder.lt_cuisine.setTextColor(getResources().getColor(R.color.ordered));
                } else if (orderObjects.get(position).getStatus().equalsIgnoreCase("ACCEPTED")) {
                    holder.lt_cuisine.setTextColor(getResources().getColor(R.color.approved));
                } else if (orderObjects.get(position).getStatus().equalsIgnoreCase("PICKEDUP")) {
                    holder.lt_cuisine.setTextColor(getResources().getColor(R.color.pending));
                }else if (orderObjects.get(position).getStatus().equalsIgnoreCase("COMPLETED")) {
                    holder.lt_cuisine.setTextColor(getResources().getColor(R.color.delivered));
                }else if (orderObjects.get(position).getStatus().equalsIgnoreCase("CANCELLED")) {
                    holder.lt_cuisine.setTextColor(getResources().getColor(R.color.rejected));
                }

                holder.img_detailItem.setOnClickListener(v -> {
                    FC_Common.order_id=orderObjects.get(position).getId();
                    ItemDialog();
                });

                holder.ll_content.setOnClickListener(v -> {
                    OrderObject item = orderObjects.get(position);
                    FC_Common.order_id = item.getId();

                    Intent intent = new Intent(context, FC_OrderPickedUpActivity.class);
                    intent.putExtra("order_id",FC_Common.order_id);
                    startActivity(intent);

                    Utils.log(context, "dfsdfdsfs" + FC_Common.order_id);

                });
            }
        }

        @Override
        public int getItemCount() {
            return orderObjects.size();
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
            LoaderTextView lt_restaurantname,lt_cuisine,lt_item,lt_currency,lt_totalprice;
            LoaderCircluarImageView lc_img;
            LinearLayout ll_content;
            ImageView img_detailItem;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_detailItem = itemView.findViewById(R.id.img_detailItem);
                lt_restaurantname = itemView.findViewById(R.id.lt_restaurantname);
                lc_img = itemView.findViewById(R.id.lc_img);
                lt_totalprice = itemView.findViewById(R.id.lt_totalprice);
                lt_cuisine = itemView.findViewById(R.id.lt_cuisine);
                lt_item = itemView.findViewById(R.id.lt_item);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                ll_content = itemView.findViewById(R.id.ll_content);
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
        Utils.playProgressBar(FC_PendingOrderActivity.this);
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
