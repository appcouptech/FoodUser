package food.user.demand.FCFragment.FCDashboardFragment.FC_Couponcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import food.user.demand.FCPojo.Fc_CouponObject.CouponObject;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.VerticalLabelView;
import food.user.demand.R;

public class Fc_Coupon extends AppCompatActivity {
    private Context context;
    private View parentLayout;
    private AC_Edittext edt_promoCode;
    private AC_Textview txt_applyPromo,txt_emptyview;
    private RecyclerView rv_couponItems;
    private Snackbar bar;
    private LoaderTextView lt_cardOffers;
    private LinearLayout ll_cardOffers;
    private Couponoffer couponoffers;
    private InputMethodManager inputMgr;
    private ArrayList<CouponObject> cardOffersObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_coupon);

        context = Fc_Coupon.this ;
        FindViewById();
        inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        cardoffersRecycler();
        CardoffersList();
        //cardOffersAdapter  Set//
        couponoffers = new Couponoffer(cardOffersObjects);
        LinearLayoutManager itemViewLLcard = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_couponItems.setLayoutManager(itemViewLLcard);
        rv_couponItems.setAdapter(couponoffers);
    }
    private void FindViewById() {
        parentLayout = findViewById(android.R.id.content);
        edt_promoCode =  findViewById(R.id.edt_promoCode);
        ImageView img_backBtn = findViewById(R.id.img_backBtn);
        txt_emptyview =  findViewById(R.id.txt_emptyview);
        txt_applyPromo =  findViewById(R.id.txt_applyPromo);
        rv_couponItems =  findViewById(R.id.rv_couponItems);
        lt_cardOffers =  findViewById(R.id.lt_cardOffers);
        ll_cardOffers =  findViewById(R.id.ll_cardOffers);


        img_backBtn.setOnClickListener(v -> onBackPressed());

        txt_applyPromo.setOnClickListener(v -> {
            if (edt_promoCode.getText().toString().equalsIgnoreCase(""))
            {
                snackBar(getResources().getString(R.string.promocode));
            }
            else {
                inputMgr.hideSoftInputFromWindow(txt_applyPromo.getWindowToken(), 0);
                FC_Common.OfferCode=edt_promoCode.getText().toString();
                ApplyPromoCode();
            }
        });


        /*CardOffers*/
        cardOffersObjects = new ArrayList<>();
        CouponObject offersObject = new CouponObject();
        offersObject.setD_images("");
        offersObject.setD_images("");
        cardOffersObjects.add(offersObject);
        offersObject.setD_images("");
        offersObject.setD_images("");
        cardOffersObjects.add(offersObject);
    }


    //CardOffer Async Task Start//
    private void CardoffersList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_CARDOFFERLIST,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optString("success").equals("1")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            cardOffersObjects.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                CouponObject playerModel = new CouponObject();
                                JSONObject product = dataArray.getJSONObject(i);
                                try {
                                    playerModel.setId(product.getString("id"));
                                    playerModel.setOffer_code(product.getString("offer_code"));
                                    playerModel.setDiscount(product.getString("discount"));
                                    playerModel.setDiscount_type(product.getString("discount_type"));
                                    playerModel.setDescription(product.getString("description"));
                                    playerModel.setEnable_limit(product.getString("enable_limit"));
                                    playerModel.setOffer_upto(product.getString("offer_upto"));
                                    playerModel.setOffer_upto(product.getString("offer_upto"));
                                    playerModel.setUse_count(product.getString("use_count"));
                                    playerModel.setCards(product.getString("cards"));
                                    cardOffersObjects.add(playerModel);
                                    if (cardOffersObjects != null) {
                                        lt_cardOffers.setVisibility(View.GONE);
                                        ll_cardOffers.setVisibility(View.VISIBLE);
                                        txt_emptyview.setVisibility(View.GONE);
                                        rv_couponItems.setVisibility(View.VISIBLE);
                                        couponoffers.visibleContentLayout();
                                    } else {
                                        lt_cardOffers.setVisibility(View.GONE);
                                        ll_cardOffers.setVisibility(View.VISIBLE);
                                        txt_emptyview.setVisibility(View.VISIBLE);
                                        rv_couponItems.setVisibility(View.GONE);

                                    /*    final int counter_cardoffer = cardoffercounter++;
                                        if (counter_cardoffer < 5) {
                                            CardoffersList();
                                        }*/
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    snackBar(String.valueOf(ex));
                                   /* final int counter_cardoffer = cardoffercounter++;
                                    if (counter_cardoffer < 5) {
                                        CardoffersList();
                                    }*/
                                } }
                        } else {

                            lt_cardOffers.setVisibility(View.GONE);
                            ll_cardOffers.setVisibility(View.VISIBLE);
                            txt_emptyview.setVisibility(View.VISIBLE);
                            rv_couponItems.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("" + e);
                      /*  final int counter_cardoffer = cardoffercounter++;
                        if (counter_cardoffer < 5) {
                            CardoffersList();
                        }*/
                    }
                }, error -> {
            String error_value = String.valueOf(error);
            snackBar("" + error_value);
                    /*final int counter_cardoffer = cardoffercounter++;
                    if (counter_cardoffer < 5) {
                        CardoffersList();
                    }*/
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        } }
    private void cardoffersRecycler() {
        couponoffers = new Couponoffer(cardOffersObjects);
        rv_couponItems.setAdapter(couponoffers);
        rv_couponItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private class Couponoffer extends RecyclerView.Adapter<Couponoffer.ViewHolder> {
        private final ArrayList<CouponObject> cardOffersObjects;
        boolean visible;
        Couponoffer( ArrayList<CouponObject> cardOffersObjects) {
            this.cardOffersObjects = cardOffersObjects;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_coupon_offer, parent, false);
            return new ViewHolder(listItem);
        }
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (visible) {
                lt_cardOffers.setVisibility(View.GONE);
                ll_cardOffers.setVisibility(View.VISIBLE);
                holder.vv_offer.setText(cardOffersObjects.get(position).getOffer_code());
                holder.txt_offer.setText(cardOffersObjects.get(position).getDiscount());
                holder.txt_cards.setText(cardOffersObjects.get(position).getCards());
                holder.ll_main.setOnClickListener(v -> {
                    FC_Common.couponcodeid=cardOffersObjects.get(position).getId();
                    FC_Common.couponcode=cardOffersObjects.get(position).getOffer_code();
                    UpdateCoupon();
                });

            } }
        @Override
        public int getItemCount() {
            return cardOffersObjects.size();
        }
        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        public void visibleContentLayout() {
            visible = true;
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {

            VerticalLabelView vv_offer;
            LoaderTextView txt_offer,txt_cards;
            LinearLayout ll_main;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ll_main = itemView.findViewById(R.id.ll_main);
                txt_cards = itemView.findViewById(R.id.txt_cards);
                txt_offer = itemView.findViewById(R.id.txt_offer);
                vv_offer = itemView.findViewById(R.id.vv_offer);

            }}}

    //CardOffer Async Task End//

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

    private void UpdateCoupon() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_COUPONUPDATE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            onBackPressed();
                        }
                        else
                        {
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

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
                params.put("couponcodeid", FC_Common.couponcodeid);
                params.put("couponcode", FC_Common.couponcode);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {

        Log.d("dfgdfgdfgdfg","dfgfdgfdgfdg");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("dfgdfgdfgdfg","check");
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ApplyPromoCode() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PROMOCODE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            onBackPressed();
                        }
                        else
                        {
                            onBackPressed();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

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
                params.put("offer_code", FC_Common.OfferCode);
                Log.d("getParams: ", "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
