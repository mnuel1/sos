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
import android.util.Log;

public class userlogin extends AppCompatActivity {
    private TextView register;
    private EditText username;
    private EditText password;
    private Button login_btn;

    UserRegDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        register = (TextView) findViewById (R.id.user_reg);
        username = (EditText) findViewById(R.id.un);
        password = (EditText) findViewById(R.id.pass);
        login_btn = (Button) findViewById(R.id.log_btn);
        DB = new UserRegDB(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userregister.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(userlogin.this, "Please enter username or password", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepass(user, pass);
                    if (checkuserpass) {
                        Toast.makeText(userlogin.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();

                        // Saving the username after successful login
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", user);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), userhome.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(userlogin.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
            }
        }