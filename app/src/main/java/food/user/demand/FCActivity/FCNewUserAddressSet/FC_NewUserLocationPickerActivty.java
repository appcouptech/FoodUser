package food.user.demand.FCActivity.FCNewUserAddressSet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import food.user.demand.FCPojo.FCLocationObject.LocationObject;
import food.user.demand.FCViews.AC_Textview;
import food.user.demand.FCViews.FC_Common;
import food.user.demand.R;

public class FC_NewUserLocationPickerActivty extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = FC_NewUserLocationPickerActivty.class.getSimpleName();
    AC_Textview txtcurrentlocation;
    RecyclerView rv_savedaddress;
    Context context;
    View parentLayout;
    ArrayList<LocationObject> locationObjects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fc_location_picker_activity);
        String apiKey = "AIzaSyBHMt8kGGgqUc11TluyJOgMrqFJfdaPbYQ";

        context= FC_NewUserLocationPickerActivty.this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
       // LocationObject = new ArrayList<>();
        if (bundle != null)
        {
            FC_Common.username = (String) bundle.get("username");
            FC_Common.usermobile = (String) bundle.get("usermobile");
            FC_Common.useremail = (String) bundle.get("useremail");
            FC_Common.usergender = (String) bundle.get("usergender");
            FC_Common.device_id = (String) bundle.get("deviceid");
            FC_Common.token_type = (String) bundle.get("token_type");
            FC_Common.access_token = (String) bundle.get("access_token");
            FC_Common.latitude = (String) bundle.get("latitude");
            FC_Common.longitude = (String) bundle.get("longitude");
            FC_Common.gpsenabled = (String) bundle.get("gpsenabled");
            FC_Common.change_address = (String) bundle.get("change_address");

            Log.d("dfgfdgdfg","dfgfdgfd"+FC_Common.latitude);
            Log.d("dfgfdgdfg","dfgfdgfd"+FC_Common.longitude);
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                LatLng latLng = place.getLatLng();
                assert latLng != null;
                FC_Common.latitude = String.valueOf(latLng.latitude);
                FC_Common.longitude = String.valueOf(latLng.longitude);
                FC_Common.change_address="location";

                Intent intent = new Intent(FC_NewUserLocationPickerActivty.this, FC_NewUserAddressActivity.class);
                intent.putExtra("username",FC_Common.username);
                intent.putExtra("usermobile",FC_Common.usermobile);
                intent.putExtra("useremail",FC_Common.useremail);
                intent.putExtra("usergender",FC_Common.usergender);
                intent.putExtra("deviceid",FC_Common.device_id);
                intent.putExtra("token_type",FC_Common.token_type);
                intent.putExtra("access_token",FC_Common.access_token);
                intent.putExtra("latitude",FC_Common.latitude);
                intent.putExtra("longitude",FC_Common.longitude);
                intent.putExtra("change_address",FC_Common.change_address);
                startActivity(intent);
            }
            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        FindviewById();
       /* LocationRecycler();
        locationlist();*/
    }

    private void FindviewById() {
        txtcurrentlocation = findViewById(R.id.txtcurrentlocation);
        parentLayout = findViewById(android.R.id.content);
        rv_savedaddress = findViewById(R.id.rv_savedaddress);

        txtcurrentlocation.setOnClickListener(this);

        LocationObject object=new LocationObject();
        object.setD_images("");
        object.setD_info("");
        locationObjects.add(object);
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.txtcurrentlocation) {
            Intent intent = new Intent(FC_NewUserLocationPickerActivty.this, FC_NewUserAddressActivity.class);
            intent.putExtra("username", FC_Common.username);
            intent.putExtra("usermobile", FC_Common.usermobile);
            intent.putExtra("useremail", FC_Common.useremail);
            intent.putExtra("usergender", FC_Common.usergender);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("latitude", FC_Common.latitude);
            intent.putExtra("longitude", FC_Common.longitude);
            intent.putExtra("gpsenabled", FC_Common.gpsenabled);
            intent.putExtra("change_address", "NewUser");
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {

        if (FC_Common.change_address.equalsIgnoreCase("NewUser")) {
           /* Intent intent = new Intent(FC_NewUserAddressActivity.this, FC_IntroProfileActivity.class);
            intent.putExtra("username", FC_Common.username);
            intent.putExtra("usermobile", FC_Common.usermobile);
            intent.putExtra("useremail", FC_Common.useremail);
            intent.putExtra("usergender", FC_Common.usergender);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("latitude", FC_Common.latitude);
            intent.putExtra("longitude", FC_Common.longitude);
            intent.putExtra("change_address", FC_Common.change_address);
            startActivity(intent);*/
            Log.d("dfgdfgdf","fdgdfgdf");
        }

        else if (FC_Common.change_address.equalsIgnoreCase("splash")){

            Log.d("dfgdfgdf","fdgdfgdf");
        }
        else if (FC_Common.change_address.equalsIgnoreCase("ManageAddress")){
            Intent intent = new Intent(FC_NewUserLocationPickerActivty.this, FC_NewUserAddressActivity.class);
            intent.putExtra("username", FC_Common.username);
            intent.putExtra("usermobile", FC_Common.usermobile);
            intent.putExtra("useremail", FC_Common.useremail);
            intent.putExtra("usergender", FC_Common.usergender);
            intent.putExtra("deviceid", FC_Common.device_id);
            intent.putExtra("token_type", FC_Common.token_type);
            intent.putExtra("access_token", FC_Common.access_token);
            intent.putExtra("latitude", FC_Common.latitude);
            intent.putExtra("longitude", FC_Common.longitude);
            intent.putExtra("change_address", FC_Common.change_address);
            startActivity(intent);
            finish();
        }


    }


}
