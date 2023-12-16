package com.example.samplelangs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mechlogin extends AppCompatActivity {
    private TextView mechregisterview;
    private EditText username;
    private EditText pass;
    private Button login_btn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechlogin);

        mechregisterview = findViewById(R.id.mechregisterview);
        username = (EditText) findViewById(R.id.mech_un);
        pass = (EditText) findViewById(R.id.mech_pass);
        login_btn = (Button)findViewById(R.id.loginbtn);
        DB = new DBHelper(this);

        mechregisterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mechregister.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String password = pass.getText().toString();

                if (user.equals("") || password.equals(""))
                    Toast.makeText(mechlogin.this, "Please enter username or password", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepass(user, password);
                    if (checkuserpass) {

                        Toast.makeText(mechlogin.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();

                        // Saving the username after successful login
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", user);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), mechhome.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(mechlogin.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });

    }
}