package com.example.mymap;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.mymap.BuildConfig.MAPS_API_KEY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.Place;



import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdvertActivity extends AppCompatActivity {
    RadioButton lostRadioButton;
    RadioButton foundRadioButton;
    EditText nameEditText;
    EditText phoneEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    EditText locationEditText;
    Button getCurrentLocationButton;
    Button saveButton;

    RadioGroup typeRadioGroup;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        lostRadioButton = findViewById(R.id.lostRadioButton);
        foundRadioButton = findViewById(R.id.foundRadioButton);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);
        saveButton = findViewById(R.id.saveButton);

        typeRadioGroup = findViewById(R.id.radioGroup);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), MAPS_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Use Google's location API to get the current location and update the content of locationEditText
                if (ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    return;
                }

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(AdvertActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    //get latitude and longitude
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();

                                    //Create a Geocoder object
                                    Geocoder geocoder = new Geocoder(AdvertActivity.this, Locale.getDefault());

                                    try {
                                        //get location
                                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                        if (!addresses.isEmpty()) {
                                            //Set the location to the content of locationEditText
                                            locationEditText.setText(addresses.get(0).getAddressLine(0));
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int checkedRadioButtonId = typeRadioGroup.getCheckedRadioButtonId();
                // Check if a radio button is checked
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(AdvertActivity.this, "You must select a type", Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = ((RadioButton)findViewById(typeRadioGroup.getCheckedRadioButtonId())).getText().toString();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String location = locationEditText.getText().toString();

                // Check if any field is empty
                if(type.isEmpty() || name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                    Toast.makeText(AdvertActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                Advert advert = new Advert();
                advert.setType(type);
                advert.setName(name);
                advert.setPhone(phone);
                advert.setDescription(description);
                advert.setDate(date);
                advert.setLocation(location);

                // Save the advert to SQLite database
                DBManager dbManager = new DBManager(AdvertActivity.this);
                dbManager.open();
                dbManager.insertAdvert(advert);
                dbManager.close();

                // Display a toast message
                Toast.makeText(AdvertActivity.this, "Save successful", Toast.LENGTH_SHORT).show();

                // Finish the AdvertActivity and go back to MainActivity
                finish();
            }
        });


        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set Autocomplete to MODE_OVERLAY
                int AUTOCOMPLETE_REQUEST_CODE = 1;
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(AdvertActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

    }
    //handle results of Places Autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                locationEditText.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
        }
    }

}

