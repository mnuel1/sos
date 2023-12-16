package com.example.samplelangs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mechhome extends AppCompatActivity {
    private Button add_mech;
    private Button add_ser;
    private Button logout;
    private Button view_feed;
    private Button add_gar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechhome);

        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        if (username.isEmpty()) {
            // The username is not empty; do something with it
            // ...
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        add_ser = findViewById(R.id.addservice);
        view_feed = findViewById(R.id.viewfeedback);
        logout = findViewById(R.id.logout);

        add_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mechemployee.class);
                startActivity(intent);
            }
        });
        view_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mechfeedback.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.apply(); // or editor.commit() if you want to block until the write is complete

                // Close the current activity
                finish();

                // Start the login activity
                Intent intent = new Intent(getApplicationContext(), mechlogin.class);
                startActivity(intent);
            }
        });

    }
}