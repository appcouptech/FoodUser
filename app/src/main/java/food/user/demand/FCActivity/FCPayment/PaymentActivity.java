package food.user.demand.FCActivity.FCPayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCFragment.FCDashboardFragment.FCCartFragmentOrderPickActivity.FC_OrderPickedUpActivity;
import food.user.demand.FCUtils.Loader.LoaderTextView;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.R;

public class PaymentActivity extends Activity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();

    LoaderTextView lt_orderNo,lt_hotelName,lt_currency;
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());
        FC_User user = FC_SharedPrefManager.getInstance(PaymentActivity.this).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        // Payment button created by you in XML layout
        Button button =  findViewById(R.id.btn_pay);
         lt_orderNo =  findViewById(R.id.lt_orderNo);
        lt_hotelName =  findViewById(R.id.lt_hotelName);
        lt_currency =  findViewById(R.id.lt_currency);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            FC_Common.Cartrestaurant_name = (String) bundle.get("Cartrestaurant_name");
            FC_Common.Cartcurrency = (String) bundle.get("Cartcurrency");
            FC_Common.Carttotal = (String) bundle.get("Carttotal");
            FC_Common.Carttotal = (String) bundle.get("Carttotal");
            FC_Common.mobile = (String) bundle.get("mobile");
            FC_Common.email = (String) bundle.get("email");
            FC_Common.note = (String) bundle.get("NOTE");

        }

        lt_hotelName.setText(FC_Common.Cartrestaurant_name);
        lt_currency.setText(FC_Common.Cartcurrency+" "+FC_Common.Carttotal);
        button.setOnClickListener(v -> startPayment());
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "FoodOnDemand");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", FC_Common.email);
            preFill.put("contact", FC_Common.mobile);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful:sdfsdf " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            FC_Common.paymenttype="CARD";
            Submitorder();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            onBackPressed();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Submitorder() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_PAYMENT,
                response -> {
                    Log.d("dffdgdfg", ">>" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        FC_Common.success = obj.getString("success");
                        FC_Common.message = obj.getString("message");
                        Log.d("ghfghfghf", "fhfgdhfd" + obj);
                        if (FC_Common.success.equalsIgnoreCase("1"))
                        {
                            FC_Common.order_id = obj.getString("order_id");
                            //paymentdialog.dismiss();
                            FC_Common.note="";
                            Intent intent = new Intent(PaymentActivity.this, FC_OrderPickedUpActivity.class);
                            intent.putExtra("order_id",FC_Common.order_id);
                            startActivity(intent);
                        }
                        else
                        {
                            Log.d("fghdfhfdg","dfgfdgdfg"+ FC_Common.message);
                            /*paymentdialog.dismiss();
                            snackBar(FC_Common.message);*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //snackBar(String.valueOf(e));
                        Log.d("dfghdghfgfdb", "fdhfdh" + e);
                        // Intent setOfHotels = new Intent(getActivity(), FC_SetOfHotelsOfferActivity.class);

                    }
                },
                error -> {
                    //displaying the error in toast if occurrs
                   // snackBar(String.valueOf(error));
                    Log.d("dfhfdghfgh", "hfdhdf" + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("delivery_date", FC_Common.preordertime);
                params.put("payment_mode", FC_Common.paymenttype);
                params.put("note", FC_Common.note);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(PaymentActivity.this).getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
