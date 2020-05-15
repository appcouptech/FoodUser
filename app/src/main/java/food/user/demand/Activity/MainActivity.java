package food.user.demand.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import food.user.demand.R;

public class MainActivity extends AppCompatActivity {

    Bitmap settingsBitmap;
    ImageView settingsButton;
    String URL_LOGIN="http://192.168.21.129/api/user/test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsButton= findViewById(R.id.settingsButton);
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
       // Utils.getDisplay1(MainActivity.this,screenSize);
        Log.d("dfdgdfdfg","dfgdf"+FoodonDemandCommon.screensize);





      /* // Toast.makeText(getApplicationContext(),"dsgsdfgg",Toast.LENGTH_LONG).show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        //Toast.makeText(getApplicationContext(),"dsgsdfgg"+metrics,Toast.LENGTH_LONG).show();
        Log.d("gjfgjfgjfg","fghfghfg"+metrics);

        int densityDpi = (int)(metrics.density * 160f);

        Toast.makeText(getApplicationContext(),"dsgsdfgg"+densityDpi,Toast.LENGTH_LONG).show();
        Log.d("gjfgjfgjfg","122fghfghfg"+densityDpi);*/



      /*  DisplayMetrics metrics1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics1);

        switch(metrics1.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(getApplicationContext(),"dsgsdfgg"+metrics1,Toast.LENGTH_LONG).show();
                Log.d("gjfgjfgjfg","122fghfghfg"+metrics1);
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(getApplicationContext(),"dsgsdfgg"+metrics1,Toast.LENGTH_LONG).show();
                Log.d("gjfgjfgjfg","122fghfghfg"+metrics1);
                break;

            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(getApplicationContext(),"dsgsdfgg"+metrics1,Toast.LENGTH_LONG).show();
                Log.d("gjfgjfgjfg","122fghfghfg"+metrics1);
                break;
        }
*/



//        Log.d("dfgdfgfd","dgf0"+Utils.getDisplay(MainActivity.this));
//        Log.d("dfgdfgfd","1dgf0"+Utils.getScreenHeight(MainActivity.this));
//        Log.d("dfgdfgfd","2dgf0"+Utils.getScreenWidth(MainActivity.this));

        Log.d("dfgdfgfd","2dgf0"+FoodonDemandCommon.screensize);
        Toast.makeText(getApplicationContext(),"dsgsdfgg"+FoodonDemandCommon.screensize,Toast.LENGTH_LONG).show();


       // Log.d("dfgdfgfd","3dgf0"+Utils.dpToPx(1));
        setSettingsButtonSize();

        Log.d("ServerResponse", "dgsfdbfdb" );

//        Intent ine =new Intent(MainActivity.this,GoogleMaps.class);
//        startActivity(ine);
        loginExecute();

    }


    private void loginExecute() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.d("ServerResponse", "" + ServerResponse);
                        Log.d("ServerResponse", "" + URL_LOGIN);
                        try {

                            JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                            FoodonDemandCommon.status = jsonResponse1.getString("success");
                          // Intent ine =new Intent(MainActivity.this,Main2Activity.class);
                           //Intent ine =new Intent(MainActivity.this, Distance_new.class);
                           Intent ine =new Intent(MainActivity.this,Distance_Maos_new.class);
                          // Intent ine =new Intent(MainActivity.this, DistanceMaps.class);
                           // Intent ine =new Intent(MainActivity.this,TestActivity.class);
                            //Intent ine =new Intent(MainActivity.this,GoogleMaps.class);
                           // Intent ine =new Intent(MainActivity.this,MapsActivity.class);
                            startActivity(ine);

                            Log.d("dfhfgdhdfhd","fdgdfgd"+FoodonDemandCommon.status);
                            Toast.makeText(MainActivity.this, "fsdf"+FoodonDemandCommon.status, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ServerResponse", "dfhdf" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                String ServerResponse = volleyError.toString();
                Log.d("ServerResponse", "dfhdf" + ServerResponse);

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void setSettingsButtonSize(){
        settingsBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.order_summary);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        settingsBitmap= Bitmap.createScaledBitmap(settingsBitmap,metrics.heightPixels / 5,metrics.heightPixels / 5,true);
        settingsButton.setImageBitmap(settingsBitmap);
    }
}
