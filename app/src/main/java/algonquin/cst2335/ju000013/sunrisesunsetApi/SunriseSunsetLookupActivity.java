package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import algonquin.cst2335.ju000013.R;

public class SunriseSunsetLookupActivity extends AppCompatActivity {

    private FavouriteViewModel favouriteViewModel;
    private EditText enterLatitude;
    private EditText enterLongitude;
    private TextView showDate;
    private TextView showSunrise;
    private TextView showSunset;
    private TextView location;
    private TextView showLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset_lookup);

        // 初始化首页toolbar控件
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);// This sets the toolbar as the app bar for the activity.

        // 初始化SunriseSunsetLookup页面控件
        // Initialize UI components
        enterLatitude = findViewById(R.id.enterLatitude);
        enterLongitude = findViewById(R.id.enterLongitude);
        showDate = findViewById(R.id.showDate);
        showSunrise = findViewById(R.id.showSunrise);
        showSunset = findViewById(R.id.showSunset);
        location = findViewById(R.id.location);
        showLocation = findViewById(R.id.showLocation);

        Button lookupButton = findViewById(R.id.lookUpButton);
        lookupButton.setOnClickListener(v -> handleLookupButtonClick(enterLatitude, enterLongitude));

        // Initialize the ViewModel for handling database operations
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Initialize Save Location Button and its listener
        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> saveLocation());

        // Initialize favourite Button and its listener
        Button favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SunriseSunsetLookupActivity.this, FavouritePageActivity.class);
                startActivity(intent);
            }
        });

        // Initialize your views and ViewModel here
        Button saveButton = findViewById(R.id.saveLocationButton); // replace with your actual save button ID
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
    }


    /* 从main首页toolbar跳转到SunriseSunsetLookup页面 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }



    /* Toast当用户输入错误的latitude或longitude */
    private boolean checkLocationFormat(String latitude, String longitude) {
        StringBuilder errorMessage = new StringBuilder();

        // 验证纬度格式
        if (!LocationValidator.isValidLatitude(latitude)) {
            errorMessage.append("Invalid Latitude, please enter again!");
        }

        // 验证经度格式
        if (!LocationValidator.isValidLongitude(longitude)) {
            if (errorMessage.length() > 0) {
                errorMessage.append("\n"); // 在两个错误消息之间添加换行符
            }
            errorMessage.append("Invalid Longitude, please enter again!");
        }

        // 如果有错误信息，显示一个包含所有错误的Toast
        if (errorMessage.length() > 0) {
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    /*Toast当用户输入错误的latitude或longitude - done！*/



    /* Save Location" button function */
    private void saveLocation() {
        // Get the current date from the TextView
        String date = showDate.getText().toString();

        // Get the sunrise and sunset times from their respective TextViews
        String sunrise = showSunrise.getText().toString();
        String sunset = showSunset.getText().toString();

        // Get the latitude and longitude from their respective EditTexts
        String latitude = enterLatitude.getText().toString().trim();
        String longitude = enterLongitude.getText().toString().trim();

        // Construct the default location name and details
        String locationName = "Location " + latitude + ", " + longitude + " on " + date + " (Sunrise: " + sunrise + ", Sunset: " + sunset + ")";

        // Create a new SaveLocation object with the retrieved information
        SaveLocation newLocation = new SaveLocation(sunrise, sunset, date, locationName, latitude, longitude);

        // Use the ViewModel to add this new location to the database
        favouriteViewModel.addFavourite(newLocation);

        // Show AlertDialog after adding the location
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Location Added Successfully!")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }



    /* Volley, get the search info from URL!!!!!!!!!!!!!!!! */
    // 处理查询按钮点击事件
    private void handleLookupButtonClick(EditText enterLatitude, EditText enterLongitude) {
        String latitude = enterLatitude.getText().toString().trim();
        String longitude = enterLongitude.getText().toString().trim();

        // Displaying the current date
        String currentDate = getCurrentDate();
        TextView dateTextView = findViewById(R.id.showDate);
        dateTextView.setText(getString(R.string.current_date_time, currentDate));

        if (!checkLocationFormat(latitude, longitude)) {
            return;
        }

        // Constructing the query URL
        String url = "https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude + "&timezone=UTC&date=today";

        // Creating Volley's JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject results = response.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");

                        // Displaying the results on the UI
                        TextView sunriseTextView = findViewById(R.id.showSunrise);
                        sunriseTextView.setText(getString(R.string.sunrise_time, sunrise));
                        TextView sunsetTextView = findViewById(R.id.showSunset);
                        sunsetTextView.setText(getString(R.string.sunset_time, sunset));

                        // Display the entered latitude and longitude
                        TextView latLongTextView = findViewById(R.id.showLocation);
                        latLongTextView.setText(getString(R.string.lat_long_info, latitude, longitude));
                    } catch (JSONException e) {



                        Toast.makeText(SunriseSunsetLookupActivity.this, "Error parsing data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(SunriseSunsetLookupActivity.this, "Error fetching data", Toast.LENGTH_LONG).show()
        );

        // Adding the request to Volley's RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    //show current date
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


}





