package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.widget.ArrayAdapter;
import android.widget.Toast;

public class userhome extends AppCompatActivity {

    private enum VehicleType {
        CAR,
        MOTOR,
        ALL
    }

    private VehicleType selectedVehicleType = VehicleType.ALL;
    private TextInputEditText search;
    private Button logoutbtn;
    private RadioButton motor;
    private RadioButton car;

    private TextView locationText;
    private ListView list;
    private LocationHelper locationHelper;
    private FusedLocationProviderClient fusedLocationClient;
    HashMap<String, Mechanic> mechanicMap = new HashMap<>(); // Map to store username and Mechanic object
    private DBHelper dbHelper;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        search = findViewById(R.id.search);
        logoutbtn = findViewById(R.id.userlogout);
        list = findViewById(R.id.mechanicslist);

        car = findViewById(R.id.CAR);
        motor = findViewById(R.id.MOTOR);

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });

        motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationHelper = new LocationHelper(fusedLocationClient);

        // Check and request location permission
        locationHelper.requestLocationPermission(this);

        locationText = findViewById(R.id.location);

        locationHelper.getLocation(this, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String address, Location location) {
                if (address != null) {
                    locationText.setText("Location: " + address);
                } else {
                    locationText.setText("Location not available");
                }
            }
        });

        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        ArrayList<String> displayData = new ArrayList<>();
        ArrayAdapter<String> adapter = null;
        try {
            dbHelper = new DBHelper(this);

            List<Mechanic> mechanics = dbHelper.getMechanicData();

            for (Mechanic mechanic : mechanics) {
                String displayInfo = "Garage: " + mechanic.getGarageName() +
                        ", Address: " + mechanic.getAddress();

                displayData.add(displayInfo);
                mechanicMap.put(mechanic.getUsername(), mechanic);
            }

            // Create an ArrayAdapter to bind the data to the ListView
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayData);

            // Set the adapter on the ListView
            list.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> finalAdapter = adapter;
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform live search as the user types
                filterList(charSequence.toString(), mechanicMap, displayData, finalAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.apply(); // or editor.commit() if you want to block until the write is complete

                // Close the current activity
                finish();

                // Start the login activity
                Intent intent = new Intent(getApplicationContext(), userlogin.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Use the position to get the corresponding Mechanic object from the mechanicMap
                Mechanic clickedMechanic = getMechanicAtPosition(position);

                if (clickedMechanic != null) {

                    // Now, you have the clicked Mechanic object
                    Intent intent = new Intent(getApplicationContext(), usermechchoice.class);

                    // Pass the clicked Mechanic as an extra to the next activity
                    intent.putExtra("clickedMechanic", clickedMechanic);

                    // Start the new activity
                    startActivity(intent);
                }
            }
        });

    }
    // Method to get the Mechanic object at a specific position in the mechanicMap
    private Mechanic getMechanicAtPosition(int position) {
        if (position < 0 || position >= mechanicMap.size()) {
            return null; // Return null if the position is out of bounds
        }

        // Iterate through the mechanicMap to find the Mechanic object at the specified position
        int currentIndex = 0;
        for (Map.Entry<String, Mechanic> entry : mechanicMap.entrySet()) {
            if (currentIndex == position) {
                return entry.getValue(); // Return the Mechanic object at the specified position
            }
            currentIndex++;
        }

        return null; // Return null if no matching Mechanic object is found
    }
    private void filterList(String searchQuery, HashMap<String, Mechanic> mechanicMap,
                            ArrayList<String> displayData, ArrayAdapter<String> adapter) {
        ArrayList<String> filteredData = new ArrayList<>();

        for (Map.Entry<String, Mechanic> entry : mechanicMap.entrySet()) {
            String username = entry.getKey();
            Mechanic mechanic = entry.getValue();

            // Check if garage name or location contains the search query
            boolean containsSearchQuery = mechanic.getGarageName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    mechanic.getAddress().toLowerCase().contains(searchQuery.toLowerCase());

            // Check if the VehicleType matches the selected type or if ALL is selected
            boolean matchesVehicleType = selectedVehicleType == VehicleType.ALL ||
                    mechanic.getVehicleType().equals(selectedVehicleType.toString());

            // If both conditions are met, add the mechanic to the filtered data
            if (containsSearchQuery && matchesVehicleType) {
                String resultInfo = "Garage: " + mechanic.getGarageName() +
                        ", Address: " + mechanic.getAddress();
                filteredData.add(resultInfo);
            }
        }

        // Update the adapter with the filtered data
        adapter.clear();
        adapter.addAll(filteredData);
        adapter.notifyDataSetChanged();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Uncheck all radio buttons
        car.setChecked(false);
        motor.setChecked(false);

        // Check the clicked radio button
        if (checked) {
            // If the clicked radio button is already checked, uncheck it
            if (view.getId() == car.getId() && selectedVehicleType == VehicleType.CAR) {
                ((RadioButton) view).setChecked(false);
                selectedVehicleType = VehicleType.ALL;
            } else if (view.getId() == motor.getId() && selectedVehicleType == VehicleType.MOTOR) {
                ((RadioButton) view).setChecked(false);
                selectedVehicleType = VehicleType.ALL;
            } else {
                // If the clicked radio button is not checked, check it
                ((RadioButton) view).setChecked(true);

                // Set the selectedVehicleType based on the clicked radio button's tag
                switch (view.getTag().toString()) {
                    case "car":
                        selectedVehicleType = VehicleType.CAR;
                        break;
                    case "motor":
                        selectedVehicleType = VehicleType.MOTOR;
                        break;
                }
            }
        }


    }


}

