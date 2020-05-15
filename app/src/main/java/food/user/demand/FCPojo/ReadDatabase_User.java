package food.user.demand.FCPojo;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReadDatabase_User {
    public String device_id;
    public String driver_id;
    public String latitude;
    public String longitude;

    public ReadDatabase_User() {
    }

    public ReadDatabase_User(String device_id, String driver_id, String latitude, String longitude) {
        this.device_id = device_id;
        this.driver_id = driver_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}


/*public class ReadDatabase_User {
    public String device_id;
    public String driver_id;
    public String latitude;
    public String longitude;

    public String getdevice_id() {
        return device_id;
    }

    public void setdevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getdriver_id() {
        return driver_id;
    }

    public void setdriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }


}*/
