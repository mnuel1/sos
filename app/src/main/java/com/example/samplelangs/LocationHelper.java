package com.example.samplelangs;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;

    public LocationHelper(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }
    public void requestLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, proceed with location-related tasks
            // Call your location-related functions or initialize location-related features here
            Toast.makeText(activity, "Location permission accepted", Toast.LENGTH_SHORT).show();
        } else {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }
    public interface LocationCallback {
        void onLocationReceived(String address, Location location);
    }
    public void getLocation(Activity activity, LocationCallback callback) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle case where user grants the permission.

            requestLocationPermission(activity);
            callback.onLocationReceived(null,null);
            return;
        }


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, loc -> {
                    if (loc != null) {
//                        Log.d("Location", "Latitude: " + loc.getLatitude() + ", Longitude: " + loc.getLongitude());
                        String address = getAddressFromLocation(activity, loc, callback);
                        callback.onLocationReceived(address,loc);
                    } else {
                        callback.onLocationReceived(null,null);
                    }
                });

    }

    private String getAddressFromLocation(Activity activity, Location location, LocationCallback callback) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String shareLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Now you can use the mapUrl to share the location, for example, by opening a share intent
        // or sending it through a messaging app
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;

        // Using an intent to share the link:

    }
}
