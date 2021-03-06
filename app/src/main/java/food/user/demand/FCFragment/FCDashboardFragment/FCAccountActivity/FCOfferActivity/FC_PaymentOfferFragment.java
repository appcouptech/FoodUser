package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCOfferActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity.FCManageAddressActivity.FC_ManagelocationPickerActivty;
import food.user.demand.FCPojo.Fc_CouponObject.CouponObject;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.FCViews.VerticalLabelView;
import food.user.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FC_PaymentOfferFragment extends Fragment {

private Context context;
private Snackbar bar;
private LinearLayout ll_cardOffers;
private LoaderTextView lt_cardOffers;
private RecyclerView rv_couponItems;
private AC_Textview txt_emptyview;
private FrameLayout ll_frame;
    private Couponoffer couponoffers;
    private ArrayList<CouponObject> cardOffersObjects;
    public FC_PaymentOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Utils.adjustFontScale(Objects.requireNonNull(getActivity()),getResources().getConfiguration());
        return inflater.inflate(R.layout.fragment_fc__payment_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity() ;
        FindViewById(view);
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
        //snackBar("dfgsfdgsdg");
    }


    private void FindViewById(View view) {

        rv_couponItems =  view.findViewById(R.id.rv_couponItems);
        lt_cardOffers =  view.findViewById(R.id.lt_cardOffers);
        ll_cardOffers =  view.findViewById(R.id.ll_cardOffers);
        txt_emptyview =  view.findViewById(R.id.txt_emptyview);
        ll_frame =  view.findViewById(R.id.ll_frame);

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
    @Override
    public void onResume() {
        super.onResume();
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
                            //snackBar(String.valueOf(counter_cardoffer));

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
    @Override
    public void onPause() {
        super.onPause();
    }

    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(ll_frame, value, Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", v -> bar.dismiss());
                    bar.setActionTextColor(Color.RED);
                    TextView tv = bar.getView().findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.CYAN);
                    bar.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } }};
        timerThread.start();
    }
}
