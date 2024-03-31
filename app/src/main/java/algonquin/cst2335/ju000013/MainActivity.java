package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Dictionary;
import java.util.Set;

import algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity;
import algonquin.cst2335.ju000013.songApi.SongSearchActivity;

/**
 * @Author: Jungmin Ju, Wei Deng, Fei Wu, Zhaohong Huang.
 * @Lab-section: CST2335-011
 * @Creation Date: 2024-03-29
 */

public class MainActivity extends AppCompatActivity {
    /**
     * To create the option menu
     * @param menu The options menu in which you place your items
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toobar, menu);
        return true;
    }

    /**
     * When you select the option menu
     * @param item The menu item that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.button1){

        } else if (id == R.id.button2) {
            Intent intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.button3) {
            Intent intent = new Intent(MainActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.button4) {
            Intent intent = new Intent(MainActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To create activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Find views by Id.
         */
        Button buttonSunriseSunsetLookup = findViewById(R.id.button1);
         Button buttonRecipeSearch = findViewById(R.id.button2);
         Button buttonDictionaryApi = findViewById(R.id.button3);
         Button buttonSongSearch = findViewById(R.id.button4);

        /**
         *  set toolbar
         */
        Toolbar tool_bar = findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);

        /**
         * Set click listener for Recipe Search
         */
        buttonRecipeSearch.setOnClickListener(click ->{
            Intent intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
        });

        /**
         * Set click listener for Dictionary API
         */
          buttonDictionaryApi.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DictionaryApiActivity.class);
                startActivity(intent);
            }
           });


        /**
         * Set click listener for Deezer Song Search API
         */
         buttonSongSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               /**
                * Replace DeezerSongSearchApiActivity.class with the actual class name for this activity
                */
              Intent intent = new Intent(MainActivity.this, SongSearchActivity.class);
              startActivity(intent);
          }
        });
    }
}
