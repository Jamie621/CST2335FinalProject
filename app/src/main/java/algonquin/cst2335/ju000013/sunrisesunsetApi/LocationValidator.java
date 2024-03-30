package algonquin.cst2335.ju000013.sunrisesunsetApi;


import android.text.TextUtils;

import java.util.regex.Pattern;

public class LocationValidator {

    // Validation format
    private static final Pattern LATITUDE_PATTERN =
            Pattern.compile("^\\d{5}$");

    // Validation format for longitude from -180 to +180 with E or W suffix
    private static final Pattern LONGITUDE_PATTERN =
            Pattern.compile("^\\d{6}$");
    // Validate latitude
    public static boolean isValidLatitude(String latitude) {
        return latitude != null && LATITUDE_PATTERN.matcher(latitude).matches();
    }

    // Validate longitude
    public static boolean isValidLongitude(String longitude) {
        return longitude != null && LONGITUDE_PATTERN.matcher(longitude).matches();
    }
}
