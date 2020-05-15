package food.user.demand.Testing_New;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import food.user.demand.R;

public class TestingOthers extends AppCompatActivity {

    RadioButton male,female;

    TextView gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_others);
        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);

        gender=(TextView)findViewById(R.id.gender);

        male.setOnCheckedChangeListener(new Radio_check());
        female.setOnCheckedChangeListener(new Radio_check());
    }
    class Radio_check implements  CompoundButton.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(male.isChecked())
            {
                gender.setText("Gender: Male");
            }
            else if(female.isChecked())
            {
                gender.setText("Gender: Female");
            }
        }
    }
}
