package com.example.mymap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button createAdvertButton;
    Button showAllItemsButton;
    Button showOnMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAdvertButton = findViewById(R.id.createAdvertButton);
        showAllItemsButton = findViewById(R.id.showAllItemsButton);
        showOnMapButton = findViewById(R.id.showOnMapButton);

        createAdvertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Start AdvertActivity
                Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
                startActivity(intent);
            }
        });

        showAllItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Start AdvertListActivity
                Intent intent = new Intent(MainActivity.this, AdvertListActivity.class);
                startActivity(intent);
            }
        });

        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Start the map activity
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
