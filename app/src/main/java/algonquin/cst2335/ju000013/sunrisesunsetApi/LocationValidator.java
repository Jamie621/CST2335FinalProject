package algonquin.cst2335.ju000013.sunrisesunsetApi;


import android.text.TextUtils;

import java.util.regex.Pattern;

public class LocationValidator {

    // Validation format
    private static final Pattern LATITUDE_PATTERN = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d{1,6})?|90(\\.0{1,6})?) [NSns]?$");
    private static final Pattern LONGITUDE_PATTERN = Pattern.compile("^[-+]?([1-9]?\\d(\\.\\d{1,6})?|1[0-7]?\\d(\\.\\d{1,6})?|180(\\.0{1,6})?) [EWew]?$");

    // 验证纬度
    public static boolean isValidLatitude(String latitude) {
        return !TextUtils.isEmpty(latitude) && LATITUDE_PATTERN.matcher(latitude).matches();
    }

    // 验证经度
    public static boolean isValidLongitude(String longitude) {
        return !TextUtils.isEmpty(longitude) && LONGITUDE_PATTERN.matcher(longitude).matches();
    }
}
