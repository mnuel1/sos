package com.example.samplelangs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class usermechchoice extends AppCompatActivity {

    private TextView locationText;
    private TextView shop;
    private TextView mechname;
    private TextView mechlocation;

    private ListView mechservicelist;

    private Button sendtow;
    private Button rate;
    private LocationHelper locationHelper;
    private FusedLocationProviderClient fusedLocationClient;
    Location mylocation;
    FeedbackDBHelper fDB;
    private ServiceDBHelper serviceDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermechchoice);

        locationText = findViewById(R.id.location);
        shop = findViewById(R.id.shop);
        mechname = findViewById(R.id.mechname);
        mechlocation = findViewById(R.id.mechloc);
        mechservicelist = findViewById(R.id.mechservicelist);
        sendtow = findViewById(R.id.sendtow);
        rate = findViewById(R.id.rate);

        // Retrieve the clicked Mechanic object from the Intent
        Mechanic clickedMechanic = (Mechanic) getIntent().getSerializableExtra("clickedMechanic");

        // Now you can use the clickedMechanic object in your usermechchoice activity
        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        String mechusername = null;
        if (clickedMechanic != null) {
            String garageName = clickedMechanic.getGarageName();
            String owner = clickedMechanic.getOwner();
            String location = clickedMechanic.getAddress();
            mechusername = clickedMechanic.getUsername();

            shop.setText(garageName);
            mechname.setText(owner);
            mechlocation.setText(location);

        }

        serviceDBHelper = new ServiceDBHelper(this);
        try {
            List<Service> services = serviceDBHelper.getServicesForMechanic(mechusername);

            // Create an ArrayList of Strings to hold the display data
            ArrayList<String> displayData = new ArrayList<>();
            for (Service service : services) {
                displayData.add("Service: " + service.getServiceName() + ", Cost: " + service.getCost());
            }

            // Create an ArrayAdapter to bind the data to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayData);

            // Set the adapter on the ListView
            mechservicelist.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationHelper = new LocationHelper(fusedLocationClient);

        // Check and request location permission
        locationHelper.requestLocationPermission(this);

        locationHelper.getLocation(this, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String address, Location location) {
                if (address != null) {
                    locationText.setText("Location: " + address);
                    mylocation = location;
                } else {
                    locationText.setText("Location not available");
                }
            }
        });

        sendtow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mapUrl = locationHelper.shareLocation(mylocation);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Emergency! Vehicle needs assistance at this location: " + mapUrl);
                startActivity(Intent.createChooser(intent, "Share location"));
            }
        });
        String finalMechusername = mechusername;
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(usermechchoice.this);
                builder.setTitle("Rate our Service! Thank you!");

                // Inflate the layout for the dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_feedback, null);
                builder.setView(dialogView);

                // Reference the components in the dialog layout
                RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

                // Set up the positive button click listener
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the values from the dialog
                        float rating = ratingBar.getRating();

                        fDB = new FeedbackDBHelper(usermechchoice.this);
                        // Insert the feedback into the database
                        fDB.insertFeedback(finalMechusername, username, (int)rating);

                        // Add any additional logic you want to perform after submitting feedback

                        // Dismiss the dialog
                        dialogInterface.dismiss();
                    }
                });

                // Set up the negative button click listener
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismiss the dialog
                        dialogInterface.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}
