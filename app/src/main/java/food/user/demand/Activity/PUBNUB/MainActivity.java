package food.user.demand.Activity.PUBNUB;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import food.user.demand.Activity.PUBNUB.util.Constants;
import food.user.demand.R;


public class MainActivity extends AppCompatActivity {

    public static PubNub pubnub; // Pubnub instance

    Button driverButton, passengerButton; // Buttons that redirect user to proper view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pubnub);

        driverButton =  findViewById(R.id.driverButton);
        passengerButton = (Button) findViewById(R.id.passengerButton);

        initPubnub();

        // Send user to Driver Activity or Passenger Activity using intents
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DriverActivity.class));
            }
        });
       /* passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PassengerActivity.class));
            }
        });*/


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            checkPermission();
        }

    }

    /*
        Creates PNConfiguration instance and enters Pubnub credentials to create Pubnub instance.
        This Pubnub instance will be used whenever we need to create connection to Pubnub.
     */
    private void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
    }

    /*
        Checks user's location permission to see whether user has granted access to fine location and coarse location.
        If not it will request these permissions.
     */
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

}