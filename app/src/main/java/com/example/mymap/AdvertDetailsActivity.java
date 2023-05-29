package com.example.mymap;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdvertDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ADVERT_ID = "advert_id";
    private Advert advert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        // get advert id from intent extras
        int advertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, -1);
        if (advertId == -1) {
            // handle error, finish the activity
            finish();
            return;
        }

        // load advert from database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        advert = dbManager.getAdvert(advertId);
        dbManager.close();

        // populate views with advert data
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);

        typeTextView.setText(advert.getType());
        nameTextView.setText(advert.getName());
        descriptionTextView.setText(advert.getDescription());
        phoneTextView.setText(advert.getPhone());
        locationTextView.setText(advert.getLocation());

        dateTextView.setText(advert.getDate().toString());

        Button removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager dbManager = new DBManager(AdvertDetailsActivity.this);
                dbManager.open();
                dbManager.deleteAdvert(advert.getId());
                dbManager.close();

                finish(); // close this activity
                Intent intent = new Intent(AdvertDetailsActivity.this, AdvertListActivity.class);
                startActivity(intent);
            }
        });
    }
}


