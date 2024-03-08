package algonquin.cst2335.ju000013;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Dictionary;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSunriseSunsetLookup = findViewById(R.id.button1);
         Button buttonRecipeSearch = findViewById(R.id.button2);
         Button buttonDictionaryApi = findViewById(R.id.button3);
         Button buttonDeezerSongSearchApi = findViewById(R.id.button4);


        // Set click listener for Sunrise & Sunset Lookup
  /*      buttonSunriseSunsetLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SunriseSunsetLookupActivity.class);
                startActivity(intent);
            }
        });*/

        // Set click listener for Recipe Search
  /*        buttonRecipeSearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
         Replace RecipeSearchActivity.class with the actual class name for this activity
                Intent intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
               startActivity(intent);
             }
          });*/

//         Set click listener for Dictionary API
          buttonDictionaryApi.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DictionaryApiActivity.class);
                startActivity(intent);
            }
           });

        // Set click listener for Deezer Song Search API
  /*           buttonDeezerSongSearchApi.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
         Replace DeezerSongSearchApiActivity.class with the actual class name for this activity
                  Intent intent = new Intent(MainActivity.this, DeezerSongSearchApiActivity.class);
                  startActivity(intent);
              }
            });*/
    }
}
