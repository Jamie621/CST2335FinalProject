package algonquin.cst2335.ju000013.recipeapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivityRecipeSearchBinding;
import algonquin.cst2335.ju000013.databinding.SearchResultBinding;

public class RecipeSearchActivity extends AppCompatActivity {

    ActivityRecipeSearchBinding binding;
    private  RecyclerView.Adapter searchAdapter;
    RecipeSearchedViewModel searchModel;
    private ArrayList<RecipeSearched> searchedRecipes;
    RecipeSearchedViewModel saveModel;
    private ArrayList<RecipeSearched> savedRecipes;
    private RecipeSearchedDAO sDAO;
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
    private final String URL_DETAIL_DATA = "https://api.spoonacular.com/recipes/";
    private final String URL_DETAIL_PARAM = "/information?apiKey=" + MY_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* achieve all the widgets */
        editSearchText = binding.editSearchText;
        buttonSearch = binding.buttonSearch;
        searchResultsRecycle = binding.searchResults;
        savedViewButton = binding.savedViewButton;

        /* save search text to SharedPreference to show automatically next time. */
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        queue = Volley.newRequestQueue(this);
        /* To specify a single column scrolling in a Vertical direction */
        searchResultsRecycle.setLayoutManager(new LinearLayoutManager(this));

        /* initialize and retrieve the ArrayList<> that it is storing */
        searchModel = new ViewModelProvider(this).get(RecipeSearchedViewModel.class);
        searchedRecipes = searchModel.searchedRecipes.getValue();
        if (searchedRecipes == null){
            searchModel.searchedRecipes.postValue(searchedRecipes = new ArrayList<>());
        }

        /* set adapter to recycleView */
        searchResultsRecycle.setAdapter(searchAdapter = new RecyclerView.Adapter<MySearchRowHolder>() {
            @NonNull
            @Override
            public MySearchRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchResultBinding binding1 = SearchResultBinding.inflate(getLayoutInflater());
                return new MySearchRowHolder(binding1.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MySearchRowHolder holder, int position) {
                RecipeSearched recipeSearched = searchedRecipes.get(position);
                holder.result_title_text.setText(recipeSearched.getTitle());
                holder.result_id_text.setText(String.format(Locale.CANADA, "%d", recipeSearched.getId()));
                // set image (String) to imageView
                String image_url = recipeSearched.getImage_url();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = getBitmap(image_url);
                        // Set the loaded bitmap to the ImageView on the UI thread
                        holder.result_image.post(() -> holder.result_image.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public int getItemCount() {
                return searchedRecipes.size();
            }
        });

        /* search function */
        buttonSearch.setOnClickListener(click -> {
            searchText = editSearchText.getText().toString();
            try {
                if(!searchText.isEmpty()){
                    // Clear the previous search results
                    searchedRecipes.clear();
                    searchAdapter.notifyDataSetChanged();// Notify the adapter that the data set has changed

                    String url = URL_REQUEST_DATA + URLEncoder.encode(searchText, "UTF-8") + URL_API_PARAM;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response -> {
                                try {
                                    Log.d(TAG, "Response: " + response.toString());
                                    JSONArray results = response.getJSONArray("results");
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject jsonObject = results.getJSONObject(i);
                                        String title = jsonObject.getString("title");
                                        String image = jsonObject.getString("image");
                                        int id = jsonObject.getInt("id");
                                        String sourceUrl = URL_DETAIL_DATA + id + URL_DETAIL_PARAM;
                                        /* add to arraylist*/
                                        searchedRecipes.add(new RecipeSearched(title, image, id, sourceUrl));

                                    }searchAdapter.notifyItemInserted(searchedRecipes.size()-1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    /* transfer String url to bitmap. */
    private static Bitmap getBitmap(String url) throws IOException {
        URL url1 = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    class MySearchRowHolder extends RecyclerView.ViewHolder {
        TextView result_title_text;
        ImageView result_image;
        TextView result_id_text;

        public MySearchRowHolder(@NonNull View itemView) {
            super(itemView);
            result_title_text = itemView.findViewById(R.id.result_title_text);
            result_image = itemView.findViewById(R.id.result_image);
            result_id_text = itemView.findViewById(R.id.result_id_text);

            /* show detail when you click the item (row). */
            itemView.setOnClickListener(click -> {
                // tell you which row (position) this row is currently in the adapter object.
                int position = getAbsoluteAdapterPosition();

                /* show detail and ask for save or not */
                RecipeSearched recipeDetail = searchedRecipes.get(position);
                String title = recipeDetail.getTitle();
                String image_url = recipeDetail.getImage_url();
                String sourceUrl = recipeDetail.getSource_url();
                Bitmap bitmap = null;
                Drawable drawable = null;
                try {
                    bitmap = getBitmap(image_url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Convert Bitmap to Drawable
                if (bitmap != null) {
                    drawable = new BitmapDrawable(getResources(), bitmap);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeSearchActivity.this);
                builder.setTitle(getString(R.string.save_alert))
                        .setIcon(drawable)
                        .setMessage(title)
                        .setMessage(sourceUrl)
                        .setNegativeButton(getString(R.string.no), (dialog, cl) -> {})
                        .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {
                            savedRecipes.add(recipeDetail); // insert into saved arrayList
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() ->
                            {
                                sDAO.insertRecipe(recipeDetail); // insert into database
                            });
                            Snackbar.make(click, getString(R.string.add_alert), Toast.LENGTH_SHORT).show();
                            Toast.makeText(RecipeSearchActivity.this, getString(R.string.add_alert), Toast.LENGTH_SHORT).show();
                        });
            });
        }
    }
}