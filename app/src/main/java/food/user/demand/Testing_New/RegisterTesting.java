package food.user.demand.Testing_New;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import food.user.demand.Activity.Distance.Distance_new;
import food.user.demand.FCViews.Utils;
import food.user.demand.R;

public class RegisterTesting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontScale(RegisterTesting.this,getResources().getConfiguration());
        setContentView(R.layout.register_testing);
    }
}
