package com.example.samplelangs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class mechregister extends AppCompatActivity  {

    private enum VehicleType {
        CAR,
        MOTOR,
        ALL
    }

    private VehicleType selectedVehicleType = VehicleType.ALL;

    private EditText owner_name;
    private EditText garage_name;
    private EditText mech_un;
    private EditText mech_pass;
    private EditText mech_reenter;
    private EditText address_input;
    private EditText mobile;
    private LocationHelper locationHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private Button reg_btn;
    private static final int LOCATION_PICKER_REQUEST_CODE = 999;
    DBHelper DB;

    private RadioButton car;
    private RadioButton motor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechregister);
        owner_name = findViewById(R.id.Owner_Name);
        garage_name = findViewById(R.id.Garage_Name);
        mech_un = findViewById(R.id.Mech_username);
        mech_pass = findViewById(R.id.Mech_pass);
        mech_reenter = findViewById(R.id.Mech_reenterpass);
        address_input = findViewById(R.id.Mech_address);
        mobile = findViewById(R.id.Mech_mobile);
        reg_btn = findViewById(R.id.mech_registerbtn);

        car = findViewById(R.id.car);
        motor = findViewById(R.id.motor);


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


        DB = new DBHelper(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationHelper = new LocationHelper(fusedLocationClient);

        // Check and request location permission
        locationHelper.requestLocationPermission(this);

        locationHelper.getLocation(this, new LocationHelper.LocationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationReceived(String address, Location location) {
                if (address != null) {
                    address_input.setText(address);
                } else {
                    address_input.setText("Location not available");
                }
            }
        });



        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = owner_name.getText().toString();
                String gname = garage_name.getText().toString();
                String uname = mech_un.getText().toString();
                String pass = mech_pass.getText().toString();
                String reenter = mech_reenter.getText().toString();
                String add = address_input.getText().toString();
                String contact = mobile.getText().toString();
                VehicleType selectedType = getSelectedVehicleType();

                if (name.equals("") || gname.equals("") || uname.equals("") || pass.equals("") || reenter.equals("") || add.equals("")|| contact.equals(""))
                    Toast.makeText(mechregister.this, "Please complete all fields!", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(reenter)){
                        Boolean checkuser = DB.checkusername(uname);
                        if(!checkuser){
                            Boolean insert = DB.insertmechdata(name, gname, uname, pass, reenter, add, contact, DBHelper.VehicleType.valueOf(selectedType.name()));
                            if (insert) {
                                Toast.makeText(mechregister.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mechregister.this, mechhome.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(mechregister.this, "Registered failed! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }


            }
        });
    }
    // Handle the click event for radio buttons
    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
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

    // Get the selected VehicleType
    public VehicleType getSelectedVehicleType() {
        return selectedVehicleType;
    }

}