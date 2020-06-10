package food.user.demand.FCFragment.FCDashboardFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCActivity.FCCartActivity.FC_CartAddressActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCFragment.FCDashboardFragment.FC_Couponcode.Fc_Coupon;
import food.user.demand.FCPojo.FCCartFragmentObject.CartFragmentObject;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCUtils.LikeButton.LikeButton;
import food.user.demand.FCUtils.Loader.LoaderImageView;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.AC_Edittext;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.CircleImageView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class FC_CartFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_main,ll_address,ll_nocart;
    private Snackbar bar;
    private Stripe stripe;
    private LikeButton vector_android_button;
    private CartAdapter cartAdapter;
    private Button payButton;
    private CardInputWidget cardInputWidget;
    private RecyclerView rv_cartItems;
    private LoaderTextView lt_totalCurrency,lt_taxCurrency,lt_discountCurrency,lt_delCurrency,lt_itemCurrency,lt_wallet,lt_currency,lt_preorder,lt_restaurantName,lt_cuisines,lt_restaurantAddress,lt_restaurantPhone,lt_totalTax,lt_totalAmount,lt_itemTotal,lt_deliveryFee,lt_totalDiscount;
    private Context context ;
    private int cartcounter = 0;
    private Handler handler;
    private BottomSheetDialog dialog;
    private LocationAdapter locationadapter;
    private ArrayList<LocationObject> locationObjects = new ArrayList<>();
    private RecyclerView rv_search;
    private ArrayList<CartFragmentObject> cartActivityObjects ;
    private AC_Textview txt_processToPay;
    private AC_Edittext edt_amt;
    private AC_Textview txt_header;
    private AC_Textview txt_address;
    private AC_Textview txt_locationSet;
    private AC_Textview txt_hotelUnavailable ;
    private AC_Textview txt_cashon,txt_applyPromo ,txt_paytm;
    private AC_Textview edt_promoCode;
    private BottomSheetDialog paymentdialog;
    private BottomSheetDialog carddialog;
    private Button btn_timeset;
    private Calendar calendar;
    private TimePicker timePicker;
    private FrameLayout frame_payment;
    private View view_payment;
    private CheckBox  chk_wallet ;
    private InputMethodManager inputMgr;
    public FC_CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_fc__cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity() ;
        FindViewById(view);

        inputMgr = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);

        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.email_verified_at = String.valueOf(user.getemail_verified_at());
        FC_Common.dial_code = String.valueOf(user.getdial_code());
        FC_Common.location_name = String.valueOf(user.getlocation_name());
        FC_Common.location_type = String.valueOf(user.getlocation_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.status = String.valueOf(user.getstatus());
        FC_Common.is_guest = String.valueOf(user.getis_guest());
        FC_Common.picture = String.valueOf(user.getpicture());
        FC_Common.device_token = String.valueOf(user.getdevice_token());
        FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.device_type = String.valueOf(user.getdevice_type());
        FC_Common.login_by = String.valueOf(user.getlogin_by());
        FC_Common.social_unique_id = String.valueOf(user.getsocial_unique_id());
        FC_Common.cust_id = String.valueOf(user.getcust_id());
        FC_Common.wallet_balance = String.valueOf(user.getwallet_balance());
        FC_Common.rating = String.valueOf(user.getrating());
        FC_Common.userotp = String.valueOf(user.getotp());
        FC_Common.created_at = String.valueOf(user.getcreated_at());
        FC_Common.updated_at = String.valueOf(user.getupdated_at());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.gender = String.valueOf(user.getgender());
        FC_Common.is_default = String.valueOf(user.getis_default());

        Utils.log(context,"sdfsdfsdfsd"+"latitude"+FC_Common.latitude);
        Utils.log(context,"sdfsdfsdfsd"+"longitude"+FC_Common.longitude);
        Utils.log(context,"sdfsdfsdfsd"+"is_default"+FC_Common.is_default);
        AllCartRecycler();
        AllCartList();

        if (FC_Common.walletchcked.equalsIgnoreCase("1"))
        {
            chk_wallet.setChecked(true);
        }
        else {
            chk_wallet.setChecked(false);
        }
        // enableSwipeToDelete();
        cartAdapter = new CartAdapter(cartActivityObjects);
        LinearLayoutManager itemViewLLres = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_cartItems.setLayoutManager(itemViewLLres);
        rv_cartItems.setAdapter(cartAdapter);
        ll_main.setOnClickListener(v -> inputMgr.hideSoftInputFromWindow(ll_main.getWindowToken(), 0));


    }

    private void FindViewById(View view) {
         ImageView img_instruction =  view.findViewById(R.id.img_instruction);
        lt_currency =  view.findViewById(R.id.lt_currency);
        lt_wallet =  view.findViewById(R.id.lt_wallet);
        lt_itemCurrency =  view.findViewById(R.id.lt_itemCurrency);
        lt_totalCurrency =  view.findViewById(R.id.lt_totalCurrency);
        lt_taxCurrency =  view.findViewById(R.id.lt_taxCurrency);
        lt_discountCurrency =  view.findViewById(R.id.lt_discountCurrency);
        lt_delCurrency =  view.findViewById(R.id.lt_delCurrency);
        ll_nocart =  view.findViewById(R.id.ll_nocart);
        ll_main = view.findViewById(R.id.ll_main);
        edt_promoCode =  view.findViewById(R.id.edt_promoCode);
        txt_applyPromo =  view.findViewById(R.id.txt_applyPromo);
        timePicker =  view.findViewById(R.id.timePicker);
        chk_wallet =  view.findViewById(R.id.chk_wallet);
        btn_timeset =  view.findViewById(R.id.btn_timeset);
        frame_payment = view.findViewById(R.id.frame_payment);
        view_payment = view.findViewById(R.id.view_payment);
        ImageView img_payment = view.findViewById(R.id.img_payment);
        lt_preorder = view.findViewById(R.id.lt_preorder);
        ImageView img_preorder = view.findViewById(R.id.img_preorder);
        vector_android_button = view.findViewById(R.id.vector_android_button);
        txt_processToPay = view.findViewById(R.id.txt_processToPay);
        txt_hotelUnavailable = view.findViewById(R.id.txt_hotelUnavailable);
        txt_locationSet = view.findViewById(R.id.txt_locationSet);
        txt_address = view.findViewById(R.id.txt_address);
        ll_address = view.findViewById(R.id.ll_address);
        ImageView img_deliveryTip = view.findViewById(R.id.img_deliveryTip);
        ImageView img_taxTip = view.findViewById(R.id.img_taxTip);
        rv_cartItems = view.findViewById(R.id.rv_cartItems);
        ImageView img_backBtn = view.findViewById(R.id.img_backBtn);
        lt_restaurantName = view.findViewById(R.id.lt_restaurantName);
        lt_cuisines = view.findViewById(R.id.lt_cuisines);
        lt_restaurantAddress = view.findViewById(R.id.lt_restaurantAddress);
        lt_restaurantPhone = view.findViewById(R.id.lt_restaurantPhone);
        lt_itemTotal = view.findViewById(R.id.lt_itemTotal);
        lt_totalAmount = view.findViewById(R.id.lt_totalAmount);
        lt_totalDiscount = view.findViewById(R.id.lt_totalDiscount);
        lt_deliveryFee = view.findViewById(R.id.lt_deliveryFee);
        lt_totalTax = view.findViewById(R.id.lt_totalTax);

        AC_Textview txt_addAddressToProcess = view.findViewById(R.id.txt_addAddressToProcess);
        AC_Textview txt_addAddress = view.findViewById(R.id.txt_addAddress);
        AC_Textview txt_selectAddress = view.findViewById(R.id.txt_selectAddress);

        img_backBtn.setOnClickListener(this);
        txt_addAddressToProcess.setOnClickListener(this);
        txt_addAddress.setOnClickListener(this);
        txt_selectAddress.setOnClickListener(this);
        txt_processToPay.setOnClickListener(this);
        edt_promoCode.setOnClickListener(this);
        img_instruction.setOnClickListener(this);

        txt_applyPromo.setVisibility(View.GONE);

        chk_wallet.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                Utils.playProgressBar(getActivity());
                FC_Common.walletchcked="1";
                AllCartList();
               // walletupdate();
                Log.d("dgfsdfgsdf","sdfsdfsd"+FC_Common.walletchcked);
            }
            else {
                Utils.playProgressBar(getActivity());
                FC_Common.walletchcked="0";
               // walletupdate();
                AllCartList();
                Log.d("dgfsdfgsdf","sdfsdfsd"+FC_Common.walletchcked);
            }
        });

        img_deliveryTip.setOnClickListener(v -> {
            Utils.log(context,"sdgsdgsdgsdgsd"+"sdfsdfsdfsdfsdf");
            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                    .setCancelable(true)
                    .setDismissOnClick(true)
                    .setCornerRadius(20f)
                    .setGravity(Gravity.TOP)
                    .setText("Distance Charge : "+FC_Common.Cartdistance_charge+"\n"+"Delivery fee : "+FC_Common.Cartdelivery_fee);
            builder.show();

        });

        img_taxTip.setOnClickListener(v -> {
            Tooltip mTooltip = new Tooltip.Builder(v, R.style.Tooltip)
                    .setDismissOnClick(true)
                    .setCancelable(true)
                    .setGravity(Gravity.TOP)
                    .setText("Packaging Charge : "+FC_Common.Cartpackaging_charge+"\n"+"Tax : "+FC_Common.Carttax)
                    .show();
        });

        txt_address.setOnClickListener(v -> {
            Utils.log(context,"sdgsdgsdgsdgsd"+"sdfsdfsdfsdfsdf");

            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                    .setCancelable(true)
                    .setDismissOnClick(true)
                    .setCornerRadius(20f)
                    .setGravity(Gravity.TOP)
                    .setText(FC_Common.location_name);
            builder.show();

        });

        vector_android_button.setOnClickListener(v -> {
            Log.d("fhfdhfdgfd","dfgfdgfdg"+FC_Common.Cartfavourite);
            if (FC_Common.Cartfavourite.equalsIgnoreCase("0"))
            {
                String favouritecode="1";
                String url=FC_URL.URL_FAVOURITEADD;
                Favourite(favouritecode,url);
            }
            else {
                String favouritecode="2";
                String url=FC_URL.URL_FAVOURITEDELETE;
                Favourite(favouritecode,url);
            }
        });


        img_preorder.setOnClickListener(v -> {


            timePicker.setVisibility(View.VISIBLE);
            btn_timeset.setVisibility(View.VISIBLE);
            calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            showTime(hour, min);
        });

        btn_timeset.setOnClickListener(v -> setTime());

        img_payment.setOnClickListener(v -> {
            FC_Common.preordertime="";
            frame_payment.setVisibility(View.GONE);
            view_payment.setVisibility(View.GONE);
        });

        cartActivityObjects = new ArrayList<>();
        CartFragmentObject fragmentObject = new CartFragmentObject();
        fragmentObject.setD_images("");
        fragmentObject.setD_info("");
        cartActivityObjects.add(fragmentObject);
        fragmentObject.setD_images("");
        fragmentObject.setD_info("");
        cartActivityObjects.add(fragmentObject);

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

    }

    @Override
    public void onResume() {
        Utils.playProgressBar(getActivity());
        FC_User user = FC_SharedPrefManager.getInstance(context).getUser();
        FC_Common.id = String.valueOf(user.getid());
        FC_Common.name = String.valueOf(user.getname());
        FC_Common.email = String.valueOf(user.getemail());
        FC_Common.mobile = String.valueOf(user.getmobile());
        FC_Common.email_verified_at = String.valueOf(user.getemail_verified_at());
        FC_Common.dial_code = String.valueOf(user.getdial_code());
        FC_Common.location_name = String.valueOf(user.getlocation_name());
        FC_Common.location_type = String.valueOf(user.getlocation_type());
        FC_Common.latitude = String.valueOf(user.getlatitude());
        FC_Common.longitude = String.valueOf(user.getlongitude());
        FC_Common.status = String.valueOf(user.getstatus());
        FC_Common.is_guest = String.valueOf(user.getis_guest());
        FC_Common.picture = String.valueOf(user.getpicture());
        FC_Common.device_token = String.valueOf(user.getdevice_token());
        FC_Common.device_id = String.valueOf(user.getdevice_id());
        FC_Common.device_type = String.valueOf(user.getdevice_type());
        FC_Common.login_by = String.valueOf(user.getlogin_by());
        FC_Common.social_unique_id = String.valueOf(user.getsocial_unique_id());
        FC_Common.cust_id = String.valueOf(user.getcust_id());
        FC_Common.wallet_balance = String.valueOf(user.getwallet_balance());
        FC_Common.rating = String.valueOf(user.getrating());
        FC_Common.userotp = String.valueOf(user.getotp());
        FC_Common.created_at = String.valueOf(user.getcreated_at());
        FC_Common.updated_at = String.valueOf(user.getupdated_at());
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FC_Common.gender = String.valueOf(user.getgender());
        FC_Common.is_default = String.valueOf(user.getis_default());
        AllCartList();
        Log.d("fdgfdgfd","dfgfdgd"+FC_Common.OfferCode);
        if (FC_Common.OfferCode.equalsIgnoreCase(""))
        {
            edt_promoCode.setText("");
            edt_promoCode.setHint(getResources().getString(R.string.enter_coupon_code));
        }
        else {
            edt_promoCode.setText(FC_Common.OfferCode);
        }
        super.onResume();
    }



    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.txt_addAddressToProcess :

                Intent addAddressToProcess = new Intent(getActivity(), FC_CartAddressActivity.class);
                startActivity(addAddressToProcess);
                break;

            case R.id.txt_addAddress :

                Intent addAddress = new Intent(getActivity(), FC_CartAddressActivity.class);
                startActivity(addAddress);
                break;
            case R.id.edt_promoCode :

                Intent promocode = new Intent(getActivity(), Fc_Coupon.class);
                promocode.putExtra("partner_id",FC_Common.Cartrestaurant_id);
                Animation animSlideUp1 = AnimationUtils.loadAnimation(context, R.anim.slide_up_menu);
                startActivity(promocode);
               /* startActivity(new Intent(getActivity(), Fc_Coupon.class));
                overridePendingTransition(R.anim.slide_up_menu, R.anim.slide_down);*/
                break;


            case R.id.txt_selectAddress :
                @SuppressLint("InflateParams")
                View view1 = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet, null);
                FindViewByIdBottomDialog(view1);
                LocationRecycler();
                locationlist();
                dialog = new BottomSheetDialog(context);
                dialog.setContentView(view1);
                dialog.show();
                break;

            case R.id.img_backBtn:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;

            case R.id.img_instruction:

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_dialog_new, null);
                AC_Edittext edt_comment = dialogView.findViewById(R.id.edt_comment);
                Button bt_cancel =  dialogView.findViewById(R.id.bt_cancel);
                Button bt_submit =  dialogView.findViewById(R.id.bt_submit);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                bt_cancel.setOnClickListener(v -> alertDialog.dismiss());

                bt_submit.setOnClickListener(v -> {

                    FC_Common.note=edt_comment.getText().toString();
                    alertDialog.dismiss();
                });
                alertDialog.show();
                break;

            case R.id.txt_processToPay:
                @SuppressLint("InflateParams")
                View view12 = getLayoutInflater().inflate(R.layout.bottom_payment_gateway, null);
                FindViewByIdBottomDialog1(view12);
                paymentdialog = new BottomSheetDialog(context);
                paymentdialog.setContentView(view12);
                paymentdialog.show();
                txt_cashon.setOnClickListener(v -> {
                    FC_Common.paymentid="";
                    FC_Common.paymenttype="CASH";
                    Submitorder();
                });

                txt_paytm.setOnClickListener(v -> {

                   /* Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("Cartrestaurant_name",FC_Common.Cartrestaurant_name);
                    intent.putExtra("Cartcurrency",FC_Common.Cartcurrency);
                    intent.putExtra("Carttotal",FC_Common.Carttotal);
                    intent.putExtra("mobile",FC_Common.mobile);
                    intent.putExtra("email",FC_Common.email);
                    intent.putExtra("CARD",FC_Common.paymenttype);
                    intent.putExtra("NOTE",FC_Common.note);
                    startActivity(intent);*/

                    AccessCheck();

                });

                Log.d("fdghfdhgfdg","fdhgfdghgfh");
                break;
        }
    }

    private void AllCartList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTCARTLIST,
                response -> {
                    Utils.log(context, "URL_PRODUCTCARTLIST" + response);
                    Utils.log(context, "URL_PRODUCTCARTLIST" + FC_URL.URL_PRODUCTCARTLIST);
                    try {
                        JSONObject obj = new JSONObject(response);

                        FC_Common.Cartsuccess= obj.getString("success");
                        if (FC_Common.Cartsuccess.equalsIgnoreCase("1"))
                        {
                            ll_nocart.setVisibility(View.GONE);
                            Utils.stopProgressBar();
                            FC_Common.Cartrestaurant_id = obj.getString("id");
                            FC_Common.Cartrestaurant_name = obj.getString("restaurant_name");
                            FC_Common.Cartrestaurant_address = obj.getString("restaurant_address");
                            FC_Common.Cartrestaurant_latitude = obj.getString("restaurant_latitude");
                            FC_Common.Cartrestaurant_longitude = obj.getString("restaurant_longitude");
                            FC_Common.Cartrestaurant_phone = obj.getString("restaurant_phone");
                            FC_Common.Cartdish_id = obj.getString("dish_id");
                            FC_Common.Cartfree_tax = obj.getString("free_tax");
                            FC_Common.Carttax_percent = obj.getString("tax_percent");
                            FC_Common.Cartfree_package = obj.getString("free_package");
                            FC_Common.Cartpackaging_charge = obj.getString("packaging_charge");
                            FC_Common.Cartcuisines = obj.getString("cuisines");
                            FC_Common.Carttotal_quantity= obj.getString("total_quantity");
                            FC_Common.Cartitem_total= obj.getString("item_total");
                            FC_Common.Cartdelivery_fee= obj.getString("delivery_fee");
                            FC_Common.Cartdistance_charge= obj.getString("distance_charge");
                            FC_Common.Cartdiscount= obj.getString("discount");
                            FC_Common.Carttax= obj.getString("tax");
                            FC_Common.Carttaxes_charges= obj.getString("taxes_charges");
                            FC_Common.Carttotal= obj.getString("total");
                            FC_Common.Cartfavourite= obj.getString("favourite");
                            FC_Common.CartWallet= obj.getString("wallet_balance");
                            FC_Common.Cartcurrency = obj.getString("currency");
                            FC_Common.Cartpromo_msg = obj.getString("promo_msg");

                            FC_Common.Cartis_default= obj.getString("is_default");
                            FC_Common.Cartservice_availability= obj.getString("service_availability");
                            FC_Common.Cartrestaurant_status= obj.getString("restaurant_status");
                            Log.d("dfhdfhdfghdfgh","sdfsdfsdfsd"+FC_Common.Cartfavourite);

                            if (FC_Common.Cartpromo_msg.equalsIgnoreCase("valid"))
                            {
                                snackBar(FC_Common.OfferCode+" "+getResources().getString(R.string.promocodevalid));
                            }
                            if (FC_Common.Cartpromo_msg.equalsIgnoreCase("expired"))
                            {
                                snackBar(FC_Common.OfferCode+" "+getResources().getString(R.string.promocodeexpired));
                                FC_Common.OfferCode="";
                            }
                           /* if (FC_Common.Cartpromo_msg.equalsIgnoreCase("invalid"))
                            {
                                FC_Common.OfferCode="";
                                snackBar(FC_Common.OfferCode+" "+getResources().getString(R.string.promocodeexpired));
                            }*/
                            if (FC_Common.Cartfavourite.equalsIgnoreCase("1"))
                            {
                                vector_android_button.setVisibility(View.VISIBLE);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                            }
                            if (!FC_Common.Cartfavourite.equalsIgnoreCase("1"))
                            {
                                vector_android_button.setVisibility(View.VISIBLE);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites));
                            }
                            if (FC_Common.Cartis_default.equalsIgnoreCase("0"))
                            {
                                //txt_home.setText(R.string.d_home);
                                txt_locationSet.setText(R.string.seem_to_new_location);
                                txt_address.setText(FC_Common.location_name);
                                ll_address.setVisibility(View.VISIBLE);
                                txt_processToPay.setVisibility(View.GONE);
                                //txt_addAddress.setVisibility(View.VISIBLE);
                            }
                            else {
                                if (FC_Common.Cartservice_availability.equalsIgnoreCase("1"))
                                {
                                    if (FC_Common.Cartrestaurant_status.equalsIgnoreCase("OPEN"))
                                    {
                                        txt_locationSet.setText(FC_Common.location_type);
                                        txt_address.setText(FC_Common.location_name);
                                        ll_address.setVisibility(View.VISIBLE);
                                        txt_processToPay.setVisibility(View.VISIBLE);
                                        // txt_addAddress.setVisibility(View.GONE);
                                        txt_hotelUnavailable.setVisibility(View.GONE);
                                    }
                                    else {
                                        txt_hotelUnavailable.setText(R.string.unavailable);
                                        txt_hotelUnavailable.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    txt_locationSet.setText(FC_Common.location_type);
                                    txt_address.setText(FC_Common.location_name);
                                    txt_processToPay.setVisibility(View.GONE);
                                    // txt_addAddress.setVisibility(View.VISIBLE);
                                    ll_address.setVisibility(View.VISIBLE);
                                    txt_hotelUnavailable.setVisibility(View.VISIBLE);
                                    txt_hotelUnavailable.setText(R.string.unavailable);
                                }

                            }
                            lt_restaurantName.setText(FC_Common.Cartrestaurant_name);
                            lt_cuisines.setText(FC_Common.Cartcuisines);
                            lt_restaurantAddress.setText(FC_Common.Cartrestaurant_address);
                            lt_restaurantPhone.setText(FC_Common.Cartrestaurant_phone);
                            lt_itemTotal.setText(FC_Common.Cartitem_total);
                            lt_totalDiscount.setText(FC_Common.Cartdiscount);
                            lt_deliveryFee.setText(FC_Common.Cartdelivery_fee);
                            lt_totalTax.setText(FC_Common.Carttaxes_charges);
                            lt_totalAmount.setText(FC_Common.Carttotal);
                            lt_currency.setText(FC_Common.Cartcurrency);
                            lt_wallet.setText(FC_Common.CartWallet);
                            lt_itemCurrency.setText(FC_Common.Cartcurrency);
                            lt_taxCurrency.setText(FC_Common.Cartcurrency);
                            lt_delCurrency.setText(FC_Common.Cartcurrency);
                            lt_discountCurrency.setText(FC_Common.Cartcurrency);
                            lt_totalCurrency.setText(FC_Common.Cartcurrency);

                            int ded=txt_address.getMaxLines();
                            Log.d("dfgsdgsdfs", String.valueOf(ded));
                            if (FC_Common.Cartsuccess.equalsIgnoreCase("1")) {
                                //LocationObject.clear();
                                JSONArray dataArray = obj.getJSONArray("product");
                                cartActivityObjects.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    CartFragmentObject playerModel = new CartFragmentObject();
                                    JSONObject product = dataArray.getJSONObject(i);
                                    try {
                                        playerModel.setId(product.getString("id"));
                                        playerModel.setPartner_id(product.getString("partner_id"));
                                        playerModel.setUser_id(product.getString("user_id"));
                                        playerModel.setProduct_id(product.getString("product_id"));
                                        playerModel.setProduct_price_id(product.getString("product_price_id"));
                                        playerModel.setPrice_original(product.getString("price_original"));
                                        playerModel.setPrice(product.getString("price"));
                                        playerModel.setQuantity(product.getString("quantity"));
                                        playerModel.setNote(product.getString("note"));
                                        playerModel.setAddon(product.getString("addon"));
                                        playerModel.setIngredient(product.getString("ingredient"));
                                        playerModel.setPreparation(product.getString("preparation"));
                                        playerModel.setProduct_name(product.getString("product_name"));
                                        playerModel.setPhoto(product.getString("photo"));
                                        playerModel.setCuisine_id(product.getString("cuisine_id"));
                                        playerModel.setDish_id(product.getString("dish_id"));
                                        playerModel.setCurrency(product.getString("currency"));
                                        cartActivityObjects.add(playerModel);

                                        if (cartActivityObjects != null) {
                                            cartAdapter.visibleContentLayout();
                                            Utils.stopProgressBar();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Utils.stopProgressBar();
                                        Log.d("dfgdfgfd", "dfgfdgfd" + ex);
                                     /*   final int counter_AllResataurant = cartcounter++;
                                        Utils.log(context, "countervalue" + "A:" + counter_AllResataurant);
                                        if (counter_AllResataurant < 5) {
                                            AllCartList();
                                        }*/

                                    }
                                }
                            }
                            else {
                                Utils.stopProgressBar();
                               /* final int counter_AllResataurant = cartcounter++;
                                Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                                if (counter_AllResataurant<5) {
                                    AllCartList();
                                }*/
                            }
                        }
                        else {
                            Utils.stopProgressBar();
                            ll_nocart.setVisibility(View.VISIBLE);
                            //Objects.requireNonNull(getActivity()).onBackPressed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackBar("Hotseller"+e);
                        Utils.stopProgressBar();
                        /*final int counter_AllResataurant = cartcounter++;
                        Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
                        if (counter_AllResataurant<5) {
                            AllCartList();
                        }*/
                        Utils.log(context, "dfgdfgdfg" + "dfgfd" + e);
                        Log.d("dfgfdgfdg", "dfgfdgdf" + e);
                    }
                }, error -> {
            //displaying the error in toast if occurrs
            String error_value = String.valueOf(error);
            Utils.stopProgressBar();
            snackBar("Hotsellernew"+error_value);
            Utils.log(context, "dfgdfgdfg" + "dfgfd" + error);
            Log.d("dfgfdgfdg", "d324dffgfdgdf" + error);
           /* final int counter_AllResataurant = cartcounter++;
            Utils.log(context, "countervalue" +"A:"+counter_AllResataurant);
            if (counter_AllResataurant<5) {
                AllCartList();
            }*/
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", FC_Common.latitude);
                params.put("longitude", FC_Common.longitude);
                params.put("wallet_check", FC_Common.walletchcked);
                params.put("offer_code", FC_Common.OfferCode);
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
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Utils.log(context,"dgsdgsdfgsd"+ex);
        }

    }
    private void AllCartRecycler() {
        cartAdapter = new CartAdapter(cartActivityObjects);
        rv_cartItems.setAdapter(cartAdapter);
        rv_cartItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        private final ArrayList<CartFragmentObject> cartActivityObjects;
        boolean visible;

        CartAdapter(ArrayList<CartFragmentObject> cartActivityObjects) {

            this.cartActivityObjects = cartActivityObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_cart_items, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (visible) {
                holder.pb_cart.setVisibility(View.GONE);
                if (cartActivityObjects.get(position).getDish_id().equalsIgnoreCase("1")) {
                    holder.img_VegNonVegType.setBackgroundResource(R.drawable.veg);
                }
                else {
                    holder.img_VegNonVegType.setBackgroundResource(R.drawable.non_veg);
                }
                holder.ll_loader.setVisibility(View.GONE);
                holder.ll_content.setVisibility(View.VISIBLE);
                //Picasso.get().load(cartActivityObjects.get(position).getPhoto()).into(holder.lc_circleItem);
                Picasso.get().load(cartActivityObjects.get(position).getPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.lc_circleItem);
                holder.txt_dishName.setText(cartActivityObjects.get(position).getProduct_name());
                holder.lt_currency.setText(cartActivityObjects.get(position).getCurrency());
                holder.txt_addon.setText(cartActivityObjects.get(position).getAddon());
                holder.lt_costlimit.setText(cartActivityObjects.get(position).getPrice());
                holder.txt_totalQty.setText(cartActivityObjects.get(position).getQuantity());
                holder.img_addon.setVisibility(View.VISIBLE);
                holder.txt_addon.setVisibility(View.VISIBLE);
            }
            holder.img_addon.setOnClickListener(v -> {
                holder.img_addon.setVisibility(View.VISIBLE);
                holder.txt_addon.setVisibility(View.VISIBLE);
                Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(true)
                        .setCornerRadius(20f)
                        .setGravity(Gravity.TOP)
                        .setText("Addon : " + cartActivityObjects.get(position).getAddon() + "\n" +
                                "Ingredient : " + cartActivityObjects.get(position).getIngredient() + "\n" +
                                "Preparation : " + cartActivityObjects.get(position).getPreparation());
                builder.show();
            });

            holder.img_plus.setOnClickListener(view -> {

                holder.pb_cart.setVisibility(View.VISIBLE);
                int count = Integer.parseInt(cartActivityObjects.get(position).getQuantity());

                count = count + 1;
                FC_Common.productID=cartActivityObjects.get(position).getProduct_id();
                FC_Common.partnerId=cartActivityObjects.get(position).getPartner_id();
                FC_Common.quantity=String.valueOf(count);
                Utils.log(context,"sdfsdfsdfsdfs"+"count : "+count);
                Utils.log(context,"sdfsdfsdfsdfs"+"quantity : "+FC_Common.quantity);
                UpdateMenu();

            });
            holder.img_minus.setOnClickListener(view -> {

                holder.pb_cart.setVisibility(View.VISIBLE);
                int count = Integer.parseInt(cartActivityObjects.get(position).getQuantity());

                count = count - 1;
                FC_Common.productID=cartActivityObjects.get(position).getProduct_id();
                FC_Common.partnerId=cartActivityObjects.get(position).getPartner_id();
                FC_Common.quantity=String.valueOf(count);
                UpdateMenu();
                Utils.log(context,"sdfsdfsdfsdfs"+"count : "+count);
                Utils.log(context,"sdfsdfsdfsdfs"+"quantity : "+FC_Common.quantity);


            });
            holder.img_close.setOnClickListener(v -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("Delete item");

                // Setting Dialog Message
                alertDialog.setMessage("Are sure You Want Remove From this item in cart");

                // On pressing the Settings button.
                alertDialog.setPositiveButton("Delete", (dialog, which) -> {
                    FC_Common.CartProduct_id=cartActivityObjects.get(position).getId();
                    CartFragmentObject vidItem = cartActivityObjects.get(position);
                    cartActivityObjects.remove(vidItem);
                    DeleteMenu();
                });

                // On pressing the cancel button
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                // Showing Alert Message
                alertDialog.show();
            });
        }



        @Override
        public int getItemCount() {
            return cartActivityObjects.size();
        }

        void visibleContentLayout() {

            visible = true;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ProgressBar pb_cart ;
            ImageView img_plus,img_VegNonVegType,img_addon,img_minus,img_close;
            AC_Textview txt_dishName,txt_addon,txt_totalQty;
            CircleImageView lc_circleItem;
            LinearLayout ll_loader,ll_content;
            LoaderTextView lt_costlimit,lt_currency;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_close = itemView.findViewById(R.id.img_close);
                lt_currency = itemView.findViewById(R.id.lt_currency);
                img_addon = itemView.findViewById(R.id.img_addon);
                txt_totalQty = itemView.findViewById(R.id.txt_totalQty);
                lt_costlimit = itemView.findViewById(R.id.lt_costlimit);
                txt_addon = itemView.findViewById(R.id.txt_addon);
                img_VegNonVegType = itemView.findViewById(R.id.img_VegNonVegType);
                txt_dishName = itemView.findViewById(R.id.txt_dishName);
                lc_circleItem = itemView.findViewById(R.id.lc_circleItem);
                ll_content = itemView.findViewById(R.id.ll_content);
                ll_loader = itemView.findViewById(R.id.ll_loader);
                pb_cart = itemView.findViewById(R.id.pb_cart);
                img_plus = itemView.findViewById(R.id.img_plus);
                img_minus = itemView.findViewById(R.id.img_minus);

            }
        }
    }
    //CartList Async Task End//


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

                            AllCartList();
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

    private void walletupdate() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_WALLETUPDATE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            AllCartList();
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
                params.put("checked", FC_Common.walletchcked);
                params.put("walletamount", FC_Common.walletamount);
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

    private void UpdateMenu() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTADDONUPDATECART,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {

                            AllCartList();
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
                params.put("product_id", FC_Common.productID);
                params.put("partner_id", FC_Common.partnerId);
                params.put("quantity", FC_Common.quantity);
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



    private void Submitorder() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PAYMENT,
                response -> {
                    Log.d("sdfsdgsdg", ">>" + response);
                    Log.d("sdfsdgsdg", ">>" + FC_URL.URL_PAYMENT);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            FC_Common.order_id = obj.getString("order_id");
                            Utils.stopProgressBar();
                            paymentdialog.dismiss();
                            FC_Common.note="";
                            FC_Common.paymentid="";

                            Intent intent = new Intent(context, FC_OrderPickedUpActivity.class);
                            intent.putExtra("order_id",FC_Common.order_id);
                            startActivity(intent);
                        }
                        else
                        {

                            Utils.stopProgressBar();
                            paymentdialog.dismiss();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Utils.stopProgressBar();
                        snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                    Utils.stopProgressBar();

                    snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("delivery_date", FC_Common.preordertime);
                params.put("payment_mode", FC_Common.paymenttype);
                params.put("note", FC_Common.note);
                params.put("wallet_check", FC_Common.walletchcked);
                params.put("paymentmethodid", FC_Common.paymentid);
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

    private void DeleteMenu() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PRODUCTCARTDELETE,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {
                            AllCartList();
                        }
                        else {
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
                params.put("cart_id", FC_Common.CartProduct_id);
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


    private void snackBar(final String value) {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(50);
                    bar = Snackbar.make(ll_main, value, Snackbar.LENGTH_LONG)
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

    private void Favourite(String favouritecode,String url) {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1")) {

                            if (favouritecode.equalsIgnoreCase("1"))
                            {
                                Utils.stopProgressBar();
                                AllCartList();
                                snackBar(FC_Common.message);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites_on));
                            }
                            else {
                                Utils.stopProgressBar();
                                AllCartList();
                                snackBar(FC_Common.message);
                                vector_android_button.setLikeDrawable(getResources().getDrawable(R.drawable.favorites));
                                vector_android_button.setUnlikeDrawable(getResources().getDrawable(R.drawable.favorites));
                            }

                        } else {
                            Utils.stopProgressBar();
                            snackBar(FC_Common.message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        snackBar(getResources().getString(R.string.reach));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    Utils.stopProgressBar();
                    //displaying the error in toast if occurrs
                    snackBar(getResources().getString(R.string.reach));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant_id", FC_Common.Cartrestaurant_id);
                Log.d("getParams: ", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("getParams: ", "" + params);
                params.put("Authorization", FC_Common.token_type + " " + FC_Common.access_token);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        requestQueue.add(stringRequest);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void FindViewByIdBottomDialog(View view) {


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

    private void FindViewByIdBottomDialog1(View view) {
        txt_cashon = view.findViewById(R.id.txt_cashon);
        AC_Textview txt_payumoney = view.findViewById(R.id.txt_payumoney);
        txt_paytm = view.findViewById(R.id.txt_paytm);
        txt_payumoney.setOnClickListener(v -> {
            paymentdialog.dismiss();
            snackBar(getResources().getString(R.string.payumoney));
        });
       /* txt_paytm.setOnClickListener(v -> {
            paymentdialog.dismiss();
            snackBar(getResources().getString(R.string.paytm));
        });*/

    }
    private void locationlist() {

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
                    //displaying the error in toast if occurrs
                    String error_value = String.valueOf(error);
                    snackBar(error_value);
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
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
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
            public Map<String, String> getHeaders() {
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

                            //onB
                            dialog.dismiss();


                            AllCartList();
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
                    String value = volleyError.toString();
                    snackBar(value);
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

    private void setTime() {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        showTime(hour, min);
    }

    private void showTime(int hour, int min) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        Log.d("sdfgsdfsd","hourdfgfdg"+hour);
        Log.d("sdfgsdfsd","mindfgfdg"+min);
        frame_payment.setVisibility(View.VISIBLE);
        view_payment.setVisibility(View.VISIBLE);
        lt_preorder.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
        FC_Common.preordertime=lt_preorder.getText().toString();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccessCheck() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.ROOTSTRIPE,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.stripe_publickey = jsonResponse1.getString("stripe_publickey");

                        final Context applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
                        PaymentConfiguration.init(applicationContext, FC_Common.stripe_publickey);
                        stripe = new Stripe(applicationContext, FC_Common.stripe_publickey);
                        if (FC_Common.status.equalsIgnoreCase("1"))
                        {
                            Utils.stopProgressBar();
                            @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.activity_checkout, null);
                            FindViewByIdBottomDialogpayment(view1);
                            carddialog = new BottomSheetDialog(context);
                            carddialog.setContentView(view1);
                            carddialog.show();

                        }
                        else {
                            Utils.stopProgressBar();
                            Utils.toast(getActivity(),getResources().getString(R.string.reach));
                        }

                    } catch (JSONException e) {
                        Utils.stopProgressBar();
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Utils.stopProgressBar();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @SuppressLint("SetTextI18n")
    private void FindViewByIdBottomDialogpayment(View view) {

        payButton = view.findViewById(R.id.payButton);
        txt_header = view.findViewById(R.id.txt_header);
        edt_amt = view.findViewById(R.id.edt_amt);
        edt_amt.setEnabled(false);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);

        edt_amt.setText(FC_Common.Carttotal);
        txt_header.setText("Total Amount To Pay");
        payButton.setOnClickListener(v -> {
            pay();
        });
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
                FC_Common.paymentid=result.id;
                FC_Common.paymenttype="CARD";
                Submitorder();
               // Payment(paymentid);
                Log.d("gfhdfgdfg","dfgdfg"+result.id);
                Log.d("gfhdfgdfg","dfgdfg"+FC_Common.paymentid);
            }

            @Override
            public void onError(@NonNull Exception e) {
                e.printStackTrace();
                Log.d("dfghfdgdfg","dgsdf"+e);
            }
        });
    }

   /* private void Payment(String paymentid) {
Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_WALLETPAYMENT,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard12" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FC_Common.status = jsonResponse1.getString("success");
                        if (FC_Common.status.equalsIgnoreCase("1")) {
                            Utils.stopProgressBar();
                            carddialog.dismiss();
                            FC_Common.paymenttype="CARD";
                            Submitorder();
                        }
                        else {
                            Utils.stopProgressBar();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.stopProgressBar();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Utils.stopProgressBar();
            Log.d("dfgdffgd","dfgdf"+value);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("paymentmethodid", paymentid);
                params.put("amount", FC_Common.Carttotal);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }*/
}
