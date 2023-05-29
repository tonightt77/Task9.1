package com.example.mymap;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mymap.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get all ads
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        List<Advert> adverts = dbManager.getAllAdverts();
        dbManager.close();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        LatLng firstLatLng = null;

        //For each ad, add a marker to the map
        for (Advert advert : adverts) {
            String location = advert.getLocation();

            //Use Geocoder to convert geographic location names to latitude and longitude
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (!addresses.isEmpty()) {
                    double lat = addresses.get(0).getLatitude();
                    double lng = addresses.get(0).getLongitude();

                    LatLng latLng = new LatLng(lat, lng);
                    if(firstLatLng == null){
                        firstLatLng = latLng;
                    }
                    mMap.addMarker(new MarkerOptions().position(latLng).title(advert.getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Move the camera to the first marker position
        if(firstLatLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10));
        }
    }

}