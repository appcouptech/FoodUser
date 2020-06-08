package food.user.demand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_URL;

public class MainActivity extends AppCompatActivity {
    private Stripe stripe;
    private static final String BACKEND_URL = "https://foodcoup.appcoup.com/api/driver/stripekey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Log.d("dgsdgsd","sdfsdf");



        AccessCheck();
        //Log.d("dfgsdgsdg","dgsdfds"+stripe);
        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> pay());
    }

    private void pay() {
        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                //pay(result.id, null);
                String msdgfsdf=result.id;

                Log.d("gfhdfgdfg","dfgdfg"+result.id);
                Log.d("gfhdfgdfg","dfgdfg"+msdgfsdf);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FC_URL.URL_TEST,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
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
                params.put("Authorization", FC_Common.token_type_dup + " " + FC_Common.access_token_dup);
               // params.put("X-Requested-With", FC_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(MainActivity.this));
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}
