package food.user.demand.FCViews;
public class FC_User {

    private String id,name,email,mobile,email_verified_at,dial_code,location_name,location_type,latitude,longitude,status,
            is_guest,picture,device_token,device_id,device_type,login_by,social_unique_id,cust_id,
            wallet_balance,rating,otp,created_at,updated_at,token_type,access_token,gender,filter_price_min,filter_price_max,is_default;


    public FC_User(String id,String name, String email,String mobile,String email_verified_at,
                   String dial_code,String location_name,String location_type,String latitude,String longitude,String status,
                   String is_guest,String picture,String device_token,String device_id,String device_type,String login_by,
                   String social_unique_id,String cust_id,String wallet_balance,String rating,
                   String otp,String created_at,String updated_at,String token_type,String access_token,
                   String gender,String filter_price_min,String filter_price_max,String is_default) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.email_verified_at = email_verified_at;
        this.dial_code = dial_code;
        this.location_name = location_name;
        this.location_type = location_type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.is_guest = is_guest;
        this.picture = picture;
        this.device_token = device_token;
        this.device_id = device_id;
        this.device_type = device_type;
        this.login_by = login_by;
        this.social_unique_id = social_unique_id;
        this.cust_id = cust_id;
        this.wallet_balance = wallet_balance;
        this.rating = rating;
        this.otp = otp;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.token_type = token_type;
        this.access_token = access_token;
        this.gender = gender;
        this.filter_price_min = filter_price_min;
        this.filter_price_max = filter_price_max;
        this.is_default = is_default;

    }


    public String getid() { return id; }
    public String getname() { return name; }
    public String getemail() { return email; }
    public String getmobile() { return mobile; }
    public String getemail_verified_at() { return email_verified_at; }
    public String getdial_code() { return dial_code; }
    public String getlocation_name() { return location_name; }
    public String getlocation_type() { return location_type; }
    public String getlatitude() { return latitude; }
    public String getlongitude() { return longitude; }
    public String getstatus() { return status; }
    public String getis_guest() { return is_guest; }
    public String getpicture() { return picture; }
    public String getdevice_token() { return device_token; }
    public String getdevice_id() { return device_id; }
    public String getdevice_type() { return device_type; }
    public String getlogin_by() { return login_by; }
    public String getsocial_unique_id() { return social_unique_id; }
    public String getcust_id() { return cust_id; }
    public String getwallet_balance() { return wallet_balance; }
    public String getrating() { return rating; }
    public String getotp() { return otp; }
    public String getcreated_at() { return created_at; }
    public String getupdated_at() { return updated_at; }
    public String gettoken_type() { return token_type; }
    public String getaccess_token() { return access_token; }
    public String getgender() { return gender; }
    public String getfilter_price_min() { return filter_price_min; }
    public String getfilter_price_max() { return filter_price_max; }
    public String getis_default() { return is_default; }
}
