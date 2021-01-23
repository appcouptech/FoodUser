package food.user.demand.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.PaymentConfiguration;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class MainActivity extends AppCompatActivity {

    Bitmap settingsBitmap;
    ImageView settingsButton;
    String URL_LOGIN="http://192.168.21.129/api/user/test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(MainActivity.this,getResources().getConfiguration());
        setContentView(R.layout.activity_main);
        settingsButton = findViewById(R.id.settingsButton);
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_TYooMQauvdEDq54NiTphI7jx"
        );
    }


}
