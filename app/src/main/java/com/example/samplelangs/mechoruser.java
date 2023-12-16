package com.example.samplelangs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mechoruser extends AppCompatActivity {

    private Button MECHANIC;
    private Button USER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechoruser);

        MECHANIC =findViewById(R.id.USER);
        USER =findViewById(R.id.USER);

        MECHANIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mechlogin.class);
                startActivity(intent);
            }
        });

        USER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userlogin.class);
                startActivity(intent);
            }
        });

    }
}