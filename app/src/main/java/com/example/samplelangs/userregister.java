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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class userregister extends AppCompatActivity {
    private EditText user_name;
    private EditText user_un;
    private EditText user_pass;
    private EditText user_reenter;
    private EditText user_address;
    private EditText user_mobile;
    private Button ureg_btn;

    UserRegDB DB;
    private LocationHelper locationHelper;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregister);
        user_name = findViewById(R.id.your_name);
        user_un = findViewById(R.id.user_username);
        user_pass = findViewById(R.id.user_password);
        user_reenter = findViewById(R.id.user_reenterpass);
        user_address = findViewById(R.id.user_address);
        user_mobile = findViewById(R.id.user_mobileno);
        ureg_btn = findViewById(R.id.user_registerbtn);
        DB = new UserRegDB(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationHelper = new LocationHelper(fusedLocationClient);

        // Check and request location permission
        locationHelper.requestLocationPermission(this);

        locationHelper.getLocation(this, new LocationHelper.LocationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationReceived(String address, Location location) {
                if (address != null) {
                    user_address.setText(address);
                } else {
                    user_address.setText("Location not available");
                }
            }
        });


        ureg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = user_name.getText().toString();
                String username = user_un.getText().toString();
                String pass = user_pass.getText().toString();
                String reenter = user_reenter.getText().toString();
                String add = user_address.getText().toString();
                String mobile = user_mobile.getText().toString();

                if (user.equals("") || pass.equals("") || username.equals("") || reenter.equals("") || add.equals("")|| mobile.equals(""))
                    Toast.makeText(userregister.this, "Please enter username or password", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(reenter)){
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser){
                            Boolean insert = DB.insertuserinfo(user, username, pass, reenter, add, mobile);

                            if (insert) {
                                Toast.makeText(userregister.this, "Registered successfully!", Toast.LENGTH_SHORT).show();

//                          IF SUCCESSFUL GO TO LOGIN
                                Intent intent = new Intent(getApplicationContext(), userlogin.class);
                                startActivity(intent);
                            }

                        }
                    }
                }

            }
        });
    }

}