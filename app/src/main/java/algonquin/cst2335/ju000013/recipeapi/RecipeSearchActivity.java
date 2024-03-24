package algonquin.cst2335.ju000013.recipeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivityRecipeSearchBinding;

public class RecipeSearchActivity extends AppCompatActivity {

    ActivityRecipeSearchBinding binding;
    EditText editSearchText;
    Button buttonSearch;
    RecyclerView searchResultsRecycle;
    Button savedViewButton;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String searchText;
    private final String TAG = getClass().getSimpleName();
    private final String MY_KEY = "83ade364f8424054ae67e5f15fe178ec";
    private final String URL_REQUEST_DATA = "https://api.spoonacular.com/recipes/complexSearch?query=";
    private final String URL_API_PARAM = "&apiKey=" + MY_KEY;
    protected RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* achieve all the widgets */
        editSearchText = binding.editSearchText;
        buttonSearch = binding.buttonSearch;
//        searchResultsRecycle = binding.searchResults;
        savedViewButton = binding.savedViewButton;

        /* save search text to SharedPreference to show automatically next time. */
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        queue = Volley.newRequestQueue(this);

        /* search function */
        buttonSearch.setOnClickListener(click -> {
            searchText = editSearchText.getText().toString();
            try {
                if(!searchText.isEmpty()){
                    String url = URL_REQUEST_DATA + URLEncoder.encode(searchText, "UTF-8") + URL_API_PARAM;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response -> {
                                Log.d(TAG, "Response: " + response.toString());
                            }),
                            (error -> {
                                Log.e(TAG, "Error:" + error.getMessage());
                                Snackbar.make(click, R.string.error_msg, Toast.LENGTH_SHORT).show();
                            }));
                    queue.add(request);
                } else {
                    Snackbar.make(click, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error encoding recipe name");
            }
            /* save search text to SharedPreference */
            editor.putString("SearchText", searchText);
            editor.apply();
        });

        /* achieve search text from SharedPreference */
        String searchText = prefs.getString("SearchText", "");
        editSearchText.setText(searchText);
    }
}