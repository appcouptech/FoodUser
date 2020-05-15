package food.user.demand.FCViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class FC_SharedPrefManager {
    private static final String SHARED_PREF_NAME = "Ala_shared";
    public static final String id = "id";
    public static final String name = "name";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String email_verified_at = "email_verified_at";
    public static final String dial_code = "dial_code";
    public static final String location_name = "location_name";
    public static final String location_type = "location_type";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String status = "status";
    public static final String is_guest = "is_guest";
    public static final String picture = "picture";
    public static final String device_token = "device_token";
    public static final String device_id = "device_id";
    public static final String device_type = "device_type";
    public static final String login_by = "login_by";
    public static final String social_unique_id = "social_unique_id";
    public static final String cust_id = "cust_id";
    public static final String wallet_balance = "wallet_balance";
    public static final String rating = "rating";
    public static final String userotp = "userotp";
    public static final String created_at = "created_at";
    public static final String updated_at = "updated_at";
    public static final String token_type = "token_type";
    public static final String access_token = "access_token";
    public static final String gender = "gender";
    public static final String filter_price_min = "filter_price_min";
    public static final String filter_price_max = "filter_price_max";
    public static final String is_default = "is_default";

    @SuppressLint("StaticFieldLeak")
    private static FC_SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private FC_SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized FC_SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FC_SharedPrefManager(context);
        }
        return mInstance;
    }
    public void userLogin(FC_User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString(Utils.id, user.getid());
        editor1.putString(Utils.name, user.getname());
        editor1.putString(Utils.email, user.getemail());
        editor1.putString(Utils.mobile, user.getmobile());
        editor1.putString(Utils.email_verified_at, user.getemail_verified_at());
        editor1.putString(Utils.dial_code, user.getdial_code());
        editor1.putString(Utils.location_name, user.getlocation_name());
        editor1.putString(Utils.location_type, user.getlocation_type());
        editor1.putString(Utils.latitude, user.getlatitude());
        editor1.putString(Utils.longitude, user.getlongitude());
        editor1.putString(Utils.status, user.getstatus());
        editor1.putString(Utils.is_guest, user.getis_guest());
        editor1.putString(Utils.picture, user.getpicture());
        editor1.putString(Utils.device_token, user.getdevice_token());
        editor1.putString(Utils.device_id, user.getdevice_id());
        editor1.putString(Utils.device_type, user.getdevice_type());
        editor1.putString(Utils.login_by, user.getlogin_by());
        editor1.putString(Utils.social_unique_id, user.getsocial_unique_id());
        editor1.putString(Utils.cust_id, user.getcust_id());
        editor1.putString(Utils.wallet_balance, user.getwallet_balance());
        editor1.putString(Utils.rating, user.getrating());
        editor1.putString(Utils.userotp, user.getotp());
        editor1.putString(Utils.created_at, user.getcreated_at());
        editor1.putString(Utils.updated_at, user.getupdated_at());
        editor1.putString(Utils.token_type, user.gettoken_type());
        editor1.putString(Utils.access_token, user.getaccess_token());
        editor1.putString(Utils.gender, user.getgender());
        editor1.putString(Utils.filter_price_min, user.getfilter_price_min());
        editor1.putString(Utils.filter_price_max, user.getfilter_price_max());
        editor1.putString(Utils.is_default, user.getis_default());
        editor1.apply();
    }



    public FC_User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new FC_User(
                sharedPreferences.getString(id, null),
                sharedPreferences.getString(name, null),
                sharedPreferences.getString(email, null),
                sharedPreferences.getString(mobile, null),
        sharedPreferences.getString(email_verified_at,null),
        sharedPreferences.getString(dial_code, null),
        sharedPreferences.getString(location_name, null),
        sharedPreferences.getString(location_type, null),
        sharedPreferences.getString(latitude, null),
        sharedPreferences.getString(longitude, null),
        sharedPreferences.getString(status, null),
        sharedPreferences.getString(is_guest, null),
        sharedPreferences.getString(picture, null),
        sharedPreferences.getString(device_token, null),
        sharedPreferences.getString(device_id, null),
        sharedPreferences.getString(device_type, null),
                sharedPreferences.getString(login_by, null),
                sharedPreferences.getString(social_unique_id, null),
                sharedPreferences.getString(cust_id, null),
                sharedPreferences.getString(wallet_balance, null),
                sharedPreferences.getString(rating, null),
                sharedPreferences.getString(userotp, null),
                sharedPreferences.getString(created_at, null),
                sharedPreferences.getString(updated_at, null),
                sharedPreferences.getString(token_type, null),
                sharedPreferences.getString(access_token, null),
                sharedPreferences.getString(gender, null),
                sharedPreferences.getString(filter_price_min, null),
                sharedPreferences.getString(filter_price_max, null),
                sharedPreferences.getString(is_default, null)
        );
    }
    public static void deleteAllSharePrefs(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}