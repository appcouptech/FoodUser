package food.user.demand.FCFragment.FCDashboardFragment.FCAccountActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import food.user.demand.FCLogin.FC_Login;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.FCViews.FC_SharedPrefManager;
import food.user.demand.FCViews.FC_URL;
import food.user.demand.FCViews.FC_User;
import food.user.demand.R;

public class FC_ReferralsActivity extends AppCompatActivity {

    private AC_Textview txt_referrals, txt_inviteFrnds;
    private ImageView img_backBtn;
    View parentLayout;
    Snackbar bar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc__referrals);

        context= FC_ReferralsActivity.this;
        FC_User user = FC_SharedPrefManager.getInstance(FC_ReferralsActivity.this).getUser();
        FC_Common.token_type = String.valueOf(user.gettoken_type());
        FC_Common.access_token = String.valueOf(user.getaccess_token());
        FindViewById();


        img_backBtn.setOnClickListener(view -> onBackPressed());

        txt_inviteFrnds.setOnClickListener(v -> {

            GetUserDetails();

        });
    }

    private void FindViewById() {
        txt_referrals = findViewById(R.id.txt_referrals);
        txt_inviteFrnds = findViewById(R.id.txt_inviteFrnds);
        parentLayout = findViewById(android.R.id.content);
        img_backBtn = findViewById(R.id.img_backBtn);



    }

    private void GetUserDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FC_URL.URL_DETAILUSER,
                ServerResponse -> {

                    Log.d("fgjfghjfghfg", "" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        //FC_Common.status = jsonResponse1.getString("success");
                        FC_Common.referealcode = jsonResponse1.getString("referral_code");
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String text = getResources().getString(R.string.refcodeuser)+" - "+FC_Common.referealcode;
                        // change with required  application package

                        intent.setPackage("com.whatsapp");
                        if (intent != null) {
                            intent.putExtra(Intent.EXTRA_TEXT, text);//
                            startActivity(Intent.createChooser(intent, text));
                        } else {

                            Toast.makeText(FC_ReferralsActivity.this, "App not found", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("fdhfdgfd","dfghfdgfdgf"+FC_Common.referealcode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("fdhbdfghdf", "dfhdf" + e);
                        snackBar(String.valueOf(e));
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            snackBar(value);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fdgdfgfdg","sdfgsdgs"+FC_Common.token_type+" "+FC_Common.access_token);
                params.put("Authorization", FC_Common.token_type+" "+FC_Common.access_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FC_ReferralsActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
