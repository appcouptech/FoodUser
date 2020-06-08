package food.user.demand.FC_Wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCPojo.FC_Wallet.WalletObject;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_Wallet extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_earningList ;
    private EarningsAdapter earningsAdapter ;
    private ArrayList<WalletObject> WalletObjects = new ArrayList<>();
    private AC_Textview txt_emptyview,txt_withdraw,txt_recharge;
    private AC_Edittext edt_amt;
    private LoaderTextView lt_wallet;
    private ImageView img_backBtn;
    private AC_Textview txt_toolbarName,txt_cancel;
    private AC_Textview txt_confirm;
    private AC_Edittext edt_commentres;
    private BottomSheetDialog paymentdialog;
    private Button payButton;
    private CardInputWidget cardInputWidget;
    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        context = FC_Wallet.this;
        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());

        FindViewById();
        AccessCheck();
        earningsAdapter = new EarningsAdapter(WalletObjects);
        rv_earningList.setAdapter(earningsAdapter);
        rv_earningList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        walletAmount();
        txt_recharge.setOnClickListener(v -> {
           /* WalletRechargeDialog walletDialog = WalletRechargeDialog.newInstance();
            walletDialog.show(Objects.requireNonNull(FCD_Wallet.this).getSupportFragmentManager(),
                    "walletDialog");*/

            @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.activity_checkout, null);
            FindViewByIdBottomDialogRating1(view1);
            paymentdialog = new BottomSheetDialog(context);
            paymentdialog.setContentView(view1);
            paymentdialog.show();

            payButton.setOnClickListener(v12 -> {
                if (edt_amt.getText().toString().trim().equalsIgnoreCase("")||
                        edt_amt.getText().toString().trim().equalsIgnoreCase("0"))
                {
                    Utils.toast(context,getResources().getString(R.string.enter_amt));
                }
                else {
                    FC_Common.recharge_amt=edt_amt.getText().toString().trim();
                    pay();
                }
            });

        });

        img_backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void FindViewByIdBottomDialogRating1(View view) {
        payButton = view.findViewById(R.id.payButton);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);
        edt_amt = view.findViewById(R.id.edt_amt);
       // toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setTitle(getResources().getString(R.string.recharge));
    }
    private void FindViewById() {

        img_backBtn = findViewById(R.id.img_backBtn);
        rv_earningList = findViewById(R.id.rv_earningList);
        txt_emptyview = findViewById(R.id.txt_emptyview);
        lt_wallet = findViewById(R.id.lt_wallet);
        txt_withdraw = findViewById(R.id.txt_withdraw);
        txt_recharge = findViewById(R.id.txt_recharge);
        img_backBtn.setVisibility(View.VISIBLE);

        txt_toolbarName = findViewById(R.id.txt_toolbarName);
        img_backBtn.setVisibility(View.VISIBLE);
        txt_toolbarName.setVisibility(View.VISIBLE);
        txt_toolbarName.setText(getResources().getString(R.string.wallet));



        WalletObject earningsObject = new WalletObject();
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);
        earningsObject.setD_image("");
        earningsObject.setD_name("");
        WalletObjects.add(earningsObject);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @SuppressLint("SetTextI18n")
    private void  walletAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_WALLETINFO,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("fdghfdgfdgd","fdgfd"+obj);
                        FC_Common.WalletCurrency=obj.getString("currency");
                        FC_Common.Walletbalance=obj.getString("wallet_balance");
                        lt_wallet.setText(FC_Common.WalletCurrency+" "+FC_Common.Walletbalance);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("recent_transactions");
                            Log.d("fghfghfg","fhfghfg"+dataArray);
                            WalletObjects.clear();
                            rv_earningList.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.GONE);
                            //lt_mainTotal.setText(FC_Common.todaycurrency+" "+FC_Common.todayTotal);
                            for (int i = 0; i < dataArray.length(); i++) {
                                WalletObject playerModel = new WalletObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {

                                    playerModel.setId(product.getString("id"));
                                    playerModel.setAmount(product.getString("amount"));
                                    playerModel.setTrans_mode(product.getString("trans_mode"));
                                    playerModel.setTrans_id(product.getString("trans_id"));
                                    playerModel.setStatus(product.getString("status"));
                                    playerModel.setCreated_at(product.getString("created_at"));
                                    playerModel.setNote(product.getString("note"));


                                    WalletObjects.add(playerModel);
                                    if (WalletObjects != null) {
                                        earningsAdapter.visibleContentLayout();
                                        Log.d("fdghfdgfdgd","654hgfnjfgfghfghfghfg");
                                    }
                                    else {
                                        rv_earningList.setVisibility(View.GONE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        Log.d("fdghfdgfdgd","243df4545");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.d("fdghfdgfdgd","65fghfgh"+ex);
                                }}
                        } else {
                            rv_earningList.setVisibility(View.GONE);
                            txt_emptyview.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Utils.snackBar(context,);
                    }
                }, error -> {
         //   String error_value = String.valueOf(error);
           // snackBar("Hotsellernew" + error_value);
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                Log.d("fghfghfghfg","fdhfgg"+FC_Common.token_type + " " + FC_Common.access_token);
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
    private class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.ViewHolder>{
        private final ArrayList<WalletObject> WalletObjects;
        boolean visible;
        EarningsAdapter(ArrayList<WalletObject> WalletObjects) {

            this.WalletObjects = WalletObjects ;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_todays_earings_item, parent, false);
            return new ViewHolder(listItem);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {

                Log.d("fdghfdgfdgd","fdh546ydfg");

                holder.viewLine.setVisibility(View.INVISIBLE);
                // holder.lt_restaurantName.setText(WalletObjects.get(position).getRestaurant_name());
                holder.lt_dateTime.setText(WalletObjects.get(position).getCreated_at());
                holder.lt_status.setText(WalletObjects.get(position).getStatus());
                holder.lt_restaurantName.setText(WalletObjects.get(position).getTrans_mode());
                if (WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("RECHARGE"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_green));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else if(WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("DEBITED"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_red));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else if(WalletObjects.get(position).getTrans_mode().equalsIgnoreCase("CREDITED"))
                {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_blue));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    holder.ll_main.setBackgroundColor(getResources().getColor(R.color.pale_blue));
                    holder.lt_restaurantName.setTextColor(getResources().getColor(R.color.black));
                    holder.lt_currency.setTextColor(getResources().getColor(R.color.black));
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date oneWayTripDate = input.parse(WalletObjects.get(position).getCreated_at());
                    assert oneWayTripDate != null;
                    FC_Common.todayDate= output.format(oneWayTripDate);
                    holder.lt_dateTime.setText(FC_Common.todayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // holder.lt_currency.setText(FC_Common.todaycurrency+" "+WalletObjects.get(position).getDelivery_charge());
                holder.lt_currency.setText(FC_Common.WalletCurrency+" "+WalletObjects.get(position).getAmount());
            }
            else {

                Log.d("fdghfdgfdgd","fghfghfghfg");
            }
        }

        @Override
        public int getItemCount() {
            return WalletObjects.size();
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
            View viewLine ;
            LinearLayout ll_main;
            LoaderTextView lt_restaurantName,lt_dateTime,lt_currency,lt_status;
            ViewHolder(@NonNull View itemView) {
                super(itemView);

                viewLine = itemView.findViewById(R.id.viewLine);
                ll_main = itemView.findViewById(R.id.ll_main);
                lt_restaurantName = itemView.findViewById(R.id.lt_restaurantName);
                lt_dateTime = itemView.findViewById(R.id.lt_dateTime);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                lt_status = itemView.findViewById(R.id.lt_status);
            }
        }
    }

    private void pay() {

        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                //pay(result.id, null);
                String paymentid=result.id;
                Payment(paymentid);
                Log.d("gfhdfgdfg","dfgdfg"+result.id);
                Log.d("gfhdfgdfg","dfgdfg"+paymentid);
            }

            @Override
            public void onError(@NonNull Exception e) {
                e.printStackTrace();
                Log.d("dfghfdgdfg","dgsdf"+e);
            }
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccessCheck() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.ROOTSTRIPE,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    Log.d("ServerResponse", "FCD_URL.ROOTSTRIPE" + FC_URL.ROOTSTRIPE);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.stripe_publickey = jsonResponse1.getString("stripe_publickey");

                        final Context applicationContext = getApplicationContext();
                        PaymentConfiguration.init(applicationContext, FC_Common.stripe_publickey);
                        stripe = new Stripe(applicationContext, FC_Common.stripe_publickey);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Log.d("dfgfdgfd","dfgsdfd"+value);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FC_Common.token_type + " " + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                // params.put("X-Requested-With", FC_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(FC_Wallet.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void Payment(String paymentid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_WALLETPAYMENT,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard12" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        if (FC_Common.status.equalsIgnoreCase("1")) {
                            paymentdialog.dismiss();
                            walletAmount();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Log.d("dfgdffgd","dfgdf"+value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("paymentmethodid", paymentid);
                params.put("amount",FC_Common.recharge_amt);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FC_Common.token_type + " " + FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_Wallet.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }



}
