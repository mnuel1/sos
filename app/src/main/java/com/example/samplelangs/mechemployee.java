package com.example.samplelangs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class mechemployee extends AppCompatActivity {
    private TextInputEditText search;
    private Button searchBtn;
    private Button addmech;
    private ListView list;
    private ServiceDBHelper serviceDBHelper;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechemployee);

        addmech = findViewById(R.id.addmechanic);
        list = findViewById(R.id.list);

        addmech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMechanicDialog();
            }
        });

        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        serviceDBHelper = new ServiceDBHelper(this);
        try {
            List<Service> services = serviceDBHelper.getServicesForMechanic(username);

            // Create an ArrayList of Strings to hold the display data
            ArrayList<String> displayData = new ArrayList<>();
            for (Service service : services) {
                displayData.add("Service: " + service.getServiceName() + ", Cost: " + service.getCost());
            }

            // Create an ArrayAdapter to bind the data to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayData);

            // Set the adapter on the ListView
            list.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showAddMechanicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Mechanic");

        // Inflate and set the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.activity_service, null);
        builder.setView(dialogView);

        // Set up the input fields
        final EditText service = dialogView.findViewById(R.id.service);
        final EditText cost = dialogView.findViewById(R.id.cost);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the click on the "Add" button
                String serviceText = service.getText().toString();
                String costText = cost.getText().toString();
                // Retrieve other fields as needed

                // Perform the addition logic, e.g., add the mechanic to the database
                addServiceToDatabase(serviceText, costText);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the click on the "Cancel" button
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void addServiceToDatabase(String servicen, String cost) {
        // Implement the logic to add the mechanic to the database
        // You can use your DBHelper class or any other mechanism
        // ...

        serviceDBHelper = new ServiceDBHelper(this);
        double costnumber =  Double.parseDouble(cost);

        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        //  Insert a new service
        serviceDBHelper.insertService(username, servicen, costnumber);

        // Prompt a Toast or a message indicating success
        Toast.makeText(this, "Service Successfully Added", Toast.LENGTH_SHORT).show();

        // After adding the mechanic, you might want to refresh your UI or do any other necessary tasks
        // ...
        // Retrieve services for the mechanic
        List<Service> services = serviceDBHelper.getServicesForMechanic(username);

        // Create an ArrayList of Strings to hold the display data
        ArrayList<String> displayData = new ArrayList<>();
        for (Service service : services) {
            displayData.add("Service: " + service.getServiceName() + ", Cost: " + service.getCost());
        }

        // Create an ArrayAdapter to bind the data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayData);

        // Set the adapter on the ListView
        list.setAdapter(adapter);
    }

}

