package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "favourites")
public class SaveLocation {
    @PrimaryKey(autoGenerate = true)
    private int id; // The primary key

    private String latitude;
    private String longitude;
    private String sunrise;
    private String sunset;
    // 其他信息字段
    private String date;
    private String location;

    public SaveLocation(String sunrise, String sunset, String date, String location, String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.location = location;
    }

    // Getter 和 Setter 方法
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
