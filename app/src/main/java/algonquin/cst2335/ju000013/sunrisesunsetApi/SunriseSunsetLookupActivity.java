package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import algonquin.cst2335.ju000013.R;

public class SunriseSunsetLookupActivity extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset_lookup);


        // 初始化首页toolbar控件
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);// This sets the toolbar as the app bar for the activity.

        // 初始化SunriseSunsetLookup页面控件
        Button lookupButton = findViewById(R.id.lookUpButton);
        EditText enterLatitude = findViewById(R.id.enterLatitude);
        EditText enterLongitude = findViewById(R.id.enterLongitude);

        lookupButton.setOnClickListener(v -> handleLookupButtonClick(enterLatitude, enterLongitude));
    }


    //从main首页toolbar跳转到SunriseSunsetLookup页面
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }










    // 检查纬度和经度格式是否正确
    private boolean checkLocationFormat(String latitude, String longitude) {
        if (!LocationValidator.isValidLatitude(latitude)) {
            // 显示提示信息
            Toast.makeText(this, "Invalid Latitude, please enter again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!LocationValidator.isValidLongitude(longitude)) {
            // 显示提示信息
            Toast.makeText(this, "Invalid Longitude, please enter again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 处理查询按钮点击事件
    private void handleLookupButtonClick(EditText enterLatitude, EditText enterLongitude) {
        // Get the string values from the EditText objects
        String latitude = enterLatitude.getText().toString().trim();
        String longitude = enterLongitude.getText().toString().trim();

        // 检查纬度和经度格式是否正确
        if (!checkLocationFormat(latitude, longitude)) {
            return;
        }

        // If the format is correct, execute the lookup operation
        //executeLookup(latitude, longitude);
    }
}


